package com.theflash.hpw.bookshare;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import at.markushi.ui.CircleButton;

public class profile extends AppCompatActivity implements View.OnClickListener {
    ImageView m;
    String gender;
EditText nam;
static int books=0;
    DatePickerDialog dialog;
    static String s;
    final static int Gallery_pick=1;
    ImageView dob;
    String generatedFilePath;
    CircleButton circleButton;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref;
    Uri filepath;
    CatLoadingView mView;
    String mydob;
    String name;
    Spinner spinner;
    String st;
    ImageView mm;
    Spinner s1;
    String Ctry;
    String PhNumber;
    String gen;
    String cty;
    String io;
    String state;
    String DateOfBirth;
    String profilename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        m = (ImageView) findViewById(R.id.imageView3);
        mm = (ImageView) findViewById(R.id.imageView4);
        mm.setOnClickListener(this);
        m.setOnClickListener(this);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        spinner = (Spinner) findViewById(R.id.spinner);
        nam=(EditText)findViewById(R.id.editText3) ;
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, indiastate.statenames));
        s1 = (Spinner) findViewById(R.id.spinner2);
        s1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, indiastate.cityname));
        dob = (ImageView) findViewById(R.id.imageView6);

        dialog = new DatePickerDialog(profile.this, new DatePickerDialog.OnDateSetListener() {
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


        circleButton = (CircleButton) findViewById(R.id.c);
        circleButton.setOnClickListener(this);
        final RadioGroup radio = (RadioGroup) findViewById(R.id.radioGroup1);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = radio.findViewById(checkedId);
                int index = radio.indexOfChild(radioButton);
                if (index == 0) {
                    gender = "male";

                } else if (index == 1) {
                    gender = "female";

                }

            }
        });
        try {
            Follow ff= new Follow("00","00","00");
            DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference();
            databaseReference2.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Follow").setValue(ff);
        }catch (Exception e)
        {}

    }

    @Override
    public void onClick(View v) {
        if(v==m)
        {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, Gallery_pick);
        }
        if(v==mm)
        {

 Intent intent = new Intent(profile.this,addbook.class);
 startActivity(intent);
        }

        if (v == circleButton) {
          name=nam.getText().toString().trim();

            EditText editText1 = (EditText) findViewById(R.id.editText4);
            String bio = editText1.getText().toString();
            st = indiastate.statenames[spinner.getSelectedItemPosition()].trim().toUpperCase();
            if (TextUtils.isEmpty(bio)) {
                editText1.setError("Bio Required");
                editText1.requestFocus();
            }
            else if(books==0)
            {
                Snackbar.make(findViewById(android.R.id.content), "Add 1 Book Minimum ", Snackbar.LENGTH_SHORT).show();
            }
            else if (mydob == null) {
                Snackbar.make(findViewById(android.R.id.content), "Choose Your Date Of Birth", Snackbar.LENGTH_SHORT).show();
            } else if (gender == null) {
                Snackbar.make(findViewById(android.R.id.content), "Choose Your Gender", Snackbar.LENGTH_SHORT).show();
            } else if (name==null) {
                nam.setError("Enter Username");
                nam.requestFocus();
                Snackbar.make(findViewById(android.R.id.content), "Please Enter Username", Snackbar.LENGTH_SHORT).show();
            }
            else {



                SharedPreferences setting1 = getApplicationContext().getSharedPreferences("data", 0);
                Ctry=setting1.getString("country","");
                 PhNumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString();
               gen=gender;
                cty=indiastate.cityname[s1.getSelectedItemPosition()].trim().toUpperCase();
               io=bio;
                 state=st;
                 DateOfBirth=mydob;
                 profilename=name;

                SharedPreferences setting2 = getApplicationContext().getSharedPreferences("data", 0);
                SharedPreferences.Editor editor2 = setting2.edit();
                editor2.putString("state",state);
                editor2.putString("city",cty);
                editor2.putString("profile name",profilename);
                editor2.putString("phone number",PhNumber);
                editor2.putString("gender",gen);
                editor2.putString("DOB",DateOfBirth);
                editor2.putString("image","avatar");
                editor2.apply();


                if(filepath!=null)
                {
                    mView = new CatLoadingView();
                    mView.setText("   Verifying");
                    mView.setCancelable(false);
                    mView.setCanceledOnTouchOutside(false);
                    showDialog();

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
                                SharedPreferences setting2 = getApplicationContext().getSharedPreferences("data", 0);
                                SharedPreferences.Editor editor2 = setting2.edit();
                                editor2.putString("url",generatedFilePath);
                                uploadavatar(generatedFilePath);


                            } else {

                            }
                        }
                    });

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

    private void uploadavatar(String generatedFilePath) {

        regain p= new regain(gen,DateOfBirth,cty,state,profilename,generatedFilePath,io,Ctry);

        String path2= PhNumber;



        try {

            timeline t3= new timeline("Hey," +profilename+ "\nAm Your Personal Assistance");
            DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference();
            databaseReference3.child(path2+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t3);

       timeline t= new timeline("I Have Created Your Profile \n You Can Check It Out Anytime!!");
            DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference();
            databaseReference2.child(path2+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t);

            timeline t1= new timeline("Good books improve our standard of living and tone up our intellectual taste they make our outlook broad-Team BookShare");
            DatabaseReference databaseReference21= FirebaseDatabase.getInstance().getReference();
            databaseReference21.child(path2+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t1);

        }catch (Exception e)
        {}


        try{

            DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference();
            databaseReference2.child(path2).setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {



                    mView.dismiss();
                    Intent intent= new Intent(profile.this,chat.class);
                    startActivity(intent);
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

    public void showDialog() {
        mView.show(getSupportFragmentManager(), "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_pick&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            filepath=data.getData();
        }
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
            m.setImageBitmap(bitmap);


        }catch (Exception e){}
    }
}