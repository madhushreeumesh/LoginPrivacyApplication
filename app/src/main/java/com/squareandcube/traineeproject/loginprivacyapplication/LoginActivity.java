package com.squareandcube.traineeproject.loginprivacyapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button mButton;

    private final AppCompatActivity activity = LoginActivity.this;
    private NestedScrollView mNestedScrollView;
    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutPassword;
    private TextInputEditText mTextInputEditTextEmail;
    private TextInputEditText mTextInputEditTextPassword;
    private AppCompatButton mAppCompatButtonLogin;
    private AppCompatTextView mTextViewLinkRegister;
    private CheckBox mcheckbox;

    DatabaseHelper db;
    private boolean loggedIn = false;
    String mEmail,mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mButton=(Button)findViewById(R.id.appCompatButtonLogin);
        db = new DatabaseHelper(this);

        List<CondidateRegisterModel> list = new ArrayList<>();
        list= db.getAllUser();
        for(int i=0;i<list.size();i++){
            Log.d("User and password",list.get(i).getEmail()+" "+list.get(i).getPassword());
        }

        mTextInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmaillogin);
        mTextInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPasswordlogin);
        mAppCompatButtonLogin = (AppCompatButton)findViewById(R.id.appCompatButtonLogin);
        mTextViewLinkRegister = (AppCompatTextView)findViewById(R.id.textViewLinkRegister);
        mcheckbox=(CheckBox)findViewById(R.id.ch_rememberme);
        initListeners();

    }


    private void initListeners() {
        mAppCompatButtonLogin.setOnClickListener(this);
        mTextViewLinkRegister.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(Utility.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        loggedIn = sharedPreferences.getBoolean(Utility.LOGGEDIN_SHARED_PREF, false);
        if(loggedIn){
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);

            finish();
        }

    }
    @Override
    public void onBackPressed(){

        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.appCompatButtonLogin:

                mEmail= mTextInputEditTextEmail.getText().toString();
                mPassword = mTextInputEditTextPassword.getText().toString();

                if(mEmail.equalsIgnoreCase("")||mPassword.equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Email and Password",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (db.checkUser(mEmail, mPassword)) {
                            SharedPreferences sharedPreferences = getSharedPreferences(Utility.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putBoolean(Utility.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Utility.SHARED_PREF_CANDIDATE_EMAIL, mEmail);

                            //Saving values to editor
                            editor.commit();


                            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                            intent.putExtra("email", mEmail);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                        }
                    else {
                        Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case  R.id.textViewLinkRegister:
                Intent registerintent= new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerintent);
                break;

        }

    }


}
