# Mancala

## Objective:
To collect as many seeds in your store as possible. The player with the most seeds in his/her store at the end of the game wins.

## Board Setup

Place six seeds in each of the six pits on your side of the game board. Your opponent should do the same. The colors of the seeds don’t matter. (For a shorter game, you can play with four seeds in each pit.)

## Rules
### Game Play
* Play always moves around the board in a counter-clockwise circle (to the right)
* The store on your right belongs to you. That is where you keep the seeds you win.
* The six pits near you are your pits.
* Only use one hand to pick up and put down seeds.
* Once you touch the seeds in a pit, you must move those seeds.
* Only put seeds in your own store, not your opponent’s store.

### Starting the Game:
store(sometimes called a “Mancala” or a “capture pit” ) seeds( sometimes called stones) On a turn, a player picks up all the seeds in one pit and “sows” them to the right, placing one seed in each of the pits along the way. If you come to your store, then add a seed to your store and continue. You may end up putting seeds in your opponent’s pits along the way.
Play alternates back and forth, with opponents picking up the seeds in one of their pits and distributing them one at a time into the pits on the right, beginning in the pit immediately to the right.

### Special Rules:

* When the last seed in your hand lands in your store, take another turn.
* When the last seed in your hand lands in one of your own pits, if that pit had been empty you get to keep all of the seeds in your opponents pit on the opposite side. Put those captured seeds, as well as the last seed that you just played on your side, into the store.

## Ending the Game:

The game is over when one player’s pits are completely empty. The other player takes the seeds remaining in her pits and puts those seeds in her store. Count up the seeds. Whoever has the most seeds wins.


>You can also find some visual explanations of the game rules by googling as search term ( Mancala or Kalaha )


---

### Pre-Requirements
- [jdk11](https://www.oracle.com/java/technologies/downloads/#java11)
- [docker-compose](https://docs.docker.com/compose/install/)
- [maven](https://maven.apache.org/download.cgi)
- [make](https://formulae.brew.sh/formula/make)
- for sonar varification you need to have user/password and placed inside setting.xml

## Security

- both service ( mancala web and mancala api ) both are exposed only via `https*`

> provided bash (.sh) script can be used to generate self-sign certificate.

```sh
   cd scripts && ./generate-keystore.sh
```

> you maybe get permission denied apply:  `chmod u+x ./*` over `script folder`



## Technologies
------------
- `Spring Boot`
- `Spring MVC`, for creating RESTful API
- `Vaadin`, A modern UI components for building web applications in Java
- `H2`, database for persisting the game information
- `Docker`, for containerization of services
- `Docker-Compose`, to link the containers
- `Swagger`, Swagger-UI, for API documentation
- `Actuator`, Monitoring the app, gathering metrics, understanding traffic.
- `WireMock`, inter-service communication testing

## How to's
### Run

- `you can run full application with test,IT,application run`
```sh
   cd scripts && ./start-service.sh
```

- `swagger`
```sh
   https://localhost:8080/swagger-ui/index.html
```
 ![swagger-image](https://github.com/shasisingh/mancala-api/blob/main/image/swagger.png?raw=true)

- `api`

```sh
   https://127.0.0.1:8080/games/v1/mancala
```

- `web`

```sh
   http://127.0.0.1:8082/games/v1/mancala/play
```
 ![web-image](https://github.com/shasisingh/mancala-api/blob/main/image/web.png?raw=true)

### Stop

- `you can stop the applications`
```sh
   cd scripts && ./stop-service.sh
```

## Test
- `make test`

####

#### mutation testing

- `cd mancala-api && mvn clean install -DskipTests org.pitest:pitest-maven:mutationCoverage`

> You can access the report at :  `{project-dir}/mancala-api/target/pit-reports/{currenttime}/index.html`

![pitest report](https://github.com/shasisingh/mancala-api/blob/main/image/Pit-Test.png?raw=true)

### Build with report

- `make report`

![JaCoco report](https://github.com/shasisingh/mancala-api/blob/main/image/JaCoco.png?raw=true)

> You can access the report at `/{project_path}/target/site/jacoco/index.html`

### Sonar
- `make start` (to initialize sonar Docker image)

> Sonar run will work only on docker run and valid sonar user/password

### Decisions
>.

### Future Steps
- Use Testcontainers for unit test so real database can be simulate
- Use caching technology for status check
- Enabled player to start game any time based on gameId, as game status are always saved.
- Apply CI/CD pipeline
- JWT implement
- WebSocket for realtime play
