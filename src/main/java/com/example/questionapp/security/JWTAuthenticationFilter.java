package com.example.questionapp.security;

import com.example.questionapp.services.UserDetailsServiceImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter { //bir request geldiğinde bu requestin authenticate olup olmadığını kontrol eden bir filter oluşturdum.
//normalde spring securitynin zaten filterları vardır.

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Autowired
    UserDetailsServiceImplementation userDetailsServiceImplementation;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String jwtToken = extractJwtFromRequest(request);                                          //token gelir metod sonucunda.
            if(StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {            //token geçerli ve varsa.
                Long id = jwtTokenProvider.getUserIdFromJWT(jwtToken);                                  //token verdik id döner.
                UserDetails user = userDetailsServiceImplementation.loadUserById(id);                   //id verip useri aldık.
                if(user != null){                                                                       //user varsa
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());  //Auth objesi oluşturduk.
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);                          //Bu işlemin sonunda request authenticate edilmiş olur. Security Context, kimliği doğrulanmış kullanıcı hakkında bilgileri içermektedir. Bu kullanıcı bilgileri SecurityContextHolder sayesinde tüm uygulamamız içerisinde kullanabiliriz. Doğrulanmamış kullanıcı ile karşılaşıldığında ise Authentication Exception fırlatılır.
                }
            }
        }catch(Exception e) {
            return;
        }
        filterChain.doFilter(request, response);                                                           //spring securitye filtera devam et deriz.
    }

    private String extractJwtFromRequest(HttpServletRequest request) {                                      //requesti alır tokeni döner.
        String bearer = request.getHeader("Authorization");                                              //requestten authorization headeri aldık.
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){                                    //request header authorizationun başında "Bearer tokenstring" şeklindedir.
            return bearer.substring("Bearer".length()+1);                                          //içinde bearer geçmeden direk tokeni döndük.
        }
        return null;
    }

}
