package com.example.questionapp.controllers;

import com.example.questionapp.dataAccess.UserRepository;
import com.example.questionapp.entities.RefreshToken;
import com.example.questionapp.entities.User;
import com.example.questionapp.requests.RefreshTokenRequest;
import com.example.questionapp.requests.UserRequest;
import com.example.questionapp.responses.AuthenticationResponse;
import com.example.questionapp.security.JWTTokenProvider;
import com.example.questionapp.services.RefreshTokenService;
import com.example.questionapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody UserRequest loginRequest){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()); //useri authentication objesine koyucaz.
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJWTToken(auth); //token oluşturmak için
        User user = userService.getUserByUsername(loginRequest.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken("Bearer " + jwtToken);
        authenticationResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authenticationResponse.setUserId(user.getId());
        return authenticationResponse;
    }
   // refresh token = access token geçerliliğini bitirdiğinde tokeni yenilemek için kullanılıcak
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequest registerRequest){       //ResponseEntity dönmemizin sebebi register olup olmadığının bilgisinin verilmesidir.
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if(userService.getUserByUsername(registerRequest.getUsername()) != null){            //böyle bir user varsa db de.
            authenticationResponse.setMessage("Username already taken.");
            return new ResponseEntity<>(authenticationResponse, HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userService.createUser(user);
        //hem register olup daha sonra da login oluyoruz.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registerRequest.getUsername(),registerRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJWTToken(auth);

        authenticationResponse.setMessage("User Successfully Registered");
        authenticationResponse.setAccessToken("Bearer "+jwtToken);
        authenticationResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authenticationResponse.setUserId(user.getId());
        return new ResponseEntity<>(authenticationResponse,HttpStatus.CREATED);
    }

    @PostMapping("/refresh")   //userin elindeki tokenin expire olduğunda refresh atması gerekiyor onun için oluşturduk. Bu yüzden buraya istek geldiğinde yeni bir access token üreticez.
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthenticationResponse authResponse = new AuthenticationResponse();
        RefreshToken token = refreshTokenService.getByUser(refreshTokenRequest.getUserId());
        if(token.getToken().equals(refreshTokenRequest.getRefreshToken()) &&
                !refreshTokenService.isRefreshExpired(token)) {
            User user = token.getUser();
            String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());  //user in access tokenini güncellemiş oluyoruz.
            authResponse.setMessage("token successfully refreshed.");
            authResponse.setAccessToken("Bearer " + jwtToken);
            authResponse.setUserId(user.getId());
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } else {
            authResponse.setMessage("refresh token is not valid.");
            return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
        }

    }
}
