package com.gameofthree.player.service;

import com.gameofthree.player.domain.Move;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class MoveSender {
    @Value("${next-player.url.move}")
    private String nextPlayerUrl;

    private RestTemplate restTemplate;
    private RetryTemplate retryTemplate;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        retryTemplate = new RetryTemplate();

        retryTemplate.setRetryPolicy(new TimeoutRetryPolicy());
    }


    public boolean send(Move move) {
        try {
            retryTemplate.execute(context -> restTemplate.postForEntity(nextPlayerUrl, move, Object.class));
            return true;
        } catch (Exception e) {
            log.error("Another player isn't available, ", e);
            return false;
        }
    }
}
