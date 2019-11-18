REM   Script: secondtask
REM   hierarchy query with filter

CREATE OR REPLACE FUNCTION Match  
   ( mainID IN NUMBER, pat IN VARCHAR2)   
   RETURN number 
IS 
    coincidenceID NUMBER :=0;  
    countOfElement NUMBER :=0;  
    id NUMBER;   
    parentid NUMBER;   
    name VARCHAR2(50);   
    CURSOR data IS   
        WITH Recursive (Id, ParentId, Name)   
        AS   
        (   
            SELECT Id, ParentId, Name   
            FROM LinuxDistribut e   
            WHERE e.id  = mainID   
            UNION ALL   
            SELECT e.Id, e.ParentId, e.Name   
            FROM LinuxDistribut e   
                JOIN Recursive r ON e.ParentId = r.Id   
        )   
        SELECT Id, ParentId, Name   
        FROM Recursive r;   
BEGIN   
   
OPEN data;   
   LOOP   
       FETCH data INTO id,parentid,name;   
              EXIT WHEN data%NOTFOUND;   
              countOfElement :=countOfElement+1;  
              IF INSTR(name,pat)>0 THEN  
                coincidenceID:=countOfElement;  
              END IF;  
    END LOOP;  
    IF countOfElement>1 AND coincidenceID=1 THEN RETURN 0;  
    ELSIF countOfElement-coincidenceID=countOfElement THEN RETURN 0;  
    ELSE RETURN coincidenceID;  
    END IF;  
CLOSE data;   
END;
/

SELECT 
    concat(lpad('-',3*LEVEL,'-'),name) AS SecondTask 
FROM 
    linuxdistribut 
    WHERE Match(id,'a')>0 
START WITH parentid IS NULL 
CONNECT BY PRIOR id=parentid;

