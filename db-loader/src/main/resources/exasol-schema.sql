drop schema if exists "FRITZBOX" cascade;
create schema "FRITZBOX";
create table "FRITZBOX"."DATA_VOLUME" ("ID" INTEGER IDENTITY, "DATE" date , "DOWNLOAD" double, "UPLOAD" double);

