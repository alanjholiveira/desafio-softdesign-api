# 🗳️ Sistema de Gestión de Votos Cooperativos

Este microservicio RESTful está diseñado para gestionar el ciclo de vida completo de los votos en una cooperativa, incluyendo la gestión de asociados, pautas, sesiones y, crucialmente, un **registro de auditoría detallado** para garantizar la trazabilidad de cada acción.

## 🚀 Endpoints API

Todos los endpoints están bajo la ruta base `/api/v1/votes`.

### 1. Crear Voto (POST)
Registra un nuevo voto en el sistema.
**Endpoint:** `POST /api/v1/votes`
**Body (JSON):**
