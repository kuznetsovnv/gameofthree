package com.gameofthree.player.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Move {
    private Action lastMoveAction;
    private int value;

    private static final int DIVIDE_VALUE = 3;
    public Move calculateNextMove() {
        for (Action action : Action.values()) {
            int value = this.value + action.getValue();
            if (value % DIVIDE_VALUE == 0) {
                return new Move(action, value / DIVIDE_VALUE);
            }
        }

        throw new IllegalArgumentException("Unable to perform a move! " + this);
    }
}
