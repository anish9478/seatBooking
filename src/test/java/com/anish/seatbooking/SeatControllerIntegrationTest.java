package com.anish.seatbooking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SeatControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldGetAllSeats() throws Exception {
        mockMvc.perform(get("/api/seat"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
    @Test
    void shouldRejectUnauthorizedUser() throws Exception{
        mockMvc.perform(get("/api/seat")).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldForbidFromCreatingSeat() throws Exception{
        mockMvc.perform(post("/api/seat")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    void shouldAllowAdminToCreateSeat() throws Exception {
        String seatNumber = "A10-" + java.util.UUID.randomUUID();
        String seatJson = """
                {"seatNumber" : "%s",
                "booked" : "false"
                }
                """.formatted(seatNumber);
        mockMvc.perform(post("/api/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(seatJson))
                .andExpect(status().isOk());
    }
}
