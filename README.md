# Import App

## Table of contents

1. [A little word from me](#a-little-word-from-me)
2. [AI disclaimer](#ai-disclaimer)
3. [Persistence](#persistence)
4. [API](#api)
5. [Importer](#importer)
6. [Run](#run)

## A little word from me

I wanted to extend the assignment a bit by pretending this would not be a one-time script, but a bigger importer of data that runs periodically. That is why some parts may look a little over-engineered for the raw assignment. I created a small read-only API to show how the system might look from the outside, and I separated persistence entities and repositories into a reusable module so both the importer and API can use the same database model.

The API is currently missing security, proper production logging, observation, and probably a few other things that would be needed in a real system. I did not add those mainly because of time.

## AI disclaimer

Since I want to continue my career in the Java Backend direction, I tend to limit my AI usage mostly to conversation and code review instead of direct code generation. In this project, the two main areas where I used AI were tests and Flyway migrations. I also used it while working in the importer package, but most of that code was rewritten by hand anyway.

## Persistence

The two main assignment entities are:

- `Municipality`
- `MunicipalityPart`

They are stored in the `persistence` module together with their Spring Data repositories and Flyway migrations. This module is intentionally separate so it can be reused by both runnable applications in the project: the importer and the API.

I also added `ImportJob` to keep a small history of import runs. It stores whether the import finished successfully or failed, the source URL, record counts, and the error message when something goes wrong. This is not strictly required for the assignment, but it makes sense if the importer is treated as something that can run repeatedly.

`MunicipalityExtended` is an extension of `Municipality`. It exists to show how the model could grow when importing a richer variant of the source data while still keeping the base municipality table usable for the simple case.

## API

The API module is intentionally small and read-only. Its purpose is to show that the imported data can be exposed by a separate application reusing the same persistence module.

Available endpoints:

- `GET /api/municipality`
- `GET /api/municipality_parts`
- `GET /api/import_jobs`

Swagger UI is available when the API is running:

```text
http://localhost:8080/swagger-ui/index.html
```

## Importer

The importer is a Spring Boot command-line application. It downloads the configured ZIP source, opens the XML file inside it, parses it with StAX, validates the parsed data, persists it, and records the import result.

The default source is configured in:

```text
importer/src/main/resources/application.properties
```

Default import:

```bash
java -jar importer.jar
```

Extended municipality import:

```bash
java -jar importer.jar --municipality-extended
```

The extended mode imports additional municipality fields into `municipality_extended` while still storing the base municipality data in `municipality`.

Duplicate handling is done before saving through `RecordMaps.uniqueByCode(...)`. Identical duplicates are accepted, but conflicting duplicates with the same code fail the import.

Importer package structure:

- `archive` downloads and opens the source archive
- `cli` parses command-line options
- `config` contains importer configuration
- `model.source` contains parsed source records
- `model.summary` contains write/import summaries
- `parser` reads the XML stream
- `parser.element` parses individual XML elements
- `parser.support` contains shared StAX/XML helpers
- `runner` coordinates the import flow
- `service` records jobs and delegates persistence
- `service.writer` performs database writes and update/create logic

## Run

Copy environment defaults first:

```bash
cp .env.example .env
```

On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

### Database only

```bash
docker compose up -d db
```

### Normal import

```bash
docker compose --profile import run --rm --build importer
```

### Extended import

```bash
docker compose --profile import run --rm --build importer --municipality-extended
```

### API only

```bash
docker compose up -d --build api
```

Then open:

```text
http://localhost:8080/swagger-ui/index.html
```

### API and database, import later

Start the database and API:

```bash
docker compose up -d --build api
```

Run the importer later:

```bash
docker compose --profile import run --rm importer
```

Or run the extended import later:

```bash
docker compose --profile import run --rm importer --municipality-extended
```

Stop everything:

```bash
docker compose down
```

Stop everything and remove database data:

```bash
docker compose down -v
```
