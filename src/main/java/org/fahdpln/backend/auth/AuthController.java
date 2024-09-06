package org.fahdpln.backend.auth;

import org.fahdpln.backend.utils.MyResponse;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/v1/login")
    public ResponseEntity<MyResponse> login(@RequestBody @Valid AuthRequest request) {

        MyResponse response = authService.login(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
