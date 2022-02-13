drop schema if exists "FRITZBOX" cascade;
create schema "FRITZBOX";
open schema "FRITZBOX";
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
  mac_address varchar(20) not null
);
create table wifi_device_details (
  device_name varchar(50) not null,
  mac_address varchar(20) not null,
  type varchar(50) not null,
  readable_name varchar(50) not null,
  owner varchar(50) not null,
  CONSTRAINT PRIMARY KEY (device_name, mac_address) ENABLE
);
create table wifi_connection (
  device_name varchar(50) not null,
  mac_address varchar(20) not null,
  wifi_type varchar(7) not null,
  speed varchar(20) null,
  "BEGIN" timestamp null,
  "END" timestamp null,
  CONSTRAINT FOREIGN KEY (device_name, mac_address) REFERENCES wifi_device_details(device_name, mac_address) DISABLE
);
CREATE OR REPLACE VIEW v_wifi_connection AS (
    SELECT DEVICE_NAME,
      MAC_ADDRESS,
      WIFI_TYPE,
      SPEED,
      "BEGIN",
      "END",
      SECONDS_BETWEEN("END", "BEGIN") AS duration_seconds
    FROM WIFI_CONNECTION
  );
CREATE OR REPLACE VIEW v_daily_wifi_connection AS (
    SELECT DEVICE_NAME,
      MAC_ADDRESS,
      "DATE",
      count(1) AS connection_count,
      sum(duration_seconds) AS total_connection_time_seconds
    FROM (
        SELECT DEVICE_NAME,
          MAC_ADDRESS,
          duration_seconds,
          CASE
            WHEN "BEGIN" IS NOT NULL THEN to_date("BEGIN")
            ELSE to_date("END")
          END AS "DATE"
        FROM v_wifi_connection
      )
    GROUP BY DEVICE_NAME,
      MAC_ADDRESS,
      "DATE"
  );