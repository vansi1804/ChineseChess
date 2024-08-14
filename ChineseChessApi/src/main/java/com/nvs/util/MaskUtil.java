package com.nvs.util;

public class MaskUtil {

  public static String maskPhoneNumber(String phoneNumber) {
    if (phoneNumber == null || phoneNumber.length() < 4) {
      return phoneNumber; // Return as is if it's too short to mask
    }

    // Number of digits to leave unmasked at the end
    int unmaskedDigitsFromEnd = 4;
    int maskLength = phoneNumber.length() - unmaskedDigitsFromEnd;
    StringBuilder masked = new StringBuilder();

    for (int i = 0; i < phoneNumber.length(); i++) {
      if (i < maskLength) {
        masked.append('*');
      } else {
        masked.append(phoneNumber.charAt(i));
      }
    }

    return masked.toString();
  }

}
