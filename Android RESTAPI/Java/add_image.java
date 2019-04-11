package com.example.chenge.app_rest;


import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chenge.app_rest.model.FileInfo;
import com.example.chenge.app_rest.remote.APIUtils;
import com.example.chenge.app_rest.remote.FileService;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class add_image extends Fragment {


    public add_image() {
        // Required empty public constructor
    }
    private static final int RESULT_UPLOAD_IMAGE = 1;
    ImageView imageToUpload;
    private Button uploadButton;
    Uri selectedImage;

    FileService fileService;
    String imagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_image, container, false);
        uploadButton = (Button) view.findViewById(R.id.btn_upload_image);
        imageToUpload = view.findViewById(R.id.imageToUpload);
        fileService = APIUtils.getFileService();



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadButton.getText().toString().equals("Upload Image")){
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, RESULT_UPLOAD_IMAGE);

                }else{
                    File file = new File(imagePath);
                    RequestBody requestbody = RequestBody.create(MediaType.parse("multipart/form"),file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(),requestbody);

                    Call<FileInfo> call = fileService.upload(body);
                    call.enqueue(new Callback<FileInfo>() {
                        @Override
                        public void onResponse(Call<FileInfo> call, Response<FileInfo> response) {
                            if(response.isSuccessful()){
                                FileInfo json_reponse = response.body();
                                if(json_reponse.getStatus().equals("success")){
                                    Toast.makeText(getActivity(),"Image Uploaded successfully",Toast.LENGTH_SHORT).show();
                                    getActivity().setTitle("Menu");
                                    menu_images menu  = new menu_images();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.fragment_container,menu).commit();

                                }else {
                                    Toast.makeText(getActivity(),"Something went worng. Please try later",Toast.LENGTH_SHORT).show();
                                }


                            }else{
                                Toast.makeText(getActivity(),"Something went worng. Please try later",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FileInfo> call, Throwable t) {
                            Toast.makeText(getActivity(),"Something went worng. Please try later",Toast.LENGTH_SHORT).show();
                           // Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();;
                        }
                    });

                }


            }
        });
        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RESULT_UPLOAD_IMAGE && resultCode== RESULT_OK && data != null){
            selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
            if(imageToUpload.getDrawable() != null){

                uploadButton.setText("Send");
                imagePath = getRealPathURI(selectedImage);

            }

        }
    }

    private String getRealPathURI(Uri uri){
        String [] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(),uri,projection,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
    }

    public static class Json_image_response {
        private String data;
        private String message;
        private String status;

        public String getStatus() {
            return status;
        }
    }
}
