REM   Script: data.sql
REM   fill data

CREATE TABLE LinuxDistribut  
	( 
	Id integer NOT NULL PRIMARY KEY,  
	ParentId integer REFERENCES LinuxDistribut, 
	Name varchar(50) NOT NULL UNIQUE 
	);

INSERT ALL 
 INTO LinuxDistribut values (1,null,'debian') 
 INTO LinuxDistribut values (2,1,'ubuntu') 
 INTO LinuxDistribut values (3,2,'kubuntu') 
 INTO LinuxDistribut values (4,2,'lubuntu') 
 INTO LinuxDistribut values (5,2,'linux mint') 
 INTO LinuxDistribut values (6,null,'slackware') 
 INTO LinuxDistribut values (7,6,'slax') 
 INTO LinuxDistribut values (8,7,'wolvix') 
 INTO LinuxDistribut values (9,7,'slampp') 
 INTO LinuxDistribut values (10,7,'dnalinux') 
 INTO LinuxDistribut values (11,6,'suse') 
 INTO LinuxDistribut values (12,11,'linkat') 
 INTO LinuxDistribut values (13,11,'opensuse') 
 INTO LinuxDistribut values (14,null,'redhat') 
 INTO LinuxDistribut values (15,14,'fedora core') 
 INTO LinuxDistribut values (16,15,'sailfish os') 
 INTO LinuxDistribut values (17,15,'fedora') 
SELECT * FROM dual;

