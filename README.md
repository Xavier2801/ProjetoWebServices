# 📦 ProjetoWebServices

API RESTful desenvolvida com **Spring Boot**, seguindo boas práticas de arquitetura em camadas. O projeto implementa um sistema de gerenciamento de recursos (como cursos, usuários, pedidos, etc.) com persistência via **JPA/Hibernate**, banco de dados embarcado **H2** para desenvolvimento/testes e **PostgreSQL** para produção.

---

## 📋 Sumário

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Como Executar](#como-executar)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Banco de Dados](#banco-de-dados)
- [Testes](#testes)
- [Perfis de Ambiente](#perfis-de-ambiente)
- [Autor](#autor)

---

## 💡 Sobre o Projeto

O **ProjetoWebServices** é uma aplicação backend que expõe uma API RESTful para operações de CRUD (Create, Read, Update, Delete). Foi construído como projeto de estudo/curso para aplicar os principais conceitos de desenvolvimento com o ecossistema Spring, incluindo:

- Arquitetura em camadas (Resource → Service → Repository)
- Mapeamento objeto-relacional com JPA/Hibernate
- Tratamento de exceções personalizadas
- Suporte a múltiplos perfis de ambiente (desenvolvimento e produção)
- Console H2 para inspeção do banco de dados em desenvolvimento

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|---|---|---|
| Java | 23 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework principal |
| Spring Web MVC | — | Exposição de endpoints REST |
| Spring Data JPA | — | Abstração de persistência |
| Hibernate | (via JPA) | ORM |
| H2 Database | — | Banco embarcado (dev/test) |
| PostgreSQL | — | Banco relacional (produção) |
| Maven | 3.x | Gerenciamento de dependências e build |

---

## ✅ Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [Java JDK 23+](https://adoptium.net/)
- [Maven 3.6+](https://maven.apache.org/) *(ou use o Maven Wrapper incluído no projeto)*
- [PostgreSQL](https://www.postgresql.org/) *(somente para o perfil de produção)*
- Git

---

## ⚙️ Configuração do Ambiente

### 1. Clone o repositório

```bash
git clone https://github.com/Xavier2801/ProjetoWebServices.git
cd ProjetoWebServices
```

### 2. Configuração de perfis (`application.properties`)

O projeto utiliza perfis Spring para separar os ambientes. Crie (ou edite) os arquivos em `src/main/resources/`:

#### `application.properties` (raiz — define o perfil ativo)
```properties
spring.profiles.active=dev
```

#### `application-dev.properties` (perfil de desenvolvimento com H2)
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

#### `application-prod.properties` (perfil de produção com PostgreSQL)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.hibernate.ddl-auto=none
```

> ⚠️ **Atenção:** Nunca comite credenciais reais. Use variáveis de ambiente ou um arquivo `.env` ignorado pelo `.gitignore`.

---

## ▶️ Como Executar

### Usando o Maven Wrapper (recomendado)

**Linux/macOS:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

### Gerando o JAR e executando

```bash
./mvnw clean package
java -jar target/course-0.0.1-SNAPSHOT.jar
```

### Especificando o perfil ao executar

```bash
java -jar target/course-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

A aplicação será iniciada por padrão em:

```
http://localhost:8080
```

---

## 📁 Estrutura do Projeto

```
ProjetoWebServices/
├── .mvn/
│   └── wrapper/             # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/exampleCurso/course/
│   │   │       ├── config/          # Configurações (CORS, seed de dados, etc.)
│   │   │       ├── entities/        # Classes de domínio / entidades JPA
│   │   │       ├── repositories/    # Interfaces Spring Data JPA
│   │   │       ├── resources/       # Controllers REST (camada de apresentação)
│   │   │       │   └── exceptions/  # Handlers de exceção HTTP
│   │   │       ├── services/        # Lógica de negócio
│   │   │       │   └── exceptions/  # Exceções de serviço customizadas
│   │   │       └── CourseApplication.java  # Ponto de entrada
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/
│           └── com/exampleCurso/course/
│               └── CourseApplicationTests.java
├── .gitattributes
├── .gitignore
├── mvnw                     # Maven Wrapper (Unix)
├── mvnw.cmd                 # Maven Wrapper (Windows)
└── pom.xml
```

---

## 🌐 Endpoints da API

A base da URL é `http://localhost:8080`. Abaixo estão os padrões REST comuns esperados neste tipo de projeto:

### Exemplo: Recurso `users`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/users` | Lista todos os usuários |
| GET | `/users/{id}` | Busca usuário por ID |
| POST | `/users` | Cria novo usuário |
| PUT | `/users/{id}` | Atualiza usuário existente |
| DELETE | `/users/{id}` | Remove um usuário |

### Exemplo de corpo da requisição (POST `/users`)

```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "phone": "11999999999",
  "password": "123456"
}
```

### Exemplo de resposta

```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@email.com",
  "phone": "11999999999"
}
```

> 📌 Consulte o código em `src/main/java/.../resources/` para ver todos os endpoints disponíveis.

---

## 🗄️ Banco de Dados

### Desenvolvimento (H2)

Com o perfil `dev` ativo, o banco H2 roda em memória e é reiniciado a cada execução. Para acessar o console web:

```
http://localhost:8080/h2-console
```

Configurações de acesso:
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User:** `sa`
- **Password:** *(vazio)*

### Produção (PostgreSQL)

Configure as credenciais no `application-prod.properties` ou via variáveis de ambiente:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/meu_banco
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=senha_segura
```

---

## 🧪 Testes

Para executar os testes automatizados:

```bash
./mvnw test
```

Os testes utilizam as dependências `spring-boot-starter-data-jpa-test` e `spring-boot-starter-webmvc-test`, que incluem suporte a:

- `@SpringBootTest` — testes de integração
- `MockMvc` — testes de camada web sem servidor real
- Banco H2 em memória para isolamento

---

## 🔀 Perfis de Ambiente

| Perfil | Banco de Dados | Uso |
|--------|---------------|-----|
| `dev` | H2 (em memória) | Desenvolvimento local |
| `test` | H2 (em memória) | Execução de testes |
| `prod` | PostgreSQL | Ambiente de produção |

Para ativar um perfil específico:

```bash
# Via variável de ambiente
export SPRING_PROFILES_ACTIVE=prod

# Via argumento JVM
java -Dspring.profiles.active=prod -jar app.jar
```

---

## 👤 Autor

**Xavier2801**

- GitHub: [@Xavier2801](https://github.com/Xavier2801)
- Repositório: [ProjetoWebServices](https://github.com/Xavier2801/ProjetoWebServices)

---

## 📄 Licença

Este projeto está sob uso livre para fins de estudo. Consulte o autor para outros usos.
