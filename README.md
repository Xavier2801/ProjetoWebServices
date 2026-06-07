# 📦 ProjetoWebServices

API RESTful desenvolvida com **Spring Boot**, seguindo boas práticas de arquitetura em camadas (Resource → Service → Repository). O projeto implementa um sistema de gerenciamento de usuários, pedidos, produtos e categorias, com persistência via **JPA/Hibernate**, banco **H2** para desenvolvimento/testes e **PostgreSQL** para produção. A aplicação está hospedada no **Render** e disponível publicamente.

🌐 **URL de produção:** `https://projetowebservices-1.onrender.com`

> ⚠️ No plano gratuito do Render, a aplicação hiberna após 15 minutos sem uso. O primeiro acesso pode demorar até 60 segundos para "acordar".

---

## 📋 Sumário

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Modelo de Domínio](#modelo-de-domínio)
- [Endpoints da API](#endpoints-da-api)
- [Como Executar Localmente](#como-executar-localmente)
- [Configuração de Ambiente](#configuração-de-ambiente)
- [Deploy em Produção](#deploy-em-produção)
- [Banco de Dados](#banco-de-dados)
- [Perfis de Ambiente](#perfis-de-ambiente)
- [Tratamento de Exceções](#tratamento-de-exceções)
- [Testes Unitários](#testes-unitários)
- [Autor](#autor)

---

## 💡 Sobre o Projeto

O **ProjetoWebServices** é uma aplicação backend que expõe uma API RESTful com operações completas de CRUD. Foi desenvolvido como projeto de estudo aplicando os principais conceitos do ecossistema Spring:

- Arquitetura em camadas (Resource → Service → Repository)
- Mapeamento objeto-relacional com JPA/Hibernate
- Relacionamentos entre entidades (OneToMany, ManyToMany, OneToOne, ManyToOne)
- Chave composta com `@EmbeddedId`
- Enumerações mapeadas no banco de dados
- Tratamento de exceções customizadas com respostas HTTP adequadas
- Suporte a múltiplos perfis de ambiente (dev, test, prod)
- Testes unitários com JUnit 5, Mockito e MockMvc (35 testes)

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|---|---|---|
| Java | 23 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework principal |
| Spring Web MVC | — | Exposição de endpoints REST |
| Spring Data JPA | — | Abstração de persistência |
| Hibernate | 7.2.12 | ORM |
| H2 Database | — | Banco embarcado (dev/test) |
| PostgreSQL | — | Banco relacional (produção) |
| Maven | 3.x | Gerenciamento de dependências e build |
| Docker | — | Containerização para deploy |
| Render | — | Hospedagem em nuvem (produção) |
| JUnit 5 | — | Framework de testes unitários |
| Mockito | — | Mock de dependências nos testes |
| MockMvc | — | Testes da camada REST |

---

## 🏗️ Arquitetura

```
Client (Postman / Browser)
        │
        ▼
┌───────────────────┐
│   Resource Layer  │  Controllers REST (@RestController)
│  UserResource     │  Recebe requisições HTTP, delega ao Service
│  OrderResource    │
│  ProductResource  │
└────────┬──────────┘
         │
         ▼
┌───────────────────┐
│   Service Layer   │  Regras de negócio (@Service)
│  UserService      │  Chama o Repository, lança exceções
│  OrderService     │
│  ProductService   │
└────────┬──────────┘
         │
         ▼
┌───────────────────┐
│ Repository Layer  │  Acesso a dados (Spring Data JPA)
│  UserRepository   │  Interfaces que estendem JpaRepository
│  OrderRepository  │
│  ProductRepository│
└────────┬──────────┘
         │
         ▼
┌───────────────────┐
│   Banco de Dados  │  H2 (dev/test) | PostgreSQL (prod)
└───────────────────┘
```

---

## 🗂️ Modelo de Domínio

### Entidades

| Entidade | Tabela | Descrição |
|---|---|---|
| `User` | `tb_user` | Usuário do sistema |
| `Order` | `tb_order` | Pedido feito por um usuário |
| `OrderItem` | `tb_order_item` | Item de um pedido (chave composta) |
| `Product` | `tb_product` | Produto disponível |
| `Category` | `tb_category` | Categoria de produtos |
| `Payment` | `tb_payment` | Pagamento associado a um pedido |

### Relacionamentos

- `User` → `Order`: um usuário pode ter vários pedidos (`@OneToMany`)
- `Order` → `User`: cada pedido pertence a um usuário (`@ManyToOne`)
- `Order` → `OrderItem`: um pedido pode ter vários itens (`@OneToMany`)
- `Order` → `Payment`: um pedido pode ter um pagamento (`@OneToOne`)
- `Product` → `Category`: um produto pode ter várias categorias (`@ManyToMany`)
- `OrderItem`: chave composta formada por `order_id` + `product_id` (`@EmbeddedId`)

### Enum OrderStatus

| Código | Status |
|---|---|
| 1 | `WAITING_PAYMENT` |
| 2 | `PAID` |
| 3 | `SHIPPED` |
| 4 | `DELIVERED` |
| 5 | `CANCELED` |

---

## 🌐 Endpoints da API

Base URL produção: `https://projetowebservices-1.onrender.com`

Base URL local: `http://localhost:8080`

### Users `/users`

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/users` | Lista todos os usuários | 200 |
| GET | `/users/{id}` | Busca usuário por ID | 200 / 404 |
| POST | `/users` | Cria novo usuário | 201 |
| PUT | `/users/{id}` | Atualiza usuário existente | 200 / 404 |
| DELETE | `/users/{id}` | Remove um usuário | 204 / 404 |

#### Corpo POST/PUT `/users`
```json
{
  "name": "Maria Brown",
  "email": "maria@gmail.com",
  "phone": "988888888",
  "password": "123456"
}
```

#### Resposta GET `/users/{id}`
```json
{
  "id": 1,
  "name": "Maria Brown",
  "email": "maria@gmail.com",
  "phone": "988888888",
  "password": "123456"
}
```

---

### Orders `/orders`

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/orders` | Lista todos os pedidos | 200 |
| GET | `/orders/{id}` | Busca pedido por ID (com itens e total) | 200 |
| POST | `/orders` | Cria novo pedido | 201 |

#### Corpo POST `/orders`
```json
{
  "moment": "2019-06-20T19:53:07Z",
  "orderStatus": "WAITING_PAYMENT",
  "client": {
    "id": 1
  }
}
```

#### Resposta GET `/orders/{id}`
```json
{
  "id": 1,
  "moment": "2019-06-20T19:53:07Z",
  "orderStatus": "WAITING_PAYMENT",
  "client": {
    "id": 1,
    "name": "Maria Brown",
    "email": "maria@gmail.com",
    "phone": "988888888",
    "password": "123456"
  },
  "items": [
    {
      "product": {
        "id": 1,
        "name": "The Lord of the Rings",
        "price": 90.5
      },
      "quantity": 2,
      "price": 90.5,
      "subTotal": 181.0
    }
  ],
  "payment": null,
  "total": 181.0
}
```

---

### Products `/products`

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/products` | Lista todos os produtos | 200 |
| GET | `/products/{id}` | Busca produto por ID | 200 |
| POST | `/products` | Cria novo produto | 201 |

#### Corpo POST `/products`
```json
{
  "name": "The Lord of the Rings",
  "description": "Lorem ipsum dolor sit amet, consectetur.",
  "price": 90.5,
  "imgUrl": ""
}
```

---

## ▶️ Como Executar Localmente

### Pré-requisitos

- [Java JDK 23+](https://adoptium.net/)
- [Maven 3.6+](https://maven.apache.org/) ou use o Maven Wrapper incluído
- Git

### Passos

```bash
# Clone o repositório
git clone https://github.com/Xavier2801/ProjetoWebServices.git
cd ProjetoWebServices

# Execute com o perfil dev (H2 em memória)
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080` com banco H2 em memória.

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

---

## ⚙️ Configuração de Ambiente

### `application.properties` (raiz)
```properties
spring.application.name=course
spring.jpa.open-in-view=true
spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}
```

### `application-dev.properties`
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.defer-datasource-initialization=true
spring.jpa.show-sql=true
```

### `application-prod.properties`
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
server.port=${PORT:8080}
```

---

## 🚀 Deploy em Produção

O projeto está configurado para deploy automático no **Render** via GitHub.

### Dockerfile
```dockerfile
FROM eclipse-temurin:23-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Variáveis de ambiente no Render

| Variável | Descrição |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `SPRING_DATASOURCE_URL` | URL JDBC do PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco |
| `PORT` | Injetada automaticamente pelo Render |

### CI/CD

Todo `git push origin main` dispara um novo deploy automático no Render com zero-downtime.

---

## 🗄️ Banco de Dados

### Desenvolvimento (H2)

Console web disponível em `http://localhost:8080/h2-console`

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:mem:testdb` |
| User | `sa` |
| Password | *(vazio)* |

### Produção (PostgreSQL no Render)

Banco PostgreSQL provisionado pelo Render. Conexão gerenciada via variáveis de ambiente. O Hibernate cria/atualiza as tabelas automaticamente com `ddl-auto=update`.

---

## 🔀 Perfis de Ambiente

| Perfil | Banco | Uso |
|---|---|---|
| `dev` | H2 em memória | Desenvolvimento local |
| `test` | H2 em memória | Testes automatizados |
| `prod` | PostgreSQL | Produção (Render) |

---

## ⚠️ Tratamento de Exceções

O projeto implementa tratamento de exceções customizado:

| Exceção | Situação | HTTP |
|---|---|---|
| `ResourceNotFoundException` | Recurso não encontrado pelo ID | 404 Not Found |
| `DatabaseException` | Violação de integridade referencial | 400 Bad Request |

---

## 🧪 Testes Unitários

O projeto conta com **35 testes unitários** distribuídos em 4 arquivos, cobrindo as camadas de Service e Resource.

### Executar os testes

```bash
./mvnw test
```

**Windows:**
```bash
mvnw.cmd test
```

### Resultado esperado

```
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
```

### Cobertura por arquivo

| Arquivo | Camada | Testes | O que cobre |
|---|---|---|---|
| `UserServiceTest` | Service | 8 | findAll, findById, insert, delete, update e exceções |
| `OrderServiceTest` | Service | 7 | findAll, findById, insert e validação de status |
| `ProductServiceTest` | Service | 7 | findAll, findById, insert e validação de preço |
| `UserResourceTest` | Resource | 10 | GET, POST, PUT, DELETE com status HTTP corretos |

### Tecnologias usadas nos testes

- **JUnit 5** — framework de testes com `@Test` e `@DisplayName`
- **Mockito** — mock de repositórios com `@Mock` e `@MockitoBean`
- **AssertJ** — asserções fluentes com `assertThat`
- **MockMvc** — simulação de requisições HTTP sem subir servidor real
- **H2** — banco em memória ativo no perfil `test`

### Estrutura dos arquivos de teste

```
src/
└── test/
    └── java/
        └── com/exampleCurso/course/
            ├── services/
            │   ├── UserServiceTest.java
            │   ├── OrderServiceTest.java
            │   └── ProductServiceTest.java
            └── resources/
                └── UserResourceTest.java
```

---

## 👤 Autor

**Xavier2801**

- GitHub: [@Xavier2801](https://github.com/Xavier2801)
- Repositório: [ProjetoWebServices](https://github.com/Xavier2801/ProjetoWebServices)
- API em produção: [projetowebservices-1.onrender.com](https://projetowebservices-1.onrender.com)

---

## 📄 Licença

Este projeto está sob uso livre para fins de estudo.
