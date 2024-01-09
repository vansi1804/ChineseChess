package com.data.dto.user;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChangePasswordRequestDTO {
      @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String phoneNumber;
}
