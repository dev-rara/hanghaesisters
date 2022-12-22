package com.team6.hanghaesisters.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.hanghaesisters.security.exceptionhandler.CustomAccessDeniedHandler;
import com.team6.hanghaesisters.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.team6.hanghaesisters.security.jwt.JwtUtil;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper om;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http
                // antMatchers -> requestMatchers 로 변경 (version 3.0.0 에서는 이렇게 사용)
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/api/user/**").permitAll()
                        .antMatchers("/api/post/hospital").permitAll()
                        .antMatchers("/api/post/category").permitAll()
                        .anyRequest().authenticated());
        http
                .logout()
                        .logoutUrl("/api/user/logout")
                        .logoutSuccessUrl("/api/user/login")
                        .addLogoutHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession();
                            session.invalidate();
                        })
                        .logoutSuccessHandler(
                            (request, response, authentication) -> response.sendRedirect("/api/user/login"))
                        .deleteCookies("remember-me");

        http
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler())
                .and()
                .headers()
                .frameOptions()
                .disable()

                .and()
                .cors()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .apply(new JwtConfig(jwtUtil, om));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:3000");
//        config.addAllowedOrigin("http://charleybucket.s3-website.ap-northeast-2.amazonaws.com"); //요거 변경하시면 됩니다.

        config.addExposedHeader(JwtUtil.AUTHORIZATION_HEADER);

        config.addAllowedMethod("*");

        config.addAllowedHeader("*");

        config.setAllowCredentials(true);

        config.validateAllowCredentials();

        // 어떤 경로에 이 설정을 적용할 지 명시
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler(){
        return new CustomAccessDeniedHandler(om);
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint(om);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
