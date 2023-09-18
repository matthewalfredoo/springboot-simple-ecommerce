package com.learning.authservice.mapper;

import com.learning.authservice.dto.AuthRequestDto;
import com.learning.authservice.dto.UserDto;
import com.learning.authservice.entity.User;

public class UserMapper {

    public static UserDto toUserDtoFromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setAddress(user.getAddress());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static User toUserFromUserDto(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static User toUserFromAuthRequestDto(AuthRequestDto authRequestDto) {
        User user = new User();
        user.setName(authRequestDto.getName());
        user.setAddress(authRequestDto.getAddress());
        user.setEmail(authRequestDto.getEmail());
        user.setPassword(authRequestDto.getPassword());
        user.setRole(authRequestDto.getRole());
        return user;
    }

}
