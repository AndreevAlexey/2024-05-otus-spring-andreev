package ru.otus.hw.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@Import(SecurityConfig.class)
public class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testCommentOnUser() throws Exception {
        mockMvc.perform(get("/comment"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void testCommentOnAdmin() throws Exception {
        mockMvc.perform(get("/comment"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    public void testCommentOnAnonymous() throws Exception {
        mockMvc.perform(get("/comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://*/login"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testCommentAddOnUser403() throws Exception {
        mockMvc.perform(get("/comment/add"))
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void testCommentAddOnAdmin() throws Exception {
        mockMvc.perform(get("/comment/add"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testCommentEditOnUser403() throws Exception {
        mockMvc.perform(get("/comment/edit/{id}", 1))
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void testCommentEditOnAdmin() throws Exception {
        mockMvc.perform(get("/comment/edit/{id}", 1))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testCommentBookOnUser() throws Exception {
        mockMvc.perform(get("/comment/book/{id}", 1))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void testCommentBookOnAdmin() throws Exception {
        mockMvc.perform(get("/comment/book/{id}", 1))
                .andExpect(status().isOk());
    }
}
