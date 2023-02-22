package ru.coolspot.chessknight.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnCorrectValueAndOkStatus() throws Exception {
        this.mockMvc
                .perform(get("/horse/rest/count?width=10&height=14&start=B1&end=A3"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void shouldReturnBadRequest() throws Exception {
        this.mockMvc
                .perform(get("/horse/rest/count?width=0&height=14&start=B1&end=A3"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("error")));
    }
}