package com.theflash.hpw.bookshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import info.hoang8f.widget.FButton;

import static com.google.common.collect.ComparisonChain.start;

public class seerequest extends Activity {

    ArrayList<String> booklink;
    ArrayList<String> number ;
    ArrayList<String> bookname;

    ArrayList<String> namee;
    int book;
    int sh;
    int req;
    int count;
    int fllag=0;
    int getCount=0;
    ArrayList<String> requestedbooks;
    ListView l;
    int vlag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seerequest);
         booklink= new ArrayList<>();
       number = new ArrayList<>();
        requestedbooks=new ArrayList<>();
        bookname =new ArrayList<>();
       namee =new ArrayList<>();


        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Requested").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getallchild(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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


    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return booklink.size();

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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.seerequestlist, null);
            ImageView first =(ImageView)convertView.findViewById(R.id.imageView);
            TextView ty=(TextView)convertView.findViewById(R.id.textView15);
            final FButton request=(FButton)convertView.findViewById(R.id.button);

            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check==0)
                    {
                        requested(position);
                        request.setText("Accepted");
                        request.setButtonColor(Color.parseColor("#C1C4C3"));
                        request.setShadowEnabled(false);
                        request.setEnabled(false);
                    }
                    else
                    {
                        Snackbar.make(findViewById(android.R.id.content), "Please wait a sec.. ", Snackbar.LENGTH_SHORT).show();
                    }



                }
            });
            final FButton request1=(FButton)convertView.findViewById(R.id.button1);
            request1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(getApplicationContext(),ViewProfile.class);
                    intent.putExtra("number",number.get(position));
                    startActivity(intent);


                }
            });

           ty.setText(namee.get(position)+ " Requested You For "+bookname.get(position));
            Picasso.with(getApplicationContext())
                    .load(booklink.get(position))
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

    String  globalnumber;
    String globalurl;
    int globalpos;
    private void requested( int position) {

        sh++;
        Follow ff= new Follow(""+book,""+req,""+sh);
        DatabaseReference databaseReference12= FirebaseDatabase.getInstance().getReference();
        databaseReference12.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Follow").setValue(ff);


        SharedPreferences setting1 = getApplication().getSharedPreferences("data", 0);
        String nam=setting1.getString("profile name","");

        timeline t3= new timeline(nam+" Accepted Your Request For  "+bookname.get(position));
        DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference();
        databaseReference3.child(number.get(position)+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t3);

        timeline t4= new timeline(" You Have Accepted Request For  "+bookname.get(position) +" Sent By "+namee.get(position));
        DatabaseReference databaseReference4= FirebaseDatabase.getInstance().getReference();
        databaseReference4.child(number.get(position)+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t4);
        vlag=1;
globalnumber=number.get(position);
globalurl=booklink.get(position);


        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().getRoot().child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Requested");
        dbNode.setValue(null);

        bookname.remove(position);
        namee.remove(position);
        number.remove(position);
        booklink.remove(position);

        if (booklink.size()==0)
        {
            finish();

        }
        else
        {
            for(int b=0;b<bookname.size();b++)
            {
                addmydetail a=new addmydetail(bookname.get(b),booklink.get(b),number.get(b),namee.get(b));
                DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference();
                databaseReference9.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Requested").child(""+(int)(Math.random()*1000)).setValue(a);

            }
        }


        try
        {
            DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference(globalnumber+"RequestedBooks");
            databaseReference9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    getall(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){}

    }



    private void getall(DataSnapshot dataSnapshot) {
        if(dataSnapshot!=null)
        {
            getCount=(int)dataSnapshot.getChildrenCount();
            if(getCount>0)
            {
                try
                {
                    DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference(globalnumber+"RequestedBooks");
                    databaseReference9.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(check==0)
                            {
                                for (DataSnapshot child : dataSnapshot.getChildren())
                                {
                                    requ(child);
                                }

                            }else {
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        check=0;
                                    }
                                }, 1000);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                catch (Exception e){}

            }
        }


    }


    private void requ(DataSnapshot child) {

        if(child!=null)
        {
            timeline t=child.getValue(timeline.class);
            if(t!=null)
            {fllag++;
               if(!requestedbooks.contains(t.info))
               {

                   requestedbooks.add(t.info);

                   if(getCount==fllag)
                   {
                       fllag=0;

                       startset(globalnumber,globalurl);
                   }
               }
                if(getCount==fllag)
                {
                    fllag=0;

                    startset(globalnumber,globalurl);
                }
            }

        }


    }

int check=0;
    private void startset(String number ,String link) {


        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().getRoot().child(number+"RequestedBooks");
        dbNode.setValue(null);

      requestedbooks.remove(link);

        check=1;
        for (int a=0;a<requestedbooks.size();a++)
        {

            timeline t10= new timeline(requestedbooks.get(a));
            DatabaseReference databaseReference10= FirebaseDatabase.getInstance().getReference();
            databaseReference10.child(number+"RequestedBooks").child(""+(int)(Math.random()*1000)).setValue(t10);
        }



    }



    private void getallchild(DataSnapshot dataSnapshot) {
        count=(int)dataSnapshot.getChildrenCount();
        if(count==0&&vlag==0)
        {
          TextView nn=  findViewById(R.id.textView18);
          nn.setText("No Request Founded");
        }
     else
        {
            final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Requested").addValueEventListener(new ValueEventListener() {
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
    }
int flag=0;
    private void setdata(DataSnapshot child) {
        flag++;
       // Toast.makeText(getApplicationContext(),"Coming flag ="+flag+" count ="+count,Toast.LENGTH_SHORT).show();
        if(child!=null)
        {
            addmydetail auth = child.getValue(addmydetail.class);

            if (auth != null) {

                if(!booklink.contains(auth.url))
                {
booklink.add(auth.url);
bookname.add(auth.book);
number.add(auth.number);
namee.add(auth.name);


                }

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

    private void setfollow(DataSnapshot dataSnapshot) {

        if(dataSnapshot!=null)
        {
            Follow ff=dataSnapshot.getValue(Follow.class);
            book= Integer.parseInt(ff.bk);
            req=Integer.parseInt(ff.req);
            sh=Integer.parseInt(ff.sh);
        }
    }

}
