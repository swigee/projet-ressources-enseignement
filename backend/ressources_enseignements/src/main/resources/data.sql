-- =========================
-- NETTOYAGE DES ANCIENNES TABLES (Legacy)
-- =========================
-- DROP TABLE IF EXISTS ressourcessyllabus;
-- DROP TABLE IF EXISTS formationressources;
-- DROP TABLE IF EXISTS userformation;
-- DROP TABLE IF EXISTS tickets;
-- DROP TABLE IF EXISTS ressources;
-- DROP TABLE IF EXISTS users;
-- DROP TABLE IF EXISTS role;
-- DROP TABLE IF EXISTS app_role;
-- DROP TABLE IF EXISTS position;
-- Note: les nouvelles tables sont gérées par ddl-auto=create

-- =========================
-- RESET AUTO_INCREMENT
-- =========================
-- ALTER TABLE assignment AUTO_INCREMENT = 1;
-- ALTER TABLE ticket AUTO_INCREMENT = 1;
-- ALTER TABLE resource AUTO_INCREMENT = 1;
-- ALTER TABLE syllabus AUTO_INCREMENT = 1;
-- ALTER TABLE formation AUTO_INCREMENT = 1;
-- ALTER TABLE role AUTO_INCREMENT = 1;
-- ALTER TABLE user AUTO_INCREMENT = 1;

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
(1, '1 rue Admin', 'admin@mail.com', 'Alice', 'Admin', 'admin123', '0101010101', 'VALIDATED', 'admin'),
(2, '2 rue Prof', 'prof@mail.com', 'Bob', 'Teacher', 'prof123', '0202020202', 'VALIDATED', 'prof'),
(3, '3 rue Etudiant', 'student@mail.com', 'Charlie', 'Student', 'student123', '0303030303', 'NONE', 'student');

-- =========================
-- USER ROLE
-- =========================
INSERT INTO user_role (role_id, user_id) VALUES
(1, 1),
(2, 2),
(3, 3);

-- =========================
-- FORMATIONS
-- =========================
INSERT INTO formation (id, name, year, class_name) VALUES
-- Année 1
(1, 'Informatique', '1', 'Classe A'),
(2, 'Informatique', '1', 'Classe B'),
(3, 'Mathématiques', '1', 'Classe A'),
(4, 'Mathématiques', '1', 'Classe B'),

-- Année 2
(5, 'Informatique', '2', 'Classe A'),
(6, 'Informatique', '2', 'Classe B'),
(7, 'Mathématiques', '2', 'Classe A'),
(8, 'Physique', '2', 'Classe A'),

-- Année 3
(9, 'Informatique', '3', 'Classe A'),
(10, 'Informatique', '3', 'Classe B');

-- =========================
-- USER FORMATION
-- =========================
INSERT INTO user_formation (formation_id, user_id) VALUES
(1, 2),  -- Prof pour Info Année 1 Classe A
(5, 2),  -- Prof pour Info Année 2 Classe A
(1, 3),  -- Étudiant pour Info Année 1 Classe A
(3, 3);  -- Étudiant pour Maths Année 1 Classe A

-- =========================
-- RESSOURCES
-- =========================
INSERT INTO resource (
    id,
    title,
    description,
    category,
    is_highlighted,
    cm_state_hours,
    cm_iut_hours,
    td_state_hours,
    td_iut_hours,
    tp_state_hours,
    tp_iut_hours,
    hours_per_week,
    hours_per_half_group
) VALUES
-- Ressources Informatique
(1, 'Programmation Java', 'Cours de base en Java', 'Programmation', false,
 10, 12, 8, 10, 6, 8, '{"1":4,"2":4,"3":4,"4":4}', 2),

(2, 'Base de données', 'Introduction aux BDD', 'Data', true,
 8, 10, 6, 8, 4, 6, '{"1":3,"2":3,"3":3,"4":3}', 1),

(3, 'Développement Web', 'HTML, CSS, JavaScript', 'Web', false,
 6, 8, 8, 10, 10, 12, '{"1":5,"2":5,"3":5,"4":5}', 3),

-- Ressources Mathématiques
(4, 'Algèbre linéaire', 'Matrices et vecteurs', 'Mathématiques', true,
 12, 14, 10, 12, 8, 10, '{"1":6,"2":6,"3":6,"4":6}', 2),

(5, 'Probabilités', 'Statistiques et probas', 'Mathématiques', false,
 10, 12, 8, 10, 6, 8, '{"1":4,"2":4,"3":4,"4":4}', 1),

-- Ressources Physique
(6, 'Mécanique classique', 'Physique générale', 'Physique', false,
 8, 10, 6, 8, 4, 6, '{"1":3,"2":3,"3":3,"4":3}', 2);

-- =========================
-- FORMATION RESSOURCES
-- =========================
INSERT INTO formation_resource (formation_id, resource_id) VALUES
-- Année 1 Classe A
(1, 1),  -- Java pour Info Année 1 Classe A
(1, 2),  -- BDD pour Info Année 1 Classe A
(3, 4),  -- Algèbre pour Maths Année 1 Classe A
(3, 5),  -- Probas pour Maths Année 1 Classe A

-- Année 1 Classe B
(2, 1),  -- Java pour Info Année 1 Classe B
(2, 3),  -- Web pour Info Année 1 Classe B

-- Année 2 Classe A
(5, 2),  -- BDD pour Info Année 2 Classe A
(5, 3),  -- Web pour Info Année 2 Classe A
(7, 4),  -- Algèbre pour Maths Année 2 Classe A
(8, 6),  -- Mécanique pour Physique Année 2 Classe A

-- Année 2 Classe B
(6, 1),  -- Java pour Info Année 2 Classe B

-- Année 3 Classe A
(9, 3);  -- Web pour Info Année 3 Classe A

-- =========================
-- SYLLABUS
-- =========================
INSERT INTO syllabus (id, description) VALUES
(1, 'Syllabus Programmation'),
(2, 'Syllabus Mathématiques'),
(3, 'Syllabus Web'),
(4, 'Syllabus Physique');

-- =========================
-- RESSOURCES SYLLABUS
-- =========================
INSERT INTO resource_syllabus (resource_id, syllabus_id) VALUES
(1, 1),  -- Java -> Syllabus Prog
(2, 1),  -- BDD -> Syllabus Prog
(3, 3),  -- Web -> Syllabus Web
(4, 2),  -- Algèbre -> Syllabus Maths
(5, 2),  -- Probas -> Syllabus Maths
(6, 4);  -- Mécanique -> Syllabus Physique

-- =========================
-- ASSIGNMENT
-- =========================
INSERT INTO assignment (id, assigned_times, lesson_type, resource_id, user_id) VALUES
(1, 5, 'CM', 1, 2),  -- Prof enseigne Java en CM
(2, 3, 'TD', 2, 2),  -- Prof enseigne BDD en TD
(3, 2, 'TP', 3, 2);  -- Prof enseigne Web en TP

-- =========================
-- TICKETS
-- =========================
INSERT INTO ticket (id, date, description, status, title, user_id) VALUES
(1, '2025-12-17', 'Problème avec Java', 'OPEN', 'Question Java', 3),
(2, '2025-12-17', 'Question sur BDD', 'CLOSED', 'Aide BDD', 3),
(3, '2025-12-18', 'Bug sur le site web', 'OPEN', 'Bug Web', 3);
