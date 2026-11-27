package org.example.controller;

import org.example.controllers.UserController;
import org.example.service.UserService;
import org.example.webconfigs.WebConfig;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.example.models.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Import(WebConfig.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getUsersTest() throws Exception {
        List<User> users = Arrays.asList(
                new User("Danil", 27),
                new User("Ivan", 30));

        when(userService.showAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")).andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", users))
                .andExpect(view().name("users"));

        verify(userService).showAllUsers();

    }

    @Test
    void createUserTest() throws Exception {
        mockMvc.perform(get("/users/new")).andExpect(status().isOk())
                .andExpect(view().name("add-user"));
    }

    @Test
    void addUserTest() throws Exception {
        mockMvc.perform(post("/users-add").
                param("name", "Danil").param("age", "27"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users"));

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).addUser(argumentCaptor.capture());

        User capturedUser = argumentCaptor.getValue();

        assertEquals("Danil", capturedUser.getName());
        assertEquals(27, capturedUser.getAge());;
    }

    @Test
    void deleteUserTest() throws Exception {
        mockMvc.perform(post("/users/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users"));

        verify(userService).removeUser(1L);
    }

    @Test
    void changeUserTest() throws Exception {
        long id = 1L;
        User user = new User("Danil", 27);
        user.setId(id);
        when(userService.getUser(id)).thenReturn(user);

        mockMvc.perform(get("/users/update").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user))
                .andExpect(view().name("update-user"));

        verify(userService).getUser(id);
    }

    @Test
    void updateUserTest() throws Exception {
        mockMvc.perform(post("/users-update").param("id", "1")
                .param("name", "Danil")
                .param("age", "27")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).updateUser(1L, "Danil", 27);
    }

}
