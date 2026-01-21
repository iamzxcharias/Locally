# Locally - Backend Dokumentation

Locally ist eine Plattform zum Entdecken und Teilen kleiner Community-Events in der Nachbarschaft, die auf einer einfachen, kartenbasierten Schnittstelle basiert. Das Backend stellt eine REST-API bereit, um Nutzer, Veranstaltungen, Teilnahmen und soziale Interaktionen zu verwalten.

## Architektur

Das Projekt implementiert eine hexagonale Architektur (Ports and Adapters), um eine strikte Trennung zwischen der Geschäftslogik und der technischen Infrastruktur zu gewährleisten.


### Verzeichnisstruktur
* domain: Enthält den Kern der Anwendung inklusive der Geschäftsmodelle, Services und Schnittstellen (Ports).
* api: Fungiert als Inbound-Adapter und stellt REST-Controller sowie DTOs für die Kommunikation mit dem Client bereit.
* persistence: Fungiert als Outbound-Adapter und beinhaltet das Datenbank-Mapping, Repositories und Mapper zur Übersetzung zwischen technischer Entity und fachlichem Modell.

## Funktionsumfang

Das System deckt folgende Anwendungsfälle ab:
* Event-Management (UC1): Erstellung und Verwaltung von Events inklusive Validierung von Titel, Datum und Geodaten.
* Event-Suche (UC2): Filtern von Veranstaltungen nach Kategorien, Zeiträumen oder Suchbegriffen mit Unterstützung für Geospatial-Daten.
* Teilnahme-System (UC3): Möglichkeit für Nutzer, ihr Interesse oder ihre Teilnahme an Events zu signalisieren.
* Soziale Funktionen (UC4 & UC5): Einladen von Freunden zu Events und Einsehen der Aktivitäten von vernetzten Nutzern.

## Datenintegrität

Die Geschäftsregeln werden direkt im Domain-Modell erzwungen, um einen konsistenten Systemzustand sicherzustellen:
* Events müssen einen Titel und einen Ersteller besitzen.
* Das Startdatum muss zum Zeitpunkt der Erstellung in der Zukunft liegen.
* Geographische Koordinaten müssen in den validen Bereichen für Breitengrad (-90 bis 90) und Längengrad (-180 bis 180) liegen.

## Starten des Projekts

### Voraussetzungen
* Installiertes Java 21.
* Maven.

### Ausführung
Um das Projekt im Entwicklungsmodus zu starten (inklusive Hot-Reload), nutzen Sie folgenden Befehl im Hauptverzeichnis:

```text
mvn quarkus:dev
```

Alternativ über den Wrapper:

```text
./mvnw quarkus:dev
```

## Testen

Das Projekt umfasst Unit Tests für die Geschäftslogik sowie Integrationstests für die REST-Schnittstellen und die Persistenzschicht.

Um alle Tests gesammelt auszuführen und das Projekt vorher zu bereinigen, verwenden Sie:

```text
mvn clean test
```

## API-Übersicht

| Endpunkt | Methode | Beschreibung |
| :--- | :--- | :--- |
| /events | GET | Abrufen und Filtern von Veranstaltungen. |
| /events | POST | Erstellen eines neuen Events. |
| /users | POST | Registrierung neuer Nutzer. |
| /participations | POST | Registrierung oder Änderung eines Teilnahmestatus. |
| /friendships | POST | Erstellen einer neuen Freundschaftsbeziehung. |
