package edjemo.icst349.amazinghunt;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by edabun on 12/23/17.
 */

public class UserProfile extends Fragment{
    Uri profileImage;
    ImageView image,nav_image;
    EditText uname,fname;
    TextView nav_name,nav_email;
    ProgressBar pBar;
    String pImageUrl;

    private static final int CHOOSE_IMAGE=101;

    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nav_email=view.findViewById(R.id.userEmail);
        nav_name=view.findViewById(R.id.userName);
        nav_image=view.findViewById(R.id.userImage);
        image=view.findViewById(R.id.profile_picture);
        pBar=view.findViewById(R.id.image_prog_bar);
        uname=view.findViewById(R.id.username);
        mAuth=FirebaseAuth.getInstance();

        //profile picture
        view.findViewById(R.id.profile_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        loadUserInformation();

        //save user data
        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            getActivity().finish();
            startActivity(new Intent(getActivity(),Index.class));
        }
    }

    public void loadUserInformation() {
        FirebaseUser user=mAuth.getCurrentUser();

        if(user!=null) {
            if(user.getPhotoUrl()!=null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(image);
            }

            if(user.getDisplayName()!=null) {
                uname.setText(user.getDisplayName());
            }
        }
    }

    //save user information in firebase
    private void saveUserInformation() {
        String username=uname.getText().toString();

        if(username.isEmpty()){
            uname.setError("Username required.");
            uname.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null && profileImage!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .setPhotoUri(Uri.parse(pImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null ){
            profileImage= data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),profileImage);
                image.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //uploading image to firebase database
    private void uploadImageToFirebaseStorage(){
        StorageReference profileImageRef= FirebaseStorage.getInstance().getReference("profilePics/"+System.currentTimeMillis()+ ".jpg");

        if(profileImage!=null){
            pBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(profileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pBar.setVisibility(View.GONE);
                            pImageUrl=taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    //selecting profile image
    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile image."), CHOOSE_IMAGE);
    }
}
