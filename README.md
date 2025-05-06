# BlogMaker API

<p align="center">
  <b>Uma API robusta para gerenciamento de blogs com Spring Boot</b>
</p>

<p align="center">
  <a href="#visão-geral">Visão Geral</a> •
  <a href="#funcionalidades-principais">Funcionalidades</a> •
  <a href="#tecnologias-utilizadas">Tecnologias</a> •
  <a href="#pré-requisitos">Pré-requisitos</a> •
  <a href="#configuração-e-execução">Instalação</a> •
  <a href="#estrutura-da-api">API</a> •
  <a href="#arquitetura-do-projeto">Arquitetura</a> •
  <a href="#testes">Testes</a>
</p>

---

## 📋 Visão Geral

O BlogMaker é uma aplicação web robusta desenvolvida com Spring Boot que permite a criação e gerenciamento de blogs de forma intuitiva e segura. Este projeto implementa uma API RESTful completa com recursos avançados de segurança, persistência de dados e arquitetura bem estruturada, seguindo as melhores práticas de desenvolvimento.

Projetado com foco em escalabilidade e manutenibilidade, o BlogMaker API serve como backend para a plataforma completa de blogging, fornecendo todos os endpoints necessários para gerenciar usuários, posts, temas e estatísticas.

## 🚀 Funcionalidades Principais

<p align="center">
  <img src="https://img.shields.io/badge/Autenticação-JWT-green" alt="JWT Auth" />
  <img src="https://img.shields.io/badge/API-RESTful-blue" alt="RESTful API" />
  <img src="https://img.shields.io/badge/Documentação-Swagger-orange" alt="Swagger" />
  <img src="https://img.shields.io/badge/Container-Docker-blue" alt="Docker" />
</p>

- **Sistema de Posts**: Criação, leitura, atualização e exclusão (CRUD) de posts
- **Gerenciamento de Usuários**: Registro, autenticação e perfis de usuário
- **Sistema de Autorização**: Controle de acesso baseado em funções (RBAC)
- **Categorização por Temas**: Organização de conteúdo por categorias
- **Pesquisa Avançada**: Filtros para busca eficiente de conteúdo
- **Métricas e Analytics**: Coleta e análise de estatísticas de uso
- **API Documentada**: Interface Swagger para fácil integração

## 🛠️ Tecnologias Utilizadas

### Backend

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-red?style=for-the-badge&logo=java&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring-boot" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-blue?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
</p>

- **Java 21**: Linguagem de programação principal
- **Spring Boot 3.x**: Framework para desenvolvimento de aplicações
- **Spring Security**: Implementação de autenticação e autorização com JWT
- **Spring Data JPA**: Persistência de dados e ORM
- **Hibernate**: Mapeamento objeto-relacional
- **PostgreSQL**: Sistema de gerenciamento de banco de dados relacional
- **Maven**: Gerenciamento de dependências e build
- **JUnit 5 & Mockito**: Framework de testes
- **Swagger/OpenAPI**: Documentação automática da API
- **Docker & Docker Compose**: Containerização e orquestração

## ⚙️ Pré-requisitos

Para executar o projeto, você precisará ter instalado:

- Java Development Kit (JDK) 21
- Maven 3.6+
- Docker e Docker Compose (recomendado)
- Git
- PostgreSQL (caso não use Docker)

## 📦 Configuração e Execução

### Clonando o Repositório

```bash
git clone https://github.com/J0aoPaulo/blogmaker.git
cd blogmaker
```

### Ambiente de Desenvolvimento

#### Método 1: Execução Local

1. **Configure o banco de dados PostgreSQL** localmente
2. **Ajuste as configurações** no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blogmaker
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
```

3. **Execute a aplicação**:

```bash
mvn clean install
mvn spring-boot:run
```

#### Método 2: Usando Docker (Recomendado)

1. **Construa e inicie os containers**:

```bash
docker-compose up -d --build
```

A aplicação estará disponível em `http://localhost:8080` e a documentação Swagger em `http://localhost:8080/swagger-ui.html`

## 🔍 Estrutura da API

