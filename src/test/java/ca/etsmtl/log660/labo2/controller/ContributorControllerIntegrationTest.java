package ca.etsmtl.log660.labo2.controller;

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
public class ContributorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGetContributor() throws Exception {
        int contributorId = 424216;
        mockMvc.perform(get("/contributors/{id}", contributorId)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOYXRhbGllTEhpbGwxOEBob3RtYWlsLmNvbSIsImlhdCI6MTcyOTk3MDQwOSwiZXhwIjoxNzMwMzMwNDA5fQ.zuQbfsKwXBEQk2iGWB-CkTlXzh0Xrubo85fmVtgnlmI"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(contributorId));
    }

}