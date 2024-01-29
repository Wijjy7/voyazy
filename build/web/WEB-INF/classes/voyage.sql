/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Family
 * Created: Dec 12, 2023
 */

create database voyage ; 
\c voyage
create table activite (
    id serial primary key  , 
    activite varchar(100)

);
ALTER TABLE activite add Column prix double precision;

INSERT INTO Activite VALUES(default,'Activite 1',1000);
INSERT INTO Activite VALUES(default,'Activite 2',2000);
INSERT INTO Activite VALUES(default,'Activite 3',1000);

create table typeLieu (
    id serial primary key  , 
    type varchar(100)

);

create table bouquet(
    id serial primary key, 
    bouquet varchar(100)

);
INSERT INTO bouquet VALUES(default,'Bouquet 1');
INSERT INTO bouquet VALUES(default,'Bouquet 2');
INSERT INTO bouquet VALUES(default,'Bouquet 3');


create table lieu  (
    id serial primary key  , 
    lieu varchar(100),
    idtypelieu int , 
    foreign key(idtypelieu) references typelieu(id) 
    
);

create table voyage (
    id serial primary key  , 
    duree int,
    idlieu int ,
    foreign key(idlieu) references lieu(id),
    idbouquet int , 
    foreign key(idbouquet) references bouquet(id),
    prix decimal 
);
ALTER TABLE voyage add column nom varchar(255);

create table activiteBouquet (
    idbouquet int,
    foreign key(idbouquet) references bouquet(id),
    idactivite int,
    foreign key(idactivite) references activite(id)
);
INSERT INTO activiteBouquet VALUES(1,1);
INSERT INTO activiteBouquet VALUES(1,2);
INSERT INTO activiteBouquet VALUES(1,3);

INSERT INTO activiteBouquet VALUES(2,3);
INSERT INTO activiteBouquet VALUES(2,4);

INSERT INTO activiteBouquet VALUES(3,4);


create table quantiteActivite(
    idVoyage int , 
    foreign key (idVoyage) references voyage(id),
    idActivite int ,
    foreign key (idActivite) references activite(id),
    quantite int , 
    duree decimal 
);

create or replace view  v_actBouquet as select activiteBouquet.idBouquet ,activite.id as idactivite , activite.activite ,activite.prix as prix from activiteBouquet join activite on activiteBouquet.idactivite = activite.id ;

CREATE TABLE Stock(
    id serial PRIMARY KEY,
    idActivite int,
    qte int,
    date date,
    
    FOREIGN KEY(idActivite) REFERENCES activite(id)
);
CREATE TABLE Reservation(
    id serial PRIMARY KEY,
    idVoyage int,
    nomClient VARCHAR(255),
    date date,

    FOREIGN KEY(idVoyage) REFERENCES voyage(id)
);
ALTER TABLE reservation add column qte int;

CREATE VIEW reservationActivite as
SELECT r.id,r.idvoyage,r.nomclient,r.date,q.idactivite,q.quantite*r.qte as qte
FROM reservation as r
JOIN quantiteactivite as q on q.idvoyage=r.idvoyage

CREATE VIEW rATotal as
SELECT idactivite,sum(qte) as qte from reservationActivite group by idactivite;

CREATE VIEW stockTotal as
SELECT idactivite , sum(qte) as qte from stock group by idactivite

CREATE OR REPLACE VIEW resteStock as 
SELECT s.idActivite ,COALESCE( s.qte-r.qte , s.qte)as qte
FROM stocktotal as s
LEFT JOIN rAtotal as r on r.idactivite=s.idactivite;

CREATE VIEW resteStockTotal as
SELECT a.id , a.activite , prix , COALESCE(r.qte,0) as qte
FROM activite as a
LEFT JOIN resteStock as r on r.idactivite=a.id

-- 16-01-24
CREATE TABLE duree(
    id serial PRIMARY KEY,
    nom VARCHAR(25)
);
INSERT INTO duree VALUES(1,'Courte');
INSERT INTO duree VALUES(2,'Moyenne');


CREATE TABLE Poste(
    id serial PRIMARY KEY,
    poste VARCHAR(255)
);
ALTER TABLE poste add column coeff int;

INSERT INTO Poste VALUES(1,'Agent',1);
INSERT INTO Poste VALUES(2,'Senior',2);
INSERT INTO Poste VALUES(3,'Expert',3);


CREATE TABLE Employer(
    id serial PRIMARY KEY,
    nom VARCHAR(255),
    salaire double precision,
    IdPoste int,
    FOREIGN KEY(idposte) REFERENCES poste(id)
);
ALTER TABLE Employer add column embauche date; 

INSERT INTO Employer VALUES (default,'E1',1000,1,'2022-01-01');
INSERT INTO Employer VALUES (default,'E2',1200,2,'2022-01-01');
INSERT INTO Employer VALUES (default,'E3',1100,3,'2022-01-01');



create table quantiteActivite(
    idVoyage int , 
    foreign key (idVoyage) references voyage(id),
    idActivite int ,
    foreign key (idActivite) references activite(id),
    quantite int , 
    duree decimal 
);

CREATE TABLE ActiviteEmployer(
    id serial PRIMARY KEY,
    idactivite int,
    idemployer int,

    FOREIGN KEY (idactivite) REFERENCES activite(id),
    FOREIGN KEY (idemployer) REFERENCES employer(id)
);


CREATE OR REPLACE VIEW voyageEmployer as
SELECT q.idvoyage,q.idactivite , a.idemployer , e.salaire*q.duree as salaire
FROM QUANTITEACTIVITE as q
JOIN ActiviteEmployer as a ON q.idactivite=a.idactivite
JOIN Employer as e ON e.id=a.idemployer;

CREATE VIEW prixEMployerVoyage as
SELECT idvoyage , sum(salaire) as salaire
FROM voyageEmployer 
GROUP BY IDVOYAGE;
 
CREATE VIEW prixEmployerVoyageDuree as 
SELECT p.idvoyage , p.salaire*v.duree as salaire
FROM prixEmployerVoyage as p 
JOIN voyage as v on v.id=p.idvoyage;


CREATE VIEW employerPoste as
SELECT e.id , e.idposte , e.nom , p.poste , e.embauche , e.salaire*p.coeff as salaire
FROM employer as e
JOIN poste as p 
    ON p.id=e.idposte;


