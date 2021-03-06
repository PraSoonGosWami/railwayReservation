package com.invaderx.railway.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.invaderx.railway.R;
import com.invaderx.railway.models.UserProfile;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView profileUsername,basicName,phoneNo,profileEmail,address,walletAmount,phonepe,debitCard,paytm;
    private FloatingActionButton editProfileDetails;
    private PopupWindow popWindow;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String phoneNumer,home,upi,card,pay;
    private int wallet;
    private LinearLayout walletLayout;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_profile);
        user= FirebaseAuth.getInstance().getCurrentUser();
        profileImageView=findViewById(R.id.profileImageView);
        profileUsername=findViewById(R.id.profileUsername);
        basicName=findViewById(R.id.basicName);
        phoneNo=findViewById(R.id.phoneNo);
        profileEmail=findViewById(R.id.profileEmail);
        address=findViewById(R.id.address);
        walletAmount=findViewById(R.id.walletAmount);
        phonepe=findViewById(R.id.phonepe);
        debitCard=findViewById(R.id.debitCard);
        paytm=findViewById(R.id.paytm);
        editProfileDetails=findViewById(R.id.editProfileDetails);
        walletLayout=findViewById(R.id.walletLayout);

        walletLayout.setEnabled(true);

        //database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();





        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Log.v("Username",name);
            profileUsername.setText(name);
            basicName.setText(name);
            profileEmail.setText(email);
            getUserDetails(user.getUid());
            getImage();
        } else {
            profileUsername.setText("No user name");
        }

        editProfileDetails.setOnClickListener(v->{
            editUserDetails(user.getUid());
        });

        walletLayout.setOnClickListener(v -> {
            addMoneyWallet(user.getUid());
            walletLayout.setEnabled(false);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wallet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.editImage)
            uploadUserImage();
        return true;
    }

    //popup for edititng user dettails
    public void editUserDetails(String uid){

        ImageView cancelEdit;
        EditText editPhone,editAddress,editPhonepe,editDebitCard,editPaytm;
        FloatingActionButton tickFab;

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflatedView = layoutInflater.inflate(R.layout.edit_profile_popup, null,false);

        cancelEdit = inflatedView.findViewById(R.id.cancelEdit);
        editPhone = inflatedView.findViewById(R.id.editPhone);
        editAddress = inflatedView.findViewById(R.id.editAddress);
        editPhonepe = inflatedView.findViewById(R.id.editPhonepe);
        editDebitCard = inflatedView.findViewById(R.id.editDebitCard);
        editPaytm = inflatedView.findViewById(R.id.editPaytm);
        tickFab = inflatedView.findViewById(R.id.tickFab);

        // get device size
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        //mDeviceHeight = size.y;
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // set height depends on the device size
        popWindow = new PopupWindow(inflatedView, width,height-60, true );
        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popWindow.setAnimationStyle(R.style.PopupAnimation);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(inflatedView, Gravity.BOTTOM, 0,100);

        cancelEdit.setOnClickListener(v -> {
            popWindow.dismiss();
        });

        if(!(phoneNo.getText().toString().equals("No Phone Number Added")))
            editPhone.setText(phoneNo.getText().toString());
        if((!address.getText().toString().equals("No Address added")))
            editAddress.setText(address.getText().toString());
        if(!(phonepe.getText().toString().equals("Link UPI")))
            editPhonepe.setText(phonepe.getText().toString());
        if (!(debitCard.getText().toString().equals("Link Debit/Credit Card")))
            editDebitCard.setText(debitCard.getText().toString());
        if (!(paytm.getText().toString().equals("Link Paytm")))
            editPaytm.setText(paytm.getText().toString());

        tickFab.setOnClickListener(v -> {
            //phone number
            if (editPhone.getText().toString().length()<10 && !(editPhone.getText().toString().isEmpty())) {
                editPhone.setError("Invalid Phone Number");
                phoneNumer="No Phone Number Added";
            }
            else if(editPhone.getText().toString().isEmpty())
                phoneNumer="No Phone Number Added";
            else
                phoneNumer=editPhone.getText().toString();

            //address
            if(editAddress.getText().toString().isEmpty())
                home="No Address added";
            else
                home=editAddress.getText().toString();

            //UPI
            if(editPhonepe.getText().toString().isEmpty())
                upi="Link UPI";
            else
                upi=editPhonepe.getText().toString();

            //Debit Card
            if(editDebitCard.getText().toString().isEmpty())
                card="Link Debit/Credit Card";
            else if (editDebitCard.getText().toString().length()<16 && !(editDebitCard.getText().toString().isEmpty())) {
                editDebitCard.setError("Invalid Card Number");
                card = "Link Debit/Credit Card";
            }
            else
                card=editDebitCard.getText().toString();

            //Paytm
            if(editPaytm.getText().toString().isEmpty())
                pay="Link Paytm";
            else if (editPaytm.getText().toString().length()<10 && !(editPaytm.getText().toString().isEmpty())) {
                editPaytm.setError("Invalid Number");
                pay = "Link Paytm";
            }
            else
                pay=editPaytm.getText().toString();



            UserProfile userProfile = new UserProfile(uid,phoneNumer,wallet,upi,card,pay,home);

            databaseReference.child("UserProfile").child(uid).setValue(userProfile)
                    .addOnSuccessListener(aVoid -> {
                        popWindow.dismiss();
                        Toast.makeText(this, "Any Changes will be saved", Toast.LENGTH_SHORT).show();
                    });


        });




    }

    //gets user details
    public void getUserDetails(String uid){
        databaseReference.child("UserProfile").orderByChild("uid").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserProfile userProfile1 = null;
                        if(dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                userProfile1=dataSnapshot1.getValue(UserProfile.class);
                            }

                            if (userProfile1.getPhone().equals("0"))
                                phoneNo.setText("No Phone Number Added");
                            else
                                phoneNo.setText(userProfile1.getPhone());
                            if(userProfile1.getHome().equals("null"))
                                address.setText("No Address added");
                            else
                                address.setText(userProfile1.getHome());
                            if(userProfile1.getUpi().equals("0"))
                                phonepe.setText("Link UPI");
                            else
                                phonepe.setText(userProfile1.getUpi());
                            if(userProfile1.getCard().equals("0"))
                                debitCard.setText("Link Debit/Credit Card");
                            else
                                debitCard.setText(userProfile1.getCard());
                            if(userProfile1.getPaytm().equals("0"))
                                paytm.setText("Link Paytm");
                            else
                                paytm.setText(userProfile1.getPaytm());
                            walletAmount.setText("₹"+userProfile1.getWallet());
                            wallet=userProfile1.getWallet();
                        }
                        else {
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(findViewById(android.R.id.content), ""+databaseError.getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                });


    }

    //adds money to wallet
    public void addMoneyWallet(String uid){

        walletLayout.setEnabled(true);

        EditText refillAmount;
        ImageView cancelRefill;
        FloatingActionButton doneFillup;

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflatedView = layoutInflater.inflate(R.layout.wallet_add_popup, null,false);

        refillAmount=inflatedView.findViewById(R.id.refillAmount);
        cancelRefill=inflatedView.findViewById(R.id.cancelRefill);
        doneFillup=inflatedView.findViewById(R.id.doneFillup);


        // get device size
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        //mDeviceHeight = size.y;
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // set height depends on the device size
        popWindow = new PopupWindow(inflatedView, width,height-60, true );
        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popWindow.setAnimationStyle(R.style.PopupAnimation);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(inflatedView, Gravity.BOTTOM, 0,100);


        cancelRefill.setOnClickListener(v -> {
            popWindow.dismiss();
            walletLayout.setEnabled(true);
        });

        doneFillup.setOnClickListener(v -> {
            if(refillAmount.getText().toString().isEmpty())
                refillAmount.setError("Enter a valid amount");
            else {
                int amount = Integer.parseInt(refillAmount.getText().toString());
                databaseReference.child("UserProfile").child(uid).child("wallet").setValue(amount+wallet)
                        .addOnSuccessListener(m->{
                            popWindow.dismiss();
                            Toast.makeText(ProfileActivity.this, "Money Added Successfully", Toast.LENGTH_SHORT).show();
                        });
            }
            walletLayout.setEnabled(true);

        });

    }

    //intent for image choose
    public void uploadUserImage(){
        // Intent to photo picker
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //uploads image to database
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child(user.getUid()+"/profile.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        //if the upload is successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();
                        getImage();
                        Toast.makeText(getApplicationContext(), "Profile Photo Updated", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(exception -> {
                        //if the upload is not successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        //and displaying error message
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(this, "Error Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    //get Image
    public void getImage(){
        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference(user.getUid()+"/profile.jpg");
        final long ONE_MEGABYTE = 1024 * 1024*10;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);

                    profileImageView.setMinimumHeight(dm.heightPixels);
                    profileImageView.setMinimumWidth(dm.widthPixels);
                    profileImageView.setImageBitmap(bm);

                })

                .addOnFailureListener(exception -> {
                    // Handle any errors
                    profileImageView.setImageResource(R.drawable.engine);
                });

    }



}
