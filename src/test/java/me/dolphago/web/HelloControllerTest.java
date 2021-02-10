package me.dolphago.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import me.dolphago.web.dto.HelloResponseDto;

@WebMvcTest
class HelloControllerTest {
    @Autowired
    private MockMvc mvc;

    @DisplayName("hello가 리턴된다.")
    @Test
    public void hello_test() throws Exception {
        mvc.perform(get("/hello"))
           .andExpect(status().isOk())
           .andExpect(content().string("hello"));
    }

    @DisplayName("롬복 기능 테스트")
    @Test
    public void lombok_test() throws Exception {
        String name = "test";
        int amount = 10;

        HelloResponseDto dto = new HelloResponseDto(name, amount);

        assertEquals(name, dto.getName());
        assertEquals(amount, dto.getAmount());
    }

    @DisplayName("hello dto가 리턴된다.")
    @Test
    public void hellodto_test() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is(name)))
           .andExpect(jsonPath("$.amount", is(amount)));

    }

}