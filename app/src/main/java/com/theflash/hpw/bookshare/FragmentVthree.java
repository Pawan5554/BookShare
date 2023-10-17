package com.theflash.hpw.bookshare;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertTheme;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.interfaces.AlertActionListener;
import com.irozon.alertview.objects.AlertAction;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import info.hoang8f.widget.FButton;


public class FragmentVthree extends Fragment implements View.OnClickListener {
    View view;
    CircularImageView pic;
    AppCompatActivity app;
    SharedPreferences setting1;
    private ListView l;
    String phonenumber;
    String imageurl;
    FButton editprofile;
    String myname;
    String bio;
    TextView n;
    int count;
    TextView b;
    ArrayList<String> link;
    ArrayList<String> bookname;
    ArrayList<String> typr;
    ArrayList<String> dat;
    ArrayList<String> aut;
TextView nobook;
    TextView noreq;
    TextView nosh;
    int books;
    int requested;
    int shared;

    public FragmentVthree() {


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.vthree, container, false);
        setting1 = getContext().getSharedPreferences("data", 0);
        phonenumber=  setting1.getString("phone number","");
        app=(AppCompatActivity)(getActivity());
        n=(TextView)view.findViewById(R.id.textView7);
        editprofile=view.findViewById(R.id.button);
        editprofile.setOnClickListener(this);
        b=(TextView)view.findViewById(R.id.textView9);
        pic = view.findViewById(R.id.vie);
        pic.setOnClickListener(this);
nobook=(TextView)view.findViewById(R.id.textView4);
        noreq=(TextView)view.findViewById(R.id.textView3);
        nosh=(TextView)view.findViewById(R.id.textView8);
       link= new ArrayList<>();
        bookname= new ArrayList<>();
       typr= new ArrayList<>();
        dat= new ArrayList<>();
        aut= new ArrayList<>();
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



        return view;


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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.people, null);
            ImageView first =(ImageView)convertView.findViewById(R.id.imageView);
            TextView one=(TextView)convertView.findViewById(R.id.textView11);
            TextView d=(TextView)convertView.findViewById(R.id.textView12);
            TextView ty=(TextView)convertView.findViewById(R.id.textView15);

                    one.setText(bookname.get(position));
                    d.setText(dat.get(position));
                    ty.setText(typr.get(position));
                    Picasso.with(view.getContext())
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


    private void getallchild(DataSnapshot dataSnapshot) {
    if(dataSnapshot!=null)
    {
        count=(int)dataSnapshot.getChildrenCount();
if(count==0)
{

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

              if(!link.contains(auth.url))
              {
                  link.add(auth.url);
                  bookname.add(auth.book);
                  typr.add(auth.type);
                  dat.add(auth.date);
                  aut.add(auth.author);
              }
                if(flag==count)
                {
                    l=(ListView)view.findViewById(R.id.list);
                    final CustomAdapter adapter = new CustomAdapter();
                    l.setAdapter(adapter);
                    l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertView alert = new AlertView("Choose an action","Do You Really Want To Delete "+ bookname.get(position)+"?", AlertStyle.DIALOG);
                            alert.addAction(new AlertAction("Yes", AlertActionStyle.POSITIVE, new AlertActionListener() {
                                @Override
                                public void onActionClick(AlertAction alertAction) {
                                    if(link.size()>1)
                                    {
                                        bookname.remove(position);
                                        typr.remove(position);
                                        dat.remove(position);
                                        link.remove(position);
                                        aut.remove(position);
                                        remove();
                                    }
                                    else
                                    {
                                        Snackbar.make(getView(), "You Cannot Delete The Last Book", Snackbar.LENGTH_SHORT).show();
                                    }


                                }
                            }));
                            alert.addAction(new AlertAction("Cancel", AlertActionStyle.NEGATIVE, new AlertActionListener() {
                                @Override
                                public void onActionClick(AlertAction alertAction) {
                                }
                            }));

                            alert.setTheme(AlertTheme.LIGHT);
                            alert.show(app);

                            return true;
                        }
                    });

                    flag=0;
                }




            }

        }

    }
int put=0;
    private void remove() {
        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().getRoot().child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"books");
        dbNode.setValue(null);
        final String path2= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"books";
        for(int a=0;a<link.size();a++)
        {
            Author p= new Author(bookname.get(a),link.get(a),aut.get(a),typr.get(a),dat.get(a),FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
put =1;
            DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference();
            databaseReference2.child(path2).child(""+(int)(Math.random()*1000)).setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    books--;
                    if(books<10)
                    {
                        nobook.setText("    0"+books);

                    }
                    else
                    {
                        nobook.setText("    "+books);
                    }

                    Follow ff= new Follow(""+books,""+requested,""+shared);
                    DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference();
                    databaseReference2.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Follow").setValue(ff);

                    Snackbar.make(getView(), "Book Removed Successfully", Snackbar.LENGTH_SHORT).show();



                }
            });


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


    @Override
    public void onClick(View v) {
        if(v==editprofile)
        {


              Intent intent= new Intent(getContext(),updateprofile.class);
              intent.putExtra("number",phonenumber);
              intent.putExtra("name",myname);
              intent.putExtra("bio",bio);
              intent.putExtra("image",imageurl);
              startActivity(intent);

        }

    }

    public void updatepic(String url)
    {
        Picasso.with(view.getContext())
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
}
