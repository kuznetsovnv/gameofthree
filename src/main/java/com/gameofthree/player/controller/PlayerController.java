package com.gameofthree.player.controller;

import com.gameofthree.player.domain.Action;
import com.gameofthree.player.domain.Move;
import com.gameofthree.player.event.NewMoveEvent;
import com.gameofthree.player.event.StartNewGameEvent;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.Null;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlayerController {
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/move")
    public void move(@RequestBody(required=false) MoveDto move) {
        NewMoveEvent event = new NewMoveEvent(move.toDomain());
        log.info("Publishing the new event: " + event);
        applicationEventPublisher.publishEvent(event);
    }

    @PostMapping("/start-new-game")
    public void startNewGame() {
        StartNewGameEvent event = new StartNewGameEvent();
        log.info("Publishing the new event: " + event);
        applicationEventPublisher.publishEvent(event);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class MoveDto {
        @Null
        private Action lastMoveAction;
        @Min(2)
        private int value;

        public Move toDomain() {
            return new Move(lastMoveAction, value);
        }
    }
}
