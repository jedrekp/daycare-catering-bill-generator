INSERT INTO APP_USER (FIRST_NAME,LAST_NAME,USERNAME,PASSWORD,DAYCARE_ROLE) VALUES
('John','Locke','jlocke','$2a$10$aeNF2779Hqy4B1GpTYVyk.tYBSHRSpjila6s337xfMhhXV4Xq04tC','HEADMASTER'),
('Marcin','Farcik','mfarcik','$2a$10$K25YawewGMbSMtqJuA.8QOgGbpJH5rY8i90nGbsJ7lZ8WI6O6qgdq','GROUP_SUPERVISOR');

INSERT INTO DAYCARE_GROUP (GROUP_NAME,GROUP_SUPERVISOR_ID) VALUES
('Dolphins',2),
('Tigers',NULL);

INSERT INTO CHILD (FIRST_NAME,LAST_NAME,PARENT_EMAIL,ARCHIVED,DAYCARE_GROUP_ID) VALUES
('Son','Gohan','songoku@fake.fake',FALSE,1),
('Lebron','James','lebronsdad@fake.fake',FALSE,1),
('Darth','Maul','darthmaulsmom@fake.fake',FALSE,1),
('Peter','Parker','peterparkersdad@fake.fake',FALSE,1),
('Frodo','Baggins','bilbobaggins@fake.fake',FALSE,2),
('Geralt','of Rivia','kaermorhen@fake.fake',FALSE,2),
('Luke','Skywalker','anakin@fake.fake',FALSE,2),
('Eric','Cartman','ericsmom@fake.fake',FALSE,2),
('Kevin','Garnet','bigticket@fake.fake',FALSE,NULL),
('Pablo','Escobar','cartelmailbox@fake.fake',FALSE,NULL),
('Johny','Cage','mortalkombat@fake.fake',TRUE,NULL);

INSERT INTO CATERING_OPTION (OPTION_NAME,DAILY_COST,DISABLED) VALUES
('Standard',12.50, FALSE),
('Milk-Free',13.65,FALSE),
('Vegetarian',14.25,FALSE);

INSERT INTO ASSIGNED_OPTION(EFFECTIVE_DATE,CHILD_ID,CATERING_OPTION_ID) VALUES
('2020-02-01',1,1),
('2020-02-12',1,3),
('2020-02-01',2,1),
('2020-02-01',3,2),
('2020-02-01',4,1),
('2020-02-01',5,3),
('2020-02-01',6,2),
('2020-02-18',6,3),
('2020-02-01',7,1),
('2020-02-01',8,1),
('2020-02-01',9,1),
('2020-02-01',10,3),
('2020-02-08',10,2),
('2020-02-01',11,1);