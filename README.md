# Easorms

Easorms is a ORM for postgresql, it's easy to use and has a lot of features.

## 💡Requirements

- Java 21
- Docker
- Docker-compose

## ⚙️ Installation

### 🏗️ Build API in local

```bash
./back/mvnw clean install
```

## 🚀 Run application

Before run application, you need change /etc/hosts file and add this line:

```text
127.0.0.1 keycloak
```

This line must be added because Keycloak and Spring services communicate via the Docker network (and therefore use "
keycloak" as hostname). The problem is that front end applications that use browser use "localhost" as hostname. There
is
therefore a problem when verifying the token because (the issuer uri is "localhost" when requesting a front service
and "keycloak" when verifying a back service). Hoping to find a solution later to avoid modifying a system file...

After that, you can run application with docker-compose:

```bash
docker-compose up -d
```
