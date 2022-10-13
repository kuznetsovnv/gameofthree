package com.gameofthree.player.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Action {
    PLUS_ONE(1),
    PLUS_ZERO(0),
    MINUS_ONE(-1);

    private final int value;
}
