DROP TABLE IF EXISTS Payments;
DROP TABLE IF EXISTS Room_Reservation;
DROP TABLE IF EXISTS Locked;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Service_reservation;
DROP TABLE IF EXISTS Service;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Creditcard;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Reservation;
DROP TABLE IF EXISTS Hotel;

CREATE TABLE IF NOT EXISTS Hotel (
  HNR INTEGER IDENTITY(1,1) PRIMARY KEY,
  Name VARCHAR(60),
  Address VARCHAR(60),
  IBAN VARCHAR(50),
  BIC VARCHAR(15),
  EMAIL VARCHAR(50),
  PASSWORD VARCHAR (50),
  CREATION_DATE DATE,
  HOST VARCHAR(50),
  PORT VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS email_passwords(
  username VARCHAR(50),
  password VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS Reservation (
  RID INTEGER IDENTITY(2000,1) PRIMARY KEY,
  CustomerID INTEGER,
  from_date DATE,
  until_date DATE,
  Total BIGINT NOT NULL CHECK(Total >= 0),
  is_paid BOOLEAN NOT NULL DEFAULT FALSE,
  is_canceled BOOLEAN NOT NULL DEFAULT FALSE,
  is_arrived BOOLEAN NOT NULL DEFAULT FALSE,
  is_checkedOut BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS email_passwords(
  username VARCHAR(50) PRIMARY KEY,
  password VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS email_passwords(
  username VARCHAR(50) PRIMARY KEY,
  password VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Creditcard (
  CNR VARCHAR(30) PRIMARY KEY,
  Holder VARCHAR(60),
  card_type VARCHAR(20),
  Exp_Month INTEGER,
  Exp_Year INTEGER,
  CVV VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS Customer (
  PID INTEGER IDENTITY(3000,1) PRIMARY KEY,
  name VARCHAR(50),
  surname VARCHAR(50),
  Address VARCHAR(50),
  ZIP VARCHAR(20),
  Place VARCHAR(50),
  Country VARCHAR(50),
  Phone VARCHAR(20),
  email VARCHAR(50),
  bdate DATE,
  Sex VARCHAR(10),
  Identification VARCHAR(30),
  Credit_Card VARCHAR(30) REFERENCES Creditcard(CNR),
  Note VARCHAR(250),
  Newsletter Boolean DEFAULT FALSE,
  RID INTEGER
);

CREATE TABLE IF NOT EXISTS Employee (
  PID INTEGER IDENTITY(1000,1) PRIMARY KEY,
  name VARCHAR(50),
  surname VARCHAR(50),
  Address VARCHAR(50),
  ZIP VARCHAR(20),
  Place VARCHAR(50),
  Country VARCHAR(50),
  Phone VARCHAR(20),
  email VARCHAR(50),
  bdate DATE,
  Sex VARCHAR(10),
  SVNR VARCHAR(20),
  IBAN VARCHAR(20),
  BIC VARCHAR(20),
  Salary BIGINT NOT NULL CHECK(Salary > 0),
  Rolle VARCHAR(50),
  Picture VARCHAR(255),
  is_Deleted BOOLEAN NOT NULL DEFAULT FALSE,
  Username VARCHAR(50),
  user_password VARCHAR(64),
  Rights  INTEGER
);


CREATE TABLE IF NOT EXISTS Service (
  SRID INTEGER IDENTITY(1,1) PRIMARY KEY,
  service_type VARCHAR(50),
  Description VARCHAR(50),
  Price BIGINT NOT NULL CHECK(Price > 0)
);



CREATE TABLE IF NOT EXISTS Category (
  Name VARCHAR PRIMARY KEY,
  Price BIGINT NOT NULL CHECK(Price > 0),
  Beds INTEGER
);

CREATE TABLE IF NOT EXISTS Room (
  RNR INTEGER PRIMARY KEY,
  Room_category VARCHAR(20),
  Extras VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Locked (
  LID INTEGER IDENTITY(4000,1) PRIMARY KEY,
  RNR INTEGER,
  Reason VARCHAR(255),
  Locked_From DATE,
  Locked_Until DATE
);

CREATE TABLE IF NOT EXISTS Room_Reservation (
  RID INTEGER REFERENCES Reservation(RID),
  RoomID INTEGER REFERENCES Room(RNR),
  Room_Price BIGINT NOT NULL CHECK(Room_price > 0),
  Breakfast Boolean,
  PRIMARY KEY(RID, RoomID)
);

CREATE TABLE IF NOT EXISTS Service_reservation (
  SRID INTEGER REFERENCES Service(SRID),
  RID INTEGER REFERENCES Reservation(RID),
  on_date DATE,
  PRIMARY KEY (SRID,RID,on_date)
);

CREATE TABLE IF NOT EXISTS Payments (
  PayID INTEGER IDENTITY(5000,1) PRIMARY KEY,
  Reservation INTEGER,
  Paid_Amount BIGINT NOT NULL CHECK(paid_amount > 0),
  Payment_Method VARCHAR(20),
  Payment_date DATE
);

-- Hotel
INSERT INTO Hotel(Name, Address, IBAN, BIC, CREATION_DATE) VALUES ('Hotel Veljkovic', 'Kliebergasse 3', 'AT671111122333333333', 'GIBAATWWXXX', '2017-05-01');

-- Defining Categories
INSERT INTO Category(Name, Price, Beds) VALUES ('Single Room', 5000 , 1);
INSERT INTO Category(Name, Price, Beds) VALUES ('Double or Twin Room', 7000 , 2);
INSERT INTO Category(Name, Price, Beds) VALUES ('Suite', 9000 , 4);

-- Defining services
INSERT INTO Service(service_type, description, price) VALUES ('Transport', 'from the airport', 1200);
INSERT INTO Service(service_type, description, price) VALUES ('Transport', 'to the airport', 1200);
INSERT INTO Service(service_type, description, price) VALUES ('Wellness', 'Swedish massage', 2000);
INSERT INTO Service(service_type, description, price) VALUES ('Wellness', 'Aromatherapy massage', 2000);
INSERT INTO Service(service_type, description, price) VALUES ('Wellness', 'Shiatsu massage', 2000);
INSERT INTO Service(service_type, description, price) VALUES ('Wellness', 'Swedish massage', 2000);
INSERT INTO Service(service_type, description, price) VALUES ('Cleaning service', 'Washing and drying your clothes', 700);
INSERT INTO Service(service_type, description, price) VALUES ('Restaurant', 'Austrian food', 300);
INSERT INTO Service(service_type, description, price) VALUES ('Restaurant', 'Italian food', 300);
INSERT INTO Service(service_type, description, price) VALUES ('Restaurant', 'Chinese food', 300);

-- Rooms
INSERT INTO Room(RNR, room_category, Extras) VALUES (101, 'Single Room', 'Fridge,Air conditioning,Sea view');
INSERT INTO Room(RNR, room_category, Extras) VALUES (102, 'Suite', 'TV,Kitchen,Bathroom,Air conditioning');
INSERT INTO Room(RNR, room_category, Extras) VALUES (103, 'Suite', 'TV,Kitchen,Bathroom,Air conditioning,Sea view');
INSERT INTO Room(RNR, room_category, Extras) VALUES (104, 'Double or Twin Room', 'TV,Fridge,Sea view');
INSERT INTO Room(RNR, room_category, Extras) VALUES (105, 'Double or Twin Room', 'TV,Air conditioning');
INSERT INTO Room(RNR, room_category, Extras) VALUES (201, 'Suite', 'TV,Kitchen,Bathroom,Air conditioning');
INSERT INTO Room(RNR, room_category, Extras) VALUES (202, 'Single Room', 'TV,Fridge,Air conditioning,Sea view');
INSERT INTO Room(RNR, room_category, Extras) VALUES (203, 'Double or Twin Room', 'TV,Air conditioning,Jacuzzi');
INSERT INTO Room(RNR, room_category, Extras) VALUES (204, 'Single Room', 'TV,Fridge,Jacuzzi,Sea view');
INSERT INTO Room(RNR, room_category, Extras) VALUES (205, 'Suite', 'Kitchen,Bathroom,Jacuzzi');
INSERT INTO Room(RNR, room_category, Extras) VALUES (301, 'Double or Twin Room', 'Fridge,Air conditioning,Jacuzzi');
INSERT INTO Room(RNR, room_category, Extras) VALUES (302, 'Single Room', 'TV,Fridge,Air conditioning,Sea view');
INSERT INTO Room(RNR, room_category, Extras) VALUES (303, 'Double or Twin Room', 'TV,Fridge,Sea view');
INSERT INTO Room(RNR, room_category, Extras) VALUES (304, 'Suite', 'TV,Kitchen,Bathroom,Jacuzzi,Sea view');
INSERT INTO Room(RNR, room_category, Extras) VALUES (305, 'Single Room', 'TV,Fridge,Air conditioning');

-- Locked rooms
INSERT INTO Locked(RNR, Reason, Locked_from, Locked_until) VALUES (302, 'Renovation', '2017-07-01', '2017-07-06');
INSERT INTO Locked(RNR, Reason, Locked_from, Locked_until) VALUES (105, 'Cleaning', '2017-08-12', '2017-08-13');
INSERT INTO Locked(RNR, Reason, Locked_from, Locked_until) VALUES (201, 'Fixing damages', '2017-08-03', '2017-08-07');

-- Employees ,they begin with 1000
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted,username,user_password,rights) VALUES ('Timothy','Howe','P.O. Box 825, 7907 Tincidunt St.','03997','Attigliano','Antarctica','+58 940 027 4992','amet.ultricies.sem@egetipsum.com','1957-04-13','male','627104151','1671072258999',' KZROOFYB',167208,'Manager','res/images/Employees/Manager1.jpg','false','username0','password',1);
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted,username,user_password,rights) VALUES ('Beatrice','Barry','422-9805 Adipiscing Street','5839YE','Hathras','Honduras','+01 744 477 1251','turpis@aultriciesadipiscing.edu','1962-11-25','female','21858149595','1604011853499',' ZQICSXCE',206957,'Receptionist','res/images/Employees/receptionist2.jpg','false','admin','0aedb7ffa6f4d0c24a3037b420a076fbe6b4b14131aca0294eeab397e160a292',1);
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted,username,user_password,rights) VALUES ('Nathaniel','Jordan','P.O. Box 708, 3180 Et Rd.','73985','Yorkton','Azerbaijan','+43 889 953 7925','volutpat@parturient.net','1965-02-28','male','6275919581518','1628072563799',' TWOKSLYX',200967,'Receptionist','res/images/Employees/receptionist3.jpg','false','username2','0aedb7ffa6f4d0c24a3037b420a076fbe6b4b14131aca0294eeab397e160a292',2);
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted,username,user_password,rights) VALUES ('Moses','Saunders','149-2499 Neque Ave','51816','Grand-Halleux','Aruba','+63 591 152 9950','Nulla.dignissim.Maecenas@Aliquam.co.uk','1989-12-16','male','62741858519','1685032817599',' WBGYLCKU',243611,'Receptionist','res/images/Employees/receptionist4.jpg','false','username3','0aedb7ffa6f4d0c24a3037b420a076fbe6b4b14131aca0294eeab397e160a292',2);
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted) VALUES ('Keane','Brooks','3150 Natoque St.','2635','Altamura','Bosnia and Herzegovina','+82 064 887 4142','non.vestibulum@viverraMaecenasiaculis.org','1966-09-30','female','1545833262712','1672090813199',' EFVNOVJJ',159679,'Cook','res/images/Employees/cook1.jpg','false');
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted) VALUES ('Byron','Kramer','Ap #928-6355 At Rd.','02865','Vremde','Bermuda','+97 769 583 4492','amet@lacusEtiambibendum.edu','1976-10-18','female','98564949149198','1601091857699',' DTGTUWNS',185822,'Cook','res/images/Employees/cook2.jpg','false');
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted) VALUES ('Buckminster','Reilly','Ap #437-3846 Eget Avenue','56077-406','Boncelles','San Marino','+40 123 398 5585','Curabitur.ut.odio@nislelementum.org','1956-10-15','female','591948334949','1626031798799','CXLTPGOT',145975,'Cleaning lady','res/images/Employees/cleaning-lady2.jpg','false');
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted) VALUES ('Clare','Huber','P.O. Box 956, 4237 Nec Rd.','98334','Braunau am Inn','Norfolk Island','+87 149 631 2016','Lorem@Nunc.net','1979-11-23','female','42341414595992','1639061780599',' GMNXCWVD',186377,'Cleaning lady','res/images/Employees/Cleaning-lady3.jpg','false');
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted) VALUES ('Deborah','Bryan','990-4394 Odio Ave','2302','Bazel','Viet Nam','+78 717 761 5262','ornare@velit.ca','1993-01-15','female','59919393924679','1629112524799',' HJEVJJAU',227497,'Cleaning lady','res/images/Employees/cleaning-lady1.jpg','false');
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted) VALUES ('Brock','Stanley','2665 Ut Ave','10808','Petit-Thier','Palau','+86 196 358 0158','mauris@tincidunt.com','1981-01-05','female','9217481462711','1650023012699',' UAOFLBBA',186386,'Technician','res/images/Employees/technician1.jpg','false');
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted) VALUES ('Chase','Massey','300-7312 Elit, Avenue','8618','Swan Hills','Cayman Islands','+04 426 244 7077','Etiam.laoreet.libero@nonmassa.org','1977-05-11','female','52348284854424','1628051429499',' DTGTUWNS',212684,'Technician','res/images/Employees/technician2.jpg','false');
INSERT INTO Employee (name,surname,address,zip,place,country,phone,email,bdate,sex,svnr,iban,bic,salary,rolle,picture,is_deleted) VALUES ('Preston','Hardy','1947 Aliquet Ave','4645','Ospedaletto Lodigiano','Puerto Rico','+68 236 293 2343','Mauris.blandit@ultricies.edu','1976-11-27','male','744114141i659944474','1640061893399',' JOALYSVX',165798,'Technician','res/images/Employees/technician3.jpg','false');

