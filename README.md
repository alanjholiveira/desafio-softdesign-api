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

## 🚦 Fluxo Completo de Operação

Para testar o fluxo completo da aplicação (Cadastro → Validação CPF → Voto → Resultado), siga esta ordem:

1. **Cadastre uma Pauta** (`POST /v1/polls`). Guarde o `id` retornado.
2. **Abra uma Sessão** (`POST /v1/sessions` enviando o `pollId`). Guarde o `id` da sessão aberta.
3. **Cadastre o Associado** (`POST /v1/associates`). Utilize o CPF válido para mock (`27603748666`) para que a simulação de serviço passe o associado como `ABLE_TO_VOTE`.
4. **Registre o Voto** (`POST /v1/votes`). Envie o ID do associado, da sessão e o voto (`YES` ou `NO`).
5. **Aguarde a Sessão Expirar**. Após 1 minuto (por padrão), o job interno (ShedLock cron) irá processar a sessão.
6. **Consulte o Resultado**. O resultado será automaticamente disparado via mensageria (RabbitMQ, fila `notificacao.pauta.queue`), finalizando o fluxo assíncrono.

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

O sistema consulta a URL externa definida em `feign.client.user-info.url` (via `GET /users/{cpf}`) para verificar se o associado tem permissão para votar. O comportamento esperado é:

- Retorno HTTP 200 `{"status": "ABLE_TO_VOTE"}` → Cadastro liberado
- Retorno HTTP 200 `{"status": "UNABLE_TO_VOTE"}` → Cadastro bloqueado (HTTP 422 propagado)
- Retorno HTTP 404 → CPF inválido (HTTP 404 propagado)

**Atenção (Mock Local):** Sabendo que a antiga API do Heroku (`https://user-info.herokuapp.com/`) não está mais no ar permanentemente, o ambiente local (`docker-compose`) já inclui um contêiner auxiliar chamado **`cpf-mock`** com a imagem do [WireMock](https://wiremock.org/).
O fluxo pelo Docker rodará local e apontará automático para ele. Os CPFs cadastrados no mock (matematicamente válidos) são:

- **`27603748666`**: retorna `200` com `ABLE_TO_VOTE` (Caminho feliz)
- **`78102795050`**: retorna `200` com `UNABLE_TO_VOTE`
- **Qualquer outro CPF**: retorna `404 Not Found` (simula CPF inexistente na Base)

Note também que a aplicação tem robustez embutida (Circuit Breaker passivo): caso o serviço saia do ar (Timout/5xx), a aplicação emite um alerta nos logs mas preserva a capacidade de votação, registrando o associado como apto (`ABLE_TO_VOTE`).

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
