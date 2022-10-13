package com.gameofthree.player.event;

import com.gameofthree.player.domain.Move;
import lombok.Data;

@Data
public class NewMoveEvent implements GameEvent {
    private final Move move;
}
