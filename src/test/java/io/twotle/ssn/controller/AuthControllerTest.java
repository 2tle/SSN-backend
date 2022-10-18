package io.twotle.ssn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twotle.ssn.dto.RegisterDTO;
import io.twotle.ssn.service.api.AuthService;
import io.twotle.ssn.service.jwt.JwtUtil;
import io.twotle.ssn.service.redis.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@DisplayName("AuthController Test")
@Rollback
class AuthControllerTest {
    private MockMvc mvc;

    @MockBean
    private AuthService authService;
    private JwtUtil jwtUtil;
    private RedisUtil redisUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mvc =
                MockMvcBuilders.standaloneSetup(new AuthController(authService, jwtUtil, redisUtil))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true)) // utf-8 필터 추가
                        .build();
    }

    @Test
    @DisplayName("POST /auth/new - Clear")
    void register() throws Exception {
        String obj = objectMapper.writeValueAsString(new RegisterDTO("21sunrin100@sunrint.hs.kr","user1111","name1"));
        ResultActions act = mvc.perform(
                post("/auth/new")
                        .content(obj)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        act.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET email exist")
    void emailExists() throws Exception {
        //String obj = objectMapper.writeValueAsString(new RegisterDTO("21sunrin100@sunrint.hs.kr","user1111","name1"));
        ResultActions act = mvc.perform(
                get("/auth/by-email/21sunrint000@sunrint.hs.kr/exists")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        act.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("exist").value(false));
    }

    @Test
    @DisplayName("GET username exist")
    void usernameExists() throws Exception {
        //String obj = objectMapper.writeValueAsString(new RegisterDTO("21sunrin100@sunrint.hs.kr","user1111","name1"));
        ResultActions act = mvc.perform(
                get("/auth/by-username/asdfasdfasfew/exists")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        act.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("exist").value(false));
    }

    @Test
    @DisplayName("POST /auth/new - ValidationErr")
    void registerValid() throws Exception {
        String obj1 = objectMapper.writeValueAsString(new RegisterDTO("randomEmadfdil@sundfrint.hs.kr","user1d111","nam2dd2e1"));
        ResultActions act1 = mvc.perform(
                post("/auth/new")
                        .content(obj1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        act1
                .andExpect(status().isBadRequest()).andDo(print());
    }





}