package com.common;

public class ValidationREGEX {
    public static final String EMAIL_REGEX 
    = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";

    public static final String PHONE_NUMBER_REGEX = "/(84[3|5|7|8|9])+([0-9]{8})\b/g";

}
