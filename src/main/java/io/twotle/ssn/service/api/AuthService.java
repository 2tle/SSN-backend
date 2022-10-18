package io.twotle.ssn.service.api;

import io.twotle.ssn.exception.CustomException;
import io.twotle.ssn.exception.ExceptionCode;
import io.twotle.ssn.dto.LoginDTO;
import io.twotle.ssn.dto.RegisterDTO;
import io.twotle.ssn.entity.User;
import io.twotle.ssn.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder bCryptPasswordEncoder;

    public User signUp(RegisterDTO registerDTO) throws CustomException{
        if(this.isEmailAvailable(registerDTO.getEmail()) && this.isUsernameAvailable(registerDTO.getUsername())) {
            throw new CustomException(ExceptionCode.ALREADY_REGISTERED);
        }
        User newUser = registerDTO.toUserEntity();
        newUser.hashPassword(bCryptPasswordEncoder);
        return this.userRepository.save(newUser);
    }

    public User signIn(LoginDTO loginDTO) throws CustomException {
        Optional<User> user = this.userRepository.findByEmail(loginDTO.getEmail());
        if(user.isEmpty()) throw new CustomException(ExceptionCode.USER_NOT_FOUND);
        if(!user.get().checkPassword(loginDTO.getPassword(), bCryptPasswordEncoder))
            throw new CustomException(ExceptionCode.USER_NOT_FOUND);
        return user.get();
    }

    public boolean isEmailAvailable(String email) {
        Optional<User> byEmail = this.userRepository.findByEmail(email);
        return !byEmail.isEmpty();
    }

    public boolean isUsernameAvailable(String username) {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        return !byUsername.isEmpty();
    }


    //delete User
    // delete user accoun




}
