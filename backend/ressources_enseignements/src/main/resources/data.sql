-- =========================
-- NETTOYAGE (ordre FK)
-- =========================

DELETE FROM assignment;
DELETE FROM tickets;
DELETE FROM ressourcessyllabus;
DELETE FROM formationressources;
DELETE FROM userformation;
DELETE FROM userrole;

DELETE FROM ressources;
DELETE FROM syllabus;
DELETE FROM formation;
DELETE FROM role;
DELETE FROM users;

-- =========================
-- RESET AUTO_INCREMENT
-- =========================
ALTER TABLE assignment AUTO_INCREMENT = 1;
ALTER TABLE tickets AUTO_INCREMENT = 1;
ALTER TABLE ressources AUTO_INCREMENT = 1;
ALTER TABLE syllabus AUTO_INCREMENT = 1;
ALTER TABLE formation AUTO_INCREMENT = 1;
ALTER TABLE role AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 1;

-- =========================
-- ROLES
-- =========================
INSERT INTO role (idrole, rights, title) VALUES
(1, 'ADMIN', b'1'),
(2, 'TEACHER', b'0'),
(3, 'STUDENT', b'0');

-- =========================
-- USERS
-- =========================
INSERT INTO users (iduser, address, email, firstname, lastname, password, phonenumber, servicevalidation, username) VALUES
(1, '1 rue Admin', 'admin@mail.com', 'Alice', 'Admin', 'admin123', '0101010101', b'1', 'admin'),
(2, '2 rue Prof', 'prof@mail.com', 'Bob', 'Teacher', 'prof123', '0202020202', b'1', 'prof'),
(3, '3 rue Etudiant', 'student@mail.com', 'Charlie', 'Student', 'student123', '0303030303', b'0', 'student');

-- =========================
-- USER ROLE
-- =========================
INSERT INTO userrole (idrole, iduser) VALUES
(1, 1),
(2, 2),
(3, 3);

-- =========================
-- FORMATIONS
-- =========================
INSERT INTO formation (idformation, name, description) VALUES
(1, 'Informatique', 'Informatique est cool!'),
(2, 'Mathématiques', 'Maths sont cool!'),
(3, 'Physique', 'Physique est intéressant!');

-- =========================
-- USER FORMATION
-- =========================
INSERT INTO userformation (idformation, iduser) VALUES
(1, 2),
(1, 3),
(2, 3);

-- =========================
-- RESSOURCES
-- =========================
INSERT INTO ressources (
    idressource, description,
    heure_cm_etat, heure_cm_iut,
    heure_td_etat, heure_td_iut,
    heure_tp_etat, heure_tp_iut,
    title
) VALUES
(1, 'Cours Java', '10', '12', '8', '10', '6', '8', 'Java'),
(2, 'Algèbre linéaire', '12', '14', '10', '12', '8', '10', 'Maths'),
(3, 'Mécanique classique', '8', '10', '6', '8', '4', '6', 'Physique');

-- =========================
-- FORMATION RESSOURCES
-- =========================
INSERT INTO formationressources (idformation, idressource) VALUES
(1, 1),
(2, 2),
(3, 3);

-- =========================
-- SYLLABUS
-- =========================
INSERT INTO syllabus (idsyllabus, descriptions) VALUES
(1, 'Syllabus Java'),
(2, 'Syllabus Maths'),
(3, 'Syllabus Physique');

-- =========================
-- RESSOURCES SYLLABUS
-- =========================
INSERT INTO ressourcessyllabus (idressource, idsyllabus) VALUES
(1, 1),
(2, 2),
(3, 3);

-- =========================
-- ASSIGNMENT
-- =========================
INSERT INTO assignment (id_assignment, assignedtimes, lessontype, idressource, iduser) VALUES
(1, 5, 'CM', 1, 2),
(2, 3, 'TD', 2, 2),
(3, 2, 'TP', 3, 3);

-- =========================
-- TICKETS
-- =========================
INSERT INTO tickets (idticket, date, description, statue, title, iduser) VALUES
(1, '2025-12-17', 'Problème Java', 'OPEN', 'Ticket Java', 3),
(2, '2025-12-17', 'Question Maths', 'CLOSED', 'Ticket Maths', 3);
