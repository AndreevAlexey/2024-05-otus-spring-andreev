package ru.otus.hw.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.SecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testBookOnUser() throws Exception {
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void testBookOnAdmin() throws Exception {
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    public void testBookOnAnonymous() throws Exception {
        mockMvc.perform(get("/book"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://*/login"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testBookAddOnUser200() throws Exception {
        mockMvc.perform(get("/book/add"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void testBookAddOnAdmin() throws Exception {
        mockMvc.perform(get("/book/add"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testBookEditOnUser200() throws Exception {
        mockMvc.perform(get("/book/edit/{id}", 1))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void testBookEditOnAdmin() throws Exception {
        mockMvc.perform(get("/book/edit/{id}", 1))
                .andExpect(status().isOk());
    }
}
