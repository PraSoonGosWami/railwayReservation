package com.invaderx.railway.auth;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.invaderx.railway.R;
import com.invaderx.railway.activity.TrainSearchActivity;
import com.invaderx.railway.models.UserProfile;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextName;
    CircularProgressButton signup;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    String name;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        //database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();

        signup=findViewById(R.id.buttonSignUp);
        signup.setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);

        editTextEmail.setTranslationX(1000f);
        editTextPassword.setTranslationX(1000f);
        editTextName.setTranslationX(1000f);
        signup.setTranslationX(1000f);
        viewAnimator();
    }

    private void registerUser() {
        name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        signup.startAnimation();
        signup.setFinalCornerRadius(200f);
        signup.setInitialCornerRadius(200f);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    signup.dispose();
                    //Adding user name
                    user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();
                    user.updateProfile(profileUpdates);

                    userDeatails(user);
                    //User Created
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();

                } else {
                    signup.revertAnimation(() -> {
                        signup.setFinalCornerRadius(200f);
                        signup.setInitialCornerRadius(200f);
                    });
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    public void viewAnimator(){
        ObjectAnimator loginButtonAnim = ObjectAnimator.ofFloat(signup, "translationX",1000f,0f);
        loginButtonAnim.setDuration(600);
        loginButtonAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        loginButtonAnim.start();

        ObjectAnimator emailAnim = ObjectAnimator.ofFloat(editTextEmail, "translationX",1000f,0f);
        emailAnim.setDuration(500);
        emailAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        emailAnim.start();

        ObjectAnimator passwordAnim = ObjectAnimator.ofFloat(editTextPassword, "translationX",1000f,0f);
        passwordAnim.setDuration(400);
        passwordAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        passwordAnim.start();

        ObjectAnimator nameAnim = ObjectAnimator.ofFloat(editTextName, "translationX",1000f,0f);
        nameAnim.setDuration(300);
        nameAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        nameAnim.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        editTextEmail.setTranslationX(1000f);
        editTextPassword.setTranslationX(1000f);
        editTextName.setTranslationX(1000f);
        signup.setTranslationX(1000f);
        viewAnimator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextEmail.setTranslationX(1000f);
        editTextPassword.setTranslationX(1000f);
        editTextName.setTranslationX(1000f);
        signup.setTranslationX(1000f);
        viewAnimator();
    }

    public void userDeatails(FirebaseUser firebaseUser){
        UserProfile userProfile=new UserProfile(firebaseUser.getUid(),"0",0,"0","0","0","null");
        databaseReference.child("UserProfile").child(firebaseUser.getUid()).setValue(userProfile)
                .addOnSuccessListener(aVoid -> Toast.makeText(SignUpActivity.this, "You are registered, Now Login", Toast.LENGTH_SHORT).show());
    }
}

