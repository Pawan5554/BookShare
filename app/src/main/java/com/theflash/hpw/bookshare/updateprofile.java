package com.theflash.hpw.bookshare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertTheme;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.interfaces.AlertActionListener;
import com.irozon.alertview.objects.AlertAction;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.picasso.Picasso;

import at.markushi.ui.CircleButton;

import static com.theflash.hpw.bookshare.profile.Gallery_pick;

public class updateprofile extends AppCompatActivity implements View.OnClickListener {
String phonenumber;
String bio;
String imageurl;
String name;
ImageView pic;
EditText username;
    EditText bioo;
    ImageView dob;
    CircleButton circleButton;
    String mydob;
   DatePickerDialog dialog;
    String gen;
    String cty;
    String state;
    String Ctry;
    String generatedFilePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref;
    CatLoadingView mView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);

        SharedPreferences setting1 = getApplicationContext().getSharedPreferences("data", 0);
        mydob=setting1.getString("DOB","");
         gen=setting1.getString("gender","");
        cty=setting1.getString("city","");
         state=setting1.getString("state","");
         url=setting1.getString("url","");
       Ctry=setting1.getString("country","");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        username=findViewById(R.id.editText3);
        circleButton = (CircleButton) findViewById(R.id.c);
        circleButton.setOnClickListener(this);
        bioo=findViewById(R.id.editText4);
        pic=findViewById(R.id.imageView3);
        phonenumber= getIntent().getExtras().getString("number");
        bio= getIntent().getExtras().getString("bio");
        name= getIntent().getExtras().getString("name");
        imageurl= getIntent().getExtras().getString("image");
        pic.setOnClickListener(this);
        dob = (ImageView) findViewById(R.id.imageView6);
        dialog = new DatePickerDialog(updateprofile.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String months = String.valueOf(month + 1);
                String days = String.valueOf(dayOfMonth);
                String years = String.valueOf(year);
                mydob = days + "/" + months + "/" + years;
                Snackbar.make(findViewById(android.R.id.content), "Your DOB :-> " + mydob, Snackbar.LENGTH_SHORT).show();
            }
        }, 1990, 1, 1);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.show();
                } catch (Exception e) {
                    Snackbar.make(findViewById(android.R.id.content), e.getMessage().toString(), Snackbar.LENGTH_SHORT).show();

                }

            }
        });

        bioo.setText(bio);
        username.setText(name);
        Picasso.with(getApplicationContext())
                .load(imageurl)
                .into(pic, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {



                    }

                    @Override
                    public void onError() {

                    }
                });


    }

    @Override
    public void onClick(View v) {
        if(v==pic)
        {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, Gallery_pick);
        }
        if(v==circleButton)
        {
            name=username.getText().toString().trim();
            bio=bioo.getText().toString().trim();
            if (TextUtils.isEmpty(bio)) {
                bioo.setError("Bio Required");
               bioo.requestFocus();
            }
            else if (mydob == null) {
                Snackbar.make(findViewById(android.R.id.content), "Choose Your Date Of Birth", Snackbar.LENGTH_SHORT).show();
            }
            else if (name==null) {
                username.setError("Enter Username");
                username.requestFocus();
                Snackbar.make(findViewById(android.R.id.content), "Please Enter Username", Snackbar.LENGTH_SHORT).show();
            }
            else {
                SharedPreferences setting2 = getApplicationContext().getSharedPreferences("data", 0);
                SharedPreferences.Editor editor2 = setting2.edit();
                editor2.putString("DOB",mydob);
                editor2.putString("profile name",name);
                editor2.putString("phone number",phonenumber);
                editor2.apply();

                if(filepath!=null|url!=null)
                {
                    mView = new CatLoadingView();
                    mView.setText("   Updating");
                    mView.setCancelable(false);
                    mView.setCanceledOnTouchOutside(false);
                    showDialog();

                  if(filepath!=null)
                  {
                      ref = storageReference.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString());
                      UploadTask uploadTask= ref.putFile(filepath);
                      Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                          @Override
                          public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                              if (!task.isSuccessful()) {
                                  throw task.getException();
                              }

                              // Continue with the task to get the download URL
                              return ref.getDownloadUrl();
                          }
                      }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                          @Override
                          public void onComplete(@NonNull Task<Uri> task) {
                              if (task.isSuccessful()) {
                                  Uri downloadUri = task.getResult();
                                  generatedFilePath=downloadUri.toString();

                                  uploadavatar(generatedFilePath);


                              } else {
                                  // Handle failures
                                  // ...
                              }
                          }
                      });
                  }
                  else {

                      uploadavatar(url);
                  }

                }
                else
                {

                    AlertView alert = new AlertView("Picture Required", "Please Select You Profile Picture", AlertStyle.DIALOG);
                    alert.addAction(new AlertAction("Upload", AlertActionStyle.POSITIVE, new AlertActionListener() {
                        @Override
                        public void onActionClick(AlertAction alertAction) {
                            Intent galleryIntent = new Intent();
                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                            galleryIntent.setType("image/*");
                            startActivityForResult(galleryIntent, Gallery_pick);
                        }
                    }));
                    alert.setTheme(AlertTheme.LIGHT);
                    alert.show(this);

                }




            }

        }


    }





    Uri filepath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_pick&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            filepath=data.getData();
        }
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
            pic.setImageBitmap(bitmap);


        }catch (Exception e){}
    }

    public void showDialog() {
        mView.show(getSupportFragmentManager(), "");
    }

    private void uploadavatar(String generatedFilePath) {

String DateOfBirth=mydob;
String profilename=name;
String io =bio;
        regain p= new regain(gen,DateOfBirth,cty,state,profilename,generatedFilePath,io,Ctry);

        String path2= phonenumber;



        try {

            timeline t3= new timeline("You Profile Has Been Updated");
            DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference();
            databaseReference3.child(path2+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t3);

        }catch (Exception e)
        {}


        try{

            DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference();
            databaseReference2.child(path2).setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mView.dismiss();
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mView.dismiss();
                            Snackbar.make(findViewById(android.R.id.content),"Something Went Wrong! Try Again",Snackbar.LENGTH_SHORT).show();
                        }
                    });



        }catch (Exception e){}

    }
}
