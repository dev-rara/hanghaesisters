package com.team6.hanghaesisters.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class HospitalDto {
    public record RequestDto(
            @Pattern(regexp = "^[가-힣|0-9|a-z|A-Z]+$", message = "띄워쓰기는 입력이 불가능하며, 한글만 입력이 가능합니다.")
            @NotBlank(message = "병원 정보를 입력하지 않았습니다.")
            String hospitalAddress
    ) {
    }
}
