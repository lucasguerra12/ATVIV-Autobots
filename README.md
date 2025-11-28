# 🚗 Automanager API (ATV IV)

> Micro-serviço para gestão de oficinas mecânicas e venda de autopeças, com segurança avançada via JWT.

Este projeto é a **quarta etapa (ATV IV)** do desenvolvimento do sistema Automanager. O foco desta versão é a implementação de uma **Camada de Segurança Completa** utilizando Spring Security e JSON Web Tokens (JWT), além de manter os níveis de maturidade REST (HATEOAS) e a estrutura de entidades complexa (Empresa, Usuário, Veículo, Venda, etc.).

## ✨ Funcionalidades Principais

* **Autenticação e Autorização:** Login seguro que gera um Token JWT.
* **Controle de Acesso por Perfil:** Regras estritas baseadas em perfis (`ADMINISTRADOR`, `GERENTE`, `VENDEDOR`, `CLIENTE`).
* **Gestão Multi-Entidade:** Cadastro de Empresas, Usuários, Veículos, Vendas, Mercadorias e Serviços.
* **Prevenção de Loops:** Tratamento de referências circulares (JSON Infinite Recursion) nas relações bidirecionais.
* **HATEOAS:** Links dinâmicos nas respostas da API.

---

## 🛠️ Tecnologias Utilizadas

* **Java 21** (LTS)
* **Spring Boot 3.2.0**
* **Spring Security 6** (Segurança)
* **JJWT 0.11.5** (Geração e Validação de Tokens)
* **Spring Data JPA** (Persistência)
* **H2 Database** (Banco de dados em memória)
* **Lombok** (Redução de código boilerplate - Versão 1.18.34+)
* **Maven** (Gerenciador de dependências)

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
* **Java JDK 21** instalado e configurado no `JAVA_HOME`.
* **Git** (para clonar o repositório).

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-seu-repositorio>
    cd ATVIV-Autobots
    ```

2.  **Instale as dependências (Maven Wrapper):**
    * **Windows:**
        ```cmd
        .\mvnw.cmd clean install
        ```
    * **Linux/Mac:**
        ```bash
        ./mvnw clean install
        ```

3.  **Execute a aplicação:**
    * **Windows:**
        ```cmd
        .\mvnw.cmd spring-boot:run
        ```
    * **Linux/Mac:**
        ```bash
        ./mvnw spring-boot:run
        ```

4.  **Aguarde a inicialização:**
    O sistema estará pronto quando vir a mensagem: `--- TODOS OS USUÁRIOS DE TESTE CRIADOS ---`.

---

## 🔐 Credenciais de Acesso (Dados de Teste)

Ao iniciar, o sistema cria automaticamente 4 usuários para facilitar os testes, um para cada perfil de segurança. **A senha é igual ao nome de usuário.**

| Perfil | Usuário (`nomeUsuario`) | Senha (`senha`) | Permissões Principais |
| :--- | :--- | :--- | :--- |
| **ADMINISTRADOR** | `admin` | `admin` | Acesso total. Pode apagar (DELETE) registros. |
| **GERENTE** | `gerente` | `gerente` | Cria produtos, empresas e usuários. Não pode apagar. |
| **VENDEDOR** | `vendedor` | `vendedor` | Cria vendas. Não pode criar produtos ou empresas. |
| **CLIENTE** | `cliente` | `cliente` | Apenas visualiza (GET) os seus próprios dados. |

---

## 📡 Como Usar a API (Autenticação)

Como a segurança está ativa, **todas** as rotas (exceto login) estão bloqueadas. Você precisa de um "crachá" (Token) para entrar.

### 1. Fazer Login (Obter Token)
Envie uma requisição **POST** para `/login` com as credenciais.

* **URL:** `http://localhost:8080/login`
* **Body (JSON):**
    ```json
    {
      "nomeUsuario": "admin",
      "senha": "admin"
    }
    ```

**Resposta (Sucesso 200 OK):**
O Token **não** vem no corpo. Verifique os **Headers (Cabeçalhos)** da resposta. Procure por:
`Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1...`

### 2. Acessar Rotas Protegidas
Para fazer qualquer outra operação (ex: Listar Empresas), você deve enviar o token.

* **URL:** `http://localhost:8080/empresa`
* **Método:** `GET`
* **Header (Authorization):**
    * Tipo: `Bearer Token`
    * Token: `<Cole aqui o código que você recebeu no login>`

---

## 🛡️ Regras de Negócio (Segurança)

O sistema implementa as seguintes regras de autorização (baseadas na Tabela 1 do projeto):

1.  **DELETE:** Apenas usuários com perfil **ADMINISTRADOR** podem excluir registros.
2.  **POST/PUT (Cadastro/Edição):**
    * **Empresas, Mercadorias, Serviços:** Apenas **ADMINISTRADOR** e **GERENTE**.
    * **Vendas:** Permitido para **ADMINISTRADOR**, **GERENTE** e **VENDEDOR**.
3.  **GET (Leitura):** Permitido para qualquer usuário autenticado (incluindo **CLIENTE**).
4.  **Bloqueio:** Se um **CLIENTE** tentar criar uma empresa, receberá erro `403 Forbidden`.

---

## 🗄️ Banco de Dados (H2 Console)

Para inspecionar o banco de dados em memória:

* **URL:** `http://localhost:8080/h2-console`
* **JDBC URL:** `jdbc:h2:mem:testdb`
* **User Name:** `sa`
* **Password:** `password`

---

## ⚠️ Solução de Problemas Comuns

* **Erro "Lombok TypeTag :: UNKNOWN":**
    * Isso ocorre devido a incompatibilidade entre versões antigas do Lombok e o Java 21.
    * **Solução:** Este projeto já está configurado com o Lombok **1.18.34**. Certifique-se de rodar `.\mvnw.cmd clean install` para forçar a atualização das dependências.

* **Erro "403 Forbidden" ao tentar Login:**
    * Verifique se está usando o método **POST**. O login não funciona via GET.
    * Verifique se o JSON do corpo está correto (`nomeUsuario` e `senha`).

* **Erro "Infinite Recursion (StackOverflow)":**
    * Este projeto usa `@JsonIgnoreProperties` nas entidades `Venda` e `Veiculo` para evitar loops infinitos ao serializar o JSON. Não remova essas anotações.

---

**Desenvolvido como parte da atividade acadêmica Automanager.**