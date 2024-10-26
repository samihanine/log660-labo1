package ca.etsmtl.log660.labo2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */


@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetFilms() throws Exception {
        mockMvc.perform(get("/films")
                        .param("title", "Black in men")
                        .param("startYear", "1997")
                        .param("endYear", "2004")
                        .param("language", "English")
                        .param("country", "USA")
                        .param("genre", "Comedy")
                        .param("director", "A F")
                        .param("actor", "A C")
                        .param("page", "1")
                        .param("pageSize", "7")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOYXRhbGllTEhpbGwxOEBob3RtYWlsLmNvbSIsImlhdCI6MTcyOTk3MDQwOSwiZXhwIjoxNzMwMzMwNDA5fQ.zuQbfsKwXBEQk2iGWB-CkTlXzh0Xrubo85fmVtgnlmI")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.films").isArray())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.pageSize").value(7));
    }

    @Test
    public void testGetFilm() throws Exception {
        int filmId = 120912;
        mockMvc.perform(get("/films/{id}", filmId)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOYXRhbGllTEhpbGwxOEBob3RtYWlsLmNvbSIsImlhdCI6MTcyOTk3MDQwOSwiZXhwIjoxNzMwMzMwNDA5fQ.zuQbfsKwXBEQk2iGWB-CkTlXzh0Xrubo85fmVtgnlmI"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(filmId))
                .andExpect(jsonPath("$.title").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "user")
    public void testRentFilm() throws Exception {
        int filmId = 120912; // Replace with a valid film ID
        mockMvc.perform(post("/films/rental/{id}", filmId)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOYXRhbGllTEhpbGwxOEBob3RtYWlsLmNvbSIsImlhdCI6MTcyOTk3MDQwOSwiZXhwIjoxNzMwMzMwNDA5fQ.zuQbfsKwXBEQk2iGWB-CkTlXzh0Xrubo85fmVtgnlmI"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Film rented successfully"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testReturnFilm() throws Exception {
        String idCopy = "120912_1"; // Replace with a valid copy ID
        mockMvc.perform(post("/films/return/{idCopy}", idCopy)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOYXRhbGllTEhpbGwxOEBob3RtYWlsLmNvbSIsImlhdCI6MTcyOTk3MDQwOSwiZXhwIjoxNzMwMzMwNDA5fQ.zuQbfsKwXBEQk2iGWB-CkTlXzh0Xrubo85fmVtgnlmI"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Film returned successfully"));
    }
}