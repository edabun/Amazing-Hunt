package edjemo.icst349.amazinghunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by edabun on 12/26/17.
 */

public class Game extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button create=view.findViewById(R.id.create_game);
        Button join=view.findViewById(R.id.join_game);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AlertDialog.Builder create_game = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
                View cView = getLayoutInflater().inflate(R.layout.create_game, null);
                create_game.setView(cView);
                AlertDialog create_game_modal= create_game.create();
                create_game_modal.show();*/
                Intent intent = new Intent(getContext(),Create_Game.class);
                startActivity(intent);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Game_List.class);
                startActivity(intent);
            }
        });
    }

}
