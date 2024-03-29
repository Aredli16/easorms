# Easorms

Easorms is a ORM for postgresql, it's easy to use and has a lot of features.

## 💡Requirements

- Java 21
- Docker
- Docker-compose

## ⚙️ Installation

### 🏗️ Build application

```bash
yarn build
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

After that, you can run application with docker-compose for production:

```bash
yarn start
```

Or you can run application for development:

```bash
yarn dev
```

Don't forget to create realm and client in Keycloak. Create a realm named "easorms" and a client named "app". Create
realm roles "admin" and assign it to user.
