# MK Corpus MCP Server

An **MCP (Model Context Protocol) server** that collects Macedonian-language texts from a news website, analyzes them, and donates them to [doniraj.vezilka.ai](https://doniraj.vezilka.ai), a platform for preserving the Macedonian language.

AI agents (Claude Desktop, MCP Inspector) use the server through MCP tools. A React dashboard offers the same functionality through a REST API.

## How it works

```
source website ──► search & scrape ──► language check ──► PostgreSQL
                                                              │
                     doniraj.vezilka.ai ◄── donation ◄── analysis
```

1. **Search:** searches the source website (WordPress search + RSS feed) and scrapes the full text of every article with Jsoup.
2. **Language check:** scores each text from 0 to 1 for how confidently it is Macedonian, based on the Cyrillic ratio and Macedonian-specific letters (ѓ, ќ, ѕ, љ, њ, џ, ј).
3. **Store:** saves each article with its source URL, so its provenance is always known.
4. **Analyze:** produces a summary, the top keywords (TF-IDF over the whole corpus) and sentence/word counts.
5. **Donate:** groups articles into batches (`DRAFT → APPROVED → SUBMITTED → ACCEPTED/REJECTED`), sends an excerpt with source attribution, and a scheduler polls the donation status.

## MCP tools

| Tool | Description |
|------|-------------|
| `search_resources` | Search the source website and store the results |
| `get_resource` | Get one stored text with its analysis |
| `analyze_resource` | Summary, keywords and statistics for a text |
| `donate_resource` | Create, approve and submit a donation |
| `corpus_stats` | Aggregate corpus statistics |
| `find_corpus_gaps` | Find under-represented topics in the donated corpus and prepare a donation batch to fill them |

Every tool call is logged, and the dashboard shows the trace.

## Tech stack

- **Backend:** Java 21, Spring Boot 3.4, Spring AI 1.0 (MCP over HTTP+SSE), PostgreSQL, Flyway, JWT, Jsoup
- **Frontend:** React 19, TypeScript, Vite, MUI

The backend is layered: `controller / MCP tool → application service (DTOs) → domain service (entities) → repository`. Both the REST API and the MCP tools call the same services.

## Getting started

Requirements: Java 21, Node 20+, Docker.

**1. Configuration:** copy the example file and fill in your values:

```bash
cp mcp-tool-backend/.env.example mcp-tool-backend/.env
```

| Variable | Meaning |
|----------|---------|
| `SOURCE_NAME` | Name of the source website (used in donation attribution) |
| `SOURCE_BASE_URL` | Base URL of a WordPress-based website |
| `VEZILKA_API_KEY` | doniraj.vezilka.ai API key |
| `JWT_SECRET_KEY` | Secret for signing JWT tokens |

**2. Database**

```bash
cd mcp-tool-backend
docker compose up -d
```

**3. Backend:** runs at `http://localhost:8080` (Swagger: `/swagger-ui/index.html`):

```bash
./mvnw spring-boot:run
```

**4. Frontend:** runs at `http://localhost:3000`:

```bash
cd ../mcp-tool-frontend
npm install
npm run dev
```

Register and log in on the dashboard to use the REST features.

## Trying the MCP server

The server speaks MCP over **HTTP+SSE**:

- event stream: `http://localhost:8080/sse`
- messages: `http://localhost:8080/mcp/message`

**MCP Inspector:** start it, choose transport **SSE**, enter the URL `http://localhost:8080/sse`, connect, and call `corpus_stats`. You should get a JSON object with the corpus statistics.

```bash
npx @modelcontextprotocol/inspector
```

**Claude Desktop:** add this to `claude_desktop_config.json` (the `mcp-remote` bridge connects Claude to the SSE endpoint):

```json
{
  "mcpServers": {
    "mk-corpus": {
      "command": "npx",
      "args": ["-y", "mcp-remote", "http://localhost:8080/sse"]
    }
  }
}
```

After restarting Claude Desktop, try asking: *"Search for articles about education and analyze the first result."*

**MCP Playground:** the dashboard's *Playground* page lists all tools with their JSON Schemas and lets you call them without an external client.

## Tests

Docker must be running (Testcontainers starts a real PostgreSQL):

```bash
cd mcp-tool-backend
./mvnw test
```

## Responsible use

Only publicly available content is collected. Requests are rate-limited (500 ms between articles), only an excerpt of each text is donated, and every donation keeps the source name and URL.

## Project layout

```
mk-corpus-mcp-server/
├── mcp-tool-backend/          Spring Boot 3.4 / Java 21 / Maven
│   └── src/main/java/mk/ukim/finki/mcptoolbackend/
│       ├── mcp/                  MCP tools (@Tool methods) and their registration
│       ├── source/               scraper for the source website
│       ├── analysis/             language detection and text analysis
│       ├── negotiation/          corpus-gap agent (find_corpus_gaps)
│       ├── integration/vezilka/  doniraj.vezilka.ai client
│       ├── service/              application (DTOs) and domain (entities) services
│       ├── repository/           Spring Data JPA repositories
│       ├── model/                entities, DTOs, enums, exceptions
│       ├── web/                  REST controllers, JWT filter, exception handlers
│       ├── jobs/                 donation-status scheduler
│       └── config/ constants/ helpers/
│   └── src/main/resources/db/migration/   Flyway migrations
└── mcp-tool-frontend/         React 19 / TypeScript / Vite / MUI
    └── src/
        ├── api/ axios/           REST calls and the axios instance (JWT)
        ├── contexts/ providers/ hooks/   state and data loading
        ├── theme/                MUI theme
        └── ui/                   pages and components
```