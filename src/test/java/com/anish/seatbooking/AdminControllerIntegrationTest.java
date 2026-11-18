package com.anish.seatbooking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldAllowAdminToAccessDashboard() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSeats").exists())
                .andExpect(jsonPath("$.availableSeats").exists())
                .andExpect(jsonPath("$.confirmedSeats").exists())
                .andExpect(jsonPath("$.totalUsers").exists());
    }
    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldForbidUserFromAccessingTheDashboard() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldAllowAdminToViewAllBookings() throws Exception {
        mockMvc.perform(get("/api/admin/bookings")).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldForbidUserFromViewingAllBookings() throws Exception {
        mockMvc.perform(get("/api/admin/bookings")).andExpect(status().isForbidden());
    }


}


