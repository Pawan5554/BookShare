package com.theflash.hpw.bookshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.roger.catloadinglibrary.CatLoadingView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.markushi.ui.CircleButton;

import static com.theflash.hpw.bookshare.profile.books;

public class addbook extends AppCompatActivity implements View.OnClickListener {
ImageView profile;
    final static int Gallery_pick=1;
    Uri filepath;
    EditText nam;
    CatLoadingView mView;
    EditText wr;
    CircleButton circleButton;
    Spinner spinner;
    FirebaseStorage storage;
    StorageReference storageReference;
    String generatedFilePath;
    StorageReference ref;
    String type;
    String name;
    String writers;
    int bookss;
    int requested;
    int shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        spinner = (Spinner) findViewById(R.id.spinner2);
        wr=(EditText)findViewById(R.id.editText4);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        nam=(EditText)findViewById(R.id.editText3);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, indiastate.option));
        profile=(ImageView)findViewById(R.id.imageView3);
    profile.setOnClickListener(this);
        circleButton = (CircleButton) findViewById(R.id.c);
        circleButton.setOnClickListener(this);
        DatabaseReference databaseReference19= FirebaseDatabase.getInstance().getReference();
        databaseReference19.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Follow").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setfollow(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void setfollow(DataSnapshot dataSnapshot) {

        if(dataSnapshot!=null)
        {
            Follow ff=dataSnapshot.getValue(Follow.class);
            bookss= Integer.parseInt(ff.bk);
            requested=Integer.parseInt(ff.req);
            shared=Integer.parseInt(ff.sh);
        }
    }
    @Override
    public void onClick(View v) {

        if(v==profile)
        {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, Gallery_pick);
        }
        if(v==circleButton)
        {
 type=indiastate.option[spinner.getSelectedItemPosition()].trim().toUpperCase();
 name=nam.getText().toString().trim();
 writers=wr.getText().toString().trim();
 if(name==null)
{
    Snackbar.make(findViewById(android.R.id.content), "Enter Item Name", Snackbar.LENGTH_SHORT).show();
    nam.requestFocus();
    nam.setError("Enter Item Name");
}

 else if(writers==null)
    {
        Snackbar.make(findViewById(android.R.id.content), "Enter Writer's Name", Snackbar.LENGTH_SHORT).show();
        wr.requestFocus();
        wr.setError("Enter Writer's Name");
    }
else
{
    mView = new CatLoadingView();
    mView.setText("    Uploading");
    mView.setCancelable(false);
    mView.setCanceledOnTouchOutside(false);
    showDialog();
    if(filepath!=null)
    {
        ref = storageReference.child("Books/" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString()+(int)(Math.random()*1000));
        UploadTask uploadTask= ref.putFile(filepath);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
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
                }
            }
        });
    }
    else
    {
        mView.dismiss();
        Snackbar.make(findViewById(android.R.id.content), "File Not Found", Snackbar.LENGTH_SHORT).show();
    }

}
        }
    }
    Author pp;
    private void uploadavatar(String generatedFilePath) {
		
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Author p= new Author(name,generatedFilePath,writers,type,date,FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
      pp= new Author(name,generatedFilePath,writers,type,date,FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        final String path2= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"books";
        try{

            DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference();
            databaseReference2.child(path2).child(""+(int)(Math.random()*1000)).setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
             DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference();
          databaseReference3.child("Recently Added Books").child(path2+(int)(Math.random()*1000)).setValue(pp);
                    books=1;
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
            try {
                bookss++;
                Follow ff= new Follow(""+bookss,""+requested,""+shared);
                DatabaseReference databaseReference12= FirebaseDatabase.getInstance().getReference();
                databaseReference12.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Follow").setValue(ff);
            }catch (Exception e)
            {}
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
            profile.setImageBitmap(bitmap);
        }catch (Exception e){}
    }
}
