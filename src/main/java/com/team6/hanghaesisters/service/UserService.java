package com.team6.hanghaesisters.service;

import com.team6.hanghaesisters.dto.MsgResponseDto;
import com.team6.hanghaesisters.dto.UserDto;
import com.team6.hanghaesisters.exception.CustomException;
import com.team6.hanghaesisters.exception.ErrorCode;
import com.team6.hanghaesisters.repository.UserRepository;
import com.team6.hanghaesisters.security.jwt.JwtUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class UserService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public MsgResponseDto signup(UserDto.SignupReqDto dto) {
        userRepository.save(dto.toEntity());
        return new MsgResponseDto("회원가입 되었습니다.", HttpStatus.OK.value());
    }

    public MsgResponseDto login(UserDto.LoginReqDto dto, HttpServletResponse httpServletResponse){
        //아직 인증 전 객체
        UsernamePasswordAuthenticationToken beforeAuthentication = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

        //인증 완료된 인증 객체
        Authentication afterAuthentication = authenticationManagerBuilder.getObject().authenticate(beforeAuthentication);

        //인증 완료된 객체로 JWT 생성
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.generateToken(afterAuthentication));
        log.info("created jwt token");

        return new MsgResponseDto("로그인 되었습니다.", HttpStatus.OK.value());
    }

    public MsgResponseDto checkIdDuplication(String username) {

        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.OVERLAP_USERID);
        }
        log.info("checked id");

        return new MsgResponseDto("사용 가능한 아이디입니다.", HttpStatus.OK.value());
    }
}
