package com.team6.hanghaesisters.validation;

import com.team6.hanghaesisters.dto.UserDto;
import com.team6.hanghaesisters.repository.UserRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 회원가입시 검증을 진행할 validator
 */
@Component
@RequiredArgsConstructor
public class SignupValidator implements ConstraintValidator<ValidSignup, UserDto.SignupReqDto> {

    private final UserRepository userRepository;

    @Override
    public void initialize(ValidSignup constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDto.SignupReqDto value, ConstraintValidatorContext context) {

        String username = value.username();
        String password = value.password();

        if(!StringUtils.hasText(username)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("유저명을 입력해 주세요").addConstraintViolation();
            return false;
        }else if(!Pattern.matches("(?=.*[a-z])(?=.*\\d)[a-z\\d]{4,10}$" ,username)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("유저명은 소문자, 숫자를 필수로 포함한 4-10자로 입력할 수 있습니다").addConstraintViolation();
            return false;
        }else if(userRepository.existsByUsername(username)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("이미 존재하는 유저명입니다").addConstraintViolation();
            return false;
        }

        if(!StringUtils.hasText(password)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호를 입력해 주세요").addConstraintViolation();
            return false;
        }else if(!Pattern.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\\d)[a-zA-Z0-9!@#$%^&*]{8,15}$" ,password)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호는 소문자, 대문자, 숫자, 특수문자(!@#$%^&*)를 필수로 포함한 8-15자로 입력할 수 있습니다").addConstraintViolation();
            return false;
        }

        return true;
    }


}
