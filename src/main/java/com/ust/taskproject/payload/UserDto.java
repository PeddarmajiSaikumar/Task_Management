package com.ust.taskproject.payload;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class UserDto {

    private String id;
    private String name;
    private String email;
    private String password;
}
