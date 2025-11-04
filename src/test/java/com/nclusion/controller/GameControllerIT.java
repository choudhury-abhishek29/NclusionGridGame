package com.nclusion.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void create_join_move_and_get_game() throws Exception {
        // register players
        mockMvc.perform(post("/api/player/register").param("playerId", "alice"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/player/register").param("playerId", "bob"))
                .andExpect(status().isOk());

        // create game
        MvcResult createRes = mockMvc.perform(post("/api/game/new"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        String content = createRes.getResponse().getContentAsString();
        String gameId = content.replaceAll(".*\"id\":\"(.*?)\".*", "$1");

        // join game
        mockMvc.perform(post("/api/game/" + gameId + "/join").param("playerId", "alice"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/game/" + gameId + "/join").param("playerId", "bob"))
                .andExpect(status().isOk());

        // make a move
        String moveBody = "{\"playerId\":\"alice\",\"row\":0,\"col\":0}";
        mockMvc.perform(post("/api/game/" + gameId + "/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moveBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.board[0][0]").value("alice"));

        // get game
        mockMvc.perform(get("/api/game/" + gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(gameId));
    }
}


