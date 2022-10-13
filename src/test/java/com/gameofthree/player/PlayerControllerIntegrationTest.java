package com.gameofthree.player;

import com.gameofthree.player.domain.Action;
import com.gameofthree.player.domain.Move;
import com.gameofthree.player.service.MoveSender;
import com.gameofthree.player.service.PlayerEventListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MoveSender moveSender;

    @Test
    public void testStartNewGame() throws Exception {
        mvc.perform(post("/start-new-game")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        waitForPostToBeProcessed();
        verify(moveSender, times(1)).send(any(Move.class));
    }

    @Test
    public void testFirstMove_minusOne() throws Exception {
        postMoveExpectOk("{\"value\" : 19}");

        verify(moveSender, times(1)).send(new Move(Action.MINUS_ONE, 6));
    }

    @Test
    public void testFirstMove_plusOne() throws Exception {
        postMoveExpectOk("{\"value\" : 20}");

        verify(moveSender, times(1)).send(new Move(Action.PLUS_ONE, 7));
    }

    @Test
    public void testNextMove_plusZero() throws Exception {
        postMoveExpectOk("{\"value\" : 18, \"lastMoveAction\" : \"PLUS_ZERO\"}");

        verify(moveSender, times(1)).send(new Move(Action.PLUS_ZERO, 6));
    }

    @Test
    public void testFirstMove_responseNotSent() throws Exception {
        postMoveExpectOk("{\"value\" : 3}");

        verify(moveSender, times(0)).send(any(Move.class));
    }

    @Test
    public void testFirstMove_win() throws Exception {
        int winsBefore = PlayerEventListener.wins.get();

        postMoveExpectOk("{\"value\" : 3}");

        verify(moveSender, times(0)).send(any(Move.class));
        assertEquals(winsBefore + 1, PlayerEventListener.wins.get());
    }
    @Test
    public void testFirstMove_noResponseFromAnotherPlayerWin() throws Exception {
        int winsBefore = PlayerEventListener.wins.get();

        Mockito.when(moveSender.send(any(Move.class))).thenReturn(false);
        postMoveExpectOk("{\"value\" : 30}");

        verify(moveSender, times(1)).send(any(Move.class));
        assertEquals(winsBefore + 1, PlayerEventListener.wins.get());
    }

    @Test
    public void testFirstMove_responseSentNoWin() throws Exception {
        int winsBefore = PlayerEventListener.wins.get();

        Mockito.when(moveSender.send(any(Move.class))).thenReturn(true);
        postMoveExpectOk("{\"value\" : 6}");

        verify(moveSender, times(1)).send(any(Move.class));
        assertEquals(winsBefore, PlayerEventListener.wins.get());
    }

    private void postMoveExpectOk(String body) throws Exception {
        mvc.perform(post("/move").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        waitForPostToBeProcessed();
    }

    // listener isn't in the same thread with publisher
    private void waitForPostToBeProcessed() throws InterruptedException {
        Thread.sleep(50);
    }
}