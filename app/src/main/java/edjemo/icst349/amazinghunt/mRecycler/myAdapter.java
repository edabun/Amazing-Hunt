package edjemo.icst349.amazinghunt.mRecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edjemo.icst349.amazinghunt.R;

/**
 * Created by edabun on 3/16/18.
 */

public class myAdapter extends RecyclerView.Adapter<myHolder> {
    DatabaseReference db;
    Context c;
    ArrayList<String> items=new ArrayList<>();

    public myAdapter(Context c, ArrayList<String> items) {
        this.c = c;
        this.items = items;
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.game_details,parent,false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(myHolder holder, int position) {
        db = FirebaseDatabase.getInstance().getReference().child("Games");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                items.add(value);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.title.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
