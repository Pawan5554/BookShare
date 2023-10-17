package com.theflash.hpw.bookshare;

import android.content.SharedPreferences;
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


public class ViewProfile extends AppCompatActivity {

    CircularImageView pic;
    private ListView l;
    String phonenumber;
    String imageurl;
    String myname;
    String bio;
    TextView n;
    int count;
    TextView b;
    ArrayList<String> link;
    ArrayList<String> bookname;
    ArrayList<String> typr;
    ArrayList<String> dat;
    TextView nobook;
    TextView noreq;
    TextView nosh;
    int books;
    int requested;
    int shared;
    ArrayList<String> requestedbooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        phonenumber= getIntent().getExtras().getString("number");
        n=(TextView)findViewById(R.id.textView7);
        b=(TextView)findViewById(R.id.textView9);
        requestedbooks=new ArrayList<>();
        pic = findViewById(R.id.vie);
        nobook=(TextView)findViewById(R.id.textView4);
        noreq=(TextView)findViewById(R.id.textView3);
        nosh=(TextView)findViewById(R.id.textView8);
        link= new ArrayList<>();
        bookname= new ArrayList<>();
        typr= new ArrayList<>();
        dat= new ArrayList<>();

        try
        {
            DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"RequestedBooks");
            databaseReference9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren())
                    {
                        requ(child);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){}

        final DatabaseReference databaseReference18 = FirebaseDatabase.getInstance().getReference();
        databaseReference18.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"MyFollowing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    check(child);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference databaseReference19= FirebaseDatabase.getInstance().getReference();
        databaseReference19.child(phonenumber+"Follow").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setfollow(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference8= FirebaseDatabase.getInstance().getReference();
        databaseReference8.child(phonenumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setall(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        try
        {
            DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference();
            databaseReference9.child(phonenumber+"books").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getallchild(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){}

    }

    private void requ(DataSnapshot child) {

        if(child!=null)
        {
            timeline t=child.getValue(timeline.class);
            if(t!=null)
            {
                requestedbooks.add(t.info);
            }

        }

    }

    private void check(DataSnapshot child) {
        if(child!=null)
        {
            timeline t=child.getValue(timeline.class);

            if(t.info.equalsIgnoreCase(phonenumber))
            {
                FButton request=(FButton)findViewById(R.id.button);
                request.setText("Following");
                request.setButtonColor(Color.parseColor("#C1C4C3"));
                request.setShadowEnabled(false);
                request.setEnabled(false);

            }


        }


    }

    private void setfollow(DataSnapshot dataSnapshot) {

        if(dataSnapshot!=null)
        {
            Follow ff=dataSnapshot.getValue(Follow.class);
            books= Integer.parseInt(ff.bk);
            requested=Integer.parseInt(ff.req);
            shared=Integer.parseInt(ff.sh);
            if(shared<10)
            {
                nosh.setText("    0"+shared);
            }
            else
            {
                nosh.setText("    "+shared);
            }

            if(requested<10)
            {
                noreq.setText("    0"+requested);

            }
            else
            {
                noreq.setText("    "+requested);
            }

            if(books<10)
            {
                nobook.setText("    0"+books);

            }
            else
            {
                nobook.setText("    "+books);
            }


        }

    }

    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return link.size();

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
            convertView = getLayoutInflater().inflate(R.layout.book1, null);
            ImageView first =(ImageView)convertView.findViewById(R.id.imageView);
            TextView one=(TextView)convertView.findViewById(R.id.textView11);
            TextView d=(TextView)convertView.findViewById(R.id.textView12);
            TextView ty=(TextView)convertView.findViewById(R.id.textView15);
            final FButton request=(FButton)convertView.findViewById(R.id.button);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    req(position);
                    request.setText("Requested");
                    request.setButtonColor(Color.parseColor("#C1C4C3"));
                    request.setShadowEnabled(false);
                    request.setEnabled(false);

                }
            });

            for (int b=0;b<requestedbooks.size();b++)
            {
                if(requestedbooks.get(b).equals(link.get(position)))
                {
                    request.setText("Requested");
                    request.setButtonColor(Color.parseColor("#C1C4C3"));
                    request.setShadowEnabled(false);
                    request.setEnabled(false);
                }

            }


            one.setText(bookname.get(position));
            d.setText(dat.get(position));
            ty.setText(typr.get(position));
            Picasso.with(getApplicationContext())
                    .load(link.get(position))
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

    int pos;
