package ca.etsmtl.log660.labo2.controller;


import ca.etsmtl.log660.labo2.controller.dto.AuthenticationRequest;
import ca.etsmtl.log660.labo2.controller.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLogin() throws Exception {
        AuthenticationRequest loginRequest = AuthenticationRequest
                .builder()
                .username("NatalieLHill18@hotmail.com")
                .password("Yae4yeipha")
                .build();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    @WithMockUser(username = "NatalieLHill18@hotmail.com")
    public void testMe() throws Exception {


        UserDto expectedUser = UserDto.builder()
                .city("Brockville")
                .email("NatalieLHill18@hotmail.com")
                .phone("613-246-6128")
                .address("2439 Parkdale Ave")
                .state("ON")
                .zip("K6V 5T3")
                .name("Hill")
                .firstName("Natalie")
                .birthDate( new Date(
                        (new SimpleDateFormat("yyyy-MM-dd"))
                                .parse("1946-01-10")
                                .getTime()
                ))
                .build();

        mockMvc.perform(get("/me")
                        .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOYXRhbGllTEhpbGwxOEBob3RtYWlsLmNvbSIsImlhdCI6MTcyOTk3MDQwOSwiZXhwIjoxNzMwMzMwNDA5fQ.zuQbfsKwXBEQk2iGWB-CkTlXzh0Xrubo85fmVtgnlmI"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(expectedUser.getEmail()))
                .andExpect(jsonPath("$.name").value(expectedUser.getName()))
                .andExpect(jsonPath("$.firstName").value(expectedUser.getFirstName()))
                .andExpect(jsonPath("$.city").value(expectedUser.getCity()))
                .andExpect(jsonPath("$.phone").value(expectedUser.getPhone()))
                .andExpect(jsonPath("$.address").value(expectedUser.getAddress()))
                .andExpect(jsonPath("$.state").value(expectedUser.getState()))
                .andExpect(jsonPath("$.zip").value(expectedUser.getZip()))
                .andExpect(jsonPath("$.birthDate").value(expectedUser.getBirthDate().toString()));
    }
}