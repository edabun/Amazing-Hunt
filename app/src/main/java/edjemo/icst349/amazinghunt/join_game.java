package edjemo.icst349.amazinghunt;

import android.*;
import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class join_game extends AppCompatActivity{
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    Button cam,submit;
    SurfaceView surfaceView;
    TextView qr_code;
    final int RequestPermissionId = 1001;
    DatabaseReference db;
    ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    TextView g_title;
    EditText key;
    String k;
    FirebaseAuth mAuth;
    FirebaseUser user;

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;
    /*private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals("edjemo.icst349.amazinghunt")){
                g_title = intent.getExtras().getString("title");
            }
        }
    };*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionId: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        getWindow().setBackgroundDrawableResource(R.drawable.road_map);
        //findViewById(R.id.camera).setOnClickListener(this);

        final ListView list = findViewById(R.id.listView);
        cam = findViewById(R.id.camera);
        submit = findViewById(R.id.submit);
        surfaceView = findViewById(R.id.cameraPreview);
        qr_code = findViewById(R.id.qr_code);
        g_title=findViewById(R.id.gameTitle);
        key = findViewById(R.id.key_word);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Intent intent = getIntent();
        final String title=intent.getStringExtra("title");

        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);

        remoteViews.setTextViewText(R.id.notif_title,"WINNER!");
        remoteViews.setTextViewText(R.id.winner_name,user.getDisplayName());
        remoteViews.setTextViewText(R.id.win_time, DateFormat.getDateTimeInstance().format(new Date()));

        notification_id= (int) System.currentTimeMillis();
        Intent button_intent = new Intent("button_clicked");
        button_intent.putExtra("id",notification_id);

        PendingIntent p_button_intent= PendingIntent.getBroadcast(context,123,button_intent,0);
        remoteViews.setOnClickPendingIntent(R.id.submit,p_button_intent);

        g_title.setText(title);
        arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,R.id.item_n,arrayList);
        list.setAdapter(arrayAdapter);
        db = FirebaseDatabase.getInstance().getReference().child("Games").child(title);
        /*db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){
                    arrayList.add(String.valueOf(dsp.getValue()));
                    arrayAdapter.notifyDataSetChanged();
                }
                list.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                arrayList.add(String.valueOf(dataSnapshot.getValue()));
                k = dataSnapshot.getValue().toString();
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AlertDialog.Builder login = new AlertDialog.Builder(join_game.this,R.style.CustomAlertDialog);
                View lView = getLayoutInflater().inflate(R.layout.camera, null);

                login.setView(lView);
                AlertDialog login_modal = login.create();
                login_modal.show();*/
                Intent intent = new Intent(join_game.this, camera.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"lol",Toast.LENGTH_SHORT).show();
            }
        });//end of cam buttonclick

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = key.getText().toString();
                if (word.equals(k)){
                    Intent i = new Intent(join_game.this, Notification.class);
                    PendingIntent pi = PendingIntent.getBroadcast(context,0,i,0);

                    builder = new NotificationCompat.Builder(context);
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true)
                            .setCustomBigContentView(remoteViews)
                            .setContentIntent(pi);

                    notificationManager.notify(notification_id,builder.build());

                }
            }
        });//end of submit buttonclick

    }

}
