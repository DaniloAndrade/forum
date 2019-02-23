package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.input.LoginInputDto;
import br.com.alura.forum.controller.dto.output.AuthenticationTokenOutputDto;
import br.com.alura.forum.security.jwt.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserAuthenticationController {


    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationTokenOutputDto> authenticate(@RequestBody LoginInputDto loginInfo) {

        try {
            Authentication authenticate = authManager.authenticate(loginInfo.build());
            String token = tokenManager.generateToken(authenticate);
            AuthenticationTokenOutputDto bearer = new AuthenticationTokenOutputDto("Bearer", token);
            return ResponseEntity.ok(bearer);

        } catch (AuthenticationException ex) {
            return ResponseEntity.badRequest().build();
        }

    }
}
