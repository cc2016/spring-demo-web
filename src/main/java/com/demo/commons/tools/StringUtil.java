package com.demo.commons.tools;

/**
 * Created by chenchen on 14/12/29.
 */
public class StringUtil {
    /**
     * 转换成十六进制字符串
     * @param hexbyte
     * @return
     */
    public static String toHex(byte[] hexbyte) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < hexbyte.length; i++) {
            int v = hexbyte[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toLowerCase();
    }

    /**
     * 转化成字节流
     * @param hexString
     * @return
     */
    public static byte[] fromHex(String hexString) {
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        if (c >= '0' && c <= '9') {
            return (byte) (c - '0');
        } else {
            return (byte) (c - 'a' + 10);
        }
    }
}
