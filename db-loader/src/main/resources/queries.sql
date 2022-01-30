SELECT * FROM (
SELECT conn.DEVICE_NAME, conn.SPEED , CONN.wifi_type, conn."TIMESTAMP" AS connected, disco."TIMESTAMP"  AS disconnected,
 CONN."TIMESTAMP" - DISCO."TIMESTAMP" AS diff,
 ROW_NUMBER() OVER (ORDER BY CONN."TIMESTAMP" - DISCO."TIMESTAMP" desc) AS row1
FROM  WIFI_EVENT conn, WIFI_EVENT disco
WHERE conn.MAC_ADDRESS = disco.MAC_ADDRESS
AND conn.EVENT_TYPE = 'connected' AND DISCO.EVENT_TYPE = 'disconnected'
AND conn."TIMESTAMP" < disco."TIMESTAMP"
)
WHERE ROW1 = 1;


SELECT * FROM (
SELECT conn.DEVICE_NAME, conn.SPEED , CONN.wifi_type, conn."TIMESTAMP" AS connected, disco."TIMESTAMP"  AS disconnected,
 DISCO."TIMESTAMP" - CONN."TIMESTAMP" AS connection_duration,
 ROW_NUMBER() OVER (ORDER BY DISCO."TIMESTAMP" - CONN."TIMESTAMP" asc) AS row1
FROM  WIFI_EVENT conn, WIFI_EVENT disco
WHERE conn.MAC_ADDRESS = disco.MAC_ADDRESS
AND conn.EVENT_TYPE = 'connected' AND DISCO.EVENT_TYPE = 'disconnected'
AND conn."TIMESTAMP" < disco."TIMESTAMP"
AND conn."TIMESTAMP" > '2022-01-01' AND disco."TIMESTAMP" > '2022-01-01'
) WHERE row1 < 10;


SELECT conn.DEVICE_NAME, conn.SPEED , CONN.wifi_type, conn."TIMESTAMP" AS connected, disco."TIMESTAMP"  AS disconnected,
 DISCO."TIMESTAMP" - CONN."TIMESTAMP" AS connection_duration
FROM  WIFI_EVENT conn, (
      SELECT disco."TIMESTAMP",
        ROW_NUMBER() OVER (ORDER BY DISCO."TIMESTAMP" - CONN."TIMESTAMP" asc) AS row1
      FROM WIFI_EVENT disco
      WHERE conn.MAC_ADDRESS = DISCO.MAC_ADDRESS
        AND DISCO.EVENT_TYPE = 'disconnected'
        AND conn."TIMESTAMP" < disco."TIMESTAMP"
        AND disco."TIMESTAMP" > '2022-01-01'
  ) AS disco
WHERE conn.MAC_ADDRESS = disco.MAC_ADDRESS
AND conn.EVENT_TYPE = 'connected' 
AND conn."TIMESTAMP" > '2022-01-01' ;


