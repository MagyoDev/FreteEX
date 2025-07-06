# API de Cálculo de Frete - freteex

## Descrição

Esta é uma API RESTful desenvolvida em **Spring Boot** para calcular o frete com base no estado do cliente, armazenar pedidos em banco **MongoDB** e gerenciar o status dos pedidos. A aplicação utiliza arquitetura hexagonal para separar camadas de negócio, interfaces e infraestrutura.

---

## Funcionalidades

* Recebe dados do cliente (nome, email, endereço, estado).
* Calcula o valor do frete conforme a regra abaixo:

  * SP, PR → Frete grátis (R\$ 0,00)
  * RJ, SC, RS → Frete de R\$ 20,00
  * MG, MT, MS, ES → Frete de R\$ 50,00
  * Outros estados → Frete não realizado (status **FALHA**)
* Controle de status do pedido, com valores possíveis:

  * `PROCESSANDO` (inicial)
  * `SAIU PARA ENTREGA`
  * `ENTREGUE`
  * `FALHA`
* Persistência dos pedidos em banco MongoDB, com campos:

  * `id`, `nome`, `email`, `endereco`, `estado`, `valorFrete`, `status`, `dataPedido`.
* Endpoints para:

  * Calcular e salvar pedido (`POST /freteex/calcular`)
  * Listar todos os pedidos (`GET /freteex/pedidos`)
  * Atualizar status do pedido (`PUT /freteex/pedidos/{id}/status`)

---

## Tecnologias Utilizadas

* Java 17
* Spring Boot 3.1.2
* Spring Data MongoDB
* Maven
* MongoDB
* REST API

---

## Configuração Inicial

1. Tenha o **MongoDB** rodando localmente ou configure a conexão no arquivo `application.properties`:

   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/freteex
   ```

2. Compile e rode a aplicação:

   ```bash
   mvn clean spring-boot:run
   ```

---

## Endpoints

### 1. Calcular frete e criar pedido

* **POST** `/freteex/calcular`
* **Body** (JSON):

  ```json
  {
    "nome": "Daniel",
    "email": "Daniel@email.com",
    "endereco": "Rua ABC",
    "estado": "SP"
  }
  ```
* **Resposta**:

  * Pedido criado com o valor do frete e status inicial `PROCESSANDO`.
  * Se estado inválido, retorna erro e status `FALHA`.

### 2. Listar todos os pedidos

* **GET** `/freteex/pedidos`
* **Resposta**:
  Lista de todos os pedidos com detalhes (`id`, `nome`, `email`, `endereco`, `estado`, `valorFrete`, `status`, `dataPedido`).

### 3. Atualizar status do pedido

* **PUT** `/freteex/pedidos/{id}/status`
* **Body** (texto plano, raw):

  ```
  ENTREGUE
  ```
* **Status possíveis no corpo**:

  * `PROCESSANDO`
  * `SAIU PARA ENTREGA`
  * `ENTREGUE`
  * `FALHA`
* **Resposta**: Pedido atualizado com novo status.

---
