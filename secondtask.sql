REM   Script: secondtask
REM   hierarchy query with filter

WITH filtered AS (
  SELECT DISTINCT * FROM linuxdistribut 
  START WITH NAME LIKE '%a%' AND parentid IN (SELECT id FROM linuxdistribut)
  CONNECT BY id = PRIOR parentid
)
SELECT 
    concat(lpad('-',3*LEVEL,'-'),name) AS SecondTask 
FROM 
    filtered 
START WITH parentid IS NULL 
CONNECT BY PRIOR id=parentid

