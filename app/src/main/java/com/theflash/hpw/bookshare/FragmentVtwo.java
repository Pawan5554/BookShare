package com.theflash.hpw.bookshare;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import info.hoang8f.widget.FButton;



public class FragmentVtwo extends Fragment implements AbsListView.OnScrollListener {
    View view;
    int count;

    int pos;
    private ListView l;
    SearchView search;

    ArrayList<String> no2;
    ArrayList<String> requestedbooks;
    int book;
 ArrayList<all> abc;
    int sh;
    int req;

    public FragmentVtwo() {
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.vtwo,container,false);
       abc=new ArrayList<>();
        no2=new ArrayList<>();
        requestedbooks=new ArrayList<>();
        search=view.findViewById(R.id.searchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<all> result= new ArrayList<>();

                for (all x:abc)
                {
                    if(x.bookname.contains(newText))
                        result.add(x);
                }

                l=(ListView)view.findViewById(R.id.list);
                final CustomAdapter  adapter = new CustomAdapter(result);
                l.setAdapter(adapter);

                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });
        try
        {
            DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference("Recently Added Books");
            databaseReference9.addValueEventListener(new ValueEventListener() {
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



        try
        {
            DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"RequestedBooks");
            databaseReference9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    getrequ(dataSnapshot);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){}

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








        return view;
    }
    int ddcount;
    private void getrequ(DataSnapshot dataSnapshot) {
        if(dataSnapshot!=null) {
            ddcount = (int) dataSnapshot.getChildrenCount();

            if(ddcount>0)
            {
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

            }

        }

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


    private void setfollow(DataSnapshot dataSnapshot) {

        if(dataSnapshot!=null)
        {
            Follow ff=dataSnapshot.getValue(Follow.class);
            book= Integer.parseInt(ff.bk);
            req=Integer.parseInt(ff.req);
            sh=Integer.parseInt(ff.sh);
        }
    }

    class CustomAdapter extends BaseAdapter implements Filterable
    {

ArrayList<all> items;

        public CustomAdapter(ArrayList<all> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();

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
            convertView = getLayoutInflater().inflate(R.layout.book1, null);
            ImageView first =(ImageView)convertView.findViewById(R.id.imageView);
            TextView one=(TextView)convertView.findViewById(R.id.textView11);
            TextView d=(TextView)convertView.findViewById(R.id.textView12);
            TextView ty=(TextView)convertView.findViewById(R.id.textView15);
            final FButton request=(FButton)convertView.findViewById(R.id.button);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requested(position);
                    request.setText("Requested");
                    request.setButtonColor(Color.parseColor("#C1C4C3"));
                    request.setShadowEnabled(false);
                    request.setEnabled(false);

                }
            });

            for (int b=0;b<requestedbooks.size();b++)
            {
                if(requestedbooks.get(b).equals(items.get(position).link))
                {
                    request.setText("Requested");
                    request.setButtonColor(Color.parseColor("#C1C4C3"));
                    request.setShadowEnabled(false);
                    request.setEnabled(false);
                }

            }

            one.setText(items.get(position).bookname);
            d.setText(items.get(position).dat);
            ty.setText(items.get(position).typr);
            Picasso.with(view.getContext())
                    .load(items.get(position).link)
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


        @Override
        public Filter getFilter() {
            return null;
        }
    }

    private void requested(int position) {
        pos=position;
        try {
            req++;
            SharedPreferences setting1 = getContext().getSharedPreferences("data", 0);
            String nam=setting1.getString("profile name","");

            timeline t10= new timeline(abc.get(position).link);
            DatabaseReference databaseReference10= FirebaseDatabase.getInstance().getReference();
            databaseReference10.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"RequestedBooks").child(""+(int)(Math.random()*1000)).setValue(t10);

            addmydetail a=new addmydetail(abc.get(position).bookname,abc.get(position).link,FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),nam);
            DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference();
            databaseReference9.child(abc.get(position).no+"Requested").child(""+(int)(Math.random()*1000)).setValue(a);


            timeline t3= new timeline(nam+" Requested You For "+abc.get(position).bookname);
            DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference();
            databaseReference3.child(abc.get(position).no+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t3);



            Follow ff= new Follow(""+book,""+req,""+sh);
            DatabaseReference databaseReference12= FirebaseDatabase.getInstance().getReference();
            databaseReference12.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Follow").setValue(ff);
        }catch (Exception e)
        {}

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child(abc.get(position).no).addValueEventListener(new ValueEventListener() {
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

                timeline t3= new timeline("You Have Requested To "+name+" For "+abc.get(pos).bookname);
                DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference();
                databaseReference3.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Timeline").child(""+(int)(Math.random()*1000)).setValue(t3);

            }


        }


    }
    int dcount=0;

    private void getallchild(DataSnapshot dataSnapshot) {

        if(dataSnapshot!=null)
        {
            count=(int)dataSnapshot.getChildrenCount();
            if(count==0)
            {
                SharedPreferences setting2 = getContext().getSharedPreferences("data", 0);
                SharedPreferences.Editor editor2 = setting2.edit();
                editor2.putString("var","0");
                editor2.putString("car","0");
                editor2.apply();
            }
            else
            {

                final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                databaseReference1.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"books").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        getallchild2(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        }
    }

    private void getallchild2(DataSnapshot dataSnapshot) {
        if(dataSnapshot!=null) {
            dcount = (int) dataSnapshot.getChildrenCount();

            final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"books").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren())
                    {
                        setdata2(child);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

    }
    int flag2=0;
    private void setdata2(DataSnapshot child) {
        flag2++;
        if(child!=null)
        {
            Author auth = child.getValue(Author.class);

            if (auth != null) {

                no2.add(auth.number);
                if(flag2==dcount)
                {
                    start();
                }

            }
        }


    }

    public  void  start()
    {

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("Recently Added Books").addValueEventListener(new ValueEventListener() {
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
    int ck=10;
    int fff=1;
    private void setdata(DataSnapshot child) {
        flag++;
        if(child!=null)
        {
            Author auth = child.getValue(Author.class);

            if (auth != null) {

                for(int a=0;a<abc.size();a++)
                {

                    if(abc.get(a).link.equals(auth.url))
                    {
                        fff=0;
                    }

                }

              if(fff==1)
              {
                  fff=1;
                  if(!no2.contains(auth.number))
                  {
                      all aa= new all(auth.url,auth.book,auth.type,auth.number,auth.date);
                     abc.add(aa);
                  }


              }
                if(count<ck&flag==count)
                {
                    SharedPreferences setting2 = getContext().getSharedPreferences("data", 0);
                    SharedPreferences.Editor editor2 = setting2.edit();
                    editor2.putString("var","0");
                    editor2.putString("car","0");
                    editor2.apply();
                    l=(ListView)view.findViewById(R.id.list);
                    final CustomAdapter adapter = new CustomAdapter(abc);
                    l.setAdapter(adapter);
                    flag=0;
                }

            }
        }
        if(flag==ck)
        {
            SharedPreferences setting2 = getContext().getSharedPreferences("data", 0);
            SharedPreferences.Editor editor2 = setting2.edit();
            editor2.putString("var","0");
            editor2.putString("car","0");
            editor2.apply();
            l=(ListView)view.findViewById(R.id.list);
            final CustomAdapter  adapter = new CustomAdapter(abc);
            l.setAdapter(adapter);
            flag=0;
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (!view.canScrollList(View.SCROLL_AXIS_VERTICAL) && scrollState == SCROLL_STATE_IDLE)
        {
            ck=ck+2;
            start();

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


}
