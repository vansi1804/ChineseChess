package com.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoding {

    private static final String SECRET_WORDS = "VanSiMeo";
    private static final int OFFSET = 4;
    public static String getMD5(String data) throws NoSuchAlgorithmException {
        new StringBuilder(data).insert(OFFSET, SECRET_WORDS);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(data.getBytes());
        return convertByteToHex(messageDigest);
    }

    private static String convertByteToHex(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
