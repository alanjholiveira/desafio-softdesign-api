# desafio-softdesign-api

API REST para gerenciamento de sessões de votação cooperativa, desenvolvida com

- **Java 21**,
- **Spring Boot 4**,
- **Oracle Database** (Relational storage via Flyway/Liquibase and JPA),
- **Testcontainers** (Robust integration testing),
- **RabbitMQ** (AMQP Messaging),
- **Swagger/OpenAPI 3.0** (Extensive API documentation),
- **WireMock** (External Service mock routing).

---

## 🏗️ Tecnologias

| Tecnologia             | Versão | Finalidade                            |
| ---------------------- | ------ | ------------------------------------- |
| Java                   | 21     | Linguagem                             |
| Spring Boot            | 4.0.3  | Framework principal                   |
| Spring Data JPA        | —      | Persistência                          |
| Liquibase              | —      | Migrations de banco                   |
| Oracle Database        | 23c    | Banco de dados relacional             |
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

### 1. Subir as dependências (Oracle + RabbitMQ)

```bash
docker-compose up -d
```

Isso sobe:

- **Oracle Database** (`gvenzl/oracle-free`): Banco de dados principal da aplicação (Porta 1521, service `FREEPDB1`).
- **RabbitMQ**: Message Broker (Porta 5672 para conexões AMQP, e 15672 para a Management UI).
- **WireMock**: Mock server emulado para as chamadas externas de CPF (Porta 8081).
- **App (API)**: A aplicação principal da solução (Porta 8080).

### 2. Rodar a aplicação

```bash
./gradlew bootRun
```

A API estará disponível em: `http://localhost:8080`

### 3. Swagger UI (documentação interativa)

Acesse: `http://localhost:8080/swagger-ui.html`

---

## 🐳 Rodar tudo via Docker

A forma mais simples é usar o `docker-compose`, que já build, sobe o Oracle, RabbitMQ, WireMock e a API num único comando:

```bash
# Build e subir todos os serviços
docker-compose up -d --build
```

Ou, se preferir rodar o container da API manualmente após subir as dependências:

```bash
# 1. Subir apenas as dependências
docker-compose up -d db rabbitmq cpf-mock

# 2. Build da imagem
docker build -t desafio-softdesign-api .

# 3. Rodar a API apontando para o Oracle
docker run --network=desafio-softdesign-api_DESAFIO_SOFTDESIGN_NETWORK \
  -e DATASOURCE_URL=jdbc:oracle:thin:@db_desafio_api:1521/FREEPDB1 \
  -e RABBITMQ_HOST=rabbitmq_desafio_api \
  -p 8080:8080 \
  desafio-softdesign-api
```

---

## 🔑 Variáveis de Ambiente

| Variável                     | Padrão                                      | Descrição                |
| ---------------------------- | ------------------------------------------- | ------------------------ |
| `DATASOURCE_URL`             | `jdbc:oracle:thin:@localhost:1521/FREEPDB1` | URL JDBC do Oracle       |
| `DATASOURCE_USER`            | `desafio`                                   | Usuário do banco         |
| `DATASOURCE_PASS`            | `desafioApi`                                | Senha do banco           |
| `RABBITMQ_HOST`              | `localhost`                                 | Host do RabbitMQ         |
| `RABBITMQ_PORT`              | `5672`                                      | Porta do RabbitMQ        |
| `RABBITMQ_USER`              | `guest`                                     | Usuário do RabbitMQ      |
| `RABBITMQ_PASS`              | `guest`                                     | Senha do RabbitMQ        |
| `RABBITMQ_VHOST`             | `/`                                         | Virtual host do RabbitMQ |
| `FEIGN_CLIENT_USER_INFO_URL` | `http://cpf-mock:8080/`                     | URL do serviço mock CPF  |

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

1.  **Cadastre uma Pauta** (`POST /v1/polls`). Guarde o `id` retornado.
2.  **Abra uma Sessão** (`POST /v1/sessions` enviando o `pollId`). Guarde o `id` da sessão aberta.
3.  **Cadastre o Associado** (`POST /v1/associates`). Utilize o CPF válido para mock (`27603748666`) para que a simulação de serviço passe o associado como `ABLE_TO_VOTE`.
4.  **Registre o Voto** (`POST /v1/votes`). Envie o ID do associado, da sessão e o voto (`YES` ou `NO`).
5.  **Aguarde a Sessão Expirar**. Após 1 minuto (por padrão), o job interno (ShedLock cron) irá processar a sessão.
6.  **Consulte o Resultado**. O resultado será automaticamente disparado via mensageria (RabbitMQ, fila `notificacao.pauta.queue`), finalizando o fluxo assíncrono.

---

## 🧪 Rodar os testes

Os testes de integração utilizam **Testcontainers** — o Docker precisa estar em execução. Os containers de **Oracle XE** e **RabbitMQ** são levantados automaticamente durante a execução dos testes.

```bash
./gradlew test
```

Para gerar o relatório de cobertura JaCoCo:

```bash
./gradlew test jacocoTestReport
# Relatório HTML: build/reports/jacoco/test/html/index.html
```

> ✅ Cobertura atual: **100%** de linhas e métodos (excluindo boilerplate de entidades, DTOs e configurações).

---

## 📚 Documentação (Swagger)

A API REST é completamente e extensivamente documentada pela especificação Swagger/OpenAPI. Classes controladoras e DTOs de Request e Response possuem anotações `@Operation`, `@ApiResponses` e `@Schema` informando descrições, exemplos e Status Codes (como 200, 404, 422).

Para acessar a UI gráfica do Swagger de forma interativa, navegue até:
**http://localhost:8080/swagger-ui.html**

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