A API do BlogMaker segue a arquitetura REST e está versionada (v1) para garantir compatibilidade futura.

### Endpoints Principais

#### 🔐 Autenticação
| Método | URL | Descrição | Status Codes |
|--------|-----|-----------|-------------|
| POST | /api/v1/auth/register | Registra um novo usuário | 201, 400, 409 |
| POST | /api/v1/auth/login | Realiza autenticação | 200, 401, 403 |
| POST | /api/v1/auth/admin/register | Registra um novo administrador | 201, 400, 403 |

#### 👤 Usuários
| Método | URL | Descrição | Status Codes |
|--------|-----|-----------|-------------|
| GET | /api/v1/users | Lista todos os usuários | 200, 403 |
| GET | /api/v1/users/{userId} | Obtém um usuário pelo ID | 200, 404 |
| PUT | /api/v1/users/{userId} | Atualiza um usuário existente | 200, 400, 403, 404 |
| DELETE | /api/v1/users/{userId} | Remove um usuário | 204, 403, 404 |

#### 📝 Posts
| Método | URL | Descrição | Status Codes |
|--------|-----|-----------|-------------|
| GET | /api/v1/posts | Lista todos os posts | 200 |
| GET | /api/v1/posts/filter | Filtra posts por critérios específicos | 200 |
| GET | /api/v1/posts/{postId} | Obtém um post pelo ID | 200, 404 |
| POST | /api/v1/posts | Cria um novo post | 201, 400, 403 |
| PUT | /api/v1/posts/{postId} | Atualiza um post existente | 200, 400, 403, 404 |
| DELETE | /api/v1/posts/{postId} | Remove um post | 204, 403, 404 |

#### 🏷️ Temas
| Método | URL | Descrição | Status Codes |
|--------|-----|-----------|-------------|
| GET | /api/v1/themes | Lista todos os temas | 200 |
| GET | /api/v1/themes/{themeId} | Obtém um tema pelo ID | 200, 404 |
| POST | /api/v1/themes | Cria um novo tema | 201, 400, 403 |
| PUT | /api/v1/themes/{themeId} | Atualiza um tema existente | 200, 400, 403, 404 |
| DELETE | /api/v1/themes/{themeId} | Remove um tema existente | 204, 403, 404 |

#### 📊 Analytics
| Método | URL | Descrição | Status Codes |
|--------|-----|-----------|-------------|
| GET | /api/v1/analytics/posts | Obtém estatísticas de posts | 200, 403 |
| GET | /api/v1/analytics/users | Obtém estatísticas de usuários | 200, 403 |
| GET | /api/v1/analytics/themes | Obtém estatísticas de temas | 200, 403 |

## 📊 Arquitetura do Projeto

O BlogMaker segue uma arquitetura em camadas bem definida, com clara separação de responsabilidades:

### Camadas

```
src/
├── main/
│   ├── java/com/blogmaker/
│   │   ├── controller/    # Controladores REST - Recebem requisições HTTP
│   │   ├── service/       # Lógica de negócio - Processamento de dados
│   │   ├── repository/    # Acesso a dados - Interação com banco de dados
│   │   ├── model/         # Entidades e DTOs - Representação de dados
│   │   │   ├── entity/    # Entidades JPA - Mapeadas para tabelas
│   │   │   └── dto/       # Data Transfer Objects - Transporte de dados
│   │   ├── config/        # Configurações do Spring
│   │   ├── security/      # Implementações de segurança (JWT, auth)
│   │   ├── exception/     # Tratamento de exceções personalizado
│   │   └── util/          # Classes utilitárias
│   └── resources/
│       ├── application.properties  # Configurações da aplicação
│       └── db/                     # Scripts SQL iniciais
└── test/                          # Testes Unitários e de Integração
    └── java/com/blogmaker/
        ├── controller/            # Testes de controladores
        ├── service/               # Testes de serviços
        └── repository/            # Testes de repositórios
```

### Fluxo de Dados

<p align="center">
  <strong>Cliente</strong> → Controller → Service → Repository → <strong>Banco de Dados</strong>
</p>

