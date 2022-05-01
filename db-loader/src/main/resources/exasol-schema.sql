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
--
-- Correlates a "connected" with its matching "disconnected" Wifi event
-- and returns Wifi connections with begin and end timestamp.
---
CREATE OR REPLACE VIEW v_wifi_connection AS (
    WITH INTERMEDIATE AS (
      SELECT we.mac_address,
        we.device_name,
        we.event_type,
        we.wifi_type,
        we.speed,
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
        ) AS disconnect_event_type,
        LEAD(DISCONNECT_CODE) OVER (
          PARTITION BY MAC_ADDRESS
          ORDER BY "TIMESTAMP" asc,
            LOG_ENTRY_ID asc
        ) AS DISCONNECT_CODE
      FROM WIFI_EVENT we
    )
    SELECT i.device_name,
      i.mac_address,
      d.readable_name,
      d."TYPE" AS device_type,
      d.OWNER,
      i.wifi_type,
      i.speed,
      i.connect_timestamp,
      i.disconnect_timestamp,
      ROUND(
        SECONDS_BETWEEN(i.disconnect_timestamp, i.connect_timestamp)
      ) duration_seconds,
      i.disconnect_code
    FROM intermediate i
      LEFT OUTER JOIN wifi_device_details d ON i.mac_address = d.mac_address
      AND i.device_name = d.device_name
    WHERE i.event_type = 'connected'
      AND i.disconnect_event_type = 'disconnected'
  );
--
-- Accumulated Wifi connection duration in hours per device
--
CREATE OR REPLACE VIEW v_daily_wifi_connection AS (
    SELECT device_name,
      mac_address,
      readable_name,
      device_type,
      owner,
      TO_DATE(connect_timestamp) AS "DATE",
      TO_DATE(MAX(disconnect_timestamp)) AS disconnect_date,
      COUNT(1) AS "COUNT",
      ROUND(SUM(duration_seconds) / (60 * 60), 2) AS duration_hours
    FROM v_wifi_connection
    GROUP BY device_name,
      mac_address,
      readable_name,
      device_type,
      owner,
      TO_DATE(connect_timestamp)
  );
--
-- List count of all log entries and all log entries with parsed event
--
CREATE OR REPLACE VIEW v_log_entry_event_count AS (
    select r."DATE",
      count(*) as log_entry_count,
      count(l.event) as log_entry_with_event_count,
      round(100 * count(l.event) / count(*), 1) as percentage
    from log_entry l
      join report_mail r on l.report_id = r.id
    group by r."DATE"
  );
--
-- List dates where reports are missing
--
CREATE OR REPLACE VIEW v_dates_with_missing_reports AS (
    SELECT "DATE"
    FROM (
        -- List all dates between the first and last report
        WITH n AS (
          SELECT level - 1 AS n
          FROM dual CONNECT BY level < (
              SELECT CURRENT_DATE() - 1 - MIN("DATE")
              FROM report_mail
            ) + 2
        )
        SELECT ADD_DAYS(
            (
              SELECT MIN("DATE")
              FROM report_mail
            ),
            n.n
          ) as "DATE"
        FROM n
      ) all_dates
    WHERE NOT EXISTS (
        SELECT 1
        FROM report_mail r
        WHERE r."DATE" = all_dates."DATE"
      )
  );
--
-- Log entries without a parsable event
--
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
-- Report mail table with cleaned up firmware version number
--
CREATE OR REPLACE VIEW v_report_mail AS (
    SELECT ID,
      "DATE",
      "TIMESTAMP",
      message_id,
      subject,
      product_name,
      REGEXP_SUBSTR(firmware_version, '^[\d.]+') AS firmware_version,
      energy_usage_percent
    FROM report_mail
  );
--
-- Firmware updates
--
CREATE OR REPLACE VIEW v_firmware_updates AS (
    SELECT curr_day."DATE",
      prev_day.firmware_version AS previous_version,
      curr_day.firmware_version AS new_version
    FROM v_report_mail curr_day,
      v_report_mail prev_day
    WHERE curr_day."DATE" -1 = prev_day."DATE"
      AND curr_day.firmware_version != prev_day.firmware_version
  );
--
-- Energy usage vs. firmware updates
--
CREATE OR REPLACE VIEW v_energy_usage AS(
    SELECT mail."DATE",
      mail.energy_usage_percent,
      fw.previous_version,
      fw.new_version,
      IF fw.new_version IS NULL THEN 0
      ELSE 1 ENDIF AS update_count
    FROM v_report_mail mail
      LEFT OUTER JOIN v_firmware_updates fw ON mail."DATE" = fw."DATE"
  );