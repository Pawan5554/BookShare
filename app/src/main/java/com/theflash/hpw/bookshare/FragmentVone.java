package com.theflash.hpw.bookshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.ArrayList;


public class FragmentVone extends Fragment implements View.OnClickListener {
    View view;
    ImageView mm;
    ImageView following;
    AppCompatActivity app;
ImageView request;
    ArrayList<String> info=new ArrayList<>();
    int count;
    private ListView l;

    public FragmentVone() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.vone,container,false);
        mm = (ImageView) view.findViewById(R.id.imageView4);
        following=(ImageView)view.findViewById(R.id.imageView7);
        following.setOnClickListener(this);
request=(ImageView)view.findViewById(R.id.imageView2);
        app=(AppCompatActivity)(getActivity());
request.setOnClickListener(this);
        mm.setOnClickListener(this);


        try
        {
            DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference();
            databaseReference9.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Timeline").addValueEventListener(new ValueEventListener() {
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



    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return info.size();

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
            convertView = getLayoutInflater().inflate(R.layout.time, null);
            ImageView first =(ImageView)convertView.findViewById(R.id.imageView5);
            TextView one=(TextView)convertView.findViewById(R.id.textView1);



            one.setText(info.get(position));
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

    public  void start()
    {

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Timeline").addValueEventListener(new ValueEventListener() {
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
           timeline auth=child.getValue(timeline.class);

            if (auth!=null)
            {
                flag++;

                if (!info.contains(auth.info))
               info.add(auth.info);
                if(flag==count)
                {
                    l=(ListView)view.findViewById(R.id.list);
                    final CustomAdapter adapter = new CustomAdapter();
                    l.setAdapter(adapter);
                    l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            AlertView alert = new AlertView("Choose An Action","Do You  Really Want To Clear Your Timeline?", AlertStyle.DIALOG);
                            alert.addAction(new AlertAction("Yes", AlertActionStyle.POSITIVE, new AlertActionListener() {
                                @Override
                                public void onActionClick(AlertAction alertAction) {
clear();
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

    public void clear()
    {
        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().getRoot().child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Timeline");
        dbNode.setValue(null);

        timeline t3= new timeline("Timeline Cleared !!");
        DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference();
        databaseReference3.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t3);
        info.clear();
        info.add("Timeline Cleared !!");
    }

    @Override
    public void onClick(View v) {
        if(v==mm)
        {
            Intent intent = new Intent(getContext(),addbook.class);
            startActivity(intent);
        }
       else if(v==request)
        {

Intent intent=new Intent(getContext(),seerequest.class);
startActivity(intent);
        }
       else if(v==following)
        {

            Intent intent= new Intent(getContext(),seemyfollower.class);
            startActivity(intent);

        }

    }



}
