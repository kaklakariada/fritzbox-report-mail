drop schema if exists "FRITZBOX" cascade;
create schema "FRITZBOX";
open schema "FRITZBOX";

create table "REPORT_MAIL" ("DATE" date primary key,
  "TIMESTAMP" TIMESTAMP,
  message_id varchar(100),
  subject varchar(100)
);

create table "DATA_VOLUME" (
  "DATE" date primary key REFERENCES "REPORT_MAIL" ("DATE"),
  "DOWNLOAD_MB" double,
  "UPLOAD_MB" double
);

create table "LOG_ENTRY" (
  "TIMESTAMP" TIMESTAMP,
  "MESSAGE" VARCHAR(2000),
  "EVENT" VARCHAR(200)
);
