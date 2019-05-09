package edjemo.icst349.amazinghunt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals("edjemo.icst349.amazinghunt")){
            Intent i=new Intent(context,join_game.class);
            String state = intent.getExtras().getString("title");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra("title",state);
            context.startActivity(i);
            Toast.makeText(context, state + " ",Toast.LENGTH_SHORT).show();
        }
    }
}
