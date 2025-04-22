# 💍 Projeto Minha Prata

## Índice

* [Descrição](#descrição)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Configuração do ambiente](#configuração-do-ambiente)
* [Execução](#execução)
* [Estrutura do Projeto](#estrutura-do-projeto)
* [Endpoint](#endpoints)
* [Documentação da API (Swagger)](#documentação-da-api-swagger)
* [Contribuições](#contribuições)
* [Observações](#observações)

## Descição

Minha Prata é um e-commerce de joias em prata desenvolvido como projeto acadêmico, com o objetivo de aplicar conceitos modernos de desenvolvimento de software, arquitetura limpa e segurança. A aplicação oferece uma experiência completa de navegação para usuários e uma interface de administração para gestão dos produtos.

Modelagem do projeto
![Modegalem](aqui vai uma imagem)

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
    - **Spring WEB**
    - **Spring Data JPA**
    - **Spring Validation**
    - **Sprint Security**
- **Lombook**
- **FlyWay**
- **Docker e Docker Compose**
- **JWT para autenticação**
- **Banco de dados**
    - **MySQL** - Configurado por um docker-compose
- **Maven**

## Configuração do Ambiente

1. Clone o Repositório:
```bash
git clone https://github.com/rafaeldsal/project-minha-prata.git
```

2. Navegue até o diretório:
```bash
cd project-minha-prata
```

3. Certifique-se de ter o Java 17 e o Maven instaldos.


4. Configure o banco de dados:
   - O projeto está configurado para utilizar o banco de dados MySQL. Execute o comando do docker-compose.yml para criar uma instância do MySQL em sua máquina
   - Para esse processo, garanta ter o docker instalado em sua máquina.
   ```bash
   docker-compose up -d
   ```

## Execução

```bash
mvn spring-boot:run
```
A aplicação estará disponível em:
📍 http://localhost:8080

## Estrutura do Projeto

```plaintext
src/
├── main/
│   ├── java/
│   │   └── com/rafaeldsal/ws/minhaprata/
│   │       ├── configuration/     # Configurações gerais do projeto (ex.: segurança, CORS, beans).
│   │       ├── controller/        # Controladores responsáveis pelos endpoints da API.
│   │       ├── dto/               # Objetos de Transferência de Dados (Data Transfer Objects).
│   │       ├── exception/         # Classes para tratamento e personalização de erros.
│   │       ├── model/             # Entidades e modelos utilizados no projeto.
│   │       ├── repository/        # Interfaces para acesso e manipulação de dados no banco.
│   │       ├── service/           # Interfaces que definem os contratos de serviço.
│   │       │   └── service/impl/      # Implementações das regras de negócio e lógica da aplicação.
│   └── resources/
│       ├── db
│       │    └── migration # Arquivos SQL para migração dos meus serviços no Banco de Dados
│       ├── application.properties # Configurações da aplicação (banco de dados, porta, etc.).
│       └── static/                # Arquivos estáticos (imagens, CSS, JavaScript, etc.).

```

Ainda não implementei teste, mas logo teremos a estrutura dessa etapa definida aqui. Pensei em uma arquitetura em camadas, trazendo princípios de SOLID e tentando aplicar conceitos de Clean Architecture.

## Endpoints

A API contém os seguintes endpoints:
- **/user**
  - **GET / ->** retornar informações sobre todos os usuários ou de um usuário especifico passando o seu ID
  - **POST / ->** cria um usuário no banco de dados, caso ainda não exista
  - **PUT / ->** atualiza um usuário com base no ID informado
  - **DELETE / ->** Deleta um usuário do nosso banco de dados
- **/product**
  - **GET / ->** retornar informações sobre todos os produtos ou de um produto especifico passando o seu ID
  - **POST / ->** cria um produto no banco de dados, caso ainda não exista
  - **PUT / ->** atualiza um produto com base no ID informado
  - **DELETE / ->** Deleta um produto do nosso banco de dados
- **/order-history**
  - **GET / ->** retornar informações sobre todos os historicos de pedido ou de um historico especifico passando o ID do pedido
- **/orders**
    - **GET / ->** retornar informações sobre todos os pedidos ou de um pedido especifico passando o seu ID
    - **POST / ->** cria um pedido no banco de dados, caso ainda não exista
    - **PUT / ->** atualiza um pedido com base no ID informado. Esse atualiza o carrinho de compras
    - **PATCH / ->** atualiza o status do pedido
- **/category**
    - **GET / ->** retornar informações sobre todos as categorias ou de uma categoria especifico passando o seu ID
    - **POST / ->** cria uma categoria no banco de dados, caso ainda não exista
    - **PUT / ->** atualiza uma categoria com base no ID informado
    - **DELETE / ->** Deleta uma categoria do nosso banco de dados
- **/auth**
    - **POST / ->** utilizada para fazer o login e validação do usuário

## Documentação da API (Swagger)

Ainda em desenvolvimento...

## Contribuições
Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do repositório.
2. Crie uma branch para suas alterações: **git checkout -b minha-feature**
3. Faça commit das suas alterações: **git commit -m 'Adiciona minha feature'**
4. Envie o repositório remoto: **git push origin minha-feature**
5. Abra um pull request

## Observações

O projeto ainda está em desenvolvimento, então pode acontecer de algumas informações contidas nesse readme mudar. De qualquer forma, espero que esteja gostando do projeto e que ele consiga mostrar tudo que tenho aprendido até esse momento.