-- Credit cards
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('5140319309135360','Christen Holden','Maestro',8,2017,'719');
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('5569709211500572','Erica Bruce','Maestro',6,2024,'762');
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('5171957269378728','Iliana Newman','Maestro',8,2042,'919');
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('5430798289525956','Madison Reyes','Mastercard',2,2021,'684');
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('5176587594909577','Paul Allison','Maestro',4,2029,'928');
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('5128925821663808','Farrah Cherry','Maestro',7,2049,'649');
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('5137293916434000','Hillary Wolfe','Mastercard',10,2037,'213');
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('5594928521612401','Shellie Sawyer','Mastercard',11,2026,'811');
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('5426460499889591','Dora Cervantes','Visa',11,2050,'902');
INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES ('4242 4242 4242 4242','Renee Mullins','Visa',8,2038,'733');

-- Customers ,they begin with 3000
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Brenna','Morgan','P.O. Box 550, 8588 Donec St.','803722','Stintino','Qatar','+95 777 477 7458','Proin.ultrices.Duis@dignissimlacus.ca','1990-01-19','female',35867597,'5140319309135360','no note','false',0);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Meredith','Webster','470-9745 Sem, Ave','873229','Patarrá','Uruguay','+95 322 170 9008','nisi@Aliquam.org','1950-06-01','female',36774644,'5569709211500572','no note','false',0);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Hyatt','Reyes','932-8435 Elit. Avenue','5263','Olsztyn','Bermuda','+59 626 047 7021','ornare.facilisis@orci.edu','1951-01-23','male',39299364,'5171957269378728','no note','false',0);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Carl','Holden','Ap #136-3202 Erat Street','72883','Akron','Oman','+08 524 985 4100','in.felis@morbi.net','1957-01-21','female',35802915,'5430798289525956','no note','true',0);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Donovan','Chaney','312-1897 Lacinia Rd.','665674','Kawartha Lakes','Saint Vincent and The Grenadines','+25 383 769 1518','Fusce@ornaresagittis.org','1953-10-26','male',39019565,'5176587594909577','no note','true',0);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Colt','Hartman','913-2120 Facilisis Rd.','32-494','Chambord','Armenia','+15 828 028 6217','cursus.vestibulum@incursuset.co.uk','1979-10-19','female',33699826,'5128925821663808','no note','true',0);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Yoshi','Rocha','4023 Enim. Ave','89039','Raipur','Curaçao','+46 063 788 0579','et.commodo.at@nequepellentesquemassa.ca','1964-09-01','male',37722147,'5137293916434000','no note','false',0);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Bell','Gill','Ap #849-690 Consequat Rd.','45-745','Seevetal','Nepal','+61 405 351 6819','auctor.non@Aliquamvulputateullamcorper.ca','1951-02-11','female',36576973,'5594928521612401','no note','false',0);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Raphael','Delgado','6073 Sapien, Ave','3049','Pondicherry','Finland','+86 112 791 4115','dapibus.id@mattis.edu','1978-06-30','female',38053188,'5426460499889591','no note','false',0);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Credit_Card,Note,Newsletter,rid) VALUES ('Stewart','Griffin','558-5204 Magna. Ave','492332','Nanded','Mongolia','+06 795 529 3281','amet.consectetuer.adipiscing@aaliquet.com','1965-08-22','male',39370410,'4242 4242 4242 4242','no note','true',0);

