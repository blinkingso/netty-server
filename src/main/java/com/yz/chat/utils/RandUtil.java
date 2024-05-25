package com.yz.chat.utils;

import java.util.Random;

public class RandUtil {
    private static final Random RANDOM = new Random();
    private static final char[] alphabet = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static String randString(int size) {
        final int mask = (2 << (int) Math.floor(Math.log(alphabet.length - 1) / Math.log(2))) - 1;
        final int step = (int) Math.ceil(1.6 * mask * size / alphabet.length);

        final StringBuilder idBuilder = new StringBuilder();
        while (true) {

            final byte[] bytes = new byte[step];
            RANDOM.nextBytes(bytes);

            for (int i = 0; i < step; i++) {

                final int alphabetIndex = bytes[i] & mask;

                if (alphabetIndex < alphabet.length) {
                    idBuilder.append(alphabet[alphabetIndex]);
                    if (idBuilder.length() == size) {
                        return idBuilder.toString();
                    }
                }

            }
        }
    }
}
