package com.douglas.truckflow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.douglas.truckflow.authentication.Login;
import com.douglas.truckflow.databinding.ActivityImageUserBinding;
import com.douglas.truckflow.home.Home;
import com.douglas.truckflow.registration.TruckerRegistration;
import com.douglas.truckflow.registration.TruckerRegistrationTwo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class imageUser extends AppCompatActivity {

    ActivityImageUserBinding binding;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    private String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectImage();


            }
        });



        binding.uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                uploadImage();

            }
        });

    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();

        String userEmail = "";


        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("USER_EMAIL")) {
            userEmail = getIntent().getStringExtra("USER_EMAIL");
            role = getIntent().getStringExtra("Role"); // Add this line to get the "Role" extra

            Log.d("imageUser", "Received email: " + userEmail);
            Log.d("imageUser", "Received role: " + role);


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = formatter.format(now);
            storageReference = FirebaseStorage.getInstance().getReference("images/"+userEmail);
        }




        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        binding.firebaseimg.setImageURI(null);
                        Toast.makeText(imageUser.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

//                        Intent i = new Intent(imageUser.this, TruckerRegistration.class);
//
//
//                        startActivity(i);

                        Intent i;
                        if ("shipper".equals(role)) {
                            // Load Home.class for shipper
                            i = new Intent(imageUser.this, Login.class);
                        } else if ("trucker".equals(role)) {
                            // Load TruckerRegistrationTwo.class for trucker
                            i = new Intent(imageUser.this, TruckerRegistration.class);
                        } else {
                            // Handle other roles or fallback to a default activity
                            i = new Intent(imageUser.this, Login.class);
                        }

                        //i.putExtra("USER_EMAIL", userEmail);


                        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("USER_EMAIL")) {
                            String userEmail = getIntent().getStringExtra("USER_EMAIL");
                            i.putExtra("USER_EMAIL", userEmail);
                        }
                        startActivity(i);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(imageUser.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


                    }
                });

    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){

            imageUri = data.getData();
            binding.firebaseimg.setImageURI(imageUri);


        }
    }

}