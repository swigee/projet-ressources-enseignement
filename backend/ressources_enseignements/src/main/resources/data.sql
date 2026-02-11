-- =========================
-- NETTOYAGE DES ANCIENNES TABLES (Legacy)
-- =========================
DROP TABLE IF EXISTS ressourcessyllabus;
DROP TABLE IF EXISTS formationressources;
DROP TABLE IF EXISTS userformation;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS ressources;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS app_role;

-- =========================
-- ROLES
-- =========================
INSERT INTO position (id, name, is_active) VALUES
(1, 'ADMIN', b'1'),
(2, 'TEACHER', b'0'),
(3, 'STUDENT', b'0');

-- =========================
-- USERS
-- =========================
INSERT INTO user (id, address, email, first_name, last_name, password, phone_number, validation_status, username) VALUES
(1, '1 rue Admin', 'admin@mail.com', 'Alice', 'Admin', 'admin123', '0101010101', 'NONE', 'admin'),
(2, '2 rue Prof', 'prof@mail.com', 'Bob', 'Teacher', 'prof123', '0202020202', 'VALIDATED', 'prof'),
(3, '3 rue Etudiant', 'student@mail.com', 'Charlie', 'Student', 'student123', '0303030303', 'NONE', 'student'),
(4, '10 rue des Lilas', 'martin.dupont@univ.fr', 'Martin', 'Dupont', 'pass123', '0604010101', 'NONE', 'mdupont'),
(5, '12 avenue Pasteur', 'sophie.bernard@univ.fr', 'Sophie', 'Bernard', 'pass123', '0604020202', 'NONE', 'sbernard'),
(6, '5 boulevard Victor Hugo', 'jean.moreau@univ.fr', 'Jean', 'Moreau', 'pass123', '0604030303', 'NONE', 'jmoreau'),
(7, '8 rue de la Paix', 'claire.petit@univ.fr', 'Claire', 'Petit', 'pass123', '0604040404', 'NONE', 'cpetit'),
(8, '3 place de la Gare', 'pierre.leroy@univ.fr', 'Pierre', 'Leroy', 'pass123', '0604050505', 'NONE', 'pleroy'),
(9, '15 rue Voltaire', 'nathalie.roux@univ.fr', 'Nathalie', 'Roux', 'pass123', '0604060606', 'NONE', 'nroux'),
(10, '22 avenue de la Republique', 'francois.garcia@univ.fr', 'Francois', 'Garcia', 'pass123', '0604070707', 'NONE', 'fgarcia'),
(11, '7 rue Descartes', 'isabelle.martinez@univ.fr', 'Isabelle', 'Martinez', 'pass123', '0604080808', 'NONE', 'imartinez'),
(12, '9 rue Pascal', 'david.thomas@univ.fr', 'David', 'Thomas', 'pass123', '0604090909', 'NONE', 'dthomas'),
(13, '18 avenue Foch', 'emilie.robert@univ.fr', 'Emilie', 'Robert', 'pass123', '0604101010', 'NONE', 'erobert'),
(14, '4 rue Moliere', 'philippe.richard@univ.fr', 'Philippe', 'Richard', 'pass123', '0604111111', 'NONE', 'prichard'),
(15, '11 boulevard Gambetta', 'anne.dubois@univ.fr', 'Anne', 'Dubois', 'pass123', '0604121212', 'NONE', 'adubois');

