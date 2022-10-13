## What is it?
Solution for [Coding_Challenge_JAVA.pdf](Coding_Challenge_JAVA.pdf)

## Implementation details
Game automatically starts every 30 sec by `StartOfTheGameScheduler.java` with random value in range [2, 100].

Scheduling configured via `schedule.start-new-game.cron=0/30 * * * * *`.

To start the game manually send POST request to `localhost:${port}/player/start-new-game` with empty body.

If one of the players isn't available in 3 tries with 1 sec delay - remaining one wins.

Each player counts his wins in `PlayerEventListener.wins` (to simplify testing).

## Deploy
To start the game with two players execute in terminal:
`docker-compose up --build`
