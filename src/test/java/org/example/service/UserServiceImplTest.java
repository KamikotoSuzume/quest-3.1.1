package org.example.service;

import org.example.dao.UserDao;
import org.example.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUserTest() {
        User user = new User();
        userService.addUser(user);
        verify(userDao).addUser(user);
    }

    @Test
    void removeUserTest() {
        long id = 1L;
        userService.removeUser(id);
        verify(userDao).removeUser(id);
    }

    @Test
    void updateUserTest() {
        long id = 1L;
        String name = "Ivan";
        int age = 30;
        userService.updateUser(id, name, age);
        verify(userDao).updateUser(id, name, age);
    }

    @Test
    void showAllUserTest() {
        List<User> users = Arrays.asList(
                new User("Ivan", 30),
                new User("Danil", 27));

        when(userDao.showAllUsers()).thenReturn(users);
        List<User> result = userService.showAllUsers();

        verify(userDao).showAllUsers();
        assertEquals(users, result);
    }

    @Test
    void getUser() {
        long id = 1L;
        User user = new User("Danil", 27);
        user.setId(id);
        when(userDao.getUser(id)).thenReturn(user);

        User userResult = userService.getUser(id);

        verify(userDao).getUser(id);
        assertEquals(user, userResult);
    }
}
