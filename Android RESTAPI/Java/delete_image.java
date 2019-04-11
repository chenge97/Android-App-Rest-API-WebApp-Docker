package com.example.chenge.app_rest;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class delete_image extends Fragment {


    public delete_image() {
        // Required empty public constructor
    }
    String email_crypted = "";
    List<String> i_data = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Encrypt encrypt = new Encrypt();
        email_crypted = encrypt.Encrypt(MainActivity.email_str);
        /*byte[] str_key = Base64.decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFjZH76hgnqI5d0GmZGY5M7yIa\n" +
                "YBZG6j7TNJl24BPORV2zC9pODoLyAbf25b3eXtui1HZZe4oLJziv3mlE5+dvVcWS\n" +
                "e/pcBXFi6ZS/t0/PI2pCMVv7I1R0oECMYlqYQklrLWwvS+eBoZZOe8Trq7kBDFCA\n" +
                "c1+Ic+vcgdW4mawlvwIDAQAB",Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(str_key);
        PublicKey publicKey = null;

        try {
            publicKey = (PublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = c.doFinal(MainActivity.email_str.getBytes());
            email_crypted = Base64.encodeToString(encrypted, Base64.DEFAULT);
            Toast.makeText(getActivity(),email_crypted,Toast.LENGTH_LONG).show();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
        e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }*/


        View view = inflater.inflate(R.layout.fragment_delete_image, container, false);
        Button delete_image = (Button) view.findViewById(R.id.btn_send_delete);
        final AutoCompleteTextView auto = view.findViewById(R.id.autoCompleteTextView);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://10.0.2.2/imglist?email="+email_crypted;


        JsonObjectRequest objReq = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject json = response;

                        JSONObject j_data= null;
                        Decrypt decrypt = new Decrypt();

                        try {
                        JSONArray jsonArray =json.getJSONArray("images");
                        /*
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

                            byte[] image_byte;
                            String image_name;
                            String image_resp;*/

                            String str_data;
                            String image_resp ="";
                            for(int i = 0; i<jsonArray.length();i++) {
                                j_data = jsonArray.getJSONObject(i);
                                str_data = j_data.getString("image");
                                /*image_byte = Base64.decode(str_data,Base64.DEFAULT);
                                image_byte = dec.doFinal(image_byte);
                                image_name = Base64.encodeToString(image_byte,Base64.DEFAULT);
                                image_byte = Base64.decode(image_name,Base64.DEFAULT);
                                image_resp  = new String(image_byte,"UTF-8");*/
                                image_resp = decrypt.decrypt(str_data);

                                Toast.makeText(getActivity(),image_resp,Toast.LENGTH_LONG).show();
                                i_data.add(image_resp);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } /*catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        }*/
                    }

                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"There was an error. Please try later.",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(objReq);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,i_data);
        auto.setAdapter(adapter);
        auto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                auto.setText(adapter.getItem(position));
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
                String result = auto.getText().toString();
                String image_name_crypted= encrypt.Encrypt(result);

                /*try {

                    byte[] str_key = Base64.decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFjZH76hgnqI5d0GmZGY5M7yIa\n" +
                            "YBZG6j7TNJl24BPORV2zC9pODoLyAbf25b3eXtui1HZZe4oLJziv3mlE5+dvVcWS\n" +
                            "e/pcBXFi6ZS/t0/PI2pCMVv7I1R0oECMYlqYQklrLWwvS+eBoZZOe8Trq7kBDFCA\n" +
                            "c1+Ic+vcgdW4mawlvwIDAQAB", Base64.DEFAULT);
                    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(str_key);
                    PublicKey publicKey = (PublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);

                    Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    c.init(Cipher.ENCRYPT_MODE, publicKey);
                    byte[] encrypted = c.doFinal(result.getBytes());
                    image_name_crypted = Base64.encodeToString(encrypted, Base64.DEFAULT);
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }*/
                String url_del ="http://10.0.2.2/delete?image="+image_name_crypted+"&email="+email_crypted;
                JsonObjectRequest objReq2 = new JsonObjectRequest(
                        Request.Method.GET,
                        url_del,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject json = response;
                                try {

                                    String status = json.getString("status");
                                    if(status.equals("success")){
                                        Toast.makeText(getActivity(),json.getString("message"),Toast.LENGTH_LONG).show();
                                        getActivity().setTitle("Menu");
                                        menu_images menu  = new menu_images();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.fragment_container,menu).commit();
                                    }else{
                                        Toast.makeText(getActivity(),json.getString("message"),Toast.LENGTH_LONG).show();
                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(),"There was an error. Please try later.",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue2.add(objReq2);

            }
        });


        return  view;
    }

}