-- Reservations , they begin with 2000
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3000, '2017-07-01', '2017-07-07', 30000, false, false);
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3001, '2017-07-13', '2017-07-16', 42000, false, false);
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3002, '2017-07-19', '2017-07-28', 58000, false, false);
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3003, '2017-08-02', '2017-08-10', 63700, true, false);
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3004, '2017-09-20', '2017-09-30', 45000, false, false);
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3005, '2017-07-29', '2017-08-07', 40300, false, false);
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3006, '2017-08-21', '2017-08-27', 54000, false, false);
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3007, '2017-06-30', '2017-07-05', 37000, false, false);
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3008, '2017-10-04', '2017-10-22', 126000, false, false);
INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid, is_canceled) VALUES(3009, '2017-10-09', '2017-10-17', 40000, false, false);

-- Guests
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Halee','Chang','702-9075 Aenean St.','T5T 2B0','Hamm','Cameroon','+29 746 000 4893','parturient@dapibusrutrum.co.uk','1995-03-04','female',34672937,'no note','true',2001);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Jonas','Shannon','P.O. Box 135, 1145 Massa. Av.','45625','Palestrina','Canada','+65 190 584 0190','consectetuer@elitfermentum.ca','1977-08-15','male',33720444,'no note','true',2001);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Lev','Turner','P.O. Box 582, 8207 Scelerisque Av.','632001','Jasper','Antigua and Barbuda','+12 928 945 5330','euismod.enim.Etiam@orciUtsemper.co.uk','1994-12-04','female',36265481,'no note','true',2002);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Keefe','Young','326-4751 Senectus Av.','AE6 9JW','Steendorp','Lithuania','+49 288 569 6553','Duis@eros.edu','1988-09-23','female',32974476,'no note','true',2003);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Caesar','Bartlett','Ap #778-5013 Vulputate Avenue','42515','Fort Saskatchewan','Togo','+30 053 753 8057','Phasellus.at.augue@aceleifendvitae.ca','1993-02-15','male',34229233,'no note','false',2003);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Cara','Acosta','6541 Augue Avenue','30218','Coquitlam','Belgium','+61 837 029 3493','lectus@metusIn.edu','1963-10-05','male',36463791,'no note','true',2003);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Paloma','Morse','772-8816 Nisi Av.','41229-994','Sint-Pieters-Woluwe','Mauritius','+67 981 252 8584','lacinia@tristiquepellentesquetellus.edu','1971-01-02','male',39300600,'no note','true',2006);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Ginger','Guerra','Ap #378-4708 Ornare, Rd.','8806LS','Wallasey','Ghana','+65 304 885 3649','dolor.elit.pellentesque@odioapurus.edu','1981-11-27','male',38818911,'no note','true',2006);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Beau','Taylor','542-6108 Enim St.','HC4M 7QA','Valkenburg aan de Geul','Bonaire, Sint Eustatius and Saba','+21 289 093 1959','Nulla@aultriciesadipiscing.net','1977-08-17','male',36641473,'no note','true',2007);
INSERT INTO Customer (name,surname,address,zip,place,country,phone,email,bdate,sex,Identification,Note,Newsletter,rid) VALUES ('Cody','Houston','240-4566 Lorem Road','15564','Sagrada Familia','Liechtenstein','+43 292 194 3084','montes.nascetur@pretium.ca','1963-02-13','male',33242578,'no note','true',2008);

