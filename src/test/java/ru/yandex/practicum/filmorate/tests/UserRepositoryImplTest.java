package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryImplTest {

    private final JdbcTemplate jdbcTemplate;

    private UserRepositoryImpl userRepositoryImpl;


    @BeforeEach
    public void setUp() {
        userRepositoryImpl = new UserRepositoryImpl(jdbcTemplate);
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM USERS");
    }


    @Test
    public void testFindUserById() {
        User newUser = new User(1, "user", "user@gmail.com", "UserName",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        User savedUser = userRepositoryImpl.findById(newUser.getId());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testFindUserByNotExistingId_shouldReturnBull() {
        User newUser = new User(1, "user", "user@gmail.com", "User Name",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        assertNull(userRepositoryImpl.findById(2));
    }

    @Test
    public void testFindUserByName() {
        User newUser = new User(1, "user", "user@gmail.com", "UserName",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        User savedUser = userRepositoryImpl.findByName(newUser.getName());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testFindUserByNotExistingName_shouldThrowEmptyResultDataException() {
        User newUser = new User(1, "user", "user@gmail.com", "User Name",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        assertThrows(EmptyResultDataAccessException.class, () -> userRepositoryImpl.findByName("UserUser"));
    }

    @Test
    public void testFindUserByEmail() {
        User newUser = new User(1, "user", "user@gmail.com", "UserName",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        User savedUser = userRepositoryImpl.findByEmail(newUser.getEmail());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testFindUserByNotExistingEmail_shouldReturnNull() {
        User newUser = new User(1, "user", "user@gmail.com", "User Name",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        assertNull(userRepositoryImpl.findByEmail("user24@gmail.com"));
    }

    @Test
    public void testFindUserByLogin() {
        User newUser = new User(1, "user", "user@gmail.com", "UserName",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        User savedUser = userRepositoryImpl.findByLogin(newUser.getLogin());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testFindUserByNotExistingLogin_shouldReturnNull() {
        User newUser = new User(1, "user", "user@gmail.com", "User Name",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        assertNull(userRepositoryImpl.findByLogin("UserLoginLogin"));
    }

    @Test
    public void testFindUserIdByName() {
        User newUser = new User(1, "user", "user@gmail.com", "UserName",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        Integer savedUserID = userRepositoryImpl.findIdByName(newUser.getName());

        assertThat(savedUserID)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser.getId());
    }

    @Test
    public void testFindUserIdByNotExistingName_shouldReturnNull() {
        User newUser = new User(1, "user", "user@gmail.com", "User Name",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        assertNull(userRepositoryImpl.findIdByName("UserUserUser"));
    }

    @Test
    public void testFindAll() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));
        List<User> newUsers = new ArrayList<>();
        newUsers.add(newUser1);
        newUsers.add(newUser2);

        userRepositoryImpl.save(newUser1);
        userRepositoryImpl.save(newUser2);

        List<User> savedUsers = userRepositoryImpl.findAll();

        assertThat(savedUsers)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUsers);
    }


    @Test
    public void testSaveUser() {
        User newUser = new User(1, "user", "user@gmail.com", "User Name",
                LocalDate.of(1990, 1, 1));

        assertDoesNotThrow(() -> userRepositoryImpl.save(newUser));
    }

    @Test
    public void testSaveUserWithEmptyName() {
        User newUser = new User(1, "", "user@gmail.com", "User Name",
                LocalDate.of(1990, 1, 1));

        assertDoesNotThrow(() -> userRepositoryImpl.save(newUser));
    }

    @Test
    public void testSaveUserWithSameLogin_shouldThrowDuplicateKeyException() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));

        userRepositoryImpl.save(newUser1);

        assertThrows(DuplicateKeyException.class, () -> userRepositoryImpl.save(newUser2));
    }

    @Test
    public void testSaveUserWithSameEmail_shouldThrowDuplicateKeyException() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user1@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));

        userRepositoryImpl.save(newUser1);

        assertThrows(DuplicateKeyException.class, () -> userRepositoryImpl.save(newUser2));
    }
    
    @Test
    public void testSaveMany() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));
        List<User> newUsers = new ArrayList<>();
        newUsers.add(newUser1);
        newUsers.add(newUser2);

        assertDoesNotThrow(() -> userRepositoryImpl.saveMany(newUsers));
    }

    @Test
    public void testSaveManyWithEmptyNames() {
        User newUser1 = new User(1, " ", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));
        List<User> newUsers = new ArrayList<>();
        newUsers.add(newUser1);
        newUsers.add(newUser2);

        assertDoesNotThrow(() -> userRepositoryImpl.saveMany(newUsers));
    }

    @Test
    public void testSaveManySameEmail_shouldThrowDuplicateKeyException() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user1@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));
        List<User> newUsers = new ArrayList<>();
        newUsers.add(newUser1);
        newUsers.add(newUser2);

        assertThrows(DuplicateKeyException.class, () -> userRepositoryImpl.saveMany(newUsers));
    }

    @Test
    public void testSaveManySameLogin_shouldThrowDuplicateKeyException() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        List<User> newUsers = new ArrayList<>();
        newUsers.add(newUser1);
        newUsers.add(newUser2);

        assertThrows(DuplicateKeyException.class, () -> userRepositoryImpl.saveMany(newUsers));
    }
    

    @Test
    public void testUpdate() {
        User newUser = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        User updatedUser = new User(1, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1995, 5, 5));

        assertDoesNotThrow(() -> userRepositoryImpl.update(updatedUser));

        User finalUser = userRepositoryImpl.findById(1);

        Assertions.assertEquals(updatedUser, finalUser, "Информация о пользователе должна быть обновлена");
        Assertions.assertEquals("User2 Name", finalUser.getLogin(), "Логин не совпадает");
        Assertions.assertEquals("user2", finalUser.getName(), "Имя не совпадает");
        Assertions.assertEquals("user2@gmail.com", finalUser.getEmail(), "Почта не совпадает");
        Assertions.assertEquals(LocalDate.of(1995, 5, 5), finalUser.getBirthday(),
                "Дата рождения не совпадает");
    } // ни в какую не хочет проходить


    @Test
    public void testDeleteByExistingId() {
        User newUser = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));

        User savedUser = userRepositoryImpl.save(newUser);

        assertDoesNotThrow(() -> userRepositoryImpl.deleteById(savedUser.getId()));
        assertNull(userRepositoryImpl.findById(1));
    }

    @Test
    public void testDeleteByNotExistingId() {
        User newUser = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        boolean deletionResult = userRepositoryImpl.deleteById(2);

        assertFalse(deletionResult);
    }

    @Test
    public void testDeleteAll() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));
        User newUser3 = new User(3, "user3", "user3@gmail.com", "User3 Name",
                LocalDate.of(1990, 1, 1));

        userRepositoryImpl.save(newUser1);
        userRepositoryImpl.save(newUser2);
        userRepositoryImpl.save(newUser3);

        assertDoesNotThrow(() -> userRepositoryImpl.deleteAll());
    }


    @Test
    public void testAddFriend() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));

        userRepositoryImpl.save(newUser1);
        userRepositoryImpl.save(newUser2);

        userRepositoryImpl.addFriend(newUser1.getId(), newUser2.getId());

        assertTrue(userRepositoryImpl.findUsersFriendsIds(newUser1.getId()).contains(newUser2.getId()));
        assertTrue(userRepositoryImpl.findUsersFriendsIds(newUser2.getId()).contains(newUser1.getId()));
    }

    @Test
    public void testRemoveFriend() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));

        userRepositoryImpl.save(newUser1);
        userRepositoryImpl.save(newUser2);

        userRepositoryImpl.addFriend(newUser1.getId(), newUser2.getId());

        assertTrue(userRepositoryImpl.findUsersFriendsIds(newUser1.getId()).contains(newUser2.getId()));
        assertTrue(userRepositoryImpl.findUsersFriendsIds(newUser2.getId()).contains(newUser1.getId()));

        userRepositoryImpl.removeFriend(newUser1.getId(), newUser2.getId());

        assertFalse(userRepositoryImpl.findUsersFriendsIds(newUser1.getId()).contains(newUser2.getId()));
        assertFalse(userRepositoryImpl.findUsersFriendsIds(newUser2.getId()).contains(newUser1.getId()));
    }

    @Test
    public void testFindUsersFriendsIds() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));
        User newUser3 = new User(3, "user3", "user3@gmail.com", "User3 Name",
                LocalDate.of(1990, 1, 1));

        userRepositoryImpl.save(newUser1);
        userRepositoryImpl.save(newUser2);
        userRepositoryImpl.save(newUser3);

        userRepositoryImpl.addFriend(newUser1.getId(), newUser2.getId());
        userRepositoryImpl.addFriend(newUser1.getId(), newUser3.getId());

        List<Integer> user1friendsIds = userRepositoryImpl.findUsersFriendsIds(newUser1.getId());
        assertEquals(2, user1friendsIds.size());
        assertTrue(user1friendsIds.contains(newUser2.getId()));
        assertTrue(user1friendsIds.contains(newUser3.getId()));

        List<Integer> user2friendsIds = userRepositoryImpl.findUsersFriendsIds(newUser2.getId());
        assertEquals(1, user2friendsIds.size());
        assertTrue(user2friendsIds.contains(newUser1.getId()));

        List<Integer> user3friendsIds = userRepositoryImpl.findUsersFriendsIds(newUser3.getId());
        assertEquals(1, user3friendsIds.size());
        assertTrue(user3friendsIds.contains(newUser1.getId()));
    }

    @Test
    public void testGetMutualFriends() {
        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));
        User newUser3 = new User(3, "user3", "user3@gmail.com", "User3 Name",
                LocalDate.of(1990, 1, 1));

        userRepositoryImpl.save(newUser1);
        userRepositoryImpl.save(newUser2);
        userRepositoryImpl.save(newUser3);

        userRepositoryImpl.addFriend(newUser1.getId(), newUser2.getId());
        userRepositoryImpl.addFriend(newUser1.getId(), newUser3.getId());

        List<Integer> commonFriendsUser2User3 = userRepositoryImpl.getCommonFriends(newUser2.getId(), newUser3.getId());
        assertEquals(1, commonFriendsUser2User3.size());
        assertTrue(commonFriendsUser2User3.contains(newUser1.getId()));

        List<Integer> commonFriendsUser1User3 = userRepositoryImpl.getCommonFriends(newUser1.getId(), newUser3.getId());
        assertEquals(0, commonFriendsUser1User3.size());

        List<Integer> commonFriendsUser1User2 = userRepositoryImpl.getCommonFriends(newUser1.getId(), newUser2.getId());
        assertEquals(0, commonFriendsUser1User2.size());
    }

}