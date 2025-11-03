## Nclusion Grid Game API

Base URL: `http://localhost:8080`

All responses return either a success payload with HTTP 200 or a 400 with a plain error message body.

### Health

- **GET** `/api/game/on`
  - Check service health
  - Example request:
    ```
    GET /api/game/on HTTP/1.1
    Host: localhost:8080
    ```

### Players

- **GET** `/api/player/listAllPlayers`

  - List all registered player IDs
  - Example request:
    ```
    GET /api/player/listAllPlayers HTTP/1.1
    Host: localhost:8080
    ```

- **POST** `/api/player/register`

  - Register a player ID
  - Query params:
    - `playerId` (required)
    - `mode` (optional, default: `join`)
  - Example requests:

    ```
    POST /api/player/register?playerId=alice HTTP/1.1
    Host: localhost:8080

    POST /api/player/register?playerId=bob&mode=join HTTP/1.1
    Host: localhost:8080
    ```

- **GET** `/api/player/{playerId}`
  - Get a player's stats/details by ID
  - Returns 400 with message `Player not registered` if unknown
  - Example request:
    ```
    GET /api/player/alice HTTP/1.1
    Host: localhost:8080
    ```

### Games

- **POST** `/api/game/new`

  - Create a new game
  - Example request:
    ```
    POST /api/game/new HTTP/1.1
    Host: localhost:8080
    ```

- **POST** `/api/game/{gameId}/join`

  - Join an existing game
  - Query params:
    - `playerId` (required) — must be registered
  - Example request:
    ```
    POST /api/game/<gameId>/join?playerId=alice HTTP/1.1
    Host: localhost:8080
    ```

- **POST** `/api/game/{gameId}/moves`

  - Make a move on the grid
  - Request body (JSON):
    - `playerId`: string
    - `row`: integer
    - `col`: integer
  - Example request:

    ```
    POST /api/game/<gameId>/moves HTTP/1.1
    Host: localhost:8080
    Content-Type: application/json

    {
      "playerId": "alice",
      "row": 0,
      "col": 2
    }
    ```

- **GET** `/api/game/{gameId}`
  - Fetch the current state of a game
  - Example request:
    ```
    GET /api/game/<gameId> HTTP/1.1
    Host: localhost:8080
    ```

### Leaderboard

- **GET** `/api/game/leaderboard`

  - Top 3 players by win count (default)
  - Query params:
    - `by` (optional): `winCount` (default) or `efficiency`
  - Example requests:

    ```
    GET /api/game/leaderboard HTTP/1.1
    Host: localhost:8080

    GET /api/game/leaderboard?by=efficiency HTTP/1.1
    Host: localhost:8080
    ```

### Typical Flow

```
1) Register players
POST /api/player/register?playerId=alice HTTP/1.1
Host: localhost:8080

POST /api/player/register?playerId=bob HTTP/1.1
Host: localhost:8080

2) Create a game
POST /api/game/new HTTP/1.1
Host: localhost:8080

→ Note: capture the returned `id` field from the response as <gameId>

3) Join the game
POST /api/game/<gameId>/join?playerId=alice HTTP/1.1
Host: localhost:8080

POST /api/game/<gameId>/join?playerId=bob HTTP/1.1
Host: localhost:8080

4) Make moves
POST /api/game/<gameId>/moves HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "playerId": "alice",
  "row": 0,
  "col": 0
}

POST /api/game/<gameId>/moves HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "playerId": "bob",
  "row": 1,
  "col": 1
}

5) Get game state
GET /api/game/<gameId> HTTP/1.1
Host: localhost:8080

6) Leaderboard
GET /api/game/leaderboard HTTP/1.1
Host: localhost:8080
```

Notes:

- Replace placeholders like `<put-created-game-id-here>` with real values.
- Use any REST client (browser, Postman, Insomnia) to issue the above requests.
