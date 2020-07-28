package com.ethereum.util;


import java.security.MessageDigest;

public class MD5Utils {
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5加密
     *
     * @param origin
     * @param charsetName
     * @return
     */
    public static String MD5Encode(String origin, String charsetName) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(origin.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(origin.getBytes(charsetName)));
            }
            return resultString;
        } catch (Exception e) {
            return null;
        }
    }

    public static String MD5Encode(String origin) {
        return MD5Encode(origin, "utf-8");
    }


}
