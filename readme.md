
# Projeto Full-Stack Angular + Spring Boot

Este repositório contém um projeto full-stack que integra o frontend em **Angular 17** com o backend em **Spring Boot** utilizando **JPA**. O banco de dados utilizado é o **H2**. A seguir, explico como configurar e executar cada parte do projeto.

## Estrutura do Projeto

- `angular17crud/` - Aplicação Angular 17
- `spring-jpa/` - Aplicação Spring Boot com JPA e banco de dados H2

## Configuração e Execução

### Frontend (Angular)
1. Acesse a pasta do projeto Angular:
   ```sh
   cd angular17crud
   ```
2. Instale as dependências:
   ```sh
   npm install
   ```
3. Execute o servidor na porta 8081:
   ```sh
   ng serve --port 8081
   ```

A aplicação Angular estará disponível em `http://localhost:8081/`.

### Backend (Spring Boot + JPA)
1. Acesse a pasta do projeto Spring Boot:
   ```sh
   cd spring-jpa
   ```
2. Certifique-se de ter o Maven instalado.
3. Compile e execute a aplicação:
   ```sh
   mvn spring-boot:run
   ```

O backend estará rodando na porta padrão `8080` e utilizando o banco de dados **H2**.

## Tecnologias Utilizadas
- **Frontend:** Angular 17, TypeScript, Bootstrap
- **Backend:** Spring Boot, JPA/Hibernate
- **Banco de Dados:** H2
- **Ferramentas:** Maven

