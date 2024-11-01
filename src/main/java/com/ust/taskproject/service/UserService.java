package com.ust.taskproject.service;
import com.ust.taskproject.entity.Users;
import com.ust.taskproject.payload.UserDto;
import com.ust.taskproject.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    private Users userDtoToEntity(UserDto userDto){
        Users users=new Users();
        users.setName(userDto.getName());     //getting name from the dto and setting it back to the userEntity
        users.setEmail(userDto.getEmail());
        users.setPassword(userDto.getPassword());
        return users;
    }

    private UserDto entityToDto(Users savedUsers){
        UserDto userDto=new UserDto();
        userDto.setId(savedUsers.getId());
        userDto.setName(savedUsers.getName());      //getting name from the entity and setting it back to the UserDto
        userDto.setEmail(savedUsers.getEmail());
        userDto.setPassword(savedUsers.getPassword());
        return userDto;
    }

    public UserDto createUser(UserDto userDto) {
        //userDto is not an entity of users entity
        Users users= userDtoToEntity(userDto);       //converted UserDto top UserEntity
        Users savedUsers=usersRepository.save(users);
        return entityToDto(savedUsers);    //returning UserDto
    }

    public List<Users> getAllUsers() {
        return null;
    }

    public Users getUserById(Long id) {
        return null;
    }
}
