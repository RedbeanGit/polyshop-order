# Order

## Description

The Order microservice is responsible for storing and managing orders, as well as orchestrating interactions between microservices during the creation of a new order. It was developed as part of a course project on creating microservices using RabbitMQ for communication between services.

## Technologies Used

- Programming Language: Java with Spring Boot
- Database: PostgreSQL
- Message Queue: RabbitMQ
- Containerization: Docker

## Installation and Configuration

1. Clone the GitHub repository:

```bash
git clone git@github.com:RedbeanGit/polyshop-order.git
```

2. Install Docker and Docker Compose on your machine if you haven't already. You can follow the installation instructions on Docker's official website: https://docs.docker.com/get-docker/ and https://docs.docker.com/compose/install/.

3. Navigate to the Order microservice directory:

```bash
cd polyshop-order
```

4. Launch Docker Compose to start the necessary containers:

```bash
docker-compose up -d
```

**Now you can choose to run the Order service inside a docker container or directly on your host.**

### Running with docker

5. Build the Docker image for the microservice using the provided Dockerfile:

```bash
docker build -t polyshop-order .
```

6. Run the container from the image you have just builded:

```bash
docker run --name polyshop_order polyshop-order
```

### Running on host

5. Start Spring Boot application:

```bash
./mvnw spring-boot:run
```

## API

List of routes/API endpoints available for this microservice:

- **GET** /orders : Retrieves all orders.
- **GET** /orders/{orderId} : Retrieves a specific order.
- **GET** /orders/{orderId}/products : Retrieves the products of a specific order.

## Message Queue

The Order microservice listens for messages from all other microservices and sends events upon order creation and order status updates.
