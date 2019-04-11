package com.example.chenge.app_rest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity {

    public static String email_str;
    public MainActivity(){
        this.email_str="";


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = (Button) findViewById(R.id.login_btn);

        final EditText password = (EditText) findViewById(R.id.password_text);
        final EditText email = (EditText) findViewById(R.id.email_text);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();

                String password_value = password.getText().toString();
                final String email_value = email.getText().toString();
                String e_s = new String("email");
                String p_s = new String("password");
                String e_v = new String(email_value);
                String p_v = new String(password_value);
                String s = e_s;
                byte[] bytes = s.getBytes();
                s = Base64.encodeToString(bytes, Base64.DEFAULT);
                Encrypt encrypt = new Encrypt();
                String s2 = encrypt.Encrypt(e_v);
                params.put(s, s2);
                s = p_s;
                bytes = s.getBytes();
                s = Base64.encodeToString(bytes, Base64.DEFAULT);
                s2 = encrypt.Encrypt(p_v);
                params.put(s, s2);



                String url = "http://10.0.2.2/login";
                final JSONObject json_params = new JSONObject(params);

                StringRequest objReq = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Gson gson = new Gson();
                                String response2 = response.toString();
                                Json_loged json = gson.fromJson(response2, Json_loged.class);
                                Decrypt decrypt = new Decrypt();
                                String message_response = decrypt.decrypt(json.message);

                                if (message_response.equals("Loged")) {
                                    MainActivity.email_str = email_value;
                                    OpenMenuActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Email or Password Incorrect", Toast.LENGTH_SHORT).show();
                                }


                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),"Something went wrong. Pleasy try later",Toast.LENGTH_LONG).show();

                            }
                        }
                ) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }


                public byte[] getBody () throws AuthFailureError {
                    String json = json_params.toString();
                    return json.getBytes();
                }

                };

                requestQueue.add(objReq);

            }
        });

    }

    public void OpenMenuActivity(){
        startActivity(new Intent(MainActivity.this,main_menu.class));
    }






}
