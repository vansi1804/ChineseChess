package com.config.validation.impl;

import java.util.Collections;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.config.exception.InvalidExceptionCustomize;
import com.config.validation.Validator;
import com.data.dto.match.MatchOthersInfoDTO;

@Component
public class MatchOthersInfoCreationValidator implements ConstraintValidator<Validator, MatchOthersInfoDTO> {

    @Override
    public boolean isValid(MatchOthersInfoDTO matchOthersInfoDTO, ConstraintValidatorContext context) {
        if (matchOthersInfoDTO == null
                || (matchOthersInfoDTO.getTime() == null
                        && matchOthersInfoDTO.getMovingTime() == null
                        && matchOthersInfoDTO.getCumulativeTime() == null
                        && matchOthersInfoDTO.getEloBet() == null)
                || (matchOthersInfoDTO.getTime() != null
                        && matchOthersInfoDTO.getMovingTime() != null
                        && matchOthersInfoDTO.getCumulativeTime() != null
                        && matchOthersInfoDTO.getEloBet() != null)) {

            return true;
        } else {
            throw new InvalidExceptionCustomize(
                    Collections.singletonMap("message", "matchOthersInfoDTO is null or matchOthersInfoDTO's fields are null or not null all"));
        }
    }

}
