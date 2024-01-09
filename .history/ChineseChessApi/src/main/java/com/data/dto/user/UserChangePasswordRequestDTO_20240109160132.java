package com.data.dto.user;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserChangePasswordRequestDTO {
      @NotBlank(message = ErrorMessage.BLANK_DATA)
      private String phoneNumber;
  @NotBlank(message = ErrorMessage.BLANK_DATA)
  @Size(
    min = Validation.PASSWORD_SIZE_MIN,
    message = ErrorMessage.INVALID_PASSWORD_SIZE
  )
  private String oldPassword;
  @NotBlank(message = ErrorMessage.BLANK_DATA)
  @Size(
    min = Validation.PASSWORD_SIZE_MIN,
    message = ErrorMessage.INVALID_PASSWORD_SIZE
  )
  private String newPassword;
  @NotBlank(message = ErrorMessage.BLANK_DATA)
  @Size(
    min = Validation.PASSWORD_SIZE_MIN,
    message = ErrorMessage.INVALID_PASSWORD_SIZE
  )
  private String newPasswordConfirm;
}
