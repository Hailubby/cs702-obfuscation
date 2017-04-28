package com.jjhhh.dice;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Decryptor {

    private String[] keyHalves = {"yolhg+O/7klcs8je3rO7nw==", "uuUn7IjZoXoewYPppP/dzA=="};

    private String[] ivHalves = {"COpJW/I+CCW9RmZi3rgywA==", "YdMDFKMHUnHRPw9XiO1Vpw=="};

    public String decrypt(String encryptedString) {
        try {
            IvParameterSpec iv = new IvParameterSpec(getOriginal(0).getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(getOriginal(1).getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] decodedString = Base64.decode(encryptedString, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(decodedString);
            String decryptedString = new String(decryptedBytes);
            return decryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getOriginal(int mode) {
        byte[] half1;
        byte[] half2;
        if (mode == 0) {
            half1 = Base64.decode(ivHalves[0], 0);
            half2 = Base64.decode(ivHalves[1], 0);
        } else {
            half1 = Base64.decode(keyHalves[0], 0);
            half2 = Base64.decode(keyHalves[1], 0);
        }
        byte[] key = new byte[half1.length];
        for (int i = 0; i < half1.length; i++) {
            key[i] = (byte) (half1[i] ^ half2[i]);
        }
        return new String(key);
    }
}

