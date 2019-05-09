package edjemo.icst349.amazinghunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.client.Firebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by edabun on 2/19/18.
 */

public class Create_Game extends AppCompatActivity implements Serializable{
    //Firebase firebase;
    DatabaseReference db;

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);
        getWindow().setBackgroundDrawableResource(R.drawable.road_map);

        final ListView list = findViewById(R.id.listView);
        final Button add_item = findViewById(R.id.add_item);
        final EditText item_name = findViewById(R.id.item);
        final Button create = findViewById(R.id.btn_create_game);
        final TextView game_title = findViewById(R.id.game_title);
        final TextView keyword = findViewById(R.id.keyword);
        //final ImageButton img = findViewById(R.id.delete_item_btn);

        final String[] items={};
        arrayList = new ArrayList<>(Arrays.asList(items));

        //Firebase.setAndroidContext(this);

        db = FirebaseDatabase.getInstance().getReference().child("Games");
        //firebase = new Firebase("https://amazing-hunt-7d9b1.firebaseio.com/");

        arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,R.id.item_n,arrayList);
        list.setAdapter(arrayAdapter);

        //add item on arrayList
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = game_title.getText().toString();
                String new_item=item_name.getText().toString();
                if(!new_item.isEmpty() && !title.isEmpty()) {
                    arrayList.add(new_item);
                    arrayAdapter.notifyDataSetChanged();
                    item_name.setText("");
                }else
                    Toast.makeText(getApplicationContext(),"Fill in the blanks",Toast.LENGTH_SHORT).show();
            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray posChecker = list.getCheckedItemPositions();

                int count = list.getCount();

                for(int item=count-1;item>=0;item--){
                    if(posChecker.get(item)){
                        arrayAdapter.remove(arrayList.get(item));
                    }
                }

                posChecker.clear();
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = game_title.getText().toString();
                String key = keyword.getText().toString();

                if(!title.isEmpty() && !key.isEmpty()) {
                    /*Intent intent = new Intent();
                    intent.setAction("edjemo.icst349.amazinghunt");
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    intent.putExtra("title",title);
                    sendBroadcast(intent);*/
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    startActivity(intent);

                    db.child(title).setValue(arrayList);
                    db.child(title).child("keyword").setValue(key);
                    Toast.makeText(getApplicationContext(), title + " has been added.", Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(getApplicationContext(),"Fill in the blanks",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
