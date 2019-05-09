package edjemo.icst349.amazinghunt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Index extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    //private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        findViewById(R.id.log_in).setOnClickListener(this);
        findViewById(R.id.sign_up).setOnClickListener(this);
        //progressBar= findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this, Home.class));
        }
    }

    @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.log_in:
                    AlertDialog.Builder login = new AlertDialog.Builder(Index.this,R.style.CustomAlertDialog);
                    View lView = getLayoutInflater().inflate(R.layout.log_in_modal, null);
                    final EditText lEmail = lView.findViewById(R.id.log_in_email);
                    final EditText lPassword = lView.findViewById(R.id.log_in_password);
                    Button lLogIn = lView.findViewById(R.id.btn_log_in);


                    lLogIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String email = lEmail.getText().toString().trim();
                            String pass = lPassword.getText().toString().trim();

                            if (email.isEmpty()) {
                                lEmail.setError("Email is required.");
                                lEmail.requestFocus();
                                return;
                            }
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                lEmail.setError("Please enter a valid email");
                                lEmail.requestFocus();
                                return;
                            }
                            if (pass.isEmpty()) {
                                lPassword.setError("Password is required.");
                                lPassword.requestFocus();
                                return;
                            }

                            if (email.isEmpty() && pass.isEmpty()) {
                                Toast.makeText(Index.this, R.string.error_log_in_msg, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(Index.this, R.string.success_log_in_msg, Toast.LENGTH_SHORT).show();
                            }

                            //sign in user
                            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(Index.this,Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });

                        /*@Override
                        public void onClick(View view) {
                            if (!lEmail.getText().toString().isEmpty() && !lPassword.getText().toString().isEmpty()) {
                                Toast.makeText(Index.this, R.string.success_log_in_msg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Index.this, R.string.error_log_in_msg, Toast.LENGTH_SHORT).show();
                            }
                        }*
                    });*/
                    login.setView(lView);
                    AlertDialog login_modal = login.create();
                    login_modal.show();


                    break;

                case R.id.sign_up:
                    AlertDialog.Builder signup = new AlertDialog.Builder(Index.this,R.style.CustomAlertDialog);
                    View sView = getLayoutInflater().inflate(R.layout.sign_up_modal, null);
                    final EditText sEmail = sView.findViewById(R.id.sign_up_email);
                    final EditText sPassword = sView.findViewById(R.id.sign_up_password);
                    Button Sign_Up = sView.findViewById(R.id.btn_register);

                    Sign_Up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String email=sEmail.getText().toString().trim();
                            String pass=sPassword.getText().toString().trim();

                            if(email.isEmpty()){
                                sEmail.setError("Email is required.");
                                sEmail.requestFocus();
                                return;
                            }

                            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                sEmail.setError("Please enter a valid email");
                                sEmail.requestFocus();
                                return;
                            }

                            if(pass.isEmpty()){
                                sPassword.setError("Password is required.");
                                sPassword.requestFocus();
                                return;
                            }

                            if (!email.isEmpty() && !pass.isEmpty()) {
                                Toast.makeText(Index.this, R.string.success_sign_up_msg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Index.this, getString(R.string.error_sign_up_msg),Toast.LENGTH_SHORT).show();
                            }


                            //progressBar.setVisibility(View.VISIBLE);
                            //creating user
                            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        finish();
                                        Intent intent=new Intent(Index.this,Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }else {

                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                            Toast.makeText(getApplicationContext(), "You are already registered.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                }
                            });
                        }
                    });
                    /*
                    Sign_Up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!sEmail.getText().toString().isEmpty() && !sPassword.getText().toString().isEmpty()) {
                                Toast.makeText(Index.this, R.string.success_sign_up_msg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Index.this, getString(R.string.error_sign_up_msg),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                        signup.setView(sView);
                        AlertDialog signup_modal = signup.create();
                        signup_modal.show();

                    break;
            }//end of switch

        }//end of onClick
}
