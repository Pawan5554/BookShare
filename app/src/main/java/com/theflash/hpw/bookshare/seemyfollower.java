package com.theflash.hpw.bookshare;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class seemyfollower extends AppCompatActivity {
ArrayList<String> number;
    ArrayList<String> url;
    ArrayList<String> name;
    ListView l;
    SearchView search;
String txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seemyfollower);
        number= new ArrayList<>();
        url= new ArrayList<>();
        name= new ArrayList<>();

        search=findViewById(R.id.searchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txt=query;

               if(txt.equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString()))
               {
                   Snackbar.make(getCurrentFocus(), "You Can't View Your Profile", Snackbar.LENGTH_SHORT).show();
               }
               else if(("+91"+txt).equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString()))
               {
                   Snackbar.make(getCurrentFocus(), "You Can't View Your Profile", Snackbar.LENGTH_SHORT).show();


               }
               else
               {
                   final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference(txt);
                   databaseReference2.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                           child(dataSnapshot);
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });

        final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"MyFollowers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
getallchild(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"MyFollowers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    set(child);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void child(DataSnapshot dataSnapshot) {
        regain r= dataSnapshot.getValue(regain.class);

        if(r!=null)
        {
           if(r.imageurl!=null)
           {
               Intent intent= new Intent(getApplicationContext(),ViewProfile.class);
               intent.putExtra("number",txt);
               startActivity(intent);

           }
        }

        else
        {
txt="+91"+txt;
            final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference(txt);
            databaseReference3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    child2(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }

    }

    private void child2(DataSnapshot dataSnapshot) {

        regain r= dataSnapshot.getValue(regain.class);

        if(r!=null)
        {
            if(r.imageurl!=null)
            {
                Intent intent= new Intent(getApplicationContext(),ViewProfile.class);
                intent.putExtra("number",txt);
                startActivity(intent);

            }
        }
        else
        {
            Snackbar.make(getCurrentFocus(), "User Not Found", Snackbar.LENGTH_SHORT).show();
        }

    }

    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return name.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.seemyfollowerlist, null);
         final CircularImageView first=(CircularImageView)convertView.findViewById(R.id.view);
           final TextView one=(TextView)convertView.findViewById(R.id.textView20);
            final FButton request=(FButton)convertView.findViewById(R.id.button1);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent= new Intent(getApplicationContext(),ViewProfile.class);
                    intent.putExtra("number",number.get(position));
                    startActivity(intent);

                }
            });

            one.setText(name.get(position));
            Picasso.with(getApplicationContext())
                    .load(url.get(position))
                    .into(first, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {



                        }

                        @Override
                        public void onError() {

                        }
                    });
            return  convertView;
        }
    }

int count=0;
    private void getallchild(DataSnapshot dataSnapshot) {
        if(dataSnapshot!=null)
        {
            count=(int)dataSnapshot.getChildrenCount();
            if(count==0)
            {

                TextView textView=findViewById(R.id.textView18);
                textView.setText("  No Followers Founded");
            }
        }
    }
int flag=0;
    private void set(DataSnapshot child) {
        if(child!=null)
        {
          if(count>0)
          {
              flag++;
              if(flag<=count)
              {
                  timeline t=child.getValue(timeline.class);
                  number.add(t.info);
                  if(flag==count)
                  {
                      for(int a=0;a<count;a++)
                      {
                          final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
                          databaseReference2.child(number.get(a)).addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 setdata(dataSnapshot);

                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError databaseError) {

                              }
                          });



                      }


                  }
              }


          }





        }

    }
int dcount=0;
    private void setdata(DataSnapshot dataSnapshot) {
        dcount++;
        if(dataSnapshot!=null)
        {
            regain r=dataSnapshot.getValue(regain.class);
          url.add(r.imageurl);
           name.add(r.profilename);


           if(dcount==count)
           {
               l=(ListView)findViewById(R.id.list);
               final CustomAdapter adapter = new CustomAdapter();
               l.setAdapter(adapter);
               dcount=0;
               flag=0;

           }


        }


    }
}
