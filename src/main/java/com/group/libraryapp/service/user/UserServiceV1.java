package com.group.libraryapp.service.user;

import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import com.group.libraryapp.repository.user.UserJdbcRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserServiceV1 {

    private final UserJdbcRepository userJdbcRepository;

    public UserServiceV1(UserJdbcRepository userJdbcRepository) {
        this.userJdbcRepository = userJdbcRepository;
    }

    public void saveUser(UserCreateRequest request) {
        userJdbcRepository.createUser(request.getName(), request.getAge());
    }

    public List<UserResponse> getUsers() {
        return userJdbcRepository.getUserList();
    }

    public void updateUser(UserUpdateRequest request) {
        if (userJdbcRepository.isUserNotExist(request.getId())) {
            throw new IllegalArgumentException();
        }

        userJdbcRepository.updateUser(request.getName(), request.getId());
    }

    public void deleteUser(String name) {
        if (userJdbcRepository.isNotUserExist(name)) {
            throw new IllegalArgumentException();
        }

        userJdbcRepository.deleteUser(name);
    }


}
