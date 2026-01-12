# Locally - Backend Architektur Dokumentation

Dieses Projekt basiert auf einer hexagonalen Architektur (Ports and Adapters) unter Verwendung von Quarkus. Diese Struktur stellt sicher, dass die Geschäftslogik strikt von technischen Infrastruktur-Details getrennt bleibt.

## Projektstruktur

Die Verzeichnisstruktur folgt der Aufteilung in Domain, API und Persistence:

```text
project-root/
├── .gitignore
├── pom.xml                              <-- Maven Konfigurationsdatei 
├── README.md                            <-- Startanleitung, Testhinweise 
├── Dockerfile                           <-- Container-Bereitstellung
├── src/
│   ├── main/
│   │   ├── java/
│   │   │    ├── domain/                 <-- 1. DOMAIN (CORE / BUSINESS LOGIC) 
│   │   │    │   ├── model/              <-- Entitäten, Value Objects (z.B. User, Event)
│   │   │    │   ├── service/            <-- Business Logik / Use Cases
│   │   │    │   └── port/               <-- Java-Interfaces (Inbound & Outbound Ports)
│   │   │    │
│   │   │    ├── api/                    <-- 2. API (ADAPTER / ENTRY POINT) 
│   │   │    │   ├── controller/         <-- REST-Controller
│   │   │    │   └── dto/                <-- Data Transfer Objects (Request/Response)
│   │   │    │
│   │   │    └── persistence/            <-- 3. PERSISTENCE (ADAPTER / DATA INFRASTRUCTURE) 
│   │   │        ├── adapter/            <-- Implementiert Outbound Ports (Domain-Interfaces)
│   │   │        ├── entity/             <-- JPA-Entities (Datenbank-Mapping)
│   │   │        ├── mapper/             <-- Mapping-Logik (Übersetzung Entity <-> Domain)
│   │   │        └── repository/         <-- Data Access Layer (Panache/Hibernate)
│   │   │
│   │   └── resources/                   
│   │       └── application.properties   <-- Konfiguration (DB, Ports, Profile)
│   │
│   └── test/                            <-- Test-Suiten
│       └── java/
│            ├── domain/                 <-- Unit Tests für die Geschäftslogik
│            │   └── service/
│            │
│            ├── api/                    <-- Integrationstests der REST-Endpunkte
│            │   └── controller/
│            │
│            └── persistence/            <-- Tests der Dateninfrastruktur
│                ├── mapper/             <-- Unit Tests für Mapping-Korrektheit
│                └── repository/         <-- Integrationstests (DB-Zugriff)

```

## Schichtentrennung und Mapping

Ein zentrales Merkmal dieser Architektur ist die Verwendung von **Mappern** innerhalb der Persistence-Schicht.

* **Zweck:** Mapper dienen als Dolmetscher zwischen den JPA-Entities (technische Datenbank-Repräsentation) und den Domain-Modellen (fachliche Repräsentation).
* **Vorteil:** Änderungen am Datenbankschema wirken sich nicht direkt auf den Core aus. Die Business-Logik bleibt "sauber" und frei von Persistenz-Annotationen.
* **Immutability:** Die Domain-Modelle verwenden finale Felder. Mapper nutzen spezialisierte Konstruktoren, um Objekte beim Laden aus der Datenbank wiederherzustellen, ohne deren Kapselung zu verletzen.

---

## Teststrategie

Das Projekt nutzt einen differenzierten Testansatz:

### Unit Tests

* **Domain Services:** Testen der Geschäftsregeln ohne Abhängigkeiten.
* **Mapper Tests:** Stellen sicher, dass Felder (insbesondere bei vielen Parametern wie beim Event) korrekt zugewiesen werden und Enums richtig übersetzt werden.

### Integration Tests

* **Persistence Layer:** Verwendung von `@QuarkusTest` zur Verifizierung der Repositories und Datenbank-Abfragen gegen eine Test-Datenbank.
* **API Layer:** End-to-End Tests der REST-Schnittstellen mit REST-assured.

---

## Starten des Projekts

1. **Entwicklungsmodus:**
```bash
./mvnw quarkus:dev

```


2. **Tests ausführen:**
```bash
mvn test

```



---
