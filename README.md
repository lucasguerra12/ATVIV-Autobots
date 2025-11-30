# 🚗 AutoManager API

API RESTful para gerenciamento de concessionárias, desenvolvida com as melhores práticas de mercado utilizando **Java 21** e **Spring Boot 3**.

Este projeto implementa um sistema completo de controle de usuários, veículos, vendas e serviços, com foco em segurança robusta utilizando **JWT (JSON Web Token)** e documentação automática via **Swagger**.

---

## 🚀 Tecnologias Utilizadas

O projeto foi atualizado para utilizar a stack mais moderna do ecossistema Java:

* **Java 21** (LTS)
* **Spring Boot 3.2.0**
* **Spring Security 6** (Autenticação e Autorização)
* **JWT (JJWT 0.11.5)** (Tokens seguros e stateless)
* **Spring Data JPA / Hibernate** (Persistência de dados)
* **MySQL** (Banco de dados relacional)
* **OpenAPI / Swagger UI** (Documentação interativa)
* **Lombok** (Redução de boilerplate code)
* **Maven** (Gerenciamento de dependências)

---

## ⚙️ Pré-requisitos

Para rodar este projeto, você precisará ter instalado em sua máquina:

1.  **Java JDK 21**
2.  **Maven** (ou usar o wrapper `mvnw` incluso)
3.  **MySQL Server** rodando na porta `3306`

---

## 🛠️ Configuração

Antes de iniciar, é necessário configurar o acesso ao banco de dados.

1.  Abra o arquivo `src/main/resources/application.properties`.
2.  Verifique se as credenciais do banco conferem com o seu ambiente local:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/automanager_db?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
````

> **Nota:** O sistema criará automaticamente o banco de dados `automanager_db` se ele não existir.

-----

## ▶️ Como Rodar

Você pode executar a aplicação facilmente via terminal:

### Usando Maven Wrapper (Recomendado)

**Windows:**

```powershell
./mvnw.cmd clean spring-boot:run
```

**Linux/Mac:**

```bash
./mvnw clean spring-boot:run
```

Após iniciar, a API estará disponível em: `http://localhost:8080`

-----

## 📚 Documentação da API (Swagger)

A API possui uma interface gráfica para testes e documentação automática. Após rodar o projeto, acesse:

👉 **[http://localhost:8080/swagger-ui.html](https://www.google.com/search?q=http://localhost:8080/swagger-ui.html)**

Lá você poderá testar todos os endpoints (`GET`, `POST`, `PUT`, `DELETE`) diretamente pelo navegador.

-----

## 🔐 Autenticação e Segurança

O sistema é protegido por JWT. Para acessar os endpoints restritos (como criar usuários ou deletar vendas), você precisa de um token.

### 1\. Usuários Padrão (Criados Automaticamente)

Ao rodar o projeto pela primeira vez, o sistema cria dois usuários para teste:

| Perfil | Usuário | Senha | Permissões |
| :--- | :--- | :--- | :--- |
| **Administrador** | `admin` | `admin123` | Acesso Total |
| **Vendedor** | `vendedor` | `vendedor123` | Vendas e Consultas |

### 2\. Como Logar (Passo a Passo)

1.  Faça uma requisição `POST` para **/auth/login** com o JSON:
    ```json
    {
      "nomeUsuario": "admin",
      "senha": "admin123"
    }
    ```
2.  Copie o **token** retornado na resposta.
3.  No Swagger (ou Postman), adicione o token no cabeçalho **Authorization** com o prefixo `Bearer`.
      * *No Swagger UI:* Clique no botão **"Authorize"** no topo da página e cole o token.

-----

## 🏛️ Arquitetura do Projeto

O projeto segue uma arquitetura limpa e unificada para facilitar a manutenção:

  * `controle/`: **Controllers** REST unificados (ex: `UsuarioController` gerencia todas as rotas de usuário).
  * `service/`: **Regras de Negócio** centralizadas.
  * `repositorios/`: Interfaces **Spring Data JPA**.
  * `model/`: **Entidades** e **DTOs**.
  * `security/`: Configurações de segurança (**Filtros** e **Configurações** do Spring Security 6).
  * `jwt/`: Lógica de geração e validação de tokens.

-----

## 📝 Status do Projeto
