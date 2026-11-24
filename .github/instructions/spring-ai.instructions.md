---
applyTo:
  - "**/*Assistant*.java"
  - "**/*Tool*.java"
  - "**/*Tools.java"
  - "**/ai/**"
---

# Spring AI, Tool Calling & RAG Guidelines

- Annotate Spring AI tools with `@Tool` and provide clear, unambiguous descriptions explaining when and how the tool should be used.
- Annotate every `@ToolParam` with a clear description including expected formats (such as `YYYY-MM-DD` for dates) and constraints.
- Validate all incoming parameters (`null` checks, valid date ranges, positive IDs) before delegating to domain services or repositories.
- Keep `ChatClient` prompt orchestration in service classes; keep controllers thin.
- When using RAG / vector search (`pgvector`), keep retrieval queries focused and ensure responses stay grounded in retrieved context.