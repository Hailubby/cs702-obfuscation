package obfuscation;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by User on 20/04/2017.
 */
public class EncryptionHelper {
    private char[] symbol = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    //Encryption method
    public String encrypt(String key, String initVector, String string){
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte[] encrypted = cipher.doFinal(string.getBytes());
            String encodedString = new String(Base64.encodeBase64(encrypted));

            return encodedString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Testing decryption. Moved to android code, kept in this tool for reference and testing
    public String decrypt(String key, String initVector, String encryptedString) throws NoSuchPaddingException, NoSuchAlgorithmException {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

            byte[] decodedString = Base64.decodeBase64(encryptedString);

            byte[] decryptedBytes = cipher.doFinal(decodedString);

            String decryptedString = new String(decryptedBytes);

            return decryptedString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Generates random string of specified length
    public String generateString(int length){
        char[] c = new char[length];

        for (int i = 0; i < length; i++){
            int num = (int)Math.floor(Math.random() * 62);
            c[i] = symbol[num];
        }

        return new String(c);
    }

    //Generates xor halves
    public String[] generateKeyHalves(String key) {
        String[] keyHalves = new String[2];

        byte[] randomHalf = new byte[key.length()];
        byte[] matchingHalf = new byte[key.length()];
        byte[] keyBytes = key.getBytes();

        for (int i = 0; i < key.length(); i++) {
            randomHalf[i] = (byte)(Math.random()*256);
            matchingHalf[i] = (byte) (randomHalf[i] ^ keyBytes[i]);
        }

        keyHalves[0] = Base64.encodeBase64String(randomHalf);
        keyHalves[1] = Base64.encodeBase64String(matchingHalf);

        return keyHalves;
    }
}
