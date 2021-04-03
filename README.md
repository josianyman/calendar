# Calendar reservation microservice

### QuickStart

1. You need [docker](https://docs.docker.com/get-docker/) and [docker-compose](https://docs.docker.com/compose/install/)
   installed
1. Clone repository `git clone https://github.com/josianyman/calendar.git`   
1. Run command `cd calendar && docker-compose up`
1. Open [Altair client](https://altair-gql.sirmuel.design/) to access GraphQL API from URL http://localhost:8080/graphql

### Concepts

Calendar reservation microservice exposes its public [GraphQL API](https://graphql.org/learn/). Backend service is
implemented with the [Spring Boot](https://spring.io/projects/spring-boot) stack with Kotlin. The service requires a
postgresql database connection, but schema is fully managed by the backend service with [Flyway](https://flywaydb.org/)
tool. In production use cases, docker database could be replaced with real, managed database.

Main abstractions in the API are `CalendarResource` and `CalendarReservation`. `CalendarResource` is physically located
reservable subject and can have positive maximal capacity of simultaneous reservations. For singleton resources capacity
is one, example identified _one Tennis field_. If you want to manage multiple instances of identical resources you can
have bigger capacity, like _10 Tennis rackets_.

You can create resources and manage reservation for your resources with API. You can also query the resource
availability for specific resource and time range. You can't accidentally over-reserve your resource with confirmed
reservations.

### Implementation Architecture

`main` folder in `src` have both Kotlin code and resources.

Kotlin code is divided into packages:

- `calendar` contains actual internal APIs for resource and reservation and JPA implementations
- `graphql` contains GraphQL API resolvers and configurations
- `util` contains utility properties

`resources` folder contains Spring Boot application configurations `application.yaml`, database migrations and GraphQL
schema definition.

`test` folder have both integration and unit tests. Integration tests are run
with [Testcontainer's](https://www.testcontainers.org/) postgresql database and unit tests just mock services.

For development purposes, you can start database with docker and start backend with command `./gradlew bootRun` to
speedup iteration process.