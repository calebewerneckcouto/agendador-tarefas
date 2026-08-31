# Agendador de Tarefas

Microsserviço responsável pelo gerenciamento de tarefas agendadas. Desenvolvido em **Java 17** com **Spring Boot**, persiste dados no **MongoDB** e se comunica com o microsserviço de **Usuário** via **OpenFeign** para validação de autenticação.

## Tecnologias

- Java 17
- Spring Boot 4.1
- Spring Security + JWT
- Spring Data MongoDB
- Spring Cloud OpenFeign
- MapStruct
- Lombok
- Gradle

## Arquitetura

```
Cliente
   │
   ▼
Agendador de Tarefas (porta 8081)
   ├── MongoDB (db_agendador)
   └── Feign → Microsserviço Usuário (porta 8080)
```

O agendador **não realiza login**. Ele valida o token JWT recebido nas requisições e consulta o microsserviço de usuário quando necessário.

## Pré-requisitos

- JDK 17+
- MongoDB em execução (`localhost:27017`)
- Microsserviço de usuário em execução (`http://localhost:8080`)

## Configuração

As propriedades ficam em `src/main/resources/application.properties`:

```properties
spring.application.name=agendador-tarefas
usuario.url=http://localhost:8080
spring.data.mongodb.uri=mongodb://localhost:27017/db_agendador
server.port=8081
```

## Como executar

```bash
# Windows
gradlew.bat bootRun

# Linux / macOS
./gradlew bootRun
```

Para compilar e rodar os testes:

```bash
./gradlew build
```

## Endpoints

Base URL: `http://localhost:8081`

| Método   | Rota              | Descrição                              | Autenticação |
|----------|-------------------|----------------------------------------|--------------|
| `POST`   | `/tarefas`        | Cria uma nova tarefa                   | Bearer Token |
| `GET`    | `/tarefas`        | Lista tarefas do usuário autenticado   | Bearer Token |
| `GET`    | `/tarefas/eventos`| Lista tarefas por período              | Não          |
| `PUT`    | `/tarefas?id={id}`| Atualiza uma tarefa                    | Não          |
| `PATCH`  | `/tarefas?status={status}&id={id}` | Altera status da notificação | Não |
| `DELETE` | `/tarefas?id={id}`| Remove uma tarefa                      | Não          |

### Status de notificação

Valores aceitos para o parâmetro `status`:

- `PENDENTE`
- `NOTIFICADO`
- `CANCELADO`

### Exemplo — criar tarefa

```http
POST /tarefas
Authorization: Bearer {seu-token-jwt}
Content-Type: application/json

{
  "nomeTarefa": "Reunião com cliente",
  "descricao": "Apresentar proposta comercial",
  "dataEvento": "31-08-2026 15:00:00"
}
```

### Exemplo — listar tarefas do usuário

```http
GET /tarefas
Authorization: Bearer {seu-token-jwt}
```

### Exemplo — buscar tarefas por período

```http
GET /tarefas/eventos?dataInicial=2026-08-01T00:00:00&dataFinal=2026-08-31T23:59:59
```

## Estrutura do projeto

```
src/main/java/com/javanauta/agendador_tarefas/
├── controller/              # Endpoints REST
├── business/
│   ├── dto/                 # Objetos de transferência
│   ├── mapper/              # Conversores MapStruct
│   └── service/             # Regras de negócio
└── infrastructure/
    ├── client/              # Clientes Feign
    ├── entity/              # Entidades MongoDB
    ├── repository/          # Repositórios
    ├── security/            # JWT e Spring Security
    ├── enums/               # Enumerações
    └── exception/           # Exceções customizadas
```

## Autenticação

1. O cliente envia o header `Authorization: Bearer {token}`.
2. O `JwtRequestFilter` valida o token JWT.
3. O `UserDetailsServiceImpl` consulta o microsserviço de usuário via Feign.
4. Após autenticado, o e-mail do token é usado para vincular e filtrar tarefas.

## CI/CD

O projeto possui workflow GitHub Actions (`.github/workflows/gradle.yml`) que executa build e testes automaticamente em pull requests para a branch `master`.

## Licença

Projeto educacional — Javanauta.
