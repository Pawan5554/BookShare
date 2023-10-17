package com.theflash.hpw.bookshare;


        import android.app.Notification;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.NotificationCompat;
        import android.support.v4.app.NotificationManagerCompat;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;


        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;

        import eu.long1.spacetablayout.SpaceTabLayout;

public class chat extends AppCompatActivity {
    SpaceTabLayout tabLayout;
    static final String CHANNEL_ID ="HARSH_MAURYA_INC";
    static final String CHANNEL_NAME ="HARSH_MAURYA_INC";
    static final String CHANNEL_DESC ="HARSH MAURYA NOTIFICATION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences setting2 = getApplicationContext().getSharedPreferences("data", 0);
        SharedPreferences.Editor editor2 = setting2.edit();
        editor2.putString("var","1");
        editor2.putString("car","1");
        editor2.apply();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add((new FragmentVone()));
        fragmentList.add((new FragmentVtwo()));
        fragmentList.add((new FragmentVthree()));
        ViewPager viewPager = (ViewPager) findViewById(R.id.v1);
        tabLayout = (SpaceTabLayout) findViewById(R.id.tab);
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);




        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                DatabaseReference databaseReference8= FirebaseDatabase.getInstance().getReference();
                databaseReference8.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"Requested").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        SharedPreferences  setting1 = getApplication().getSharedPreferences("data", 0);
                      String value=  setting1.getString("var","");
                      if(value.equalsIgnoreCase("0"))
                        diaplayNotification("You Have A New Requested For The Book","Tap To Open Book Share");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference();
                databaseReference9.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"MyFollowers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SharedPreferences  setting1 = getApplication().getSharedPreferences("data", 0);
                        String value=  setting1.getString("car","");
                        if(value.equalsIgnoreCase("0"))
                        diaplayNotification("You Have A New Follower","Tap To Open Book Share");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    public void diaplayNotification(String Title,String Text)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder mBuilder=
                new NotificationCompat.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_sms_black_24dp)
                        .setContentTitle(Title)
                        .setContentText(Text)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Intent notificationIntent = new Intent(getApplicationContext(), chat.class);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);



        mBuilder.setContentIntent(intent);
        NotificationManagerCompat nNotificationMgr = NotificationManagerCompat.from(this);
        nNotificationMgr.notify(1,mBuilder.build());




    }
}
