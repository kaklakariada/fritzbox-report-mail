drop schema if exists "FRITZBOX" cascade;
create schema "FRITZBOX";
open schema "FRITZBOX";

create table "REPORT_MAIL" (
  id integer PRIMARY KEY,
  "DATE" date NOT NULL,
  "TIMESTAMP" TIMESTAMP NOT NULL,
  message_id varchar(100) NOT NULL,
  subject varchar(100) NOT NULL
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