1. O cliente faz uma requisição HTTP para um endpoint específico
2. O Controller valida os dados de entrada e chama o Service apropriado
3. O Service aplica a lógica de negócio e utiliza os Repositories para operações de dados
4. O Repository executa as operações no banco de dados
5. Os dados processados retornam pelo mesmo caminho até o cliente

## 🔒 Segurança

O BlogMaker implementa um sistema robusto de segurança com múltiplas camadas de proteção:

- **Autenticação**: Baseada em JWT (JSON Web Token)
- **Autorização**: Controle granular de permissões baseado em roles (USER, ADMIN)
- **Criptografia**: Senhas armazenadas com BCrypt (hashing + salt)
- **Proteção contra ataques**: 
  - CSRF (Cross-Site Request Forgery)
  - XSS (Cross-Site Scripting)
  - SQL Injection (via Hibernate/JPA)
- **Sessão**: Sistema stateless para melhor escalabilidade
- **CORS**: Configuração para permitir apenas origens confiáveis

## 🐳 Configuração do Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/blogmaker
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres

  db:
    image: postgres:15-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=blogmaker
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - blogmaker_data:/var/lib/postgresql/data

volumes:
  blogmaker_data:
```

## 🧪 Testes

O BlogMaker possui uma cobertura abrangente de testes unitários e de integração, garantindo a qualidade e robustez do código.

### Tipos de Testes

- **Testes Unitários**: Verificam componentes individuais isoladamente
- **Testes de Integração**: Verificam a interação entre componentes
- **Testes de API**: Verificam os endpoints REST de ponta a ponta

### Executando Testes

```bash
# Executar todos os testes
mvn test

# Executar apenas testes unitários
mvn test -Dtest=**/unit/**

# Executar apenas testes de integração
mvn test -Dtest=**/integration/**

# Relatório de cobertura de testes
mvn jacoco:report
```

O relatório de cobertura estará disponível em `target/site/jacoco/index.html`

## 📋 Exemplos de Uso

### Autenticação e obtenção do token

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"usuario@email.com","password":"senha123"}'
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "usuario",
  "email": "usuario@email.com",
  "roles": ["USER"]
}
```

### Criação de um post (autenticado)

```bash
curl -X POST http://localhost:8080/api/v1/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d '{"title":"Meu primeiro post","content":"Conteúdo do post","themeId":1}'
```

Resposta:
```json
{
  "id": 1,
  "title": "Meu primeiro post",
  "content": "Conteúdo do post",
  "createdAt": "2024-08-15T10:30:00",
  "updatedAt": "2024-08-15T10:30:00",
  "author": {
    "id": 1,
    "username": "usuario"
  },
  "theme": {
    "id": 1,
    "name": "Tecnologia"
  }
}
```

## 🚀 Sugestões de Hospedagem

- **AWS Elastic Beanstalk**: Solução PaaS simples e gerenciada
- **Heroku**: Deploy simples integrado com GitHub
- **Digital Ocean**: Droplets com Docker já configurado
- **Azure App Service**: Serviço de hospedagem gerenciado da Microsoft
- **Google Cloud Run**: Plataforma serverless para containers

## 👥 Contribuição

Sua contribuição é muito bem-vinda! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Implemente suas mudanças e adicione testes quando possível
4. Execute os testes para garantir que nada foi quebrado
5. Faça commit das suas alterações (`git commit -m 'Adiciona nova funcionalidade'`)
6. Faça push para a branch (`git push origin feature/nova-funcionalidade`)
7. Abra um Pull Request detalhando suas alterações

### Diretrizes de Contribuição

- Siga o padrão de código existente
- Escreva testes para novas funcionalidades
- Atualize a documentação quando necessário
- Respeite o fluxo de trabalho Git

## 📄 Licença

Este projeto está licenciado sob a [GNU License](LICENSE.md).

## 📞 Contato

Para dúvidas, sugestões ou colaborações:

- **E-mail**: [contato.joaopaulodeveloper@gmail.com](mailto:contato.joaopaulodeveloper@gmail.com)
- **GitHub**: [github.com/J0aoPaulo](https://github.com/J0aoPaulo)

---
