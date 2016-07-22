package com.demo.commons.tools;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Token解析和创建
 */
public class TokenUtil {
    private static byte[] POST_ENCRYPT_KEY;
    private static byte[] POST_ENCRYPT_IV;

    static {
        try {
            POST_ENCRYPT_KEY = "D7C6F71A12153EE5".getBytes("UTF-8");
            POST_ENCRYPT_IV = "55C930D827BDABFD".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("TokenUtil UnsupportedEncodingException", e);
        }
    }

    public static String createToken(int userId){
        if(userId <= 0){
            return StringUtils.EMPTY;
        }
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String token = Integer.toString(userId) + "|" + formatter.format(now);
        try {
            byte[] bytes = encrypt(token.getBytes("UTF-8"),POST_ENCRYPT_KEY,POST_ENCRYPT_IV);
            return StringUtil.toHex(bytes);
        }catch (Exception e){
            e.printStackTrace();
            return StringUtils.EMPTY;
        }

    }

    /**
     * AESC加密生成token字符串
     * @param bytes
     * @param key
     * @param iv
     * @return
     */
    private static byte[] encrypt(byte[] bytes, byte[] key, byte[] iv) {
        Cipher cipher = null;
        try {
            //创建密码器
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            //初始化
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            byte[] pbytes;
            if (bytes.length % 16 == 0) {
                pbytes = bytes;
            } else {
                pbytes = new byte[bytes.length + (16 - bytes.length % 16)];
                System.arraycopy(bytes, 0, pbytes, 0, bytes.length);
            }
            return cipher.doFinal(pbytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("encrypt NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("encrypt NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("encrypt InvalidKeyException", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("encrypt InvalidAlgorithmParameterException", e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("encrypt IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("encrypt BadPaddingException", e);
        }
    }

    /**
     * 解析token获取userid
     * @param token
     * @return
     */
    public static int parseToken(String token) {
        try {
            if (token == null || token.length() == 0) {
                return 0;
            }
            String value = null;
            byte[] bytes = StringUtil.fromHex(token);
            byte[] data = decrypt(bytes,POST_ENCRYPT_KEY,POST_ENCRYPT_IV);
            value = new String(data, "UTF-8");
            if (StringUtils.isBlank(value)) {
                return 0;
            }
            String[] user = value.trim().split("\\|");
            if (user.length == 2) {
                int userId = 0;
                userId = Integer.parseInt(user[0]);
                return userId;
            }else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 解密token
     * @param bytes
     * @param key
     * @param iv
     * @return
     */
    private static byte[] decrypt(byte[] bytes, byte[] key, byte[] iv) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            byte[] decrBuffer = cipher.doFinal(bytes);
            int blank = 0;
            for (int i = decrBuffer.length - 1; i >= 0; i--) {
                if (decrBuffer[i] == '\0') {
                    blank++;
                } else {
                    break;
                }
            }
            byte[] result = decrBuffer;
            if (blank > 0) {
                byte[] decrBytes = new byte[decrBuffer.length - blank];
                System.arraycopy(decrBuffer, 0, decrBytes, 0, decrBytes.length);
                result = decrBytes;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("decrypt NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("decrypt NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("decrypt InvalidKeyException", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("decrypt InvalidAlgorithmParameterException", e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("decrypt IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("decrypt BadPaddingException", e);
        }
    }
}
