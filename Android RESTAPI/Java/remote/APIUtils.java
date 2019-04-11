package com.example.chenge.app_rest.remote;

import android.util.Base64;

import com.example.chenge.app_rest.Encrypt;
import com.example.chenge.app_rest.MainActivity;

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

import retrofit2.Retrofit;

public class APIUtils {

    private APIUtils(){

    }
    static Encrypt encrypt = new Encrypt();
    static String email_crypted = encrypt.Encrypt(MainActivity.email_str).replace('/','@');


    public  static final String API_URL = "http://10.0.2.2/"+email_crypted+"/";

    public  static FileService getFileService(){

        return RetrofitClient.getClient(API_URL).create(FileService.class);
    }
}

