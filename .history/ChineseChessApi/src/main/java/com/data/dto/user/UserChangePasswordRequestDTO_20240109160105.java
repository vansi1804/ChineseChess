package com.data.dto.user;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserChangePasswordRequestDTO {
      @NotBlank(message = ErrorMessage.BLANK_DATA)
  private String phoneNumber;
}
