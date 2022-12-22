package com.team6.hanghaesisters.dto;

import com.team6.hanghaesisters.entity.User;
import com.team6.hanghaesisters.validation.ValidSignup;
import javax.validation.constraints.NotBlank;

public class UserDto {

    @ValidSignup
    public record SignupReqDto(String username, String password){

        public User toEntity(){
            return new User(this.username, this.password);
        }

    }

    public record LoginReqDto(@NotBlank(message = "유저명을 입력해 주세요") String username,
                              @NotBlank(message = "비밀번호를 입력해 주세요") String password){

    }

}
