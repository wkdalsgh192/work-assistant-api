package com.squadb.workassistantapi.web.controller;

import com.squadb.workassistantapi.domain.exceptions.OutOfStockException;
import com.squadb.workassistantapi.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RentalController.class)
class RentalControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean RentalService rentalService;
    private MockHttpSession mockHttpSession;
    private final long mockLoginMemberId = 1L;

    @BeforeEach
    public void setup() {
        mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("MEMBER_ID", mockLoginMemberId);
    }

    @DisplayName("책 등록 api 성공 테스트")
    @Test
    public void rentBookApiTest() throws Exception {
        //given
        final long mockBookId = 1L;
        given(rentalService.rentBook(mockBookId, mockLoginMemberId, false)).willReturn(1L);

        //when
        ResultActions perform = mockMvc.perform(post("/rent/books/1")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("SUCCESS")));
    }

    @DisplayName("책 등록 api 재고 소진 테스트, OUT_OF_STOCK 이란 메시지가 응답해야함")
    @Test
    public void outOfStockTest() throws Exception {
        //given
        final long mockBookId = 1L;
        given(rentalService.rentBook(mockBookId, mockLoginMemberId, false)).willThrow(OutOfStockException.class);

        //when
        ResultActions perform = mockMvc.perform(post("/rent/books/1")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OUT_OF_STOCK")));
    }

}