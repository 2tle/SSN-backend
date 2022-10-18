package io.twotle.ssn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.twotle.ssn.dto.*;
import io.twotle.ssn.exception.CustomException;
import io.twotle.ssn.entity.User;
import io.twotle.ssn.service.api.AuthService;
import io.twotle.ssn.service.jwt.JwtUtil;
import io.twotle.ssn.service.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Api(tags = {"1. Auth"})
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @ApiOperation(value = "Register", notes = "Create a new user.")
    @PostMapping("/new")
    public ResponseEntity<TokensDTO> signUp(@RequestBody @Validated RegisterDTO registerDTO) throws CustomException {
        User newUser = this.authService.signUp(registerDTO);
        String access = this.jwtUtil.generateToken(newUser);
        String refresh = this.jwtUtil.generateRefreshToken(newUser);
        this.redisUtil.setDataExpire(refresh, newUser.getEmail(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokensDTO(access,refresh));
    }

    @ApiOperation(value = "Login", notes = "Login")
    @PostMapping("/local")
    public ResponseEntity<TokensDTO> signIn(@RequestBody @Validated LoginDTO loginDTO) throws CustomException {
        User user = this.authService.signIn(loginDTO);
        String access = this.jwtUtil.generateToken(user);
        String refresh = this.jwtUtil.generateRefreshToken(user);
        this.redisUtil.setDataExpire(refresh, user.getEmail(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokensDTO(access,refresh));
    }

    @ApiOperation(value="Email using check", notes = "Check your email is using."   )
    @GetMapping("/by-email/{email}/exists")
    public ResponseEntity<ExistResponseDTO> isExistEmail(@PathVariable(name = "email") String email) throws CustomException {
        return ResponseEntity.status(HttpStatus.OK).body(new ExistResponseDTO(this.authService.isEmailAvailable(email)));
    }

    @ApiOperation(value = "Username using check", notes = "Check your username is using.")
    @GetMapping("/by-username/{username}/exists")
    public ResponseEntity<ExistResponseDTO> isExistUsername(@PathVariable(name = "username") String username) throws CustomException {
        return ResponseEntity.status(HttpStatus.OK).body(new ExistResponseDTO(this.authService.isUsernameAvailable(username)));
    }






}
