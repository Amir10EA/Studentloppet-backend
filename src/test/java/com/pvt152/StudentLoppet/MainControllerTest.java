package com.pvt152.StudentLoppet;

import com.pvt152.StudentLoppet.controller.MainController;
import com.pvt152.StudentLoppet.service.ActivityService;
import com.pvt152.StudentLoppet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private ActivityService activityService;

    @BeforeEach
    void setup() {
        Mockito.when(userService.setName(anyString(), anyString(), anyString())).thenReturn(true);
        doThrow(new IllegalArgumentException("First name must contain only alphabetic characters and spaces."))
                .when(userService).setName(anyString(), Mockito.eq("Anders123"), anyString());
        doThrow(new IllegalArgumentException("Last name must contain only alphabetic characters and spaces."))
                .when(userService).setName(anyString(), anyString(), Mockito.eq("Andersson123"));

    }

    @Test
    public void testValidNameSet() throws Exception {
        mockMvc.perform(get("/studentloppet/set/user@example.com/Anders/Andersson")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testInvalidFirstName() throws Exception {
        mockMvc.perform(get("/studentloppet/set/user@example.com/Anders123/Andersson")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidLastName() throws Exception {
        mockMvc.perform(get("/studentloppet/set/user@example.com/Anders/Andersson123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