public  void  req(int position)
{
    pos=position;
    requested++;
    Follow ff= new Follow(""+books,""+requested,""+shared);
    DatabaseReference databaseReference12= FirebaseDatabase.getInstance().getReference();
    databaseReference12.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Follow").setValue(ff);
    SharedPreferences setting1 = getApplication().getSharedPreferences("data", 0);
    String nam=setting1.getString("profile name","");

    addmydetail a=new addmydetail(bookname.get(position),link.get(position),FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),nam);
    DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference();
    databaseReference9.child(phonenumber+"Requested").child(""+(int)(Math.random()*1000)).setValue(a);

    timeline t3= new timeline(nam+" Requested You For "+bookname.get(position));
    DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference();
    databaseReference3.child(phonenumber+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t3);

    final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
    databaseReference1.child(phonenumber).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            Acceptdata(dataSnapshot);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}

    private void Acceptdata(DataSnapshot dataSnapshot) {

        if(dataSnapshot!=null)
        {
            regain r=dataSnapshot.getValue(regain.class);

            if(r!=null)
            {

                String name=r.profilename;

                timeline t3= new timeline("You Have Requested To "+name+" For "+bookname.get(pos));
                DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference();
                databaseReference3.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t3);

            }
        }

    }

    private void getallchild(DataSnapshot dataSnapshot) {
        if(dataSnapshot!=null)
        {
            count=(int)dataSnapshot.getChildrenCount();
            if(count==0)
            {
                Snackbar.make(findViewById(android.R.id.content),"No Books Founded",Snackbar.LENGTH_SHORT).show();
            }
            else
            {
                start();
            }
        }

    }
    public  void  start()
    {

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child(phonenumber+"books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    setdata(child);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    int flag=0;
    private void setdata(DataSnapshot child) {

        if(child!=null)
        {
            Author auth=child.getValue(Author.class);

            if (auth!=null)
            {
                flag++;
                link.add(auth.url);
                bookname.add(auth.book);
                typr.add(auth.type);
                dat.add(auth.date);
                if(flag==count)
                {
                    l=(ListView)findViewById(R.id.list);
                    final CustomAdapter adapter = new CustomAdapter();
                    l.setAdapter(adapter);
                    flag=0;
                }
            }

        }

    }

    private void setall(DataSnapshot dataSnapshot) {
        if(dataSnapshot!=null)
        {
            regain r=dataSnapshot.getValue(regain.class);
            imageurl=r.imageurl;
            bio=r.bio;
            myname=r.profilename;
            n.setText(myname);
            b.setText(bio);
            updatepic(imageurl);
        }

    }




    public void updatepic(String url)
    {
        Picasso.with(getApplicationContext())
                .load(url)

                .into(pic, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {



                    }

                    @Override
                    public void onError() {

                    }
                });

    }


    public void OnClick(View view)
    {
        FButton request=(FButton)findViewById(R.id.button);
        request.setText("Following");
        request.setButtonColor(Color.parseColor("#C1C4C3"));
        request.setShadowEnabled(false);
        request.setEnabled(false);
        timeline fff= new timeline(phonenumber);
        DatabaseReference databaseReference121= FirebaseDatabase.getInstance().getReference();
        databaseReference121.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"MyFollowing").child(""+(int)(Math.random()*1000)).setValue(fff);
        timeline ff= new timeline(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        DatabaseReference databaseReference12= FirebaseDatabase.getInstance().getReference();
        databaseReference12.child(phonenumber+"MyFollowers").child(""+(int)(Math.random()*1000)).setValue(ff);
        timeline t4= new timeline("You Are Following "+myname);
        DatabaseReference databaseReference34= FirebaseDatabase.getInstance().getReference();
        databaseReference34.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t4);
        SharedPreferences setting1 = getApplicationContext().getSharedPreferences("data", 0);
      String num=  setting1.getString("profile name","");
        timeline t44= new timeline(num+" Is Now Following You");
        DatabaseReference databaseReference344= FirebaseDatabase.getInstance().getReference();
        databaseReference344.child(phonenumber+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t44);
    }

}
