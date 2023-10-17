package com.theflash.hpw.bookshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.concurrent.TimeUnit;

import at.markushi.ui.CircleButton;

public class main2 extends AppCompatActivity  implements View.OnClickListener {
    TextView resend;

    long previous=0;
    public FirebaseAuth firebaseAuth;
    EditText editText;
    TextView ph;
    int signin=0;
    CircleButton circleButton;
    String phone;
    public ImageView ver;
    CatLoadingView mView;
    String codesent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth=FirebaseAuth.getInstance();
        resend=(TextView)findViewById(R.id.textView19);
        resend.setOnClickListener(this);
        circleButton=(CircleButton)findViewById(R.id.c);
        circleButton.setOnClickListener(this);
        phone=getIntent().getStringExtra("number");
        editText=(EditText)findViewById(R.id.editText2);
        ph=(TextView)findViewById(R.id.textView3);
        ph.setText(phone);

        sendVerificationCode();
    }

    @Override
    public void onClick(View v) {
        if(v==circleButton)
        {

            if(signin==0)
            {
                String cr=editText.getText().toString();
                if(cr.isEmpty()|cr.length()<6)
                {
                    editText.setError("Enter Valid Code");
                    editText.requestFocus();
                    return;
                }
                else {
                    verifycode(cr);
                }
            }
        }

        if(v==resend)
        {
            sendVerificationCode();
            Snackbar.make(findViewById(android.R.id.content),"Code Sent Wait 30 Sec To Send Again",Snackbar.LENGTH_SHORT).show();

        }
    }

    public void verifycode(String code)
    {
        mView = new CatLoadingView();
        mView.setText("    Verifying");
        mView.setCancelable(false);
        mView.setCanceledOnTouchOutside(false);
        showDialog();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent,code);
        signInWithCredential(credential);
    }
    public void showDialog() {
        mView.show(getSupportFragmentManager(), "");
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            try {

                                DatabaseReference dr= FirebaseDatabase.getInstance().getReference(phone);
                                dr.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        setnext(dataSnapshot);
                                        signin=1;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }catch (Exception e){}
                        }
                        else
                        {
                            Snackbar.make(findViewById(android.R.id.content),"Code Verification Failed",Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void setnext( DataSnapshot dataSnapshot) {
        regain user= dataSnapshot.getValue(regain.class);
        if(user!=null)
        {

            String g=user.Gender;
            if(g!=null)
            {
                SharedPreferences setting1 = getApplicationContext().getSharedPreferences("data", 0);
                String Ctry=setting1.getString("country","");
                String gen=user.Gender;
                String DateOfBirth=user.DOB;
                String state=user.state;
                String cty=user.city;
                String profilenumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                SharedPreferences setting2 = getApplicationContext().getSharedPreferences("data", 0);
                SharedPreferences.Editor editor2 = setting2.edit();
                editor2.putString("state",state);
                editor2.putString("city",cty);
                editor2.putString("profile name",profilenumber);
                editor2.putString("phone number",phone);
                editor2.putString("gender",gen);
                editor2.putString("DOB",DateOfBirth);
                editor2.putString("last","on");
                editor2.putString("image","Mountain");
                editor2.apply();
                mView.dismiss();
                start();

            }
            else {
                mView.dismiss();
                Intent intent = new Intent(main2.this, profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        }
        else
        {
            mView.dismiss();
            Intent intent = new Intent(main2.this, profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }




    }



    public void start()
    {
        Intent intent= new Intent(main2.this,chat.class);
        startActivity(intent);
        finish();
    }
    public void sendVerificationCode()
    {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code= phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                editText.setText(code);
                // add verify
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Snackbar.make(findViewById(android.R.id.content),e.getMessage(),Snackbar.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesent=s;
        }
    };

}
