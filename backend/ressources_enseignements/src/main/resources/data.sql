-- =========================
-- ROLES
-- =========================
INSERT INTO position (id, name, is_active) VALUES
(1, 'ADMIN', true),
(2, 'TEACHER', false),
(3, 'STUDENT', false)
ON CONFLICT (id) DO NOTHING;

-- =========================
-- USERS
-- =========================
INSERT INTO users (id, address, email, first_name, last_name, password, phone_number, validation_status, username, type) VALUES
(1, '1 rue Admin', 'admin@mail.com', 'Alice', 'Admin', '$2a$10$G9CG3yuKGV9l6mDuxZy/ZuHgKgp62NAm/1ZXhcqXoZM4Om021JV3K', '0101010101', 'NONE', 'P2600001', 'PERMANENT'),
(2, '2 rue Prof', 'prof@mail.com', 'Bob', 'Teacher', '$2a$10$1hrZWYwDa5MopVXf1LeGmusoN2lrzH06tFHBGLTIsk92dHE6p3p8G', '0202020202', 'VALIDATED', 'P2600002', 'PERMANENT'),
(3, '3 rue Etudiant', 'student@mail.com', 'Charlie', 'Student', '$2a$10$x9fz8aI5xHPa4L5N1tBtaOtXE0dLjzFvyjPVRoWRXwyK9FYgCS8AO', '0303030303', 'NONE', 'P2600003', NULL),
(4, '10 rue des Lilas', 'martin.dupont@univ.fr', 'Martin', 'Dupont', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604010101', 'NONE', 'P2600004', 'VACATAIRE'),
(5, '12 avenue Pasteur', 'sophie.bernard@univ.fr', 'Sophie', 'Bernard', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604020202', 'NONE', 'P2600005', 'PERMANENT'),
(6, '5 boulevard Victor Hugo', 'jean.moreau@univ.fr', 'Jean', 'Moreau', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604030303', 'NONE', 'P2600006', 'VACATAIRE'),
(7, '8 rue de la Paix', 'claire.petit@univ.fr', 'Claire', 'Petit', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604040404', 'NONE', 'P2600007', 'VACATAIRE'),
(8, '3 place de la Gare', 'pierre.leroy@univ.fr', 'Pierre', 'Leroy', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604050505', 'NONE', 'P2600008', 'PERMANENT'),
(9, '15 rue Voltaire', 'nathalie.roux@univ.fr', 'Nathalie', 'Roux', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604060606', 'NONE', 'P2600009', 'VACATAIRE'),
(10, '22 avenue de la Republique', 'francois.garcia@univ.fr', 'Francois', 'Garcia', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604070707', 'NONE', 'P2600010', 'VACATAIRE'),
(11, '7 rue Descartes', 'isabelle.martinez@univ.fr', 'Isabelle', 'Martinez', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604080808', 'NONE', 'P2600011', 'VACATAIRE'),
(12, '9 rue Pascal', 'david.thomas@univ.fr', 'David', 'Thomas', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604090909', 'NONE', 'P2600012', 'PERMANENT'),
(13, '18 avenue Foch', 'emilie.robert@univ.fr', 'Emilie', 'Robert', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604101010', 'NONE', 'P2600013', 'VACATAIRE'),
(14, '4 rue Moliere', 'philippe.richard@univ.fr', 'Philippe', 'Richard', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604111111', 'NONE', 'P2600014', 'PERMANENT'),
(15, '11 boulevard Gambetta', 'anne.dubois@univ.fr', 'Anne', 'Dubois', '$2a$10$.XcVqasfSqKAfc/RtKksje55IYTesCCGyr/58zsV462DPvES9c7Xu', '0604121212', 'NONE', 'P2600015', 'VACATAIRE')
ON CONFLICT (id) DO NOTHING;

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
(2, 15)
ON CONFLICT DO NOTHING;

-- =========================
-- FORMATIONS (BUT Informatique)
-- =========================
INSERT INTO formation (id, name, year, class_name, description) VALUES
(1, 'Informatique', '1', 'G1', 'Formation BUT Informatique annee 1 - groupe A'),
(2, 'Informatique', '1', 'G2', 'Formation BUT Informatique annee 1 - groupe B'),
(3, 'Informatique', '2', 'G1', 'Formation BUT Informatique annee 2 - groupe A'),
(4, 'Informatique', '2', 'G2', 'Formation BUT Informatique annee 2 - groupe B'),
(5, 'Informatique', '3', 'G1', 'Formation BUT Informatique annee 3 - groupe A'),
(6, 'Informatique', '3', 'G2', 'Formation BUT Informatique annee 3 - groupe B'),
(7, 'Reseaux et Telecommunications', '1', 'G1', 'Formation BUT R&T annee 1 - fondamentaux reseaux'),
(8, 'Reseaux et Telecommunications', '2', 'G1', 'Formation BUT R&T annee 2 - administration et securite'),
(9, 'Science des Donnees', '1', 'G1', 'Formation BUT SD annee 1 - statistiques et programmation'),
(10, 'Science des Donnees', '2', 'G1', 'Formation BUT SD annee 2 - machine learning et BI'),
(11, 'Reseaux et Telecommunications', '1', 'G2', 'Formation BUT R&T annee 1 - groupe B'),
(12, 'Reseaux et Telecommunications', '2', 'G2', 'Formation BUT R&T annee 2 - groupe B'),
(13, 'Science des Donnees', '1', 'G2', 'Formation BUT SD annee 1 - groupe B'),
(14, 'Science des Donnees', '2', 'G2', 'Formation BUT SD annee 2 - groupe B'),
(15, 'Informatique', '1', 'G3', 'Formation BUT Informatique annee 1 - groupe C'),
(16, 'Informatique', '1', 'G4', 'Formation BUT Informatique annee 1 - groupe D'),
(17, 'Informatique', '2', 'G3', 'Formation BUT Informatique annee 2 - groupe C'),
(18, 'Informatique', '2', 'G4', 'Formation BUT Informatique annee 2 - groupe D'),
(19, 'Informatique', '3', 'G3', 'Formation BUT Informatique annee 3 - groupe C'),
(20, 'Informatique', '3', 'G4', 'Formation BUT Informatique annee 3 - groupe D')
ON CONFLICT (id) DO NOTHING;

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
(3, 15), (5, 15)
ON CONFLICT DO NOTHING;


-- =========================
-- RESSOURCES (37 modules BUT Informatique)
-- =========================
INSERT INTO resource (
    id, title, description, category,
    cm_state_hours, cm_iut_hours, td_state_hours, td_iut_hours, tp_state_hours, tp_iut_hours,
    hours_per_week, hours_per_half_group, semester
) VALUES

-- ========== ANNEE 1 - SEMESTRE 1 (6 modules) ==========
(1, 'Initiation au developpement', 'Apprentissage des bases de la programmation', 'Programmation',
 20, 0, 30, 0, 50, 0,
 '{"1":{"cm":1,"td":2,"tp":3,"total":6},"2":{"cm":1,"td":2,"tp":3,"total":6},"3":{"cm":1,"td":2,"tp":3,"total":6},"4":{"cm":1,"td":2,"tp":3,"total":6},"5":{"cm":1,"td":2,"tp":3,"total":6},"6":{"cm":1,"td":2,"tp":3,"total":6},"7":{"cm":1,"td":2,"tp":3,"total":6},"8":{"cm":1,"td":2,"tp":3,"total":6},"9":{"cm":1,"td":2,"tp":3,"total":6},"10":{"cm":1,"td":2,"tp":3,"total":6},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":1,"td":1,"tp":2,"total":4},"17":{"cm":1,"td":1,"tp":2,"total":4},"18":{"cm":1,"td":1,"tp":2,"total":4},"19":{"cm":1,"td":1,"tp":2,"total":4},"20":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 1),

(2, 'Bases de donnees', 'Introduction aux bases de donnees relationnelles', 'Data',
 15, 0, 25, 0, 40, 0,
 '{"1":{"cm":1,"td":2,"tp":2,"total":5},"2":{"cm":1,"td":2,"tp":2,"total":5},"3":{"cm":1,"td":2,"tp":2,"total":5},"4":{"cm":1,"td":2,"tp":2,"total":5},"5":{"cm":1,"td":2,"tp":2,"total":5},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":0,"td":1,"tp":2,"total":3},"17":{"cm":0,"td":1,"tp":2,"total":3},"18":{"cm":0,"td":1,"tp":2,"total":3},"19":{"cm":0,"td":1,"tp":2,"total":3},"20":{"cm":0,"td":1,"tp":2,"total":3}}',
 0, 1),

(3, 'Architecture des ordinateurs', 'Fonctionnement interne des ordinateurs', 'Systemes',
 20, 0, 20, 0, 20, 0,
 '{"1":{"cm":1,"td":1,"tp":1,"total":3},"2":{"cm":1,"td":1,"tp":1,"total":3},"3":{"cm":1,"td":1,"tp":1,"total":3},"4":{"cm":1,"td":1,"tp":1,"total":3},"5":{"cm":1,"td":1,"tp":1,"total":3},"6":{"cm":1,"td":1,"tp":1,"total":3},"7":{"cm":1,"td":1,"tp":1,"total":3},"8":{"cm":1,"td":1,"tp":1,"total":3},"9":{"cm":1,"td":1,"tp":1,"total":3},"10":{"cm":1,"td":1,"tp":1,"total":3},"11":{"cm":1,"td":1,"tp":1,"total":3},"12":{"cm":1,"td":1,"tp":1,"total":3},"13":{"cm":1,"td":1,"tp":1,"total":3},"14":{"cm":1,"td":1,"tp":1,"total":3},"15":{"cm":1,"td":1,"tp":1,"total":3},"16":{"cm":1,"td":1,"tp":1,"total":3},"17":{"cm":1,"td":1,"tp":1,"total":3},"18":{"cm":1,"td":1,"tp":1,"total":3},"19":{"cm":1,"td":1,"tp":1,"total":3},"20":{"cm":1,"td":1,"tp":1,"total":3}}',
 0, 1),

(4, 'Mathematiques discretes', 'Logique, ensembles, graphes', 'Mathematiques',
 30, 0, 40, 0, 10, 0,
 '{"1":{"cm":2,"td":2,"tp":1,"total":5},"2":{"cm":2,"td":2,"tp":1,"total":5},"3":{"cm":2,"td":2,"tp":1,"total":5},"4":{"cm":2,"td":2,"tp":1,"total":5},"5":{"cm":2,"td":2,"tp":1,"total":5},"6":{"cm":2,"td":2,"tp":1,"total":5},"7":{"cm":2,"td":2,"tp":1,"total":5},"8":{"cm":2,"td":2,"tp":1,"total":5},"9":{"cm":2,"td":2,"tp":1,"total":5},"10":{"cm":2,"td":2,"tp":1,"total":5},"11":{"cm":1,"td":2,"tp":0,"total":3},"12":{"cm":1,"td":2,"tp":0,"total":3},"13":{"cm":1,"td":2,"tp":0,"total":3},"14":{"cm":1,"td":2,"tp":0,"total":3},"15":{"cm":1,"td":2,"tp":0,"total":3},"16":{"cm":1,"td":2,"tp":0,"total":3},"17":{"cm":1,"td":2,"tp":0,"total":3},"18":{"cm":1,"td":2,"tp":0,"total":3},"19":{"cm":1,"td":2,"tp":0,"total":3},"20":{"cm":1,"td":2,"tp":0,"total":3}}',
 0, 1),

(5, 'Introduction aux systemes', 'Systemes d''exploitation et ligne de commande', 'Systemes',
 10, 0, 15, 0, 25, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":1,"total":3},"7":{"cm":1,"td":1,"tp":1,"total":3},"8":{"cm":1,"td":1,"tp":1,"total":3},"9":{"cm":1,"td":1,"tp":1,"total":3},"10":{"cm":1,"td":1,"tp":1,"total":3},"11":{"cm":0,"td":1,"tp":1,"total":2},"12":{"cm":0,"td":1,"tp":1,"total":2},"13":{"cm":0,"td":1,"tp":1,"total":2},"14":{"cm":0,"td":1,"tp":1,"total":2},"15":{"cm":0,"td":1,"tp":1,"total":2},"16":{"cm":0,"td":0,"tp":1,"total":1},"17":{"cm":0,"td":0,"tp":1,"total":1},"18":{"cm":0,"td":0,"tp":1,"total":1},"19":{"cm":0,"td":0,"tp":1,"total":1},"20":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 1),

(6, 'Gestion de projet', 'Methodes de gestion de projet informatique', 'Transversal',
 10, 0, 20, 0, 10, 0,
 '{"1":{"cm":1,"td":1,"tp":1,"total":3},"2":{"cm":1,"td":1,"tp":1,"total":3},"3":{"cm":1,"td":1,"tp":1,"total":3},"4":{"cm":1,"td":1,"tp":1,"total":3},"5":{"cm":1,"td":1,"tp":1,"total":3},"6":{"cm":1,"td":1,"tp":1,"total":3},"7":{"cm":1,"td":1,"tp":1,"total":3},"8":{"cm":1,"td":1,"tp":1,"total":3},"9":{"cm":1,"td":1,"tp":1,"total":3},"10":{"cm":1,"td":1,"tp":1,"total":3},"11":{"cm":0,"td":1,"tp":0,"total":1},"12":{"cm":0,"td":1,"tp":0,"total":1},"13":{"cm":0,"td":1,"tp":0,"total":1},"14":{"cm":0,"td":1,"tp":0,"total":1},"15":{"cm":0,"td":1,"tp":0,"total":1},"16":{"cm":0,"td":1,"tp":0,"total":1},"17":{"cm":0,"td":1,"tp":0,"total":1},"18":{"cm":0,"td":1,"tp":0,"total":1},"19":{"cm":0,"td":1,"tp":0,"total":1},"20":{"cm":0,"td":1,"tp":0,"total":1}}',
 0, 1),

-- ========== ANNEE 1 - SEMESTRE 2 (6 modules) ==========
(7, 'Programmation orientee objet', 'Concepts OOP en Java', 'Programmation',
 25, 0, 35, 0, 60, 0,
 '{"21":{"cm":2,"td":2,"tp":3,"total":7},"22":{"cm":2,"td":2,"tp":3,"total":7},"23":{"cm":2,"td":2,"tp":3,"total":7},"24":{"cm":2,"td":2,"tp":3,"total":7},"25":{"cm":2,"td":2,"tp":3,"total":7},"26":{"cm":1,"td":2,"tp":3,"total":6},"27":{"cm":1,"td":2,"tp":3,"total":6},"28":{"cm":1,"td":2,"tp":3,"total":6},"29":{"cm":1,"td":2,"tp":3,"total":6},"30":{"cm":1,"td":2,"tp":3,"total":6},"31":{"cm":1,"td":2,"tp":3,"total":6},"32":{"cm":1,"td":2,"tp":3,"total":6},"33":{"cm":1,"td":2,"tp":3,"total":6},"34":{"cm":1,"td":2,"tp":3,"total":6},"35":{"cm":1,"td":2,"tp":3,"total":6},"36":{"cm":1,"td":1,"tp":3,"total":5},"37":{"cm":1,"td":1,"tp":3,"total":5},"38":{"cm":1,"td":1,"tp":3,"total":5},"39":{"cm":1,"td":1,"tp":3,"total":5},"40":{"cm":1,"td":1,"tp":3,"total":5}}',
 0, 2),

(8, 'Developpement web', 'HTML, CSS, JavaScript et frameworks', 'Web',
 20, 0, 30, 0, 70, 0,
 '{"21":{"cm":1,"td":2,"tp":4,"total":7},"22":{"cm":1,"td":2,"tp":4,"total":7},"23":{"cm":1,"td":2,"tp":4,"total":7},"24":{"cm":1,"td":2,"tp":4,"total":7},"25":{"cm":1,"td":2,"tp":4,"total":7},"26":{"cm":1,"td":2,"tp":4,"total":7},"27":{"cm":1,"td":2,"tp":4,"total":7},"28":{"cm":1,"td":2,"tp":4,"total":7},"29":{"cm":1,"td":2,"tp":4,"total":7},"30":{"cm":1,"td":2,"tp":4,"total":7},"31":{"cm":1,"td":1,"tp":3,"total":5},"32":{"cm":1,"td":1,"tp":3,"total":5},"33":{"cm":1,"td":1,"tp":3,"total":5},"34":{"cm":1,"td":1,"tp":3,"total":5},"35":{"cm":1,"td":1,"tp":3,"total":5},"36":{"cm":1,"td":1,"tp":3,"total":5},"37":{"cm":1,"td":1,"tp":3,"total":5},"38":{"cm":1,"td":1,"tp":3,"total":5},"39":{"cm":1,"td":1,"tp":3,"total":5},"40":{"cm":1,"td":1,"tp":3,"total":5}}',
 0, 2),

(9, 'Algorithmique', 'Structures de donnees et algorithmes', 'Programmation',
 25, 0, 45, 0, 30, 0,
 '{"21":{"cm":2,"td":3,"tp":2,"total":7},"22":{"cm":2,"td":3,"tp":2,"total":7},"23":{"cm":2,"td":3,"tp":2,"total":7},"24":{"cm":2,"td":3,"tp":2,"total":7},"25":{"cm":2,"td":3,"tp":2,"total":7},"26":{"cm":1,"td":2,"tp":2,"total":5},"27":{"cm":1,"td":2,"tp":2,"total":5},"28":{"cm":1,"td":2,"tp":2,"total":5},"29":{"cm":1,"td":2,"tp":2,"total":5},"30":{"cm":1,"td":2,"tp":2,"total":5},"31":{"cm":1,"td":2,"tp":1,"total":4},"32":{"cm":1,"td":2,"tp":1,"total":4},"33":{"cm":1,"td":2,"tp":1,"total":4},"34":{"cm":1,"td":2,"tp":1,"total":4},"35":{"cm":1,"td":2,"tp":1,"total":4},"36":{"cm":1,"td":2,"tp":1,"total":4},"37":{"cm":1,"td":2,"tp":1,"total":4},"38":{"cm":1,"td":2,"tp":1,"total":4},"39":{"cm":1,"td":2,"tp":1,"total":4},"40":{"cm":1,"td":2,"tp":1,"total":4}}',
 0, 2),

(10, 'Reseaux', 'Fondamentaux des reseaux informatiques', 'Reseaux',
 15, 0, 25, 0, 30, 0,
 '{"21":{"cm":1,"td":2,"tp":2,"total":5},"22":{"cm":1,"td":2,"tp":2,"total":5},"23":{"cm":1,"td":2,"tp":2,"total":5},"24":{"cm":1,"td":2,"tp":2,"total":5},"25":{"cm":1,"td":2,"tp":2,"total":5},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":1,"total":3},"32":{"cm":1,"td":1,"tp":1,"total":3},"33":{"cm":1,"td":1,"tp":1,"total":3},"34":{"cm":1,"td":1,"tp":1,"total":3},"35":{"cm":1,"td":1,"tp":1,"total":3},"36":{"cm":0,"td":1,"tp":1,"total":2},"37":{"cm":0,"td":1,"tp":1,"total":2},"38":{"cm":0,"td":1,"tp":1,"total":2},"39":{"cm":0,"td":1,"tp":1,"total":2},"40":{"cm":0,"td":1,"tp":1,"total":2}}',
 0, 2),

(11, 'Probabilites et statistiques', 'Probabilites, statistiques descriptives', 'Mathematiques',
 20, 0, 30, 0, 10, 0,
 '{"21":{"cm":1,"td":2,"tp":1,"total":4},"22":{"cm":1,"td":2,"tp":1,"total":4},"23":{"cm":1,"td":2,"tp":1,"total":4},"24":{"cm":1,"td":2,"tp":1,"total":4},"25":{"cm":1,"td":2,"tp":1,"total":4},"26":{"cm":1,"td":2,"tp":1,"total":4},"27":{"cm":1,"td":2,"tp":1,"total":4},"28":{"cm":1,"td":2,"tp":1,"total":4},"29":{"cm":1,"td":2,"tp":1,"total":4},"30":{"cm":1,"td":2,"tp":1,"total":4},"31":{"cm":1,"td":1,"tp":0,"total":2},"32":{"cm":1,"td":1,"tp":0,"total":2},"33":{"cm":1,"td":1,"tp":0,"total":2},"34":{"cm":1,"td":1,"tp":0,"total":2},"35":{"cm":1,"td":1,"tp":0,"total":2},"36":{"cm":1,"td":1,"tp":0,"total":2},"37":{"cm":1,"td":1,"tp":0,"total":2},"38":{"cm":1,"td":1,"tp":0,"total":2},"39":{"cm":1,"td":1,"tp":0,"total":2},"40":{"cm":1,"td":1,"tp":0,"total":2}}',
 0, 2),

(12, 'Projet tutore S2', 'Projet en equipe du semestre 2', 'Projet',
 0, 0, 10, 0, 30, 0,
 '{"21":{"cm":0,"td":1,"tp":2,"total":3},"22":{"cm":0,"td":1,"tp":2,"total":3},"23":{"cm":0,"td":1,"tp":2,"total":3},"24":{"cm":0,"td":1,"tp":2,"total":3},"25":{"cm":0,"td":1,"tp":2,"total":3},"26":{"cm":0,"td":1,"tp":2,"total":3},"27":{"cm":0,"td":1,"tp":2,"total":3},"28":{"cm":0,"td":1,"tp":2,"total":3},"29":{"cm":0,"td":1,"tp":2,"total":3},"30":{"cm":0,"td":1,"tp":2,"total":3},"31":{"cm":0,"td":0,"tp":1,"total":1},"32":{"cm":0,"td":0,"tp":1,"total":1},"33":{"cm":0,"td":0,"tp":1,"total":1},"34":{"cm":0,"td":0,"tp":1,"total":1},"35":{"cm":0,"td":0,"tp":1,"total":1},"36":{"cm":0,"td":0,"tp":1,"total":1},"37":{"cm":0,"td":0,"tp":1,"total":1},"38":{"cm":0,"td":0,"tp":1,"total":1},"39":{"cm":0,"td":0,"tp":1,"total":1},"40":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 2),

-- ========== ANNEE 2 - SEMESTRE 1 (7 modules) ==========
(13, 'Developpement d''applications', 'Developpement d''applications completes', 'Programmation',
 20, 0, 30, 0, 70, 0,
 '{"1":{"cm":1,"td":2,"tp":4,"total":7},"2":{"cm":1,"td":2,"tp":4,"total":7},"3":{"cm":1,"td":2,"tp":4,"total":7},"4":{"cm":1,"td":2,"tp":4,"total":7},"5":{"cm":1,"td":2,"tp":4,"total":7},"6":{"cm":1,"td":2,"tp":4,"total":7},"7":{"cm":1,"td":2,"tp":4,"total":7},"8":{"cm":1,"td":2,"tp":4,"total":7},"9":{"cm":1,"td":2,"tp":4,"total":7},"10":{"cm":1,"td":2,"tp":4,"total":7},"11":{"cm":1,"td":1,"tp":3,"total":5},"12":{"cm":1,"td":1,"tp":3,"total":5},"13":{"cm":1,"td":1,"tp":3,"total":5},"14":{"cm":1,"td":1,"tp":3,"total":5},"15":{"cm":1,"td":1,"tp":3,"total":5},"16":{"cm":1,"td":1,"tp":3,"total":5},"17":{"cm":1,"td":1,"tp":3,"total":5},"18":{"cm":1,"td":1,"tp":3,"total":5},"19":{"cm":1,"td":1,"tp":3,"total":5},"20":{"cm":1,"td":1,"tp":3,"total":5}}',
 0, 1),

(14, 'Bases de donnees avancees', 'SQL avance, NoSQL, optimisation', 'Data',
 20, 0, 30, 0, 50, 0,
 '{"1":{"cm":1,"td":2,"tp":3,"total":6},"2":{"cm":1,"td":2,"tp":3,"total":6},"3":{"cm":1,"td":2,"tp":3,"total":6},"4":{"cm":1,"td":2,"tp":3,"total":6},"5":{"cm":1,"td":2,"tp":3,"total":6},"6":{"cm":1,"td":2,"tp":3,"total":6},"7":{"cm":1,"td":2,"tp":3,"total":6},"8":{"cm":1,"td":2,"tp":3,"total":6},"9":{"cm":1,"td":2,"tp":3,"total":6},"10":{"cm":1,"td":2,"tp":3,"total":6},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":1,"td":1,"tp":2,"total":4},"17":{"cm":1,"td":1,"tp":2,"total":4},"18":{"cm":1,"td":1,"tp":2,"total":4},"19":{"cm":1,"td":1,"tp":2,"total":4},"20":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 1),

(15, 'Programmation systeme', 'Programmation bas niveau et systeme', 'Systemes',
 25, 0, 25, 0, 50, 0,
 '{"1":{"cm":2,"td":2,"tp":3,"total":7},"2":{"cm":2,"td":2,"tp":3,"total":7},"3":{"cm":2,"td":2,"tp":3,"total":7},"4":{"cm":2,"td":2,"tp":3,"total":7},"5":{"cm":2,"td":2,"tp":3,"total":7},"6":{"cm":1,"td":1,"tp":3,"total":5},"7":{"cm":1,"td":1,"tp":3,"total":5},"8":{"cm":1,"td":1,"tp":3,"total":5},"9":{"cm":1,"td":1,"tp":3,"total":5},"10":{"cm":1,"td":1,"tp":3,"total":5},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":1,"td":1,"tp":2,"total":4},"17":{"cm":1,"td":1,"tp":2,"total":4},"18":{"cm":1,"td":1,"tp":2,"total":4},"19":{"cm":1,"td":1,"tp":2,"total":4},"20":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 1),

(16, 'Qualite de developpement', 'Tests, integration continue, qualite logicielle', 'Programmation',
 15, 0, 25, 0, 40, 0,
 '{"1":{"cm":1,"td":2,"tp":2,"total":5},"2":{"cm":1,"td":2,"tp":2,"total":5},"3":{"cm":1,"td":2,"tp":2,"total":5},"4":{"cm":1,"td":2,"tp":2,"total":5},"5":{"cm":1,"td":2,"tp":2,"total":5},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":1,"td":1,"tp":2,"total":4},"12":{"cm":1,"td":1,"tp":2,"total":4},"13":{"cm":1,"td":1,"tp":2,"total":4},"14":{"cm":1,"td":1,"tp":2,"total":4},"15":{"cm":1,"td":1,"tp":2,"total":4},"16":{"cm":0,"td":1,"tp":2,"total":3},"17":{"cm":0,"td":1,"tp":2,"total":3},"18":{"cm":0,"td":1,"tp":2,"total":3},"19":{"cm":0,"td":1,"tp":2,"total":3},"20":{"cm":0,"td":1,"tp":2,"total":3}}',
 0, 1),

(17, 'IHM et ergonomie', 'Interfaces homme-machine et experience utilisateur', 'Web',
 10, 0, 20, 0, 30, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":0,"td":1,"tp":1,"total":2},"12":{"cm":0,"td":1,"tp":1,"total":2},"13":{"cm":0,"td":1,"tp":1,"total":2},"14":{"cm":0,"td":1,"tp":1,"total":2},"15":{"cm":0,"td":1,"tp":1,"total":2},"16":{"cm":0,"td":1,"tp":1,"total":2},"17":{"cm":0,"td":1,"tp":1,"total":2},"18":{"cm":0,"td":1,"tp":1,"total":2},"19":{"cm":0,"td":1,"tp":1,"total":2},"20":{"cm":0,"td":1,"tp":1,"total":2}}',
 0, 1),

(18, 'Anglais technique', 'Anglais applique a l''informatique', 'Transversal',
 0, 0, 20, 0, 0, 0,
 '{"1":{"cm":0,"td":1,"tp":0,"total":1},"2":{"cm":0,"td":1,"tp":0,"total":1},"3":{"cm":0,"td":1,"tp":0,"total":1},"4":{"cm":0,"td":1,"tp":0,"total":1},"5":{"cm":0,"td":1,"tp":0,"total":1},"6":{"cm":0,"td":1,"tp":0,"total":1},"7":{"cm":0,"td":1,"tp":0,"total":1},"8":{"cm":0,"td":1,"tp":0,"total":1},"9":{"cm":0,"td":1,"tp":0,"total":1},"10":{"cm":0,"td":1,"tp":0,"total":1},"11":{"cm":0,"td":1,"tp":0,"total":1},"12":{"cm":0,"td":1,"tp":0,"total":1},"13":{"cm":0,"td":1,"tp":0,"total":1},"14":{"cm":0,"td":1,"tp":0,"total":1},"15":{"cm":0,"td":1,"tp":0,"total":1},"16":{"cm":0,"td":1,"tp":0,"total":1},"17":{"cm":0,"td":1,"tp":0,"total":1},"18":{"cm":0,"td":1,"tp":0,"total":1},"19":{"cm":0,"td":1,"tp":0,"total":1},"20":{"cm":0,"td":1,"tp":0,"total":1}}',
 0, 1),

(19, 'Projet tutore S3', 'Projet en equipe du semestre 3', 'Projet',
 0, 0, 10, 0, 40, 0,
 '{"1":{"cm":0,"td":1,"tp":2,"total":3},"2":{"cm":0,"td":1,"tp":2,"total":3},"3":{"cm":0,"td":1,"tp":2,"total":3},"4":{"cm":0,"td":1,"tp":2,"total":3},"5":{"cm":0,"td":1,"tp":2,"total":3},"6":{"cm":0,"td":1,"tp":2,"total":3},"7":{"cm":0,"td":1,"tp":2,"total":3},"8":{"cm":0,"td":1,"tp":2,"total":3},"9":{"cm":0,"td":1,"tp":2,"total":3},"10":{"cm":0,"td":1,"tp":2,"total":3},"11":{"cm":0,"td":0,"tp":2,"total":2},"12":{"cm":0,"td":0,"tp":2,"total":2},"13":{"cm":0,"td":0,"tp":2,"total":2},"14":{"cm":0,"td":0,"tp":2,"total":2},"15":{"cm":0,"td":0,"tp":2,"total":2},"16":{"cm":0,"td":0,"tp":2,"total":2},"17":{"cm":0,"td":0,"tp":2,"total":2},"18":{"cm":0,"td":0,"tp":2,"total":2},"19":{"cm":0,"td":0,"tp":2,"total":2},"20":{"cm":0,"td":0,"tp":2,"total":2}}',
 0, 1),

-- ========== ANNEE 2 - SEMESTRE 2 (6 modules) ==========
(20, 'Developpement mobile', 'Applications mobiles Android et iOS', 'Programmation',
 15, 0, 25, 0, 60, 0,
 '{"21":{"cm":1,"td":2,"tp":3,"total":6},"22":{"cm":1,"td":2,"tp":3,"total":6},"23":{"cm":1,"td":2,"tp":3,"total":6},"24":{"cm":1,"td":2,"tp":3,"total":6},"25":{"cm":1,"td":2,"tp":3,"total":6},"26":{"cm":1,"td":1,"tp":3,"total":5},"27":{"cm":1,"td":1,"tp":3,"total":5},"28":{"cm":1,"td":1,"tp":3,"total":5},"29":{"cm":1,"td":1,"tp":3,"total":5},"30":{"cm":1,"td":1,"tp":3,"total":5},"31":{"cm":1,"td":1,"tp":3,"total":5},"32":{"cm":1,"td":1,"tp":3,"total":5},"33":{"cm":1,"td":1,"tp":3,"total":5},"34":{"cm":1,"td":1,"tp":3,"total":5},"35":{"cm":1,"td":1,"tp":3,"total":5},"36":{"cm":0,"td":1,"tp":3,"total":4},"37":{"cm":0,"td":1,"tp":3,"total":4},"38":{"cm":0,"td":1,"tp":3,"total":4},"39":{"cm":0,"td":1,"tp":3,"total":4},"40":{"cm":0,"td":1,"tp":3,"total":4}}',
 0, 2),

(21, 'Intelligence Artificielle', 'Introduction a l''IA et au machine learning', 'Data',
 25, 0, 30, 0, 45, 0,
 '{"21":{"cm":2,"td":2,"tp":3,"total":7},"22":{"cm":2,"td":2,"tp":3,"total":7},"23":{"cm":2,"td":2,"tp":3,"total":7},"24":{"cm":2,"td":2,"tp":3,"total":7},"25":{"cm":2,"td":2,"tp":3,"total":7},"26":{"cm":1,"td":2,"tp":2,"total":5},"27":{"cm":1,"td":2,"tp":2,"total":5},"28":{"cm":1,"td":2,"tp":2,"total":5},"29":{"cm":1,"td":2,"tp":2,"total":5},"30":{"cm":1,"td":2,"tp":2,"total":5},"31":{"cm":1,"td":1,"tp":2,"total":4},"32":{"cm":1,"td":1,"tp":2,"total":4},"33":{"cm":1,"td":1,"tp":2,"total":4},"34":{"cm":1,"td":1,"tp":2,"total":4},"35":{"cm":1,"td":1,"tp":2,"total":4},"36":{"cm":1,"td":1,"tp":2,"total":4},"37":{"cm":1,"td":1,"tp":2,"total":4},"38":{"cm":1,"td":1,"tp":2,"total":4},"39":{"cm":1,"td":1,"tp":2,"total":4},"40":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 2),

(22, 'Securite informatique', 'Principes de securite et cryptographie', 'Securite',
 20, 0, 30, 0, 50, 0,
 '{"21":{"cm":1,"td":2,"tp":3,"total":6},"22":{"cm":1,"td":2,"tp":3,"total":6},"23":{"cm":1,"td":2,"tp":3,"total":6},"24":{"cm":1,"td":2,"tp":3,"total":6},"25":{"cm":1,"td":2,"tp":3,"total":6},"26":{"cm":1,"td":2,"tp":3,"total":6},"27":{"cm":1,"td":2,"tp":3,"total":6},"28":{"cm":1,"td":2,"tp":3,"total":6},"29":{"cm":1,"td":2,"tp":3,"total":6},"30":{"cm":1,"td":2,"tp":3,"total":6},"31":{"cm":1,"td":1,"tp":2,"total":4},"32":{"cm":1,"td":1,"tp":2,"total":4},"33":{"cm":1,"td":1,"tp":2,"total":4},"34":{"cm":1,"td":1,"tp":2,"total":4},"35":{"cm":1,"td":1,"tp":2,"total":4},"36":{"cm":1,"td":1,"tp":2,"total":4},"37":{"cm":1,"td":1,"tp":2,"total":4},"38":{"cm":1,"td":1,"tp":2,"total":4},"39":{"cm":1,"td":1,"tp":2,"total":4},"40":{"cm":1,"td":1,"tp":2,"total":4}}',
 0, 2),

(23, 'Administration systeme', 'Administration de serveurs et services', 'Systemes',
 15, 0, 20, 0, 45, 0,
 '{"21":{"cm":1,"td":1,"tp":3,"total":5},"22":{"cm":1,"td":1,"tp":3,"total":5},"23":{"cm":1,"td":1,"tp":3,"total":5},"24":{"cm":1,"td":1,"tp":3,"total":5},"25":{"cm":1,"td":1,"tp":3,"total":5},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":2,"total":4},"32":{"cm":1,"td":1,"tp":2,"total":4},"33":{"cm":1,"td":1,"tp":2,"total":4},"34":{"cm":1,"td":1,"tp":2,"total":4},"35":{"cm":1,"td":1,"tp":2,"total":4},"36":{"cm":0,"td":1,"tp":2,"total":3},"37":{"cm":0,"td":1,"tp":2,"total":3},"38":{"cm":0,"td":1,"tp":2,"total":3},"39":{"cm":0,"td":1,"tp":2,"total":3},"40":{"cm":0,"td":1,"tp":2,"total":3}}',
 0, 2),

(24, 'Gestion de donnees massives', 'Big Data et traitement de donnees', 'Data',
 20, 0, 25, 0, 35, 0,
 '{"21":{"cm":1,"td":2,"tp":2,"total":5},"22":{"cm":1,"td":2,"tp":2,"total":5},"23":{"cm":1,"td":2,"tp":2,"total":5},"24":{"cm":1,"td":2,"tp":2,"total":5},"25":{"cm":1,"td":2,"tp":2,"total":5},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":2,"total":4},"32":{"cm":1,"td":1,"tp":2,"total":4},"33":{"cm":1,"td":1,"tp":2,"total":4},"34":{"cm":1,"td":1,"tp":2,"total":4},"35":{"cm":1,"td":1,"tp":2,"total":4},"36":{"cm":1,"td":1,"tp":1,"total":3},"37":{"cm":1,"td":1,"tp":1,"total":3},"38":{"cm":1,"td":1,"tp":1,"total":3},"39":{"cm":1,"td":1,"tp":1,"total":3},"40":{"cm":1,"td":1,"tp":1,"total":3}}',
 0, 2),

(25, 'Projet tutore S4', 'Projet en equipe du semestre 4', 'Projet',
 0, 0, 10, 0, 50, 0,
 '{"21":{"cm":0,"td":1,"tp":3,"total":4},"22":{"cm":0,"td":1,"tp":3,"total":4},"23":{"cm":0,"td":1,"tp":3,"total":4},"24":{"cm":0,"td":1,"tp":3,"total":4},"25":{"cm":0,"td":1,"tp":3,"total":4},"26":{"cm":0,"td":1,"tp":3,"total":4},"27":{"cm":0,"td":1,"tp":3,"total":4},"28":{"cm":0,"td":1,"tp":3,"total":4},"29":{"cm":0,"td":1,"tp":3,"total":4},"30":{"cm":0,"td":1,"tp":3,"total":4},"31":{"cm":0,"td":0,"tp":2,"total":2},"32":{"cm":0,"td":0,"tp":2,"total":2},"33":{"cm":0,"td":0,"tp":2,"total":2},"34":{"cm":0,"td":0,"tp":2,"total":2},"35":{"cm":0,"td":0,"tp":2,"total":2},"36":{"cm":0,"td":0,"tp":2,"total":2},"37":{"cm":0,"td":0,"tp":2,"total":2},"38":{"cm":0,"td":0,"tp":2,"total":2},"39":{"cm":0,"td":0,"tp":2,"total":2},"40":{"cm":0,"td":0,"tp":2,"total":2}}',
 0, 2),

-- ========== ANNEE 3 - SEMESTRE 1 - ALTERNANCE (7 modules) ==========
(26, 'Architecture logicielle', 'Patterns et architecture logicielle', 'Programmation',
 20, 0, 20, 0, 30, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":1,"td":1,"tp":1,"total":3},"12":{"cm":1,"td":1,"tp":1,"total":3},"13":{"cm":1,"td":1,"tp":1,"total":3},"14":{"cm":1,"td":1,"tp":1,"total":3},"15":{"cm":1,"td":1,"tp":1,"total":3},"16":{"cm":1,"td":1,"tp":1,"total":3},"17":{"cm":1,"td":1,"tp":1,"total":3},"18":{"cm":1,"td":1,"tp":1,"total":3},"19":{"cm":1,"td":1,"tp":1,"total":3},"20":{"cm":1,"td":1,"tp":1,"total":3}}',
 0, 1),

(27, 'Cloud computing', 'Services cloud et deploiement', 'Systemes',
 15, 0, 15, 0, 30, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":1,"td":1,"tp":1,"total":3},"12":{"cm":1,"td":1,"tp":1,"total":3},"13":{"cm":1,"td":1,"tp":1,"total":3},"14":{"cm":1,"td":1,"tp":1,"total":3},"15":{"cm":1,"td":1,"tp":1,"total":3},"16":{"cm":0,"td":0,"tp":1,"total":1},"17":{"cm":0,"td":0,"tp":1,"total":1},"18":{"cm":0,"td":0,"tp":1,"total":1},"19":{"cm":0,"td":0,"tp":1,"total":1},"20":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 1),

(28, 'DevOps', 'Integration et deploiement continus', 'Systemes',
 10, 0, 15, 0, 35, 0,
 '{"1":{"cm":1,"td":1,"tp":2,"total":4},"2":{"cm":1,"td":1,"tp":2,"total":4},"3":{"cm":1,"td":1,"tp":2,"total":4},"4":{"cm":1,"td":1,"tp":2,"total":4},"5":{"cm":1,"td":1,"tp":2,"total":4},"6":{"cm":1,"td":1,"tp":2,"total":4},"7":{"cm":1,"td":1,"tp":2,"total":4},"8":{"cm":1,"td":1,"tp":2,"total":4},"9":{"cm":1,"td":1,"tp":2,"total":4},"10":{"cm":1,"td":1,"tp":2,"total":4},"11":{"cm":0,"td":1,"tp":2,"total":3},"12":{"cm":0,"td":1,"tp":2,"total":3},"13":{"cm":0,"td":1,"tp":2,"total":3},"14":{"cm":0,"td":1,"tp":2,"total":3},"15":{"cm":0,"td":1,"tp":2,"total":3},"16":{"cm":0,"td":0,"tp":1,"total":1},"17":{"cm":0,"td":0,"tp":1,"total":1},"18":{"cm":0,"td":0,"tp":1,"total":1},"19":{"cm":0,"td":0,"tp":1,"total":1},"20":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 1),

(29, 'Management de projet', 'Gestion avancee de projets informatiques', 'Transversal',
 10, 0, 20, 0, 0, 0,
 '{"1":{"cm":1,"td":1,"tp":0,"total":2},"2":{"cm":1,"td":1,"tp":0,"total":2},"3":{"cm":1,"td":1,"tp":0,"total":2},"4":{"cm":1,"td":1,"tp":0,"total":2},"5":{"cm":1,"td":1,"tp":0,"total":2},"6":{"cm":1,"td":1,"tp":0,"total":2},"7":{"cm":1,"td":1,"tp":0,"total":2},"8":{"cm":1,"td":1,"tp":0,"total":2},"9":{"cm":1,"td":1,"tp":0,"total":2},"10":{"cm":1,"td":1,"tp":0,"total":2},"11":{"cm":0,"td":1,"tp":0,"total":1},"12":{"cm":0,"td":1,"tp":0,"total":1},"13":{"cm":0,"td":1,"tp":0,"total":1},"14":{"cm":0,"td":1,"tp":0,"total":1},"15":{"cm":0,"td":1,"tp":0,"total":1},"16":{"cm":0,"td":1,"tp":0,"total":1},"17":{"cm":0,"td":1,"tp":0,"total":1},"18":{"cm":0,"td":1,"tp":0,"total":1},"19":{"cm":0,"td":1,"tp":0,"total":1},"20":{"cm":0,"td":1,"tp":0,"total":1}}',
 0, 1),

(30, 'Veille technologique', 'Veille et innovation technologique', 'Transversal',
 0, 0, 10, 0, 10, 0,
 '{"1":{"cm":0,"td":1,"tp":1,"total":2},"2":{"cm":0,"td":1,"tp":1,"total":2},"3":{"cm":0,"td":1,"tp":1,"total":2},"4":{"cm":0,"td":1,"tp":1,"total":2},"5":{"cm":0,"td":1,"tp":1,"total":2},"6":{"cm":0,"td":1,"tp":1,"total":2},"7":{"cm":0,"td":1,"tp":1,"total":2},"8":{"cm":0,"td":1,"tp":1,"total":2},"9":{"cm":0,"td":1,"tp":1,"total":2},"10":{"cm":0,"td":1,"tp":1,"total":2}}',
 0, 1),

(31, 'Mission en entreprise S5', 'Periode en entreprise du semestre 5', 'Entreprise',
 0, 0, 0, 0, 0, 0,
 '{}',
 0, 1),

(32, 'Rapport d''activite S5', 'Redaction du rapport d''activite S5', 'Transversal',
 0, 0, 10, 0, 10, 0,
 '{"1":{"cm":0,"td":1,"tp":1,"total":2},"2":{"cm":0,"td":1,"tp":1,"total":2},"3":{"cm":0,"td":1,"tp":1,"total":2},"4":{"cm":0,"td":1,"tp":1,"total":2},"5":{"cm":0,"td":1,"tp":1,"total":2},"6":{"cm":0,"td":1,"tp":1,"total":2},"7":{"cm":0,"td":1,"tp":1,"total":2},"8":{"cm":0,"td":1,"tp":1,"total":2},"9":{"cm":0,"td":1,"tp":1,"total":2},"10":{"cm":0,"td":1,"tp":1,"total":2}}',
 0, 1),

-- ========== ANNEE 3 - SEMESTRE 2 - ALTERNANCE (5 modules) ==========
(33, 'Securite avancee', 'Securite avancee et pentest', 'Securite',
 15, 0, 15, 0, 30, 0,
 '{"21":{"cm":1,"td":1,"tp":2,"total":4},"22":{"cm":1,"td":1,"tp":2,"total":4},"23":{"cm":1,"td":1,"tp":2,"total":4},"24":{"cm":1,"td":1,"tp":2,"total":4},"25":{"cm":1,"td":1,"tp":2,"total":4},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":1,"total":3},"32":{"cm":1,"td":1,"tp":1,"total":3},"33":{"cm":1,"td":1,"tp":1,"total":3},"34":{"cm":1,"td":1,"tp":1,"total":3},"35":{"cm":1,"td":1,"tp":1,"total":3},"36":{"cm":0,"td":0,"tp":1,"total":1},"37":{"cm":0,"td":0,"tp":1,"total":1},"38":{"cm":0,"td":0,"tp":1,"total":1},"39":{"cm":0,"td":0,"tp":1,"total":1},"40":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 2),

(34, 'Intelligence Artificielle avancee', 'Deep learning et IA avancee', 'Data',
 15, 0, 15, 0, 30, 0,
 '{"21":{"cm":1,"td":1,"tp":2,"total":4},"22":{"cm":1,"td":1,"tp":2,"total":4},"23":{"cm":1,"td":1,"tp":2,"total":4},"24":{"cm":1,"td":1,"tp":2,"total":4},"25":{"cm":1,"td":1,"tp":2,"total":4},"26":{"cm":1,"td":1,"tp":2,"total":4},"27":{"cm":1,"td":1,"tp":2,"total":4},"28":{"cm":1,"td":1,"tp":2,"total":4},"29":{"cm":1,"td":1,"tp":2,"total":4},"30":{"cm":1,"td":1,"tp":2,"total":4},"31":{"cm":1,"td":1,"tp":1,"total":3},"32":{"cm":1,"td":1,"tp":1,"total":3},"33":{"cm":1,"td":1,"tp":1,"total":3},"34":{"cm":1,"td":1,"tp":1,"total":3},"35":{"cm":1,"td":1,"tp":1,"total":3},"36":{"cm":0,"td":0,"tp":1,"total":1},"37":{"cm":0,"td":0,"tp":1,"total":1},"38":{"cm":0,"td":0,"tp":1,"total":1},"39":{"cm":0,"td":0,"tp":1,"total":1},"40":{"cm":0,"td":0,"tp":1,"total":1}}',
 0, 2),

(35, 'Projet de fin d''etudes', 'Projet de fin d''etudes en equipe', 'Projet',
 0, 0, 20, 0, 80, 0,
 '{"21":{"cm":0,"td":1,"tp":4,"total":5},"22":{"cm":0,"td":1,"tp":4,"total":5},"23":{"cm":0,"td":1,"tp":4,"total":5},"24":{"cm":0,"td":1,"tp":4,"total":5},"25":{"cm":0,"td":1,"tp":4,"total":5},"26":{"cm":0,"td":1,"tp":4,"total":5},"27":{"cm":0,"td":1,"tp":4,"total":5},"28":{"cm":0,"td":1,"tp":4,"total":5},"29":{"cm":0,"td":1,"tp":4,"total":5},"30":{"cm":0,"td":1,"tp":4,"total":5},"31":{"cm":0,"td":1,"tp":4,"total":5},"32":{"cm":0,"td":1,"tp":4,"total":5},"33":{"cm":0,"td":1,"tp":4,"total":5},"34":{"cm":0,"td":1,"tp":4,"total":5},"35":{"cm":0,"td":1,"tp":4,"total":5},"36":{"cm":0,"td":1,"tp":4,"total":5},"37":{"cm":0,"td":1,"tp":4,"total":5},"38":{"cm":0,"td":1,"tp":4,"total":5},"39":{"cm":0,"td":1,"tp":4,"total":5},"40":{"cm":0,"td":1,"tp":4,"total":5}}',
 0, 2),

(36, 'Mission en entreprise S6', 'Periode en entreprise du semestre 6', 'Entreprise',
 0, 0, 0, 0, 0, 0,
 '{}',
 0, 2),

(37, 'Memoire professionnel', 'Redaction du memoire professionnel', 'Transversal',
 0, 0, 30, 0, 0, 0,
 '{"21":{"cm":0,"td":2,"tp":0,"total":2},"22":{"cm":0,"td":2,"tp":0,"total":2},"23":{"cm":0,"td":2,"tp":0,"total":2},"24":{"cm":0,"td":2,"tp":0,"total":2},"25":{"cm":0,"td":2,"tp":0,"total":2},"26":{"cm":0,"td":2,"tp":0,"total":2},"27":{"cm":0,"td":2,"tp":0,"total":2},"28":{"cm":0,"td":2,"tp":0,"total":2},"29":{"cm":0,"td":2,"tp":0,"total":2},"30":{"cm":0,"td":2,"tp":0,"total":2},"31":{"cm":0,"td":1,"tp":0,"total":1},"32":{"cm":0,"td":1,"tp":0,"total":1},"33":{"cm":0,"td":1,"tp":0,"total":1},"34":{"cm":0,"td":1,"tp":0,"total":1},"35":{"cm":0,"td":1,"tp":0,"total":1},"36":{"cm":0,"td":1,"tp":0,"total":1},"37":{"cm":0,"td":1,"tp":0,"total":1},"38":{"cm":0,"td":1,"tp":0,"total":1},"39":{"cm":0,"td":1,"tp":0,"total":1},"40":{"cm":0,"td":1,"tp":0,"total":1}}',
 0, 2)
ON CONFLICT (id) DO NOTHING;


-- =========================
-- RESSOURCES R&T et SD
-- =========================
INSERT INTO resource (
    id, title, description, category,
    cm_state_hours, cm_iut_hours, td_state_hours, td_iut_hours, tp_state_hours, tp_iut_hours,
    hours_per_week, hours_per_half_group, semester
) VALUES

(38, 'Reseaux locaux', 'Introduction aux reseaux locaux et Ethernet', 'Reseaux',
 20, 0, 20, 0, 20, 0, '{}', 0, 1),

(39, 'Protocoles TCP/IP', 'Modele OSI et protocoles TCP/IP', 'Reseaux',
 15, 0, 25, 0, 20, 0, '{}', 0, 1),

(40, 'Systemes Linux', 'Administration Linux et ligne de commande', 'Systemes',
 10, 0, 15, 0, 25, 0, '{}', 0, 1),

(41, 'Bases des telecommunications', 'Fondamentaux des telecoms et signaux', 'Telecom',
 25, 0, 20, 0, 10, 0, '{}', 0, 1),

-- ========== R&T ANNEE 1 - SEMESTRE 2 ==========
(42, 'Routage et commutation', 'Protocoles de routage IP et commutation', 'Reseaux',
 20, 0, 20, 0, 30, 0, '{}', 0, 2),

(43, 'Securite reseau fondamentaux', 'Introduction a la securite des reseaux', 'Securite',
 15, 0, 15, 0, 20, 0, '{}', 0, 2),

(44, 'Telephonie IP', 'VoIP et protocoles SIP', 'Telecom',
 20, 0, 20, 0, 20, 0, '{}', 0, 2),

(45, 'Virtualisation', 'Virtualisation de serveurs et VM', 'Systemes',
 10, 0, 10, 0, 30, 0, '{}', 0, 2),

-- ========== R&T ANNEE 2 - SEMESTRE 1 ==========
(46, 'Administration systemes', 'Administration avancee Linux/Windows', 'Systemes',
 15, 0, 20, 0, 35, 0, '{}', 0, 1),

(47, 'Reseaux sans fil', 'WiFi, 4G/5G et protocoles mobiles', 'Reseaux',
 20, 0, 20, 0, 25, 0, '{}', 0, 1),

(48, 'Securite avancee reseaux', 'Firewall, VPN et audit de securite', 'Securite',
 20, 0, 15, 0, 25, 0, '{}', 0, 1),

(49, 'Services cloud', 'AWS, Azure et architectures cloud', 'Cloud',
 15, 0, 20, 0, 30, 0, '{}', 0, 1),

-- ========== R&T ANNEE 2 - SEMESTRE 2 ==========
(50, 'Supervision et monitoring', 'Outils de supervision reseau Nagios/Zabbix', 'Reseaux',
 15, 0, 15, 0, 30, 0, '{}', 0, 2),

(51, 'Cryptographie et PKI', 'Cryptographie appliquee et PKI', 'Securite',
 25, 0, 20, 0, 15, 0, '{}', 0, 2),

(52, 'Projet reseau S4', 'Projet integre infrastructure reseau', 'Projet',
 0, 0, 15, 0, 35, 0, '{}', 0, 2),

(53, 'Telecoms mobiles', 'Architecture des reseaux mobiles 5G', 'Telecom',
 20, 0, 20, 0, 20, 0, '{}', 0, 2),

-- ========== SD ANNEE 1 - SEMESTRE 1 ==========
(54, 'Statistiques descriptives', 'Statistiques et distributions univariees', 'Statistiques',
 25, 0, 30, 0, 20, 0, '{}', 0, 1),

(55, 'Programmation Python', 'Python pour la science des donnees', 'Programmation',
 15, 0, 20, 0, 35, 0, '{}', 0, 1),

(56, 'Bases de donnees pour SD', 'SQL et modelisation des donnees', 'Data',
 20, 0, 20, 0, 20, 0, '{}', 0, 1),

(57, 'Algebre lineaire pour SD', 'Vecteurs, matrices et transformations', 'Mathematiques',
 30, 0, 30, 0, 10, 0, '{}', 0, 1),

-- ========== SD ANNEE 1 - SEMESTRE 2 ==========
(58, 'Probabilites et inference', 'Probabilites et tests statistiques', 'Statistiques',
 25, 0, 30, 0, 15, 0, '{}', 0, 2),

(59, 'Analyse de donnees', 'ACP et analyse factorielle', 'Data',
 20, 0, 20, 0, 25, 0, '{}', 0, 2),

(60, 'Visualisation de donnees', 'Matplotlib, Seaborn et dashboards', 'Data',
 15, 0, 15, 0, 30, 0, '{}', 0, 2),

(61, 'SQL avance', 'SQL avance, vues et optimisation', 'Data',
 15, 0, 20, 0, 25, 0, '{}', 0, 2),

-- ========== SD ANNEE 2 - SEMESTRE 1 ==========
(62, 'Machine Learning', 'Algorithmes ML supervises et non-supervises', 'IA',
 25, 0, 20, 0, 25, 0, '{}', 0, 1),

(63, 'Business Intelligence', 'Entrepots de donnees et reporting', 'Data',
 20, 0, 20, 0, 25, 0, '{}', 0, 1),

(64, 'Traitement du langage naturel', 'NLP et analyse textuelle', 'IA',
 20, 0, 15, 0, 25, 0, '{}', 0, 1),

(65, 'Statistiques avancees', 'Regression, ANOVA et series temporelles', 'Statistiques',
 25, 0, 25, 0, 15, 0, '{}', 0, 1),

-- ========== SD ANNEE 2 - SEMESTRE 2 ==========
(66, 'Deep Learning', 'Reseaux de neurones et frameworks TensorFlow', 'IA',
 20, 0, 15, 0, 35, 0, '{}', 0, 2),

(67, 'Big Data et NoSQL', 'Hadoop, Spark et bases NoSQL', 'Data',
 20, 0, 20, 0, 30, 0, '{}', 0, 2),

(68, 'Projet data science S4', 'Projet integre analyse et modelisation', 'Projet',
 0, 0, 15, 0, 40, 0, '{}', 0, 2),

(69, 'Ethique et IA', 'Enjeux sociaux et ethiques de l''IA', 'Transversal',
 30, 0, 20, 0, 10, 0, '{}', 0, 2)
ON CONFLICT (id) DO NOTHING;

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
(5, 37), (6, 37),
-- Informatique G3/G4 Annee 1 (IDs 1-12) -> formations 15 (G3), 16 (G4)
(15, 1), (16, 1), (15, 2), (16, 2), (15, 3), (16, 3), (15, 4), (16, 4),
(15, 5), (16, 5), (15, 6), (16, 6), (15, 7), (16, 7), (15, 8), (16, 8),
(15, 9), (16, 9), (15, 10), (16, 10), (15, 11), (16, 11), (15, 12), (16, 12),
-- Informatique G3/G4 Annee 2 (IDs 13-25) -> formations 17 (G3), 18 (G4)
(17, 13), (18, 13), (17, 14), (18, 14), (17, 15), (18, 15), (17, 16), (18, 16),
(17, 17), (18, 17), (17, 18), (18, 18), (17, 19), (18, 19), (17, 20), (18, 20),
(17, 21), (18, 21), (17, 22), (18, 22), (17, 23), (18, 23), (17, 24), (18, 24),
(17, 25), (18, 25),
-- Informatique G3/G4 Annee 3 (IDs 26-37) -> formations 19 (G3), 20 (G4)
(19, 26), (20, 26), (19, 27), (20, 27), (19, 28), (20, 28), (19, 29), (20, 29),
(19, 30), (20, 30), (19, 31), (20, 31), (19, 32), (20, 32), (19, 33), (20, 33),
(19, 34), (20, 34), (19, 35), (20, 35), (19, 36), (20, 36), (19, 37), (20, 37),
-- R&T Annee 1 (IDs 38-45) -> formations 7 (G1), 11 (G2)
(7, 38), (11, 38), (7, 39), (11, 39), (7, 40), (11, 40), (7, 41), (11, 41),
(7, 42), (11, 42), (7, 43), (11, 43), (7, 44), (11, 44), (7, 45), (11, 45),
-- R&T Annee 2 (IDs 46-53) -> formations 8 (G1), 12 (G2)
(8, 46), (12, 46), (8, 47), (12, 47), (8, 48), (12, 48), (8, 49), (12, 49),
(8, 50), (12, 50), (8, 51), (12, 51), (8, 52), (12, 52), (8, 53), (12, 53),
-- SD Annee 1 (IDs 54-61) -> formations 9 (G1), 13 (G2)
(9, 54), (13, 54), (9, 55), (13, 55), (9, 56), (13, 56), (9, 57), (13, 57),
(9, 58), (13, 58), (9, 59), (13, 59), (9, 60), (13, 60), (9, 61), (13, 61),
-- SD Annee 2 (IDs 62-69) -> formations 10 (G1), 14 (G2)
(10, 62), (14, 62), (10, 63), (14, 63), (10, 64), (14, 64), (10, 65), (14, 65),
(10, 66), (14, 66), (10, 67), (14, 67), (10, 68), (14, 68), (10, 69), (14, 69)
ON CONFLICT DO NOTHING;

-- =========================
-- SYLLABUS
-- =========================
INSERT INTO syllabus (id, description) VALUES
(1, 'Syllabus Programmation'),
(2, 'Syllabus Data'),
(3, 'Syllabus Web'),
(4, 'Syllabus Systemes')
ON CONFLICT (id) DO NOTHING;

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
(14, 2)
ON CONFLICT DO NOTHING;

-- =========================
-- ASSIGNMENT
-- =========================
INSERT INTO assignment (id, assigned_times, lesson_type, resource_id, user_id) VALUES
(1, 20, 'CM', 1, 1),
(2, 25, 'TD', 2, 1),
(3, 20, 'CM', 7, 1),
(4, 30, 'CM', 13, 1), -- Dev App Year 2
(5, 25, 'TD', 26, 1) -- Arch Log Year 3
ON CONFLICT (id) DO NOTHING;

-- =========================
-- TICKETS
-- =========================
INSERT INTO ticket (id, date, resolution_date, description, status, title, user_id) VALUES
(1, '2025-12-17', NULL, 'Erreur sur mes heures de TD en Java, il manque 2h.', 'OPEN', 'Heures manquantes', 2),
(2, '2025-12-17', '2025-12-18', 'J''ai trop d''heures de CM affectées en BDD.', 'RESOLVED', 'Trop d''heures CM', 2),
(3, '2025-12-18', NULL, 'Impossible de modifier mes heures prévisionnelles.', 'OPEN', 'Problème modification heures', 4),
(4, '2025-12-19', NULL, 'Mes heures de TP en réseau ne sont pas comptabilisées.', 'IN_PROGRESS', 'Heures TP manquantes', 5),
(5, '2025-12-20', NULL, 'Serait-il possible de décaler mes heures de projet tutoré ?', 'OPEN', 'Décalage heures projet', 6),
(6, '2025-12-21', '2025-12-23', 'Le système n''enregistre pas mes nouvelles heures vacataires.', 'RESOLVED', 'Enregistrement heures', 7),
(7, '2025-12-22', NULL, 'Problème de validation des mes services par le responsable.', 'IN_PROGRESS', 'Validation des services', 4);


INSERT INTO semester (semester_id, year, semester_number, parcours) VALUES
-- Informatique
(1, 1, 1, ''),
(2, 1, 2, ''),
(3, 2, 1, 'DACS'),
(4, 2, 2, 'DACS'),
(5, 3, 1, 'DACS'),
(6, 3, 2, 'DACS'),

-- R&T
(7, 1, 1, ''),
(8, 1, 2, ''),
(9, 2, 1, ''),
(10, 2, 2, 'RA'),
(11, 3, 1, 'RA'),
(12, 3, 2, 'RA');

INSERT INTO semester_resource (semester_id, resource_id) VALUES

-- =========================
-- SEMESTRE 1
-- =========================
(1, 38),
(1, 39),
(1, 40),
(1, 41),

(1, 46),
(1, 47),
(1, 48),
(1, 49),

(1, 54),
(1, 55),
(1, 56),
(1, 57),

(1, 62),
(1, 63),
(1, 64),
(1, 65),

-- =========================
-- SEMESTRE 2
-- =========================
(2, 42),
(2, 43),
(2, 44),
(2, 45),

(2, 50),
(2, 51),
(2, 52),
(2, 53),

(2, 58),
(2, 59),
(2, 60),
(2, 61),

(2, 66),
(2, 67),
(2, 68),
(2, 69);

-- =========================
-- VACATAIRES
-- Profiles 1-5: already converted → user_id is populated (statut = VALIDE)
-- Profiles 6-8: still in the recruitment pipeline → user_id is NULL
-- =========================
INSERT INTO vacataire (
    id,
    nom, prenom, date_naissance,
    departement, fonction, experience, profil, competences,
    responsable_recrutement, etablissement, site,
    vue_en_amont, transmis_responsable, signature_responsable,
    source_connaissance, source_connaissance_autre,
    statut,
    user_id
) VALUES
-- Converted contractors (linked to existing users)
(1, 'Dupont',   'Martin',   '1985-03-12', 'Informatique',                'Développeur',         '5 ans en entreprise',    'Expert backend Java',      'Java, Spring, Docker',          'Alice Admin', 'IUT Lyon',       'Lyon',      true,  true,  'A. Admin',  'ANCIEN_ETUDIANT',     NULL, 'VALIDE',      4),
(2, 'Moreau',   'Jean',     '1990-07-24', 'Réseaux',                     'Ingénieur réseau',    '3 ans en ESN',           'Spécialiste réseaux',      'Cisco, Linux, Wireshark',       'Alice Admin', 'IUT Lyon',       'Lyon',      true,  true,  'A. Admin',  'RECOMMANDATION',      NULL, 'VALIDE',      6),
(3, 'Petit',    'Claire',   '1988-11-05', 'Développement web',           'Développeuse full-stack','4 ans startup',        'React, Node.js expert',    'React, Node.js, PostgreSQL',    'Alice Admin', 'IUT Grenoble',   'Grenoble',  true,  true,  'A. Admin',  'ANNONCE',             NULL, 'VALIDE',      7),
(4, 'Roux',     'Nathalie', '1992-02-18', 'Data',                        'Data Analyst',        '2 ans en banque',        'Python, BI spécialisée',   'Python, SQL, Power BI',         'Alice Admin', 'IUT Lyon',       'Lyon',      true,  false, NULL,        'ANCIEN_ETUDIANT',     NULL, 'VALIDE',      9),
(5, 'Garcia',   'Francois', '1983-09-30', 'Sécurité informatique',       'Expert cybersécurité','8 ans indépendant',      'OSCP certifié',            'Pentesting, SIEM, ISO 27001',   'Alice Admin', 'IUT Saint-Etienne','Saint-Etienne',true,true,'A. Admin', 'RECOMMANDATION',      NULL, 'VALIDE',      10),
-- Pending contractors (no user account yet)
(6, 'Lambert',  'Thomas',   '1995-06-14', 'Intelligence Artificielle',   'Ingénieur ML',        '1 an post-doc',          'Spécialiste NLP',          'Python, TensorFlow, PyTorch',   'Bob Teacher', 'Université Lyon 1','Lyon',    false, false, NULL,        'SALON',               NULL, 'A_CONTACTER', NULL),
(7, 'Lefevre',  'Julie',    '1991-04-22', 'Développement mobile',        'Développeuse iOS/Android','3 ans agence',       'Swift, Kotlin',            'Swift, Kotlin, Flutter',        'Bob Teacher', 'IUT Valence',    'Valence',   true,  false, NULL,        'RECOMMANDATION',      NULL, 'EN_COURS',    NULL),
(8, 'Simon',    'Alexandre','1987-12-01', 'Base de données',             'DBA Oracle',          '6 ans en entreprise',    'Expert Oracle/PostgreSQL', 'Oracle, PostgreSQL, MongoDB',   'Bob Teacher', 'IUT Lyon',       'Lyon',      true,  false, NULL,        'ANCIEN_ETUDIANT',     NULL, 'EN_COURS',    NULL)
ON CONFLICT (id) DO NOTHING;

-- =========================
-- RESET SEQUENCES
-- =========================
SELECT setval('position_id_seq',  (SELECT MAX(id) FROM position));
SELECT setval('users_id_seq',     (SELECT MAX(id) FROM users));
SELECT setval('formation_id_seq', (SELECT MAX(id) FROM formation));
SELECT setval('resource_id_seq',  (SELECT MAX(id) FROM resource));
SELECT setval(pg_get_serial_sequence('semester', 'semester_id'), (SELECT COALESCE(MAX(semester_id), 0) FROM semester));
SELECT setval('syllabus_id_seq',  (SELECT MAX(id) FROM syllabus));
SELECT setval('assignment_id_seq',(SELECT MAX(id) FROM assignment));
SELECT setval('ticket_id_seq',    (SELECT MAX(id) FROM ticket));
SELECT setval('vacataire_id_seq', (SELECT MAX(id) FROM vacataire));
