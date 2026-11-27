package org.example.dao;

import org.example.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<User> typedQuery;

    @InjectMocks
    private UserDaoImpl userDao;

    @Test
    void addUserTest() {
        User user = new User();
        userDao.addUser(user);
        verify(entityManager).persist(user);
    }

    @Test
    void removeUserTest() {
        long id = 1L;
        User user = new User();
        user.setId(id);
        when(entityManager.find(User.class, id)).thenReturn(user);
        userDao.removeUser(id);
        verify(entityManager).remove(user);
    }

    @Test
    void updateUserTest() {
        long id = 1L;
        User user = new User("Danil", 27);
        user.setId(id);
        when(entityManager.find(User.class, id)).thenReturn(user);
        String name = "Ivan";
        int age = 30;
        userDao.updateUser(id, name, age);
        verify(entityManager).find(User.class, id);
        assertEquals(name, user.getName());
        assertEquals(age, user.getAge());
    }

    @Test
    void showAllUserTest() {
        List<User> users = Arrays.asList(
                new User("Ivan", 30),
                new User("Danil", 27));

        when(entityManager.createQuery("SELECT u FROM User u", User.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(users);

        List<User> result = userDao.showAllUsers();

        verify(entityManager).createQuery("SELECT u FROM User u", User.class );
        verify(typedQuery).getResultList();
        assertEquals(users, result);
    }

    @Test
    void getUserTest() {
        long id = 1L;
        User user = new User("Danil", 27);
        user.setId(id);
        when(entityManager.find(User.class, id)).thenReturn(user);

        User userResult = userDao.getUser(id);

        verify(entityManager).find(User.class, id);
        assertEquals(userResult, user);
    }
}
