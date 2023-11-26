package org.sec.Crypt;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESUtil {
    public static void aesMain(String plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 生成随机密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();

        // 创建加密器
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // 加密消息
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

        System.out.println("Plaintext: " + plaintext);
        System.out.println("Ciphertext: " + new String(ciphertext));

        // 创建解密器
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // 解密消息
        byte[] decrypted = cipher.doFinal(ciphertext);

        System.out.println("Decrypted: " + new String(decrypted));
    }

    /** 获取AES key */
    public static String getAesKey() throws NoSuchAlgorithmException {
        // 生成随机密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
    /** 获取加密器 */
    public static Cipher getEncryptInstance(SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher;
    }
    /** 获取解密器 */
    public static Cipher getDecryptInstance(SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher;
    }

    /** 加密*/
    public static byte[] encrypt(Cipher cipher,String plaintext) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(plaintext.getBytes());
    }
    /** 解密*/
    public static String decrypt(Cipher cipher,String plaintext) throws IllegalBlockSizeException, BadPaddingException {
        byte[] decrypted = cipher.doFinal(plaintext.getBytes());
        return new String(decrypted);
    }
}
