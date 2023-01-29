package com.example.questionapp.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.example.questionapp.security.JWTAuthenticationEntryPoint;
import com.example.questionapp.security.JWTAuthenticationFilter;
import com.example.questionapp.services.UserDetailsServiceImplementation;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration{                                                                 //security package da oluşturduğumuz classların hepsinin otomatik şekilde oluşmasını sağlıcak bir configuration classı oluşturdum

    private UserDetailsServiceImplementation userDetailsService;

    private JWTAuthenticationEntryPoint handler;                                                     //exception olduğunda handler eder

    public SecurityConfiguration(UserDetailsServiceImplementation userDetailsService, JWTAuthenticationEntryPoint handler) {
        this.userDetailsService = userDetailsService;
        this.handler = handler;
    }

    @Bean
    public JWTAuthenticationFilter JWTAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter() {  //cross originden gelen istekleri filtreleme yapıyoruz.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");  //herşeye izin veriyoruz şimdilik.
        config.addAllowedHeader("*");   //herşeye izin veriyoruz şimdilik.
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()   //cors ekledik. Yukarıda yazdığımız CorsFilter beanini kullanır.
                .and()
                .csrf().disable()   //postmanden de istek atacağımız için disable ettik.
                .exceptionHandling().authenticationEntryPoint(handler).and()  //exception olduğunda bizim yazdığımız  authenticationEntryPoint i kullanıcak.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()       //stateless session oluşturduk.
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll() //auth ile ilgili istek geldiğinde izin ver. token isteme yani.
                .requestMatchers(HttpMethod.GET,"/posts").permitAll() //posts ile gelen api isteklerine sadece get olunca izin ver diyoruz.
                .requestMatchers(HttpMethod.GET,"/comments").permitAll()
                .anyRequest().authenticated(); //bunlar dışında istek gelirse authenticated mı diye kontrol et.

        httpSecurity.addFilterBefore(JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); //bizim yaptıgımız jwtAuthenticationFilter da ekle UsernamePasswordAuthenticationFilter dan önce.
        return httpSecurity.build();
    }

}
