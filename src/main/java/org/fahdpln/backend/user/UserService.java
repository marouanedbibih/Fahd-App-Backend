package org.fahdpln.backend.user;

import java.util.List;

import org.fahdpln.backend.exception.MyAlreadyExistException;
import org.fahdpln.backend.exception.MyNotFoundException;
import org.fahdpln.backend.utils.MyError;
import org.fahdpln.backend.utils.MyErrorResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // Fins user by username
    public User findByUsername(String username) throws MyNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new MyNotFoundException(MyErrorResponse.builder()
                        .message("User not found with username: " + username)
                        .build()));
    }

    // Delete user
    public void deleteUser(UserDTO userDTO) throws MyNotFoundException {
        // Find the user by id
        User user = this.findUserById(userDTO.getUserId());
        // Delete the user
        userRepository.delete(user);
    }

    // Udapte user
    public User updateUser(UserDTO userDTO) throws MyNotFoundException, MyAlreadyExistException {
        // Find the user by id
        User user = this.findUserById(userDTO.getUserId());
        // Check if the user already exists in the database except the user with the
        // given id
        this.checkIfUserExistsExceptId(userDTO);
        // Update the user
        user = this.saveUserInDB(userDTO);
        return user;
    }

    // Check if the user exists in the database except the user with the given id
    public void checkIfUserExistsExceptId(UserDTO userDTO) throws MyAlreadyExistException {
        // check if the username already exists
        if (userRepository.existsByUsernameAndIdNot(userDTO.getUsername(), userDTO.getUserId())) {
            throw new MyAlreadyExistException(MyErrorResponse.builder()
                    .errors(List.of(MyError.builder()
                            .key("username")
                            .message("Username already exists")
                            .build()))
                    .build());
        }
        // Check if the email already exists
        if (userRepository.existsByEmailAndIdNot(userDTO.getEmail(), userDTO.getUserId())) {
            throw new MyAlreadyExistException(MyErrorResponse.builder()
                    .errors(List.of(MyError.builder()
                            .key("email")
                            .message("Email already exists")
                            .build()))
                    .build());
        }
        // Check if the phone number already exists
        if (userRepository.existsByPhoneAndIdNot(userDTO.getPhone(), userDTO.getUserId())) {
            throw new MyAlreadyExistException(MyErrorResponse.builder()
                    .errors(List.of(MyError.builder()
                            .key("phone")
                            .message("Phone number already exists")
                            .build()))
                    .build());
        }
    }

    // Find user by ID
    private User findUserById(Long id) throws MyNotFoundException {
        return userRepository.findById(id).orElseThrow(
                () -> new MyNotFoundException(MyErrorResponse.builder()
                        .message("User not found with id: " + id)
                        .build()));
    }

    // This method is used to create a new user
    public User createNewUser(UserDTO userDTO) throws MyAlreadyExistException {
        // check if the user already exists
        this.checkIfUserExists(userDTO);
        // save the user in the database
        User user = this.saveUserInDB(userDTO);
        return user;
    }

    // check if the user exists in the database
    private void checkIfUserExists(UserDTO userDTO) throws MyAlreadyExistException {
        // check if the username already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new MyAlreadyExistException(MyErrorResponse.builder()
                    .errors(List.of(MyError.builder()
                            .key("username")
                            .message("Username already exists")
                            .build()))
                    .build());
        }
        // Check if the email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new MyAlreadyExistException(MyErrorResponse.builder()
                    .errors(List.of(MyError.builder()
                            .key("email")
                            .message("Email already exists")
                            .build()))
                    .build());
        }
        // Check if the phone number already exists
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            throw new MyAlreadyExistException(MyErrorResponse.builder()
                    .errors(List.of(MyError.builder()
                            .key("phone")
                            .message("Phone number already exists")
                            .build()))
                    .build());
        }
    }

    // This method is used to save a user in the database
    private User saveUserInDB(UserDTO userDTO) {
        if (userDTO.getPassword() != null) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        User user = User.builder()
                .username(userDTO.getUsername())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .createdAt(userDTO.getCreatedAt())
                .updatedAt(userDTO.getUpdatedAt())
                .build();
        if (userDTO.getUserId() != null) {
            user.setId(userDTO.getUserId());
        }
        user = userRepository.save(user);
        return user;
    }

}
