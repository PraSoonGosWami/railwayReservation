package com.invaderx.railway.auth;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.invaderx.railway.R;
import com.invaderx.railway.activity.TrainDetailsActivity;
import com.invaderx.railway.activity.TrainSearchActivity;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;

public class AdminLogin extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    EditText editTextEmail;
    EditText editTextPassword;
    CircularProgressButton loginButton;
    TextView userLogin;
    final String adminUid = "QrouSopdUsQMR9hgNghOwofdmSl2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton= findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(this);
        //User Login
        userLogin= findViewById(R.id.user_login_text_view);
        userLogin.setOnClickListener(view -> {
            startActivity(new Intent(AdminLogin.this,LoginActivity.class));
        });
        editTextEmail.setTranslationX(1000f);
        editTextPassword.setTranslationX(1000f);
        loginButton.setTranslationX(1000f);
        viewAnimator();


    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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

        loginButton.startAnimation();
        loginButton.setFinalCornerRadius(200f);
        loginButton.setInitialCornerRadius(200f);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                String uId= user.getUid();
                if(uId.equals(adminUid)) {
                    loginButton.dispose();
                    Intent intent = new Intent(AdminLogin.this, TrainDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else{
                    FirebaseAuth.getInstance().signOut();
                    loginButton.revertAnimation(() -> {
                        loginButton.setFinalCornerRadius(200f);
                        loginButton.setInitialCornerRadius(200f);
                    });
                    Toast.makeText(getApplicationContext(),"Invalid Admin details,Please Enter Correct details",Toast.LENGTH_SHORT).show();
                }
            } else {
                loginButton.revertAnimation(() -> {
                    loginButton.setFinalCornerRadius(200f);
                    loginButton.setInitialCornerRadius(200f);
                });
                Toast.makeText(getApplicationContext(),"Invalid Admin details,Please Enter Correct details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginButton.setTranslationX(1000f);
        editTextEmail.setTranslationX(1000f);
        editTextPassword.setTranslationX(1000f);
        viewAnimator();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, TrainSearchActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginButton.setTranslationX(1000f);
        editTextEmail.setTranslationX(1000f);
        editTextPassword.setTranslationX(1000f);
        viewAnimator();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, TrainSearchActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonLogin:
                userLogin();
                break;
        }
    }


    public void viewAnimator(){
        ObjectAnimator loginButtonAnim = ObjectAnimator.ofFloat(loginButton, "translationX",1000f,0f);
        loginButtonAnim.setDuration(500);
        loginButtonAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        loginButtonAnim.start();

        ObjectAnimator emailAnim = ObjectAnimator.ofFloat(editTextEmail, "translationX",1000f,0f);
        emailAnim.setDuration(300);
        emailAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        emailAnim.start();

        ObjectAnimator passwordAnim = ObjectAnimator.ofFloat(editTextPassword, "translationX",1000f,0f);
        passwordAnim.setDuration(400);
        passwordAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        passwordAnim.start();
    }

}
