# Objectvault

**ObjectVault: A Distributed File Storage Platform**  
ObjectVault is a secure and scalable object storage solution built with Spring Boot, S3, Redis, and Streamlit. It features JWT-authenticated endpoints for file upload, download, and deletion, optimized with Redis caching for low-latency performance. Deployed in GCP, the project uses Docker Compose for streamlined deployment and CI/CD pipelines via GitHub Actions for seamless updates.

## 📦 Technology Stack
- SpringBoot(Java)
- Redis
- Docker
- S3 Storage (Minio)
- SQL
- Streamlit


## 🏗️ Architecture


![sysarch](assets/ObjectVault.jpeg?raw=true "sysarch")


## Building the project on local

Clone the repository
```sh
git clone https://github.com/Athyadw45/Objectvault.git
```

`cd` into the repository and create `.env` file with following variables
```.dotenv
DB_ENDPOINT=localhost
DB_PORT=3306
DATABASE_NAME=
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET_KEY= 
MINIO_ROOT_USER=
MINIO_ROOT_PASSWORD=
MINIO_CLIENT_USERNAME=
MINIO_CLIENT_PASSWORD=
MINIO_SERVER_ENDPOINT=localhost
OBJECTVAULT_HOST=localhost
OBJECTVAULT_PORT=3001
REDIS_HOST=localhost:7000,locahost:7001,locahost:7002,locahost:7003,locahost:7004,locahost:7005 
```

`cd` into `mysqldb` and create another `.env` file
```dotenv
MYSQL_DATABASE=
MYSQL_ROOT_PASSWORD=
MYSQL_USER=
MYSQL_PASSWORD=
```

set local environment variables from root directory
```sh
 . ./setLocalEnv.sh 
```

Spinup mySQL and S3 (minio) containers
```sh
docker compose up streamlit_ui minio mysqldb -d 
```

Start reids container
```sh
cd redis
docker compose up -d 
```

To build and run springboot application
```sh
./gradlew clean build bootRun 
```

Access the UI from the browser `localhost:8501`


![screenshot1](assets/ss1.png?raw=true "screenshot1")

![screenshot2](assets/ss2.png?raw=true "screenshot2")




