package com.anish.seatbooking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterNewUser() throws Exception {
        Map<String, String> request = Map.of("username", "testUser" + java.util.UUID.randomUUID(),
                "password", "password123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRegisterAndLogin() throws Exception {
        String userName = "loginUser"  + java.util.UUID.randomUUID();
        String password = "password123";

        Map<String, String > register = Map.of("username", userName, "password", password);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        Map<String ,String> login = Map.of("username", userName, "password", password);
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.message").value("login successfull"));

    }

    @Test
    void shouldAccessProtectedApiUsingJwt() throws Exception {
        String username = "jwtUser" + java.util.UUID.randomUUID();
        String password = "password123";

        Map<String, String> register = Map.of("username", username, "password" ,password);
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        Map<String, String> login = Map.of("username", username, "password", password);
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(loginResponse).get("token").asText();

        mockMvc.perform(get("/api/seat")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectInvalidJwtToken() throws Exception {
        mockMvc.perform(get("/api/seat")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectWithoutJwtToken() throws Exception {
        mockMvc.perform(get("/api/seat")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldForbidNormalUserFromCreatingAdmin() throws Exception {
        String adminJson = """
                {
                "username" : "newAdmin",
                "password" : "admin123"
                }
                """;
        mockMvc.perform(post("/api/auth/admin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(adminJson))
                .andExpect(status().isForbidden());
    }
}
