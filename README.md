API REST para sistema de controle de estoque desenvolvida com Spring Boot.
Este backend fornece os recursos necessários para:

- CRUD completo de produtos
- Registro de entrada de estoque
- Registro de saída de estoque com validação de saldo
- Exclusão segura de produtos
- Exclusão forçada de produtos com movimentações
- Consulta de resumo por tipo de produto
- Consulta de lucro por produto
- Testes unitários de service e controller


## Tecnologias Utilizadas

- Java 17
- Spring Boot 4.0.3
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Flyway
- H2 Database (em memória)
- JUnit 5
- Mockito

## Requisitos

- Java 17
- Maven 3.9+

Verifique a versão do Java:
java -version
Deve retornar Java 17.


## Configuração do Banco de Dados

O projeto utiliza H2 em memória.

Configuração atual:

spring.datasource.url=jdbc:h2:mem:estoque_db  
spring.datasource.username=root  
spring.datasource.password=root

Console do H2 disponível em:

http://localhost:8080/h2-console

JDBC URL:
jdbc:h2:mem:estoque_db

User:
root

Password:
root


## Migrações com Flyway

As tabelas são criadas automaticamente via Flyway.

Local das migrations:

src/main/resources/db/migration

O Hibernate está configurado apenas para validar o schema:

spring.jpa.hibernate.ddl-auto=validate

Eu implementei um seed para simular dados e testar o paginate e tudo mais, para ficar mais fácil


## Instalação

1º Passo

Clone o repositório:

git clone <URL_DO_REPOSITORIO>

Entre na pasta:

cd estoque-backend


2º Passo

Execute o projeto:

mvn spring-boot:run Ou pode executar direto no Intellij


## Porta da Aplicação

A aplicação roda na porta:

http://localhost:8080


## Principais Endpoints

# Produtos
Lista produtos com paginação e filtros (tipo e descrição).
GET /produtos  

Cria um produto.
POST /produtos

Busca produto por ID.
GET /produtos/{id}  

Atualiza um produto existente.
PUT /produtos/{id}

Exclui produto (bloqueia se houver movimentações vinculadas).
DELETE /produtos/{id}

Exclui produto e remove todas as movimentações vinculadas.
DELETE /produtos/{id}/forcar  

# Resumo e Lucro

Retorna resumo de produtos por tipo, incluindo quantidade disponível e total de saída.
GET /produtos/resumo  

Consulta o lucro total de um produto específico.
GET /produtos/{id}/lucro  

# Movimentações de Estoque

Lista movimentações com paginação e filtros (produto e tipo).
GET /movimentos  

Registra entrada ou saída de estoque.
POST /movimentos  


## Testes

O projeto possui testes unitários para os services e controllers

Execute os testes com:

mvn test Ou utilize o Intellij


## Autor

José Augusto  
Desenvolvedor Full Stack