-- Service Reservations
INSERT INTO Service_reservation(SRID, RID, on_date) VALUES (1, 2001, '2017-07-13');
INSERT INTO Service_reservation(SRID, RID, on_date) VALUES (2, 2001, '2017-07-16');
INSERT INTO Service_reservation(SRID, RID, on_date) VALUES (4, 2007, '2017-07-03');
INSERT INTO Service_reservation(SRID, RID, on_date) VALUES (6, 2002, '2017-07-21');
INSERT INTO Service_reservation(SRID, RID, on_date) VALUES (9, 2005, '2017-07-30');
INSERT INTO Service_reservation(SRID, RID, on_date) VALUES (7, 2003, '2017-08-05');

-- Room reservations
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2000, 101, 5000, false);
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2001, 103, 9000, false);
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2002, 104, 7000, false);
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2003, 201, 9000, false);
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2004, 305, 5000, false);
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2005, 202, 5000, false);
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2006, 304, 9000, false);
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2007, 303, 7000, false);
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2008, 203, 7000, false);
INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (2009, 204, 5000, false);

-- Payments
INSERT INTO Payments(Reservation, Paid_Amount, Payment_Method, Payment_date) VALUES (2000, 15000, 'Cash payment', '2017-05-02');
INSERT INTO Payments(Reservation, Paid_Amount, Payment_Method, Payment_date) VALUES (2003, 63700, 'Credit Card', '2017-05-03');
INSERT INTO Payments(Reservation, Paid_Amount, Payment_Method, Payment_date) VALUES (2007, 20000, 'Credit Card', '2017-04-29');
