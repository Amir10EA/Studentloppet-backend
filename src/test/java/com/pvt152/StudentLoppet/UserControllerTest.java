package com.pvt152.StudentLoppet;

import com.pvt152.StudentLoppet.controller.UserController;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private ActivityService activityService;

    @BeforeEach
    void setup() {
        when(userService.setName(anyString(), anyString(), anyString())).thenReturn(true);
        doThrow(new IllegalArgumentException("First name must contain only alphabetic characters and spaces."))
                .when(userService).setName(anyString(), eq("Anders123"), anyString());
        doThrow(new IllegalArgumentException("Last name must contain only alphabetic characters and spaces."))
                .when(userService).setName(anyString(), anyString(), eq("Andersson123"));



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

    @Test
    public void testInvalidWeight() throws Exception {
        doThrow(new IllegalArgumentException("Weight must be a positive number"))
            .when(userService).setWeight(anyString(), eq(-75.0));

    mockMvc.perform(MockMvcRequestBuilders.get("/studentloppet/setWeight/user@example.com/-75")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void testValidWeight() throws Exception {
        when(userService.setWeight(anyString(), eq(75.0))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.get("/studentloppet/setWeight/user@example.com/75")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInvalidIncreaseScoreValue() throws Exception {
        doThrow(new IllegalStateException("Score must be a positive number. No decimal accepted"))
                .when(userService).increaseScore(anyString(), eq(-25));
        mockMvc.perform(MockMvcRequestBuilders.get("/studentloppet/increaseScore/user@example.com/-25")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
    @Test
    public void testValidIncreaseScoreValue() throws Exception {
        when(userService.increaseScore(anyString(), eq(25))).thenReturn("OK");
        mockMvc.perform(MockMvcRequestBuilders.get("/studentloppet/increaseScore/user@example.com/25")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
