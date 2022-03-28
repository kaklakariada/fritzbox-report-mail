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
          PARTITION BY MAC_ADDRESS
          ORDER BY "TIMESTAMP" asc,
            LOG_ENTRY_ID asc
        ) AS DISCONNECT_timestamp,
        LEAD(EVENT_TYPE) OVER (
          PARTITION BY MAC_ADDRESS
          ORDER BY "TIMESTAMP" asc,
            LOG_ENTRY_ID asc
        ) AS DISCONNECT_event,
        LEAD(DISCONNECT_CODE) OVER (
          PARTITION BY MAC_ADDRESS
          ORDER BY "TIMESTAMP" asc,
            LOG_ENTRY_ID asc
        ) AS DISCONNECT_CODE
      FROM WIFI_EVENT we
    )
    SELECT d.READABLE_NAME,
      d."TYPE" AS device_type,
      d.OWNER,
      i.device_name,
      i.mac_address,
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
CREATE OR REPLACE VIEW v_log_entry_without_event AS (
    SELECT id,
      report_id,
      "TIMESTAMP",
      message
    FROM log_entry
    WHERE event IS NULL
  );
--
CREATE OR REPLACE LUA SCALAR SCRIPT LUA_MAX(a NUMBER, b number) RETURNS number AS function run(ctx) return math.max(ctx [1], ctx [2])
end;
CREATE OR REPLACE LUA SCALAR SCRIPT LUA_MIN(a NUMBER, b number) RETURNS number AS function run(ctx) return math.min(ctx [1], ctx [2])
end;
--
CREATE OR REPLACE VIEW v_daily_wifi_connection AS (
    SELECT r."DATE",
      c.readable_name,
      c.owner,
      c.device_type,
      c.mac_address,
      SUM(
        (
          lua_min(
            POSIX_TIME(c.disconnect_timestamp),
            POSIX_TIME(TO_TIMESTAMP(r."DATE") + 1)
          ) - lua_max(
            POSIX_TIME(c.connect_timestamp),
            POSIX_TIME(TO_TIMESTAMP(r."DATE"))
          )
        )
      ) as duration_seconds,
      sum(1) AS connection_count
    FROM report_mail r,
      v_wifi_connection c
    WHERE c.connect_timestamp <= TO_TIMESTAMP(r."DATE") + 1
      AND c.disconnect_timestamp >= TO_TIMESTAMP(r."DATE")
    GROUP BY c.readable_name,
      c.device_type,
      c.owner,
      c.device_type,
      c.mac_address,
      r."DATE"
  );
--