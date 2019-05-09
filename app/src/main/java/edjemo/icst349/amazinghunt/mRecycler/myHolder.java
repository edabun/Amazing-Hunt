package edjemo.icst349.amazinghunt.mRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import edjemo.icst349.amazinghunt.R;

/**
 * Created by edabun on 3/16/18.
 */

public class myHolder extends RecyclerView.ViewHolder {

    TextView title;
    ListView itemList;
    Button joinBtn;

    public myHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.game_title);
        itemList = itemView.findViewById(R.id.listView);
        joinBtn = itemView.findViewById(R.id.join_game_btn);
    }
}
