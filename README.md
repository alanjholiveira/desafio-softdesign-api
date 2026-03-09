# desafio-softdesign-api

API REST para gerenciamento de sessões de votação cooperativa, desenvolvida com Java 21 + Spring Boot 4.

---

## 🏗️ Tecnologias

| Tecnologia             | Versão | Finalidade                            |
| ---------------------- | ------ | ------------------------------------- |
| Java                   | 21     | Linguagem                             |
| Spring Boot            | 4.0.3  | Framework principal                   |
| Spring Data JPA        | —      | Persistência                          |
| Liquibase              | —      | Migrations de banco                   |
| PostgreSQL             | 14     | Banco de dados                        |
| RabbitMQ               | 4.2.4  | Mensageria (resultado de votação)     |
| Spring Cloud OpenFeign | —      | Integração com serviço externo de CPF |
| ShedLock               | 7.6.0  | Bloqueio distribuído do scheduler     |
| SpringDoc OpenAPI 3    | 3.0.2  | Documentação automática (Swagger UI)  |
| Testcontainers         | —      | Testes de integração                  |

---

## ⚙️ Pré-requisitos

- **Docker** e **Docker Compose** instalados
- **Java 21** instalado (para rodar fora do Docker)
- **Gradle** (o wrapper `./gradlew` já está incluso no repositório)

---

## 🚀 Como executar

### 1. Subir as dependências (PostgreSQL + RabbitMQ)

```bash
docker-compose up -d
```

Isso sobe:

- PostgreSQL na porta **5432**
- RabbitMQ na porta **5672** (management UI na **15672**)

### 2. Rodar a aplicação

```bash
./gradlew bootRun
```

A API estará disponível em: `http://localhost:8080`

### 3. Swagger UI (documentação interativa)

Acesse: `http://localhost:8080/swagger-ui.html`

---

## 🐳 Rodar tudo via Docker

Para construir e rodar a aplicação em container (junto com as dependências):

```bash
# Build da imagem
docker build -t desafio-softdesign-api .

# Rodar com as dependências do docker-compose
docker-compose up -d
docker run --network=desafio-softdesign-api_DESAFIO_SOFTDESIGN_NETWORK \
  -e DATASOURCE_URL=jdbc:postgresql://app_db:5432/desafio_api \
  -e RABBITMQ_HOST=app-rabbitmq \
  -p 8080:8080 \
  desafio-softdesign-api
```

---

## 🔑 Variáveis de Ambiente

| Variável          | Padrão                                         | Descrição                |
| ----------------- | ---------------------------------------------- | ------------------------ |
| `DATASOURCE_URL`  | `jdbc:postgresql://localhost:5432/desafio_api` | URL do banco             |
| `DATASOURCE_USER` | `desafio`                                      | Usuário do banco         |
| `DATASOURCE_PASS` | `desafioApi`                                   | Senha do banco           |
| `RABBITMQ_HOST`   | `localhost`                                    | Host do RabbitMQ         |
| `RABBITMQ_PORT`   | `5672`                                         | Porta do RabbitMQ        |
| `RABBITMQ_USER`   | `guest`                                        | Usuário do RabbitMQ      |
| `RABBITMQ_PASS`   | `guest`                                        | Senha do RabbitMQ        |
| `RABBITMQ_VHOST`  | `/`                                            | Virtual host do RabbitMQ |

---

## 📋 Endpoints da API

### Associados – `v1/associates`

| Método | Endpoint         | Descrição                  |
| ------ | ---------------- | -------------------------- |
| `GET`  | `/v1/associates` | Lista todos os associados  |
| `POST` | `/v1/associates` | Cadastra um novo associado |

### Pautas – `v1/polls`

| Método | Endpoint    | Descrição               |
| ------ | ----------- | ----------------------- |
| `GET`  | `/v1/polls` | Lista todas as pautas   |
| `POST` | `/v1/polls` | Cadastra uma nova pauta |

### Sessões – `v1/sessions`

| Método | Endpoint       | Descrição                  |
| ------ | -------------- | -------------------------- |
| `GET`  | `/v1/sessions` | Lista todas as sessões     |
| `POST` | `/v1/sessions` | Abre uma sessão de votação |

> O body pode incluir `expiration` (ISO-8601). Se omitido, a sessão expira em **1 minuto**.

### Votação – `v1/votes`

| Método | Endpoint                | Descrição                         |
| ------ | ----------------------- | --------------------------------- |
| `POST` | `/v1/votes`             | Registra um voto (`YES` ou `NO`)  |
| `GET`  | `/v1/votes/{sessionId}` | Contabiliza o resultado da sessão |

---

## 📦 Exemplos de Request

### Cadastrar pauta

```json
POST /v1/polls
{
  "name": "Aprovação do orçamento 2026",
  "description": "Votação sobre a aprovação do orçamento anual para 2026"
}
```

### Abrir sessão (com expiração customizada)

```json
POST /v1/sessions
{
  "poll": { "id": "uuid-da-pauta" },
  "expiration": "2026-03-09T22:00:00"
}
```

### Abrir sessão (com expiração padrão de 1 minuto)

```json
POST /v1/sessions
{
  "poll": { "id": "uuid-da-pauta" }
}
```

### Votar

```json
POST /v1/votes
{
  "associate": "uuid-do-associado",
  "session": "uuid-da-sessao",
  "vote": "YES"
}
```

---

## 🧪 Rodar os testes

Os testes de integração utilizam **Testcontainers** — o Docker precisa estar em execução.

```bash
./gradlew test
```

---

## 🔄 Versionamento da API

A API utiliza **versionamento por URI path** (`/v1/`). Versões futuras serão adicionadas como `/v2/`, mantendo retrocompatibilidade. Essa abordagem foi escolhida por ser a mais explícita, simples de documentar no Swagger e amplamente adotada em APIs REST públicas.

---

## 📡 Integração com Serviço Externo (Bônus 1)

Ao registrar um associado, o sistema consulta `GET https://user-info.herokuapp.com/users/{cpf}` para verificar se o CPF pode votar:

- `ABLE_TO_VOTE` → associado habilitado
- `UNABLE_TO_VOTE` → voto bloqueado (HTTP 422)
- CPF inválido → HTTP 404 propagado como erro

---

## 🏛️ Arquitetura

```
src/main/java/br/com/softdesign/desafio/
├── application/
│   ├── mapper/         # Conversão entre entidade e DTO
│   └── rest/v1/        # Controllers REST + request/response DTOs
├── domain/
│   ├── entity/         # Entidades JPA
│   ├── event/          # Eventos de saída (RabbitMQ)
│   ├── schedule/       # Job de encerramento automático de sessões
│   └── service/        # Regras de negócio
└── infrastructure/
    ├── client/         # Feign client (serviço externo CPF)
    ├── config/         # Configurações (OpenAPI, RabbitMQ, ShedLock)
    ├── enums/          # Enums de domínio
    ├── exception/      # Exceções customizadas + GlobalExceptionHandler
    └── repository/     # Repositórios Spring Data JPA
```
