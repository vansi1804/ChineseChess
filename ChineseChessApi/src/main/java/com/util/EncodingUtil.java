package com.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EncodingUtil {

    private static final String SECRET_WORDS = "VanSiMeo";
    private static final int OFFSET = 4; // index to insert SECRET_WORDS
    public static String getMD5(String data){
        new StringBuilder(data).insert(OFFSET, SECRET_WORDS);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;    
        }
        byte[] messageDigest = md.digest(data.getBytes());
        return convertByteToHex(messageDigest);
    }

    private static String convertByteToHex(byte[] data) {
        return IntStream.range(0, data.length)
                .mapToObj(i -> String.format("%02x", data[i] & 0xFF))
                .collect(Collectors.joining());
    }

}
