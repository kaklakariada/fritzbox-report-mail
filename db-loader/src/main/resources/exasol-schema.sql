create table "REPORT_MAIL" (
  id integer PRIMARY KEY,
  "DATE" date NOT NULL,
  "TIMESTAMP" TIMESTAMP NOT NULL,
  message_id varchar(100) NOT NULL,
  subject varchar(100) NOT NULL,
  product_name varchar(20) not null,
  firmware_version varchar(100) not null,
  energy_usage_percent integer not null
);
create table "DATA_VOLUME" (
  report_id integer primary key REFERENCES "REPORT_MAIL" (id),
  "DATE" date,
  "DOWNLOAD_MB" double NOT NULL,
  "UPLOAD_MB" double NOT NULL,
  "TOTAL_MB" double NOT NULL
);
create table "LOG_ENTRY" (
  id integer PRIMARY KEY,
  report_id integer REFERENCES "REPORT_MAIL" (id),
  "TIMESTAMP" TIMESTAMP NOT NULL,
  "MESSAGE" VARCHAR(2000) NOT NULL,
  "EVENT" VARCHAR(200) NULL
);
create table wifi_event (
  log_entry_id integer primary key references log_entry (id),
  "TIMESTAMP" TIMESTAMP NOT NULL,
  event_type varchar(12) not null,
  wifi_type varchar(7) not null,
  device_name varchar(50) null,
  speed varchar(20) null,
  mac_address varchar(20) not null,
  disconnect_code varchar(20) null
);
create table wifi_device_details (
  device_name varchar(50) not null,
  mac_address varchar(20) not null,
  type varchar(50) not null,
  readable_name varchar(50) not null,
  owner varchar(50) not null,
  CONSTRAINT PRIMARY KEY (device_name, mac_address) ENABLE
);
CREATE OR REPLACE VIEW v_wifi_connection AS (
    with intermediate as (
      SELECT we.MAC_ADDRESS,
        we.DEVICE_NAME,
        we.EVENT_TYPE,
        we.wifi_type,
        we.SPEED,
        we."TIMESTAMP" AS connect_timestamp,
        LEAD("TIMESTAMP") OVER (
          PARTITION BY DEVICE_NAME,
          MAC_ADDRESS
          ORDER BY "TIMESTAMP" asc,
            LOG_ENTRY_ID asc
        ) AS DISCONNECT_timestamp,
        LEAD(EVENT_TYPE) OVER (
          PARTITION BY DEVICE_NAME,
          MAC_ADDRESS
          ORDER BY "TIMESTAMP" asc,
            LOG_ENTRY_ID asc
        ) AS DISCONNECT_event,
        LEAD(DISCONNECT_CODE) OVER (
          PARTITION BY DEVICE_NAME,
          MAC_ADDRESS
          ORDER BY "TIMESTAMP" asc,
            LOG_ENTRY_ID asc
        ) AS DISCONNECT_CODE
      FROM WIFI_EVENT we
    )
    SELECT d.READABLE_NAME,
      d."TYPE" AS device_type,
      d.OWNER,
      i.wifi_type,
      i.speed,
      i.connect_timestamp,
      i.disconnect_timestamp,
      SECONDS_BETWEEN(i.disconnect_timestamp, i.connect_timestamp) duration_seconds,
      i.DISCONNECT_CODE
    FROM intermediate i
      LEFT OUTER JOIN wifi_device_details d ON i.mac_address = d.MAC_ADDRESS
      AND i.device_name = d.DEVICE_NAME
    WHERE i.EVENT_TYPE = 'connected'
      AND i.disconnect_event = 'disconnected'
  );
CREATE OR REPLACE VIEW v_daily_wifi_connection AS (
    SELECT READABLE_NAME,
      DEVICE_TYPE,
      OWNER,
      to_date(CONNECT_TIMESTAMP) AS "DATE",
      to_date(max(DISCONNECT_TIMESTAMP)) AS DISCONNECT_DATE,
      count(1) AS "COUNT",
      round(sum(duration_seconds) / (60 * 60), 2) AS duration_hours
    FROM V_WIFI_CONNECTION
    GROUP BY READABLE_NAME,
      DEVICE_TYPE,
      OWNER,
      to_date(CONNECT_TIMESTAMP)
  );
CREATE OR REPLACE VIEW v_log_entry_without_event AS (
    SELECT id,
      report_id,
      "TIMESTAMP",
      message
    FROM log_entry
    WHERE event IS NULL
  );