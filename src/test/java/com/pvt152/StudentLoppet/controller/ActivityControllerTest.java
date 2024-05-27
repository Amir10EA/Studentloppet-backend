package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ActivityController.class)
public class ActivityControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ActivityService activityService;

        @Test
        void testLogActivityWithValidParameters() throws Exception {

                double durationInMinutes = 30.0;
                long durationInSeconds = (long) (durationInMinutes * 60);

                Activity activity = new Activity(5.0, durationInMinutes, null, 10);
                given(activityService.logActivity(any(String.class), any(Double.class), any(Double.class)))
                                .willReturn(activity);

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/activities/addActivity/{email}/{distance}/{duration}", "user@example.com",
                                                5.0,
                                                durationInSeconds)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void testLogActivityWithInvalidDistance() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/activities/addActivity/{email}/{distance}/{duration}", "user@example.com",
                                                -5, 30)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(
                                                MockMvcResultMatchers.content().string(
                                                                containsString("Error: Invalid distance or duration")));

        }

        @Test
        void testLogActivityWithNegativeDuration() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/activities/addActivity/{email}/{distance}/{duration}", "user@example.com",
                                                5.0, -1)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.content()
                                                .string("Error: Invalid distance or duration"));
        }

}
