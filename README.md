# BlogMaker

## 📋 Visão Geral

O BlogMaker é uma aplicação web robusta desenvolvida com Spring Boot que permite a criação e gerenciamento de blogs de forma intuitiva e segura. Este projeto implementa uma API RESTful completa com recursos avançados de segurança, persistência de dados e arquitetura bem estruturada, seguindo as melhores práticas de desenvolvimento.

## 🚀 Funcionalidades Principais

- Gerenciamento completo de posts (criar, visualizar, editar, excluir)
- Sistema de autenticação e autorização baseado em JWT
- Suporte a múltiplos usuários com diferentes níveis de permissão
- Interface RESTful bem definida e documentada
- Persistência de dados eficiente com PostgreSQL
- Containerização completa para facilitar deploy e escalabilidade

## 🛠️ Tecnologias Utilizadas

- **Back-end**:
   - Java 17
   - Spring Boot 3.x
   - Spring Security (JWT)
   - Spring Data JPA
   - Hibernate
   - Maven

- **Banco de Dados**:
   - PostgreSQL

- **DevOps**:
   - Docker
   - Docker Compose

- **Testes**:
   - JUnit 5
   - Mockito
   - Spring Test

- **Documentação**:
   - Swagger

## ⚙️ Pré-requisitos

Para executar o projeto, você precisará ter instalado:

- Java Development Kit (JDK) 17
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

1. Configure o banco de dados PostgreSQL localmente
2. Ajuste as configurações no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blogmaker
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

3. Execute a aplicação:

```bash
mvn clean install
mvn spring-boot:run
```

#### Método 2: Usando Docker (Recomendado)

1. Construa e inicie os containers:

```bash
docker-compose up -d --build
```

A aplicação estará disponível em `http://localhost:8080`

## 🔍 Estrutura da API

A API do BlogMaker segue a arquitetura REST e está versionada (v1).

### Endpoints Principais

| Método | URL | Descrição |
|--------|-----|-----------|
| GET | /api/v1/posts | Lista todos os posts |
| GET | /api/v1/posts/{id} | Obtém um post pelo ID |
| POST | /api/v1/posts | Cria um novo post |
| PUT | /api/v1/posts/{id} | Atualiza um post existente |
| DELETE | /api/v1/posts/{id} | Remove um post |
| GET | /api/v1/users | Lista todos os usuários |
| GET | /api/v1/users/{id} | Obtém um usuário pelo ID |
| POST | /api/v1/auth/login | Realiza autenticação |
| POST | /api/v1/auth/register | Registra um novo usuário |
| POST | /api/v1/auth/refresh | Atualiza o token de acesso |

## 📊 Arquitetura do Projeto

### Camadas

```
src/
├── main/
│   ├── java/com/blogmaker/
│   │   ├── controller/    # Controladores REST
│   │   ├── service/       # Lógica de negócio
│   │   ├── repository/    # Acesso a dados
│   │   ├── model/         # Entidades e DTOs
│   │   ├── config/        # Configurações
│   │   ├── security/      # Implementações de segurança
│   │   ├── exception/     # Tratamento de exceções
│   └── resources/
│       ├── application.properties  # Configurações da aplicação
│       └── db/                     # Scripts SQL iniciais
└── test/                          # Testes Unitários e de Integração
```

### Fluxo de Dados

1. O cliente faz uma requisição HTTP para um endpoint
2. O Controller recebe a requisição e valida os dados de entrada
3. O Controller chama o Service apropriado
4. O Service aplica a lógica de negócio e utiliza os Repositories
5. O Service retorna os dados processados para o Controller
6. O Controller transforma os dados em uma resposta HTTP

## 🔒 Segurança

O BlogMaker implementa um sistema robusto de segurança:

- **Autenticação**: Baseada em JWT (JSON Web Token)
- **Autorização**: Controle granular de permissões baseado em roles
- **Criptografia**: Senhas armazenadas com BCrypt
- **Sessão**: Sistema stateless para melhor escalabilidade

## 🐳 Configuração do Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 🧪 Testes

O BlogMaker possui uma cobertura abrangente de testes unitários e de integração.

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

### Criação de um post (autenticado)

```bash
curl -X POST http://localhost:8080/api/v1/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d '{"title":"Meu primeiro post","content":"Conteúdo do post","theme":"java"}'
```

### Sugestões de Hospedagem

- **AWS Elastic Beanstalk**: Solução PaaS simples e gerenciada
- **Heroku**: Deploy simples integrado com GitHub
- **Digital Ocean**: Droplets com Docker já configurado
- **Azure App Service**: Serviço de hospedagem gerenciado da Microsoft

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
