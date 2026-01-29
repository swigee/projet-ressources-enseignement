-- CLEANUP SCRIPT
SET FOREIGN_KEY_CHECKS = 0;

-- Drop Old Ghost Tables
DROP TABLE IF EXISTS ressourcessyllabus;
DROP TABLE IF EXISTS formationressources;
DROP TABLE IF EXISTS userformation;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS ressources;
DROP TABLE IF EXISTS users;

-- Drop New Tables (to ensure fresh start)
DROP TABLE IF EXISTS resource_syllabus;
DROP TABLE IF EXISTS formation_resource;
DROP TABLE IF EXISTS user_formation;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS assignment;
DROP TABLE IF EXISTS resource;
DROP TABLE IF EXISTS syllabus;
DROP TABLE IF EXISTS formation;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS user;

SET FOREIGN_KEY_CHECKS = 1;
