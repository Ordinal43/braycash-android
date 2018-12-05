package id.ac.ukdw.braycash.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import id.ac.ukdw.braycash.R;
import id.ac.ukdw.braycash.Utils.UniversalImageLoader;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private Context mContext;

    private ImageView saveChanges;
    private ImageView mProfilePhoto;
    private Button changePhoto;
    private EditText editName;
    private TextView editPhone;
    private ProgressBar mProgressBar;

    String profileImgURL;
    Uri imgUri;
    boolean isImgSet;
    String name;
    String phone;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mContext = getActivity();

        saveChanges = (ImageView) view.findViewById(R.id.saveChanges);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo);
        changePhoto = (Button) view.findViewById(R.id.change_profile_photo);
        editName = (EditText) view.findViewById(R.id.display_name);
        editPhone = (TextView) view.findViewById(R.id.phone_number);
        mProgressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);

        isImgSet = false;

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userRef = mFirebaseDatabase.getReference("users/" + mAuth.getUid());

        setUserData();

        // back arrow for going back to "ProfileActivity"
        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set open document intent
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                startActivityForResult(intent, 123);
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // mendapatkan alamat dri
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is from the upload image
        if(requestCode == 123) {
            imgUri = data.getData();

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imgUri.toString(), mProfilePhoto);

            isImgSet = true;
        }

    }

    private void setUserData() {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                profileImgURL = dataSnapshot.child("profilePhoto").getValue().toString();
                name = dataSnapshot.child("name").getValue().toString();
                phone = dataSnapshot.child("phone").getValue().toString();

                UniversalImageLoader.setImage(profileImgURL, mProfilePhoto, null, "");
                editName.setText(name);
                editPhone.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveChanges() {
        if(name.equals("") || phone.equals("")) {
            Toast.makeText(mContext, "All fields must not be empty!", Toast.LENGTH_SHORT).show();
        } else {

            mProgressBar.setVisibility(View.VISIBLE);

            if(isImgSet) {
                Toast.makeText(mContext, "Saving...", Toast.LENGTH_SHORT).show();

                StorageReference ref = FirebaseStorage.getInstance().getReference();
                ref.child(mAuth.getUid()).putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageReference newRef = FirebaseStorage.getInstance().getReference();
                        newRef.child(mAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                userRef.child("profilePhoto").setValue(uri.toString());
                                saveName();

                            }
                        });
                    }
                });
            } else {
                getActivity().finish();
            }
        }

    }

    private void saveName() {
        String newName = editName.getText().toString();

        if(!newName.equals(name)) {

            userRef.child("name").setValue(newName).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    getActivity().finish();
                }
            });
        }
    }
}
