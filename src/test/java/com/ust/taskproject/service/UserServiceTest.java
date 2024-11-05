package com.ust.taskproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ust.taskproject.entity.Users;
import com.ust.taskproject.payload.UserDto;
import com.ust.taskproject.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UsersRepository usersRepository;

    /**
     * Test {@link UserService#createUser(UserDto)}.
     * <p>
     * Method under test: {@link UserService#createUser(UserDto)}
     */
    @Test
    @DisplayName("Test createUser(UserDto)")
    void testCreateUser() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("iloveyou");
        when(usersRepository.save(Mockito.<Users>any())).thenReturn(users);

        UserDto userDto = new UserDto();
        userDto.setEmail("jane.doe@example.org");
        userDto.setId("42");
        userDto.setName("Name");
        userDto.setPassword("iloveyou");

        // Act
        UserDto actualCreateUserResult = userService.createUser(userDto);

        // Assert
        verify(usersRepository).save(isA(Users.class));
        assertEquals(userDto, actualCreateUserResult);
    }
}
