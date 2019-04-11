package com.example.chenge.app_rest;

import android.util.Base64;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Decrypt {

    public String decrypt(String todecrypt){
        byte[] by_message = todecrypt.getBytes();
        String todecrypt_decrypted="";
        String message = "";

        try {
            KeyFactory keyFactory = null;
            byte[] str_key = Base64.decode("MIICXAIBAAKBgQCFjZH76hgnqI5d0GmZGY5M7yIaYBZG6j7TNJl24BPORV2zC9pO\n" +
                    "DoLyAbf25b3eXtui1HZZe4oLJziv3mlE5+dvVcWSe/pcBXFi6ZS/t0/PI2pCMVv7\n" +
                    "I1R0oECMYlqYQklrLWwvS+eBoZZOe8Trq7kBDFCAc1+Ic+vcgdW4mawlvwIDAQAB\n" +
                    "AoGALKk3FGMoSLrZQQ4dAhHFwHyHjwJq8LQM5lxEpbgxZ11TBDkGe+vmZj+k4W/a\n" +
                    "mq5mDIG4QhxKAYjQMB/UXDKg9eKeuDx5XSwKORy40YJtSqY1lMwgtcKwGQ7Uxkqr\n" +
                    "FWlbZpDfA4fs15NuWtFLNNheJ1iFX3VDtJtIme+MMxvVv4ECQQC/dK/UUNbyi10x\n" +
                    "qsr5WS5Eu7EZlrNsB/qUapYJBSCMjS8DSfCIlX8EsoDfYzn5Wl+Trk2QaYjlGzeP\n" +
                    "khYQRCivAkEAspOqyFmmRzCIjrpFiVHSH2RJldxW9Hw7Qwq/RJ5+/mGja88eZU6+\n" +
                    "0DeBjGzWDYoiTetqUSVCziiQesQwHA/38QJAQJo9ImVMwnboMXQyHUVMaYDz13CU\n" +
                    "hmWC1kXI7q4+N28EaBWxBkV7oLgi6D3xOASYr5pnLc2OldBDRTzEGSUGnQJAbEEq\n" +
                    "adQ3AbcBQYzYNJueRpt0JF3zdLiO8GBmfMGceLdV6zge1Ak9kVnkte0Qghq4GwZY\n" +
                    "aCKvceyTUWj3RTvE8QJBAJxXjlRGUTEuwEGovZison+Th4RuROuECiV9rf7MMj6a\n" +
                    "olisWCbR5r+DQKT5M4Wq+Yu9CgPxIRuAK42jKRKahko=",Base64.DEFAULT);
            keyFactory = KeyFactory.getInstance("RSA");
            KeySpec ks = new PKCS8EncodedKeySpec(str_key);
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(ks);
            Cipher dec = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            dec.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] message_byte = Base64.decode(by_message,Base64.DEFAULT);
            message_byte = dec.doFinal(message_byte);
            message = Base64.encodeToString(message_byte,Base64.DEFAULT);
            message_byte = Base64.decode(message,Base64.DEFAULT);
            todecrypt_decrypted  = new String(message_byte,"UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return todecrypt_decrypted;
    }
}
