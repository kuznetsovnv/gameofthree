package com.gameofthree.player.service;

import com.gameofthree.player.domain.Move;
import com.gameofthree.player.event.NewMoveEvent;
import com.gameofthree.player.event.StartNewGameEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerEventListener {
    private final MoveSender moveSender;

    public static AtomicInteger wins = new AtomicInteger();

    private static final int WINNING_VALUE = 1;
    private static final int MINIMAL_PLAYABLE_VALUE = 2;
    private static final int MAXIMUM_PLAYABLE_VALUE = 100;

    @EventListener
    public void newMoveEventProcessor(NewMoveEvent event) {
        log.info("The new event received: " + event);

        Move myMove = event.getMove().calculateNextMove();
        log.info("My move: " + myMove);

        if (myMove.getValue() == WINNING_VALUE) {
            wins.incrementAndGet();
            log.info("Hey, I won! It's my win #" + wins);
            return;
        }

        send(myMove);
    }

    @EventListener
    public void startOfTheGameEventProcessor(StartNewGameEvent event) {
        log.info("The new event received: " + event);

        Move myMove = new Move(null, getStartingValue());
        log.info("My move: " + myMove);

        send(myMove);
    }

    private void send(Move move) {
        boolean success = moveSender.send(move);

        if (!success) {
            wins.incrementAndGet();
            log.info("If he isn't playing - I won, hah! It's my win #" + wins);
        }
    }

    private int getStartingValue() {
        return ThreadLocalRandom.current().nextInt(MINIMAL_PLAYABLE_VALUE, MAXIMUM_PLAYABLE_VALUE + 1);
    }
}
