# To-Do App - Fullstack (Spring Boot + React + Couchbase)

This is a fullstack To-Do application that supports user authentication and individual task management.  
It includes:

- Spring Boot Backend with JWT authentication and Couchbase support
- React Frontend
- Dockerized deployment option

---

## 1. Run the Project via Docker (Recommended)

This setup will automatically build and run the **frontend**, **backend**, and **Couchbase** together.

### Prerequisites

- Docker and Docker Compose installed

### Step 1: Clone this Repository

```bash
git clone https://github.com/oguzhanerten/todo-fullstack.git
cd todo-fullstack
```

Make Sure the Folder Structure Looks Like This

```bash
todo-fullstack/
├── docker-compose.yml
└── couchbase/
    └── init.sh
```

To run this project via Docker Compose, please make sure to include the `couchbase/init.sh` script alongside the `docker-compose.yml` file, preserving the same folder structure.

Then simply run:

```bash
docker compose up --build
```

**Note:** No need to build anything locally. All images are pulled from Docker Hub:

- Backend: `oguzhanerten/todo-backend`
- Frontend: `oguzhanerten/todo-frontend`

- Setup Time: ~1 minute with Docker.

### To Access the App, visit:

- **Frontend**: [http://localhost:3000](http://localhost:3000)
- **Swagger (Backend API Docs)**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Couchbase Console**: [http://localhost:8091](http://localhost:8091)
- Username: `admin`
- Password: `123456`

---

## 2. Manual Setup (For Development)

### Backend Setup

#### Prerequisites

- Java 17 or higher
- Maven
- Couchbase Server running locally on `localhost:8091`, with:
- If you’re running the backend without Docker, please make sure:
  • Couchbase server is running on localhost:8091
  • A bucket named todo_bucket exists
  • Admin credentials are:
  • Username: admin
  • Password: 123456

Note: A bucket named todo_bucket will be automatically created only when using Docker via the init.sh script.

-

#### Build and Run

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

#### Run Tests

```bash
mvn test
```

---

### Frontend Setup

#### Prerequisites

- Node.js (version 18 or higher recommended)
- npm

#### Install and Start

```bash
cd frontend
npm install
npm start
```

---

## Project Structure

```
├── backend/               # Spring Boot backend
├── frontend/              # React frontend
├── docker-compose.yml     # Docker configuration
├── README.md              # Project documentation
├── couchbase/init.sh      # Couchbase DB init
```

---

## Authentication

JWT-based login and registration system is implemented.

---

## Test Coverage

- Unit tests written for service and controller layers
- Execute with: `mvn test`
- For code coverage, run tests via your IDE (e.g., IntelliJ) with “Run with Coverage”.
