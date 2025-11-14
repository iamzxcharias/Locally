# Locally
Practical Project for the Backend Module.

# File Structure
```
project-root/
├── .gitignore
├── pom.xml                                 <-- Maven Konfigurationsdatei 
├── README.md                               <-- Wichtig: Startanleitung, Testhinweise 
├── Dockerfile                              <-- Für die Bereitstellung als Container 
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/thws/backendsystems/project/
│   │   │       ├── domain/                 <-- 1. DOMAIN (CORE / BUSINESS LOGIC) 
│   │   │       │   ├── model/              <-- Entitäten, Value Objects (z.B. Customer)
│   │   │       │   ├── service/            <-- Business Logik / Use Cases (z.B. CustomerService) 
│   │   │       │   └── port/               <-- Java-Interfaces (Ports) 
│   │   │       │
│   │   │       ├── api/                    <-- 2. API (ADAPTER / ENTRY POINT) 
│   │   │       │   ├── controller/         <-- REST-Controller (z.B. CustomerController) 
│   │   │       │   └── dto/                <-- Data Transfer Objects (Request/Response)
│   │   │       │
│   │   │       └── persistence/            <-- 3. PERSISTENCE (ADAPTER / DATA INFRASTRUCTURE) 
│   │   │           ├── adapter/            <-- Implementiert Domain-Ports (z.B. JpaCustomerAdapter)
│   │   │           ├── entity/             <-- JPA-Entities (Datenbank-Mapping) 
│   │   │           └── repository/         <-- Data Access Layer (z.B. Panache/Hibernate) 
│   │   │
│   │   └── resources/                      <-- Quarkus Konfigurationsdateien
│   │       └── application.properties      <-- DB-Zugangsdaten, Ports, etc.
│   │
│   └── test/                               <-- Hier kommen alle Tests hinein 
│       └── java/
│           └── com/thws/backendsystems/project/
│               ├── domain/                 <-- Unit Tests für die Geschäftslogik 
│               │   └── service/
│               │       └── CustomerServiceTest.java
│               │
│               ├── api/                    <-- Integrationstests auf API-Ebene 
│               │   └── controller/
│               │       └── CustomerControllerIT.java  (IT = Integration Test)
│               │
│               └── persistence/            <-- Tests der Persistence Komponente 
│                   └── repository/
│                       └── CustomerRepositoryTest.java
│
└── screencast.mp4                          <-- Optional, falls im Repository gespeichert 
```