package com.theflash.hpw.bookshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertTheme;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.interfaces.AlertActionListener;
import com.irozon.alertview.objects.AlertAction;

import at.markushi.ui.CircleButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public EditText phon;
    Spinner spinner;
    String cd;
    CircleButton circleButton;
    String country;
    String  c;
   FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phon= (EditText)findViewById(R.id.editText);
        TextView contactus= (TextView)findViewById(R.id.textView);
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content),"Email us on riveshmaurya350@gmail.com",Snackbar.LENGTH_SHORT).show();
            }
        });
        circleButton=(CircleButton)findViewById(R.id.c);
        spinner =(Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,countrydata.countrynames));
            firebaseAuth= FirebaseAuth.getInstance();

            if(firebaseAuth.getCurrentUser()!=null)
            {

                SharedPreferences setting1 = getApplicationContext().getSharedPreferences("data", 0);
                c=  setting1.getString("state","");



                if(c.equalsIgnoreCase(""))
                {
                    Intent intent = new Intent(MainActivity.this, profile.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this, chat.class);
                    startActivity(intent);
                    finish();
                }
                }
       else
        {
            AlertView alert = new AlertView("Important Notice", "By Registering in the app you are accepting all terms and conditions", AlertStyle.DIALOG);
            alert.addAction(new AlertAction("Accept", AlertActionStyle.POSITIVE, new AlertActionListener() {
                @Override
                public void onActionClick(AlertAction alertAction) {
                    Snackbar.make(findViewById(android.R.id.content),"Thank You",Snackbar.LENGTH_SHORT).show();
                }
            }));
            alert.setTheme(AlertTheme.LIGHT);
            alert.show(this);


        }
        circleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==circleButton)
        {
            if (spinner.getSelectedItem().toString().trim().equals("")) {
                Snackbar.make(findViewById(android.R.id.content),"Please Select Your Country",Snackbar.LENGTH_SHORT).show();

            }
            else {

                cd = countrydata.countryareacode[spinner.getSelectedItemPosition()+1].trim();
                country=countrydata.countrynames[spinner.getSelectedItemPosition()].trim();
                SharedPreferences setting2 = getApplicationContext().getSharedPreferences("data", 0);
                SharedPreferences.Editor editor2 = setting2.edit();
                editor2.putString("country",country);
                editor2.apply();
                String phone = phon.getText().toString();
                if (phone.isEmpty()) {
                    phon.setError("Phone Number Required");
                    phon.requestFocus();

                } else {
                    phone = cd + phone;
                    Intent intent = new Intent(MainActivity.this, main2.class);
                    intent.putExtra("number", phone);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent);
                    finish();
                }
            }


        }
    }
}
