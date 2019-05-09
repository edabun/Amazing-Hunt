package edjemo.icst349.amazinghunt;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edjemo.icst349.amazinghunt.mRecycler.myAdapter;

public class Game_List extends AppCompatActivity {
    DatabaseReference db;
    ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    RecyclerView rv;
    myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        final Button btn=findViewById(R.id.join_game_btn);
        final ListView listView = findViewById(R.id.listView);
        final TextView title = findViewById(R.id.game_title);


        db = FirebaseDatabase.getInstance().getReference().child("Games");
        arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,R.id.item_n,arrayList);

        listView.setAdapter(arrayAdapter);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    arrayList.add(value);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rv = findViewById(R.id.reycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter= new myAdapter(this,arrayList);
        rv.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),join_game.class);
                startActivity(intent);
            }
        });
    }
}
