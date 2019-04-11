package com.example.chenge.app_rest;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encrypt {

    public String Encrypt(String toencrypt){
        String toencrypt_encrypted ="";
        try {

            byte[] str_key = Base64.decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFjZH76hgnqI5d0GmZGY5M7yIa\n" +
                    "YBZG6j7TNJl24BPORV2zC9pODoLyAbf25b3eXtui1HZZe4oLJziv3mlE5+dvVcWS\n" +
                    "e/pcBXFi6ZS/t0/PI2pCMVv7I1R0oECMYlqYQklrLWwvS+eBoZZOe8Trq7kBDFCA\n" +
                    "c1+Ic+vcgdW4mawlvwIDAQAB",Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(str_key);
            PublicKey publicKey = (PublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);

            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encrypted = c.doFinal(toencrypt.getBytes());
            toencrypt_encrypted = Base64.encodeToString(encrypted, Base64.DEFAULT);



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }  catch (IllegalBlockSizeException e) { ;
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return toencrypt_encrypted;
    }
}
