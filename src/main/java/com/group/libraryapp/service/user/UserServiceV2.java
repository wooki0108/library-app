package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceV2 {

    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 아래 있는 함수가 시작될 때 start transaction;을 해준다(트랜잭션 시작)
    // 함수가 예외 없이 잘 끝났으면 commit;
    // 문제가 있다면 rollback;
    @Transactional
    public void saveUser(UserCreateRequest request) {
        userRepository.save(new User(request.getName(), request.getAge()));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
//                .stream().map(user -> new UserResponse(user.getId(), user.getName(), user.getAge()))
                .stream().map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUser(UserUpdateRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);

        user.updateName(request.getName());
        // 영속성 컨텍스트 dirty check 기능으로 save 하지 않더라도 변경 자동 저장
//        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String name) {
        User user = userRepository.findByName(name).orElseThrow(IllegalArgumentException::new);

//        if (user == null) {
//            throw new IllegalArgumentException();
//        }

        userRepository.delete(user);
    }

}
