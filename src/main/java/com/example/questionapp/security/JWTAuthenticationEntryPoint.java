package com.example.questionapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

@Component  //springin bean olarak inject edebilmesi için.
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {      //eğer ki unauthorizated istek gelirse diye yazdığım bir entry point

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {  //eger bir hata yakalanırsa respondda unauthorized dönmesi için bu fonksiyon oluşturdum.

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage()); //response a erroru ekledik. 401 unauthorized
    }
}
