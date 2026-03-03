# Ressources Enseignements SAE

Ce projet est une application web de gestion des ressources d'enseignements, développée avec Angular (frontend) et Spring Boot (backend). Il permet la gestion des utilisateurs, des rôles, des formations, des tickets et des affectations enseignants.

## Prérequis

- Docker et Docker Compose
- Node.js et npm (pour développement frontend)
- Maven (pour développement backend)

## Installation rapide (via Docker)

1. Cloner le projet :

   ```
   git clone https://forge.univ-lyon1.fr/hlib.fedorchenko/ressources-enseignements-sae.git
   ```
2. Lancer les conteneurs :

   ```
   docker compose up --build
   ```
3. Accéder à l'application :

   - http://localhost:4200/

## Lancement manuel (développement)

### Prérequis

- Maven
- Java 21
- Wamp (pour la base de données) (creer une base de données nommée `ressources_enseignements`)

### Backend

- Installer Maven : https://maven.apache.org/download.cgi
- Se placer dans `backend/ressources_enseignements` puis lancer :
  ```
  mvn clean spring-boot:run
  ```

### Frontend

- Installer Node.js : https://nodejs.org/
- Se placer dans `frontend/ressources_enseignements` puis lancer :
  ```
  npm install
  ng serve
  ```
- Accéder à : http://localhost:4200

## Structure du projet

- `backend/ressources_enseignements` : API Spring Boot
- `frontend/ressources_enseignements` : Application Angular

## Fonctionnalités principales

- Gestion des utilisateurs et rôles
- Gestion des formations et affectations
- Système de tickets
- Authentification

## Comptes pour tester

- Admin

  username: admin

  password: Admin123
- Prof

  username: prof

  password: Prof123

## Auteur

SERIANI Selsebil

SACCONE Enzo

RANDRIANOMENJANAHARY Andry

FEDORCHENKO Hlib

Projet réalisé dans le cadre de la SAE.

---
