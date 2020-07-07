package com.yiyun.rmj.utils;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by 80999 on 2018/11/28.
 */
public class DESHelper {

    private final static String DES = "DES";
    private final static String KEY = "s12lzcq7c1tts5xpi02vhb4atwuyfh14";

    public DESHelper() {
    }

    //调用此方法加密
    public static String encrypt(String pliantext) {
        try {
            return encodeBase64(encryptDES(pliantext, KEY));
        } catch (Exception e) {
            e.printStackTrace();
            return pliantext;
        }
    }

    public static String encrypt(String pliantext, String key) throws Exception {
        return encodeBase64(encryptDES(pliantext, KEY));
    }

    //调用此方法解密
    public static String decrypt(String ciphertext) throws Exception {
        Log.e("Pan","ciphertext="+ciphertext);
        byte[] bytes = ciphertext.getBytes();
        byte[] decodebyteStr = decodeBase64(bytes);
        String str = decryptDES(decodebyteStr, KEY);
        return str;
    }

    public static String decrypt(String ciphertext, String key) throws Exception {
        return decryptDES(decodeBase64(ciphertext.getBytes()), KEY);
    }

    /**
     * base64编码
     *
     * @param binaryData
     * @return
     * @throws Exception
     */
    private static String encodeBase64(byte[] binaryData) throws Exception {
        try {
            return Base64.encodeToString(binaryData, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("BASE64编码失败!");
        }
    }

    /**
     * Base64解码
     *
     * @param binaryData
     * @return
     */
    private static byte[] decodeBase64(byte[] binaryData) {
        try {
            return Base64.decode(binaryData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("BASE64解码失败！");
        }
    }

    //加密
    public static byte[] encryptDES(String data, String key) {

        try {
            // 生成一个可信任的随机数源 ,  SHA1PRNG: 仅指定算法名称
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec deskey = new DESKeySpec(key.getBytes("UTF-8"));

            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = keyFactory.generateSecret(deskey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);
            //用密匙初始化Cipher对象,
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(data.getBytes("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("syqerro", e.getMessage());
        }
        return null;
    }

    //解密
    public static String decryptDES(byte[] data, String key) {

        try {
            // 算法要求有一个可信任的随机数源,  SHA1PRNG: 仅指定算法名称
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // 创建一个DESKeySpec对象
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            // 真正开始解密操作
            return new String(cipher.doFinal(data), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("syq", e.getMessage().toString());
        }
        return null;
    }

//   /**
//	*
//	* @Title: getKeyLength8
//	* @Description: (获取固定长度8的key)
//	* @param @param key
//	* @param @return    设定文件
//	* @return String    返回类型
//	* @throws
//	 */
//	public static String getKeyLength8(String key) {
//		key = key == null ? "" : key.trim();
//		int tt = key.length() % 64;
//
//		String temp = "";
//		for (int i = 0; i < 64 - tt; i++) {
//			temp += "D";
//		}
//		return key + temp;
//	}


}
