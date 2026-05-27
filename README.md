# Proyecto Gestión

## Requisitos previos

Asegúrate de tener instalado lo siguiente antes de continuar:

- Java 25
- Maven
- PostgreSQL
- WSL2 con Ubuntu
- Redis (instalado en WSL2)
- Git

---

## 1. Clonar el repositorio

```bash
git clone https://github.com/utp-proyectos/prisma-api.git
cd prisma-api
```

---

## 2. Configurar variables de entorno

Copia el archivo de ejemplo y rellena tus valores:

```bash
cp .env.example .env
```

Abre el `.env` y completa cada variable con tus datos locales:

```properties
DB_HOST=localhost
DB_PORT=5432
DB_NAME=prisma
DB_USERNAME=postgres
DB_PASSWORD=tu_password

REDIS_HOST=localhost
REDIS_PORT=6379

JWT_SECRET=        # genera uno con: openssl rand -base64 32

GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=

GITHUB_CLIENT_ID=
GITHUB_CLIENT_SECRET=

MAIL_USERNAME=
MAIL_PASSWORD=
```

---

## 3. Crear la base de datos en PostgreSQL

Abre una terminal y conéctate a PostgreSQL:

```bash
psql -U postgres
```

Crea la base de datos:

```sql
CREATE DATABASE prisma;
```

Verifica que se creó:

```sql
\l
```

Sal de psql:

```sql
\q
```

---

## 4. Iniciar Redis en WSL2

Instalar wsl (te pedira crear usuario y contraseña al instalar ubuntu):

```bash
wsl --install
```

> Debes reiniciar tu pc para continuar

Actualizar paquetes:

```bash
sudo apt update && sudo apt upgrade -y
```

instalar redis:

```bash
sudo apt install redis-server
```

inicia el servidor de Redis:

```bash
sudo service redis-server start
```

Verifica que está corriendo:

```bash
redis-cli ping
```

Deberías ver `PONG`. Si no, intenta:

```bash
sudo service redis-server restart
```

> Redis debe estar corriendo antes de arrancar la aplicación.

> Recuerda iniciarlo cada vez que reinicies tu máquina.

---

## 5. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

---