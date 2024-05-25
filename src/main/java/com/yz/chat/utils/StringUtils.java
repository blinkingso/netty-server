package com.yz.chat.utils;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.trim().isEmpty();
    }
}
