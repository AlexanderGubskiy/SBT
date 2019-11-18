REM   Script: firsttask
REM   hierarchy query

SELECT 
    concat(lpad('-',3*LEVEL,'-'),name) AS FirstTask 
FROM 
    linuxdistribut 
START WITH parentid IS NULL 
CONNECT BY PRIOR id=parentid;

