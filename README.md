# Locally - Backend Documentation

Locally is a platform for discovering and sharing small community events in the neighborhood, based on a simple, map-based interface. The backend provides a REST API to manage users, events, participations, and social interactions.

## Architecture

The project implements a hexagonal architecture (Ports and Adapters) to ensure a strict separation between business logic and technical infrastructure.


### Directory Structure
* domain: Contains the core of the application including business models, services, and interfaces (ports).
* api: Acts as the inbound adapter and provides REST controllers as well as DTOs for communication with the client.
* persistence: Acts as the outbound adapter and contains database mapping, repositories, and mappers for translating between technical entities and the domain model.

## Scope of Functionality

The system covers the following use cases:
* Event Management (UC1): Creation and management of events including validation of title, date, and geodata.
* Event Search (UC2): Filtering events by categories, time ranges, or search terms with support for geospatial data.
* Participation System (UC3): Allows users to indicate their interest in or participation in events.
* Social Features (UC4 & UC5): Inviting friends to events and viewing the activities of connected users.

## Data Integrity

Business rules are enforced directly in the domain model to ensure a consistent system state:
* Events must have a title and a creator.
* The start date must be in the future at the time of creation.
* Geographic coordinates must be within valid ranges for latitude (-90 to 90) and longitude (-180 to 180).

## Running the Project

### Prerequisites
* Docker (Windows: Docker Desktop must be installed and running)
* Docker Compose
* No processes running on port 8080

### Deployment
Clone the repository and start the application from the project root (where docker-compose.yml is located):

```bash
git clone https://github.com/iamzxcharias/Locally.git
cd Locally
```

## Build and Start the Docker Container
```bash
docker-compose up --build -d
```
Verify the container is running:
```bash
docker ps
```

## Application Access
Once started, the application will be available at:
* http://localhost:8080/events

If you want to watch the startup logs:
```bash
docker logs -f locally
```

## Testing
You can run the test suite inside the running container with:
```bash
docker exec -it locally mvn test
```
You will see the test output directly in the console.
If you want to inspect the test reports afterwards, they are located in: `target/surefire-reports`

## Stopping the Application
If you need to stop the application:
```bash
docker-compose down
```
This will stop and remove the application container.

## Full Cleanup
If you want to completely reset the environment:
```bash
docker-compose down -v --remove-orphans
```
After that, you have to rebuild and start everything again.

## Restarting the Application After Stopping
If you've previously stopped the application container or the database container and want to restart it:
```bash
docker-compose up -d
```
If you made changes and need to rebuild before restarting, use:
```bash
docker-compose build --no-cache -d
docker-compose up -d
```

## API-Übersicht

| Endpunkt | Methode | Beschreibung |
| :--- | :--- | :--- |
| /events | GET | Abrufen und Filtern von Veranstaltungen. |
| /events | POST | Erstellen eines neuen Events. |
| /users | POST | Registrierung neuer Nutzer. |
| /participations | POST | Registrierung oder Änderung eines Teilnahmestatus. |
| /friendships | POST | Erstellen einer neuen Freundschaftsbeziehung. |
