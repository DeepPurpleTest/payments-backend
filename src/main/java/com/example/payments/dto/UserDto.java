package com.example.payments.dto;

import com.example.payments.util.ApplicationConstants.Validation;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

@Getter
@Builder
@Jacksonized
public class UserDto {
    @Nullable
    private Long id;
    @Length(min = Validation.MIN_NAME_LENGTH,
            max = Validation.MAX_NAME_LENGTH,
            message = Validation.NAME_MESSAGE)
    private String name;
    @Length(min = Validation.MIN_SURNAME_LENGTH,
            max = Validation.MAX_SURNAME_LENGTH,
            message = Validation.SURNAME_MESSAGE)
    private String surname;
    @Nullable
    @Length(min = Validation.MIN_MIDDLE_NAME_LENGTH,
            max = Validation.MAX_MIDDLE_NAME_LENGTH,
            message = Validation.MIDDLE_NAME_MESSAGE)
    private String middleName;
    @Nullable
    @Length() // todo complete length
    private String phoneNumber;
}
