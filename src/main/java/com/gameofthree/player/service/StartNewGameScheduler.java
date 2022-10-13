package com.gameofthree.player.service;

import com.gameofthree.player.event.StartNewGameEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartNewGameScheduler {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(cron = "${schedule.start-new-game.cron}")
    public void startNewGame() {
        StartNewGameEvent event = new StartNewGameEvent();
        log.info("Publishing the new event: " + event);
        applicationEventPublisher.publishEvent(event);
    }
}
