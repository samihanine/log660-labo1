package ca.etsmtl.log660.labo2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
public class BaseDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetLanguages() throws Exception {
        mockMvc.perform(get("/data/languages")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOYXRhbGllTEhpbGwxOEBob3RtYWlsLmNvbSIsImlhdCI6MTcyOTk3MDQwOSwiZXhwIjoxNzMwMzMwNDA5fQ.zuQbfsKwXBEQk2iGWB-CkTlXzh0Xrubo85fmVtgnlmI"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetGenres() throws Exception {
        mockMvc.perform(get("/data/genres")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOYXRhbGllTEhpbGwxOEBob3RtYWlsLmNvbSIsImlhdCI6MTcyOTk3MDQwOSwiZXhwIjoxNzMwMzMwNDA5fQ.zuQbfsKwXBEQk2iGWB-CkTlXzh0Xrubo85fmVtgnlmI"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
}