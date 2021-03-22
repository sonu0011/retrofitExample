package com.sonu.retrofitexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private APICalls calls;
    private static final String TAG = "MainActivity";
    private ImageView imageView;
    private EditText editText;
    private Button button;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        editText = findViewById(R.id.description);
        button = findViewById(R.id.upload_btn);
        imageView.setOnClickListener(v -> {
            checkPermission();
        });
        button.setOnClickListener(v -> {
            uploadImage();
        });
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("userId", "1");
//        parameters.put("_sort", "id");
//        parameters.put("_order", "desc");
//
//        calls = RetrofitClient.getInstance().getApi();
//        calls.getCoomentsofSpecificUser(parameters)
//                .enqueue(new Callback<List<Post>>() {
//                    @Override
//                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                        Log.d(TAG, "onResponse: specfic post"+response.body().get(0).toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Post>> call, Throwable t) {
//                        Log.d(TAG, "onFailure: "+t.getMessage());
//                    }
//                });
//        calls.createPost(45, "title", "body")
//                .enqueue(new Callback<Post>() {
//                    @Override
//                    public void onResponse(Call<Post> call, Response<Post> response) {
//
//                        Log.d(TAG, "onResponse: " + response.body().toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<Post> call, Throwable t) {
//
//                        Log.d(TAG, "onFailure: " + t.getMessage());
//                    }
//                });
    }

    private void uploadImage() {

        if (imageUri == null) {
            Toast.makeText(this, "Please select an imge", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadImageAndDescToServer();
    }

    private void uploadImageAndDescToServer() {
        Log.d(TAG, "uploadImageAndDescToServer: "+imageUri.getPath());
        RequestBody desc = RequestBody.create(MultipartBody.FORM, editText.getText().toString());
        File file = new File(imageUri.getPath());

        RequestBody filePart = RequestBody.create(
                MediaType.parse(getContentResolver().getType(imageUri)),
                file
        );
        MultipartBody.Part filePart1 = MultipartBody.Part.createFormData("photo", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

//
//        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("photo",
//                file.getName(), filePart);
        calls = RetrofitClient.getInstance().getApi();
        calls.upload(desc, filePart1)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse:  uploade");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    123
            );
        } else {

            chooseImage();

        }
    }

    public void getPost(View view) {


        calls.getAllPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d(TAG, "onResponse:  thread" + Thread.currentThread().getName());
                Log.d(TAG, "onResponse: " + response.body().get(0).getBody());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        Log.d(TAG, "onCreate: after calls");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 123) {
            if (grantResults.length > 0) {
                //choose image
                chooseImage();
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        123
                );
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK).setType("image/*");
        startActivityForResult(intent, 456);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 456) {

            if (resultCode == RESULT_OK) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }
}