-- =========================
-- USER ROLE
-- =========================
INSERT INTO user_role (role_id, user_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(2, 8),
(2, 9),
(2, 10),
(2, 11),
(2, 12),
(2, 13),
(2, 14),
(2, 15);

-- =========================
-- FORMATIONS (BUT Informatique)
-- =========================
INSERT INTO formation (id, name, year, class_name) VALUES
(1, 'Informatique', '1', 'Classe A'),
(2, 'Informatique', '1', 'Classe B'),
(3, 'Informatique', '2', 'Classe A'),
(4, 'Informatique', '2', 'Classe B'),
(5, 'Informatique', '3', 'Classe A'),
(6, 'Informatique', '3', 'Classe B');

-- =========================
-- USER FORMATION
-- =========================
INSERT INTO user_formation (formation_id, user_id) VALUES
(1, 2),
(3, 2),
(1, 3),
(1, 4), (2, 4), (3, 4),
(1, 5), (2, 5),
(1, 6), (3, 6),
(1, 7), (2, 7),
(3, 8), (4, 8),
(1, 9), (3, 9),
(5, 10), (6, 10),
(1, 11), (2, 11), (3, 11),
(3, 12), (4, 12),
(5, 13), (6, 13),
(1, 14), (2, 14),
(3, 15), (5, 15);


-- =========================
-- RESSOURCES (37 modules BUT Informatique)
-- =========================
INSERT INTO resource (
    id, title, description, category, is_highlighted,
    cm_state_hours, cm_iut_hours, td_state_hours, td_iut_hours, tp_state_hours, tp_iut_hours,
    hours_per_week, hours_per_half_group, semester
) VALUES

-- ========== ANNEE 1 - SEMESTRE 1 (6 modules) ==========
(1, 'Initiation au developpement', 'Apprentissage des bases de la programmation', 'Programmation', false,
 20, 0, 30, 0, 50, 0,
 '{"1":{"cm":1,"td":2,"tp":3,"total":6},"2":{"cm":1,"td":2,"tp":3,"total":6},"3":{"cm":1,"td":2,"tp":3,"total":6},"4":{"cm":1,"td":2,"tp":3,"total":6},"5":{"cm":1,"td":2,"tp":3,"total":6},"6":{"cm":1,"td":2,"tp":3,"total":6},"7":{"cm":1,"td":2,"tp":3,"total":6},"8":{"cm":1,"td":2,"tp":3,"total":6},"9":{"cm":1,"td":2,"tp":3,"total":6},"10":{"cm":1,"td":2,"tp":3,"total":6},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":1,"td":1,"tp":2,"total":4},"17":{"cm":1,"td":1,"tp":2,"total":4},"18":{"cm":1,"td":1,"tp":2,"total":4},"19":{"cm":1,"td":1,"tp":2,"total":4},"20":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 1),

(2, 'Bases de donnees', 'Introduction aux bases de donnees relationnelles', 'Data', false,
 15, 0, 25, 0, 40, 0,
 '{"1":{"cm":1,"td":2,"tp":2,"total":5},"2":{"cm":1,"td":2,"tp":2,"total":5},"3":{"cm":1,"td":2,"tp":2,"total":5},"4":{"cm":1,"td":2,"tp":2,"total":5},"5":{"cm":1,"td":2,"tp":2,"total":5},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":0,"td":1,"tp":2,"total":3},"17":{"cm":0,"td":1,"tp":2,"total":3},"18":{"cm":0,"td":1,"tp":2,"total":3},"19":{"cm":0,"td":1,"tp":2,"total":3},"20":{"cm":0,"td":1,"tp":2,"total":3}}',
 0, 1),

(3, 'Architecture des ordinateurs', 'Fonctionnement interne des ordinateurs', 'Systemes', false,
 20, 0, 20, 0, 20, 0,
 '{"1":{"cm":1,"td":1,"tp":1,"total":3},"2":{"cm":1,"td":1,"tp":1,"total":3},"3":{"cm":1,"td":1,"tp":1,"total":3},"4":{"cm":1,"td":1,"tp":1,"total":3},"5":{"cm":1,"td":1,"tp":1,"total":3},"6":{"cm":1,"td":1,"tp":1,"total":3},"7":{"cm":1,"td":1,"tp":1,"total":3},"8":{"cm":1,"td":1,"tp":1,"total":3},"9":{"cm":1,"td":1,"tp":1,"total":3},"10":{"cm":1,"td":1,"tp":1,"total":3},"11":{"cm":1,"td":1,"tp":1,"total":3},"12":{"cm":1,"td":1,"tp":1,"total":3},"13":{"cm":1,"td":1,"tp":1,"total":3},"14":{"cm":1,"td":1,"tp":1,"total":3},"15":{"cm":1,"td":1,"tp":1,"total":3},"16":{"cm":1,"td":1,"tp":1,"total":3},"17":{"cm":1,"td":1,"tp":1,"total":3},"18":{"cm":1,"td":1,"tp":1,"total":3},"19":{"cm":1,"td":1,"tp":1,"total":3},"20":{"cm":1,"td":1,"tp":1,"total":3}}',
 0, 1),

(4, 'Mathematiques discretes', 'Logique, ensembles, graphes', 'Mathematiques', false,
 30, 0, 40, 0, 10, 0,
 '{"1":{"cm":2,"td":2,"tp":1,"total":5},"2":{"cm":2,"td":2,"tp":1,"total":5},"3":{"cm":2,"td":2,"tp":1,"total":5},"4":{"cm":2,"td":2,"tp":1,"total":5},"5":{"cm":2,"td":2,"tp":1,"total":5},"6":{"cm":2,"td":2,"tp":1,"total":5},"7":{"cm":2,"td":2,"tp":1,"total":5},"8":{"cm":2,"td":2,"tp":1,"total":5},"9":{"cm":2,"td":2,"tp":1,"total":5},"10":{"cm":2,"td":2,"tp":1,"total":5},"11":{"cm":1,"td":2,"tp":0,"total":3},"12":{"cm":1,"td":2,"tp":0,"total":3},"13":{"cm":1,"td":2,"tp":0,"total":3},"14":{"cm":1,"td":2,"tp":0,"total":3},"15":{"cm":1,"td":2,"tp":0,"total":3},"16":{"cm":1,"td":2,"tp":0,"total":3},"17":{"cm":1,"td":2,"tp":0,"total":3},"18":{"cm":1,"td":2,"tp":0,"total":3},"19":{"cm":1,"td":2,"tp":0,"total":3},"20":{"cm":1,"td":2,"tp":0,"total":3}}',
 0, 1),

(5, 'Introduction aux systemes', 'Systemes d''exploitation et ligne de commande', 'Systemes', false,
 10, 0, 15, 0, 25, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":1,"total":3},"7":{"cm":1,"td":1,"tp":1,"total":3},"8":{"cm":1,"td":1,"tp":1,"total":3},"9":{"cm":1,"td":1,"tp":1,"total":3},"10":{"cm":1,"td":1,"tp":1,"total":3},"11":{"cm":0,"td":1,"tp":1,"total":2},"12":{"cm":0,"td":1,"tp":1,"total":2},"13":{"cm":0,"td":1,"tp":1,"total":2},"14":{"cm":0,"td":1,"tp":1,"total":2},"15":{"cm":0,"td":1,"tp":1,"total":2},"16":{"cm":0,"td":0,"tp":1,"total":1},"17":{"cm":0,"td":0,"tp":1,"total":1},"18":{"cm":0,"td":0,"tp":1,"total":1},"19":{"cm":0,"td":0,"tp":1,"total":1},"20":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 1),

(6, 'Gestion de projet', 'Methodes de gestion de projet informatique', 'Transversal', false,
 10, 0, 20, 0, 10, 0,
 '{"1":{"cm":1,"td":1,"tp":1,"total":3},"2":{"cm":1,"td":1,"tp":1,"total":3},"3":{"cm":1,"td":1,"tp":1,"total":3},"4":{"cm":1,"td":1,"tp":1,"total":3},"5":{"cm":1,"td":1,"tp":1,"total":3},"6":{"cm":1,"td":1,"tp":1,"total":3},"7":{"cm":1,"td":1,"tp":1,"total":3},"8":{"cm":1,"td":1,"tp":1,"total":3},"9":{"cm":1,"td":1,"tp":1,"total":3},"10":{"cm":1,"td":1,"tp":1,"total":3},"11":{"cm":0,"td":1,"tp":0,"total":1},"12":{"cm":0,"td":1,"tp":0,"total":1},"13":{"cm":0,"td":1,"tp":0,"total":1},"14":{"cm":0,"td":1,"tp":0,"total":1},"15":{"cm":0,"td":1,"tp":0,"total":1},"16":{"cm":0,"td":1,"tp":0,"total":1},"17":{"cm":0,"td":1,"tp":0,"total":1},"18":{"cm":0,"td":1,"tp":0,"total":1},"19":{"cm":0,"td":1,"tp":0,"total":1},"20":{"cm":0,"td":1,"tp":0,"total":1}}',
 0, 1),

-- ========== ANNEE 1 - SEMESTRE 2 (6 modules) ==========
(7, 'Programmation orientee objet', 'Concepts OOP en Java', 'Programmation', false,
 25, 0, 35, 0, 60, 0,
 '{"21":{"cm":2,"td":2,"tp":3,"total":7},"22":{"cm":2,"td":2,"tp":3,"total":7},"23":{"cm":2,"td":2,"tp":3,"total":7},"24":{"cm":2,"td":2,"tp":3,"total":7},"25":{"cm":2,"td":2,"tp":3,"total":7},"26":{"cm":1,"td":2,"tp":3,"total":6},"27":{"cm":1,"td":2,"tp":3,"total":6},"28":{"cm":1,"td":2,"tp":3,"total":6},"29":{"cm":1,"td":2,"tp":3,"total":6},"30":{"cm":1,"td":2,"tp":3,"total":6},"31":{"cm":1,"td":2,"tp":3,"total":6},"32":{"cm":1,"td":2,"tp":3,"total":6},"33":{"cm":1,"td":2,"tp":3,"total":6},"34":{"cm":1,"td":2,"tp":3,"total":6},"35":{"cm":1,"td":2,"tp":3,"total":6},"36":{"cm":1,"td":1,"tp":3,"total":5},"37":{"cm":1,"td":1,"tp":3,"total":5},"38":{"cm":1,"td":1,"tp":3,"total":5},"39":{"cm":1,"td":1,"tp":3,"total":5},"40":{"cm":1,"td":1,"tp":3,"total":5}}',
 0, 2),

(8, 'Developpement web', 'HTML, CSS, JavaScript et frameworks', 'Web', false,
 20, 0, 30, 0, 70, 0,
 '{"21":{"cm":1,"td":2,"tp":4,"total":7},"22":{"cm":1,"td":2,"tp":4,"total":7},"23":{"cm":1,"td":2,"tp":4,"total":7},"24":{"cm":1,"td":2,"tp":4,"total":7},"25":{"cm":1,"td":2,"tp":4,"total":7},"26":{"cm":1,"td":2,"tp":4,"total":7},"27":{"cm":1,"td":2,"tp":4,"total":7},"28":{"cm":1,"td":2,"tp":4,"total":7},"29":{"cm":1,"td":2,"tp":4,"total":7},"30":{"cm":1,"td":2,"tp":4,"total":7},"31":{"cm":1,"td":1,"tp":3,"total":5},"32":{"cm":1,"td":1,"tp":3,"total":5},"33":{"cm":1,"td":1,"tp":3,"total":5},"34":{"cm":1,"td":1,"tp":3,"total":5},"35":{"cm":1,"td":1,"tp":3,"total":5},"36":{"cm":1,"td":1,"tp":3,"total":5},"37":{"cm":1,"td":1,"tp":3,"total":5},"38":{"cm":1,"td":1,"tp":3,"total":5},"39":{"cm":1,"td":1,"tp":3,"total":5},"40":{"cm":1,"td":1,"tp":3,"total":5}}',
 0, 2),

(9, 'Algorithmique', 'Structures de donnees et algorithmes', 'Programmation', false,
 25, 0, 45, 0, 30, 0,
 '{"21":{"cm":2,"td":3,"tp":2,"total":7},"22":{"cm":2,"td":3,"tp":2,"total":7},"23":{"cm":2,"td":3,"tp":2,"total":7},"24":{"cm":2,"td":3,"tp":2,"total":7},"25":{"cm":2,"td":3,"tp":2,"total":7},"26":{"cm":1,"td":2,"tp":2,"total":5},"27":{"cm":1,"td":2,"tp":2,"total":5},"28":{"cm":1,"td":2,"tp":2,"total":5},"29":{"cm":1,"td":2,"tp":2,"total":5},"30":{"cm":1,"td":2,"tp":2,"total":5},"31":{"cm":1,"td":2,"tp":1,"total":4},"32":{"cm":1,"td":2,"tp":1,"total":4},"33":{"cm":1,"td":2,"tp":1,"total":4},"34":{"cm":1,"td":2,"tp":1,"total":4},"35":{"cm":1,"td":2,"tp":1,"total":4},"36":{"cm":1,"td":2,"tp":1,"total":4},"37":{"cm":1,"td":2,"tp":1,"total":4},"38":{"cm":1,"td":2,"tp":1,"total":4},"39":{"cm":1,"td":2,"tp":1,"total":4},"40":{"cm":1,"td":2,"tp":1,"total":4}}',
 0, 2),

(10, 'Reseaux', 'Fondamentaux des reseaux informatiques', 'Reseaux', false,
 15, 0, 25, 0, 30, 0,
 '{"21":{"cm":1,"td":2,"tp":2,"total":5},"22":{"cm":1,"td":2,"tp":2,"total":5},"23":{"cm":1,"td":2,"tp":2,"total":5},"24":{"cm":1,"td":2,"tp":2,"total":5},"25":{"cm":1,"td":2,"tp":2,"total":5},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":1,"total":3},"32":{"cm":1,"td":1,"tp":1,"total":3},"33":{"cm":1,"td":1,"tp":1,"total":3},"34":{"cm":1,"td":1,"tp":1,"total":3},"35":{"cm":1,"td":1,"tp":1,"total":3},"36":{"cm":0,"td":1,"tp":1,"total":2},"37":{"cm":0,"td":1,"tp":1,"total":2},"38":{"cm":0,"td":1,"tp":1,"total":2},"39":{"cm":0,"td":1,"tp":1,"total":2},"40":{"cm":0,"td":1,"tp":1,"total":2}}',
 0, 2),

(11, 'Probabilites et statistiques', 'Probabilites, statistiques descriptives', 'Mathematiques', false,
 20, 0, 30, 0, 10, 0,
 '{"21":{"cm":1,"td":2,"tp":1,"total":4},"22":{"cm":1,"td":2,"tp":1,"total":4},"23":{"cm":1,"td":2,"tp":1,"total":4},"24":{"cm":1,"td":2,"tp":1,"total":4},"25":{"cm":1,"td":2,"tp":1,"total":4},"26":{"cm":1,"td":2,"tp":1,"total":4},"27":{"cm":1,"td":2,"tp":1,"total":4},"28":{"cm":1,"td":2,"tp":1,"total":4},"29":{"cm":1,"td":2,"tp":1,"total":4},"30":{"cm":1,"td":2,"tp":1,"total":4},"31":{"cm":1,"td":1,"tp":0,"total":2},"32":{"cm":1,"td":1,"tp":0,"total":2},"33":{"cm":1,"td":1,"tp":0,"total":2},"34":{"cm":1,"td":1,"tp":0,"total":2},"35":{"cm":1,"td":1,"tp":0,"total":2},"36":{"cm":1,"td":1,"tp":0,"total":2},"37":{"cm":1,"td":1,"tp":0,"total":2},"38":{"cm":1,"td":1,"tp":0,"total":2},"39":{"cm":1,"td":1,"tp":0,"total":2},"40":{"cm":1,"td":1,"tp":0,"total":2}}',
 0, 2),

(12, 'Projet tutore S2', 'Projet en equipe du semestre 2', 'Projet', false,
 0, 0, 10, 0, 30, 0,
 '{"21":{"cm":0,"td":1,"tp":2,"total":3},"22":{"cm":0,"td":1,"tp":2,"total":3},"23":{"cm":0,"td":1,"tp":2,"total":3},"24":{"cm":0,"td":1,"tp":2,"total":3},"25":{"cm":0,"td":1,"tp":2,"total":3},"26":{"cm":0,"td":1,"tp":2,"total":3},"27":{"cm":0,"td":1,"tp":2,"total":3},"28":{"cm":0,"td":1,"tp":2,"total":3},"29":{"cm":0,"td":1,"tp":2,"total":3},"30":{"cm":0,"td":1,"tp":2,"total":3},"31":{"cm":0,"td":0,"tp":1,"total":1},"32":{"cm":0,"td":0,"tp":1,"total":1},"33":{"cm":0,"td":0,"tp":1,"total":1},"34":{"cm":0,"td":0,"tp":1,"total":1},"35":{"cm":0,"td":0,"tp":1,"total":1},"36":{"cm":0,"td":0,"tp":1,"total":1},"37":{"cm":0,"td":0,"tp":1,"total":1},"38":{"cm":0,"td":0,"tp":1,"total":1},"39":{"cm":0,"td":0,"tp":1,"total":1},"40":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 2),

-- ========== ANNEE 2 - SEMESTRE 1 (7 modules) ==========
(13, 'Developpement d''applications', 'Developpement d''applications completes', 'Programmation', false,
 20, 0, 30, 0, 70, 0,
 '{"1":{"cm":1,"td":2,"tp":4,"total":7},"2":{"cm":1,"td":2,"tp":4,"total":7},"3":{"cm":1,"td":2,"tp":4,"total":7},"4":{"cm":1,"td":2,"tp":4,"total":7},"5":{"cm":1,"td":2,"tp":4,"total":7},"6":{"cm":1,"td":2,"tp":4,"total":7},"7":{"cm":1,"td":2,"tp":4,"total":7},"8":{"cm":1,"td":2,"tp":4,"total":7},"9":{"cm":1,"td":2,"tp":4,"total":7},"10":{"cm":1,"td":2,"tp":4,"total":7},"11":{"cm":1,"td":1,"tp":3,"total":5},"12":{"cm":1,"td":1,"tp":3,"total":5},"13":{"cm":1,"td":1,"tp":3,"total":5},"14":{"cm":1,"td":1,"tp":3,"total":5},"15":{"cm":1,"td":1,"tp":3,"total":5},"16":{"cm":1,"td":1,"tp":3,"total":5},"17":{"cm":1,"td":1,"tp":3,"total":5},"18":{"cm":1,"td":1,"tp":3,"total":5},"19":{"cm":1,"td":1,"tp":3,"total":5},"20":{"cm":1,"td":1,"tp":3,"total":5}}',
 0, 1),

(14, 'Bases de donnees avancees', 'SQL avance, NoSQL, optimisation', 'Data', false,
 20, 0, 30, 0, 50, 0,
 '{"1":{"cm":1,"td":2,"tp":3,"total":6},"2":{"cm":1,"td":2,"tp":3,"total":6},"3":{"cm":1,"td":2,"tp":3,"total":6},"4":{"cm":1,"td":2,"tp":3,"total":6},"5":{"cm":1,"td":2,"tp":3,"total":6},"6":{"cm":1,"td":2,"tp":3,"total":6},"7":{"cm":1,"td":2,"tp":3,"total":6},"8":{"cm":1,"td":2,"tp":3,"total":6},"9":{"cm":1,"td":2,"tp":3,"total":6},"10":{"cm":1,"td":2,"tp":3,"total":6},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":1,"td":1,"tp":2,"total":4},"17":{"cm":1,"td":1,"tp":2,"total":4},"18":{"cm":1,"td":1,"tp":2,"total":4},"19":{"cm":1,"td":1,"tp":2,"total":4},"20":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 1),

(15, 'Programmation systeme', 'Programmation bas niveau et systeme', 'Systemes', false,
 25, 0, 25, 0, 50, 0,
 '{"1":{"cm":2,"td":2,"tp":3,"total":7},"2":{"cm":2,"td":2,"tp":3,"total":7},"3":{"cm":2,"td":2,"tp":3,"total":7},"4":{"cm":2,"td":2,"tp":3,"total":7},"5":{"cm":2,"td":2,"tp":3,"total":7},"6":{"cm":1,"td":1,"tp":3,"total":5},"7":{"cm":1,"td":1,"tp":3,"total":5},"8":{"cm":1,"td":1,"tp":3,"total":5},"9":{"cm":1,"td":1,"tp":3,"total":5},"10":{"cm":1,"td":1,"tp":3,"total":5},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":1,"td":1,"tp":2,"total":4},"17":{"cm":1,"td":1,"tp":2,"total":4},"18":{"cm":1,"td":1,"tp":2,"total":4},"19":{"cm":1,"td":1,"tp":2,"total":4},"20":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 1),

(16, 'Qualite de developpement', 'Tests, integration continue, qualite logicielle', 'Programmation', false,
 15, 0, 25, 0, 40, 0,
 '{"1":{"cm":1,"td":2,"tp":2,"total":5},"2":{"cm":1,"td":2,"tp":2,"total":5},"3":{"cm":1,"td":2,"tp":2,"total":5},"4":{"cm":1,"td":2,"tp":2,"total":5},"5":{"cm":1,"td":2,"tp":2,"total":5},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":0,"td":1,"tp":2,"total":3},"17":{"cm":0,"td":1,"tp":2,"total":3},"18":{"cm":0,"td":1,"tp":2,"total":3},"19":{"cm":0,"td":1,"tp":2,"total":3},"20":{"cm":0,"td":1,"tp":2,"total":3}}',
 0, 1),

(17, 'IHM et ergonomie', 'Interfaces homme-machine et experience utilisateur', 'Web', false,
 10, 0, 20, 0, 30, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":0,"td":1,"tp":1,"total":2},"12":{"cm":0,"td":1,"tp":1,"total":2},"13":{"cm":0,"td":1,"tp":1,"total":2},"14":{"cm":0,"td":1,"tp":1,"total":2},"15":{"cm":0,"td":1,"tp":1,"total":2},"16":{"cm":0,"td":1,"tp":1,"total":2},"17":{"cm":0,"td":1,"tp":1,"total":2},"18":{"cm":0,"td":1,"tp":1,"total":2},"19":{"cm":0,"td":1,"tp":1,"total":2},"20":{"cm":0,"td":1,"tp":1,"total":2}}',
 0, 1),

(18, 'Anglais technique', 'Anglais applique a l''informatique', 'Transversal', false,
 0, 0, 20, 0, 0, 0,
 '{"1":{"cm":0,"td":1,"tp":0,"total":1},"2":{"cm":0,"td":1,"tp":0,"total":1},"3":{"cm":0,"td":1,"tp":0,"total":1},"4":{"cm":0,"td":1,"tp":0,"total":1},"5":{"cm":0,"td":1,"tp":0,"total":1},"6":{"cm":0,"td":1,"tp":0,"total":1},"7":{"cm":0,"td":1,"tp":0,"total":1},"8":{"cm":0,"td":1,"tp":0,"total":1},"9":{"cm":0,"td":1,"tp":0,"total":1},"10":{"cm":0,"td":1,"tp":0,"total":1},"11":{"cm":0,"td":1,"tp":0,"total":1},"12":{"cm":0,"td":1,"tp":0,"total":1},"13":{"cm":0,"td":1,"tp":0,"total":1},"14":{"cm":0,"td":1,"tp":0,"total":1},"15":{"cm":0,"td":1,"tp":0,"total":1},"16":{"cm":0,"td":1,"tp":0,"total":1},"17":{"cm":0,"td":1,"tp":0,"total":1},"18":{"cm":0,"td":1,"tp":0,"total":1},"19":{"cm":0,"td":1,"tp":0,"total":1},"20":{"cm":0,"td":1,"tp":0,"total":1}}',
 0, 1),

(19, 'Projet tutore S3', 'Projet en equipe du semestre 3', 'Projet', false,
 0, 0, 10, 0, 40, 0,
 '{"1":{"cm":0,"td":1,"tp":2,"total":3},"2":{"cm":0,"td":1,"tp":2,"total":3},"3":{"cm":0,"td":1,"tp":2,"total":3},"4":{"cm":0,"td":1,"tp":2,"total":3},"5":{"cm":0,"td":1,"tp":2,"total":3},"6":{"cm":0,"td":1,"tp":2,"total":3},"7":{"cm":0,"td":1,"tp":2,"total":3},"8":{"cm":0,"td":1,"tp":2,"total":3},"9":{"cm":0,"td":1,"tp":2,"total":3},"10":{"cm":0,"td":1,"tp":2,"total":3},"11":{"cm":0,"td":0,"tp":2,"total":2},"12":{"cm":0,"td":0,"tp":2,"total":2},"13":{"cm":0,"td":0,"tp":2,"total":2},"14":{"cm":0,"td":0,"tp":2,"total":2},"15":{"cm":0,"td":0,"tp":2,"total":2},"16":{"cm":0,"td":0,"tp":2,"total":2},"17":{"cm":0,"td":0,"tp":2,"total":2},"18":{"cm":0,"td":0,"tp":2,"total":2},"19":{"cm":0,"td":0,"tp":2,"total":2},"20":{"cm":0,"td":0,"tp":2,"total":2}}',
 0, 1),

-- ========== ANNEE 2 - SEMESTRE 2 (6 modules) ==========
(20, 'Developpement mobile', 'Applications mobiles Android et iOS', 'Programmation', false,
 15, 0, 25, 0, 60, 0,
 '{"21":{"cm":1,"td":2,"tp":3,"total":6},"22":{"cm":1,"td":2,"tp":3,"total":6},"23":{"cm":1,"td":2,"tp":3,"total":6},"24":{"cm":1,"td":2,"tp":3,"total":6},"25":{"cm":1,"td":2,"tp":3,"total":6},"26":{"cm":1,"td":1,"tp":3,"total":5},"27":{"cm":1,"td":1,"tp":3,"total":5},"28":{"cm":1,"td":1,"tp":3,"total":5},"29":{"cm":1,"td":1,"tp":3,"total":5},"30":{"cm":1,"td":1,"tp":3,"total":5},"31":{"cm":1,"td":1,"tp":3,"total":5},"32":{"cm":1,"td":1,"tp":3,"total":5},"33":{"cm":1,"td":1,"tp":3,"total":5},"34":{"cm":1,"td":1,"tp":3,"total":5},"35":{"cm":1,"td":1,"tp":3,"total":5},"36":{"cm":0,"td":1,"tp":3,"total":4},"37":{"cm":0,"td":1,"tp":3,"total":4},"38":{"cm":0,"td":1,"tp":3,"total":4},"39":{"cm":0,"td":1,"tp":3,"total":4},"40":{"cm":0,"td":1,"tp":3,"total":4}}',
 0, 2),

(21, 'Intelligence Artificielle', 'Introduction a l''IA et au machine learning', 'Data', false,
 25, 0, 30, 0, 45, 0,
 '{"21":{"cm":2,"td":2,"tp":3,"total":7},"22":{"cm":2,"td":2,"tp":3,"total":7},"23":{"cm":2,"td":2,"tp":3,"total":7},"24":{"cm":2,"td":2,"tp":3,"total":7},"25":{"cm":2,"td":2,"tp":3,"total":7},"26":{"cm":1,"td":2,"tp":2,"total":5},"27":{"cm":1,"td":2,"tp":2,"total":5},"28":{"cm":1,"td":2,"tp":2,"total":5},"29":{"cm":1,"td":2,"tp":2,"total":5},"30":{"cm":1,"td":2,"tp":2,"total":5},"31":{"cm":1,"td":1,"tp":2,"total":4},"32":{"cm":1,"td":1,"tp":2,"total":4},"33":{"cm":1,"td":1,"tp":2,"total":4},"34":{"cm":1,"td":1,"tp":2,"total":4},"35":{"cm":1,"td":1,"tp":2,"total":4},"36":{"cm":1,"td":1,"tp":2,"total":4},"37":{"cm":1,"td":1,"tp":2,"total":4},"38":{"cm":1,"td":1,"tp":2,"total":4},"39":{"cm":1,"td":1,"tp":2,"total":4},"40":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 2),

(22, 'Securite informatique', 'Principes de securite et cryptographie', 'Securite', false,
 20, 0, 30, 0, 50, 0,
 '{"21":{"cm":1,"td":2,"tp":3,"total":6},"22":{"cm":1,"td":2,"tp":3,"total":6},"23":{"cm":1,"td":2,"tp":3,"total":6},"24":{"cm":1,"td":2,"tp":3,"total":6},"25":{"cm":1,"td":2,"tp":3,"total":6},"26":{"cm":1,"td":2,"tp":3,"total":6},"27":{"cm":1,"td":2,"tp":3,"total":6},"28":{"cm":1,"td":2,"tp":3,"total":6},"29":{"cm":1,"td":2,"tp":3,"total":6},"30":{"cm":1,"td":2,"tp":3,"total":6},"31":{"cm":1,"td":1,"tp":2,"total":4},"32":{"cm":1,"td":1,"tp":2,"total":4},"33":{"cm":1,"td":1,"tp":2,"total":4},"34":{"cm":1,"td":1,"tp":2,"total":4},"35":{"cm":1,"td":1,"tp":2,"total":4},"36":{"cm":1,"td":1,"tp":2,"total":4},"37":{"cm":1,"td":1,"tp":2,"total":4},"38":{"cm":1,"td":1,"tp":2,"total":4},"39":{"cm":1,"td":1,"tp":2,"total":4},"40":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 2),

(23, 'Administration systeme', 'Administration de serveurs et services', 'Systemes', false,
 15, 0, 20, 0, 45, 0,
 '{"21":{"cm":1,"td":1,"tp":3,"total":5},"22":{"cm":1,"td":1,"tp":3,"total":5},"23":{"cm":1,"td":1,"tp":3,"total":5},"24":{"cm":1,"td":1,"tp":3,"total":5},"25":{"cm":1,"td":1,"tp":3,"total":5},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":2,"total":4},"32":{"cm":1,"td":1,"tp":2,"total":4},"33":{"cm":1,"td":1,"tp":2,"total":4},"34":{"cm":1,"td":1,"tp":2,"total":4},"35":{"cm":1,"td":1,"tp":2,"total":4},"36":{"cm":0,"td":1,"tp":2,"total":3},"37":{"cm":0,"td":1,"tp":2,"total":3},"38":{"cm":0,"td":1,"tp":2,"total":3},"39":{"cm":0,"td":1,"tp":2,"total":3},"40":{"cm":0,"td":1,"tp":2,"total":3}}',
 0, 2),

(24, 'Gestion de donnees massives', 'Big Data et traitement de donnees', 'Data', false,
 20, 0, 25, 0, 35, 0,
 '{"21":{"cm":1,"td":2,"tp":2,"total":5},"22":{"cm":1,"td":2,"tp":2,"total":5},"23":{"cm":1,"td":2,"tp":2,"total":5},"24":{"cm":1,"td":2,"tp":2,"total":5},"25":{"cm":1,"td":2,"tp":2,"total":5},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":2,"total":4},"32":{"cm":1,"td":1,"tp":2,"total":4},"33":{"cm":1,"td":1,"tp":2,"total":4},"34":{"cm":1,"td":1,"tp":2,"total":4},"35":{"cm":1,"td":1,"tp":2,"total":4},"36":{"cm":1,"td":1,"tp":1,"total":3},"37":{"cm":1,"td":1,"tp":1,"total":3},"38":{"cm":1,"td":1,"tp":1,"total":3},"39":{"cm":1,"td":1,"tp":1,"total":3},"40":{"cm":1,"td":1,"tp":1,"total":3}}',
 0, 2),

(25, 'Projet tutore S4', 'Projet en equipe du semestre 4', 'Projet', false,
 0, 0, 10, 0, 50, 0,
 '{"21":{"cm":0,"td":1,"tp":3,"total":4},"22":{"cm":0,"td":1,"tp":3,"total":4},"23":{"cm":0,"td":1,"tp":3,"total":4},"24":{"cm":0,"td":1,"tp":3,"total":4},"25":{"cm":0,"td":1,"tp":3,"total":4},"26":{"cm":0,"td":1,"tp":3,"total":4},"27":{"cm":0,"td":1,"tp":3,"total":4},"28":{"cm":0,"td":1,"tp":3,"total":4},"29":{"cm":0,"td":1,"tp":3,"total":4},"30":{"cm":0,"td":1,"tp":3,"total":4},"31":{"cm":0,"td":0,"tp":2,"total":2},"32":{"cm":0,"td":0,"tp":2,"total":2},"33":{"cm":0,"td":0,"tp":2,"total":2},"34":{"cm":0,"td":0,"tp":2,"total":2},"35":{"cm":0,"td":0,"tp":2,"total":2},"36":{"cm":0,"td":0,"tp":2,"total":2},"37":{"cm":0,"td":0,"tp":2,"total":2},"38":{"cm":0,"td":0,"tp":2,"total":2},"39":{"cm":0,"td":0,"tp":2,"total":2},"40":{"cm":0,"td":0,"tp":2,"total":2}}',
 0, 2),

-- ========== ANNEE 3 - SEMESTRE 1 - ALTERNANCE (7 modules) ==========
(26, 'Architecture logicielle', 'Patterns et architecture logicielle', 'Programmation', false,
 20, 0, 20, 0, 30, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":1,"td":1,"tp":1,"total":3},"12":{"cm":1,"td":1,"tp":1,"total":3},"13":{"cm":1,"td":1,"tp":1,"total":3},"14":{"cm":1,"td":1,"tp":1,"total":3},"15":{"cm":1,"td":1,"tp":1,"total":3},"16":{"cm":1,"td":1,"tp":1,"total":3},"17":{"cm":1,"td":1,"tp":1,"total":3},"18":{"cm":1,"td":1,"tp":1,"total":3},"19":{"cm":1,"td":1,"tp":1,"total":3},"20":{"cm":1,"td":1,"tp":1,"total":3}}',
 0, 1),

(27, 'Cloud computing', 'Services cloud et deploiement', 'Systemes', false,
 15, 0, 15, 0, 30, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":1,"td":1,"tp":1,"total":3},"12":{"cm":1,"td":1,"tp":1,"total":3},"13":{"cm":1,"td":1,"tp":1,"total":3},"14":{"cm":1,"td":1,"tp":1,"total":3},"15":{"cm":1,"td":1,"tp":1,"total":3},"16":{"cm":0,"td":0,"tp":1,"total":1},"17":{"cm":0,"td":0,"tp":1,"total":1},"18":{"cm":0,"td":0,"tp":1,"total":1},"19":{"cm":0,"td":0,"tp":1,"total":1},"20":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 1),

(28, 'DevOps', 'Integration et deploiement continus', 'Systemes', false,
 10, 0, 15, 0, 35, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":0,"td":1,"tp":2,"total":3},"12":{"cm":0,"td":1,"tp":2,"total":3},"13":{"cm":0,"td":1,"tp":2,"total":3},"14":{"cm":0,"td":1,"tp":2,"total":3},"15":{"cm":0,"td":1,"tp":2,"total":3},"16":{"cm":0,"td":0,"tp":1,"total":1},"17":{"cm":0,"td":0,"tp":1,"total":1},"18":{"cm":0,"td":0,"tp":1,"total":1},"19":{"cm":0,"td":0,"tp":1,"total":1},"20":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 1),

(29, 'Management de projet', 'Gestion avancee de projets informatiques', 'Transversal', false,
 10, 0, 20, 0, 0, 0,
 '{"1":{"cm":1,"td":1,"tp":0,"total":2},"2":{"cm":1,"td":1,"tp":0,"total":2},"3":{"cm":1,"td":1,"tp":0,"total":2},"4":{"cm":1,"td":1,"tp":0,"total":2},"5":{"cm":1,"td":1,"tp":0,"total":2},"6":{"cm":1,"td":1,"tp":0,"total":2},"7":{"cm":1,"td":1,"tp":0,"total":2},"8":{"cm":1,"td":1,"tp":0,"total":2},"9":{"cm":1,"td":1,"tp":0,"total":2},"10":{"cm":1,"td":1,"tp":0,"total":2},"11":{"cm":0,"td":1,"tp":0,"total":1},"12":{"cm":0,"td":1,"tp":0,"total":1},"13":{"cm":0,"td":1,"tp":0,"total":1},"14":{"cm":0,"td":1,"tp":0,"total":1},"15":{"cm":0,"td":1,"tp":0,"total":1},"16":{"cm":0,"td":1,"tp":0,"total":1},"17":{"cm":0,"td":1,"tp":0,"total":1},"18":{"cm":0,"td":1,"tp":0,"total":1},"19":{"cm":0,"td":1,"tp":0,"total":1},"20":{"cm":0,"td":1,"tp":0,"total":1}}',
 0, 1),

(30, 'Veille technologique', 'Veille et innovation technologique', 'Transversal', false,
 0, 0, 10, 0, 10, 0,
 '{"1":{"cm":0,"td":1,"tp":1,"total":2},"2":{"cm":0,"td":1,"tp":1,"total":2},"3":{"cm":0,"td":1,"tp":1,"total":2},"4":{"cm":0,"td":1,"tp":1,"total":2},"5":{"cm":0,"td":1,"tp":1,"total":2},"6":{"cm":0,"td":1,"tp":1,"total":2},"7":{"cm":0,"td":1,"tp":1,"total":2},"8":{"cm":0,"td":1,"tp":1,"total":2},"9":{"cm":0,"td":1,"tp":1,"total":2},"10":{"cm":0,"td":1,"tp":1,"total":2}}',
 0, 1),

(31, 'Mission en entreprise S5', 'Periode en entreprise du semestre 5', 'Entreprise', false,
 0, 0, 0, 0, 0, 0,
 '{}',
 0, 1),

(32, 'Rapport d''activite S5', 'Redaction du rapport d''activite S5', 'Transversal', false,
 0, 0, 10, 0, 10, 0,
 '{"1":{"cm":0,"td":1,"tp":1,"total":2},"2":{"cm":0,"td":1,"tp":1,"total":2},"3":{"cm":0,"td":1,"tp":1,"total":2},"4":{"cm":0,"td":1,"tp":1,"total":2},"5":{"cm":0,"td":1,"tp":1,"total":2},"6":{"cm":0,"td":1,"tp":1,"total":2},"7":{"cm":0,"td":1,"tp":1,"total":2},"8":{"cm":0,"td":1,"tp":1,"total":2},"9":{"cm":0,"td":1,"tp":1,"total":2},"10":{"cm":0,"td":1,"tp":1,"total":2}}',
 0, 1),

-- ========== ANNEE 3 - SEMESTRE 2 - ALTERNANCE (5 modules) ==========
(33, 'Securite avancee', 'Securite avancee et pentest', 'Securite', false,
 15, 0, 15, 0, 30, 0,
 '{"21":{"cm":1,"td":1,"tp":2,"total":4},"22":{"cm":1,"td":1,"tp":2,"total":4},"23":{"cm":1,"td":1,"tp":2,"total":4},"24":{"cm":1,"td":1,"tp":2,"total":4},"25":{"cm":1,"td":1,"tp":2,"total":4},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":1,"total":3},"32":{"cm":1,"td":1,"tp":1,"total":3},"33":{"cm":1,"td":1,"tp":1,"total":3},"34":{"cm":1,"td":1,"tp":1,"total":3},"35":{"cm":1,"td":1,"tp":1,"total":3},"36":{"cm":0,"td":0,"tp":1,"total":1},"37":{"cm":0,"td":0,"tp":1,"total":1},"38":{"cm":0,"td":0,"tp":1,"total":1},"39":{"cm":0,"td":0,"tp":1,"total":1},"40":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 2),

(34, 'Intelligence Artificielle avancee', 'Deep learning et IA avancee', 'Data', false,
 15, 0, 15, 0, 30, 0,
 '{"21":{"cm":1,"td":1,"tp":2,"total":4},"22":{"cm":1,"td":1,"tp":2,"total":4},"23":{"cm":1,"td":1,"tp":2,"total":4},"24":{"cm":1,"td":1,"tp":2,"total":4},"25":{"cm":1,"td":1,"tp":2,"total":4},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":1,"total":3},"32":{"cm":1,"td":1,"tp":1,"total":3},"33":{"cm":1,"td":1,"tp":1,"total":3},"34":{"cm":1,"td":1,"tp":1,"total":3},"35":{"cm":1,"td":1,"tp":1,"total":3},"36":{"cm":0,"td":0,"tp":1,"total":1},"37":{"cm":0,"td":0,"tp":1,"total":1},"38":{"cm":0,"td":0,"tp":1,"total":1},"39":{"cm":0,"td":0,"tp":1,"total":1},"40":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 2),

(35, 'Projet de fin d''etudes', 'Projet de fin d''etudes en equipe', 'Projet', false,
 0, 0, 20, 0, 80, 0,
 '{"21":{"cm":0,"td":1,"tp":4,"total":5},"22":{"cm":0,"td":1,"tp":4,"total":5},"23":{"cm":0,"td":1,"tp":4,"total":5},"24":{"cm":0,"td":1,"tp":4,"total":5},"25":{"cm":0,"td":1,"tp":4,"total":5},"26":{"cm":0,"td":1,"tp":4,"total":5},"27":{"cm":0,"td":1,"tp":4,"total":5},"28":{"cm":0,"td":1,"tp":4,"total":5},"29":{"cm":0,"td":1,"tp":4,"total":5},"30":{"cm":0,"td":1,"tp":4,"total":5},"31":{"cm":0,"td":1,"tp":4,"total":5},"32":{"cm":0,"td":1,"tp":4,"total":5},"33":{"cm":0,"td":1,"tp":4,"total":5},"34":{"cm":0,"td":1,"tp":4,"total":5},"35":{"cm":0,"td":1,"tp":4,"total":5},"36":{"cm":0,"td":1,"tp":4,"total":5},"37":{"cm":0,"td":1,"tp":4,"total":5},"38":{"cm":0,"td":1,"tp":4,"total":5},"39":{"cm":0,"td":1,"tp":4,"total":5},"40":{"cm":0,"td":1,"tp":4,"total":5}}',
 0, 2),

(36, 'Mission en entreprise S6', 'Periode en entreprise du semestre 6', 'Entreprise', false,
 0, 0, 0, 0, 0, 0,
 '{}',
 0, 2),

(37, 'Memoire professionnel', 'Redaction du memoire professionnel', 'Transversal', false,
 0, 0, 30, 0, 0, 0,
 '{"21":{"cm":0,"td":2,"tp":0,"total":2},"22":{"cm":0,"td":2,"tp":0,"total":2},"23":{"cm":0,"td":2,"tp":0,"total":2},"24":{"cm":0,"td":2,"tp":0,"total":2},"25":{"cm":0,"td":2,"tp":0,"total":2},"26":{"cm":0,"td":2,"tp":0,"total":2},"27":{"cm":0,"td":2,"tp":0,"total":2},"28":{"cm":0,"td":2,"tp":0,"total":2},"29":{"cm":0,"td":2,"tp":0,"total":2},"30":{"cm":0,"td":2,"tp":0,"total":2},"31":{"cm":0,"td":1,"tp":0,"total":1},"32":{"cm":0,"td":1,"tp":0,"total":1},"33":{"cm":0,"td":1,"tp":0,"total":1},"34":{"cm":0,"td":1,"tp":0,"total":1},"35":{"cm":0,"td":1,"tp":0,"total":1},"36":{"cm":0,"td":1,"tp":0,"total":1},"37":{"cm":0,"td":1,"tp":0,"total":1},"38":{"cm":0,"td":1,"tp":0,"total":1},"39":{"cm":0,"td":1,"tp":0,"total":1},"40":{"cm":0,"td":1,"tp":0,"total":1}}',
 0, 2);

-- =========================
-- FORMATION RESSOURCES
-- =========================
INSERT INTO formation_resource (formation_id, resource_id) VALUES
-- Annee 1 (IDs 1-12) -> formations 1 (Classe A) et 2 (Classe B)
(1, 1), (2, 1),
(1, 2), (2, 2),
(1, 3), (2, 3),
(1, 4), (2, 4),
(1, 5), (2, 5),
(1, 6), (2, 6),
(1, 7), (2, 7),
(1, 8), (2, 8),
(1, 9), (2, 9),
(1, 10), (2, 10),
(1, 11), (2, 11),
(1, 12), (2, 12),
-- Annee 2 (IDs 13-25) -> formations 3 (Classe A) et 4 (Classe B)
(3, 13), (4, 13),
(3, 14), (4, 14),
(3, 15), (4, 15),
(3, 16), (4, 16),
(3, 17), (4, 17),
(3, 18), (4, 18),
(3, 19), (4, 19),
(3, 20), (4, 20),
(3, 21), (4, 21),
(3, 22), (4, 22),
(3, 23), (4, 23),
(3, 24), (4, 24),
(3, 25), (4, 25),
-- Annee 3 (IDs 26-37) -> formations 5 (Classe A) et 6 (Classe B)
(5, 26), (6, 26),
(5, 27), (6, 27),
(5, 28), (6, 28),
(5, 29), (6, 29),
(5, 30), (6, 30),
(5, 31), (6, 31),
(5, 32), (6, 32),
(5, 33), (6, 33),
(5, 34), (6, 34),
(5, 35), (6, 35),
(5, 36), (6, 36),
(5, 37), (6, 37);

-- =========================
-- SYLLABUS
-- =========================
INSERT INTO syllabus (id, description) VALUES
(1, 'Syllabus Programmation'),
(2, 'Syllabus Data'),
(3, 'Syllabus Web'),
(4, 'Syllabus Systemes');

-- =========================
-- RESSOURCES SYLLABUS
-- =========================
INSERT INTO resource_syllabus (resource_id, syllabus_id) VALUES
(1, 1),
(2, 2),
(3, 4),
(7, 1),
(8, 3),
(13, 1),
(14, 2);

-- =========================
-- ASSIGNMENT
-- =========================
INSERT INTO assignment (id, assigned_times, lesson_type, resource_id, user_id) VALUES
(1, 20, 'CM', 1, 1),
(2, 25, 'TD', 2, 1),
(3, 20, 'CM', 7, 1),
(4, 30, 'CM', 13, 1), -- Dev App Year 2
(5, 25, 'TD', 26, 1); -- Arch Log Year 3

-- =========================
-- TICKETS
-- =========================
INSERT INTO ticket (id, date, description, status, title, user_id) VALUES
(1, '2025-12-17', 'Question sur le cours de Java', 'OPEN', 'Question Java', 3),
(2, '2025-12-17', 'Question sur les BDD', 'CLOSED', 'Aide BDD', 3),
(3, '2025-12-18', 'Bug sur le site web', 'OPEN', 'Bug Web', 3);
