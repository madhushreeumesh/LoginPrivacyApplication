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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener{

    Button mButton;

    private final AppCompatActivity activity = AdminActivity.this;
    private NestedScrollView mNestedScrollView;
    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutPassword;
    private TextInputEditText EditTextEmail;
    private TextInputEditText EditTextPassword;
    private AppCompatButton mAppCompatButtonLogin;
    private AppCompatTextView mTextViewLinkRegister;
    static String admin;
    static String password;

    DatabaseHelper db;
    private boolean loggedIn = false;
    String mEmail,mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mButton=(Button)findViewById(R.id.appCompatButtonLogin);
        db = new DatabaseHelper(this);

        List<CondidateRegisterModel> list = new ArrayList<>();
        list= db.getAllUser();
        for(int i=0;i<list.size();i++){
            Log.d("User and password",list.get(i).getEmail()+" "+list.get(i).getPassword());
        }

        EditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmaillogin);
        EditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPasswordlogin);
        mAppCompatButtonLogin = (AppCompatButton)findViewById(R.id.appCompatButtonLogin);
        initListeners();

    }


    private void initListeners() {
        mAppCompatButtonLogin.setOnClickListener(this);
    }

    @Override
    public void onBackPressed(){

        Intent intent=new Intent(AdminActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(Utility.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        loggedIn = sharedPreferences.getBoolean(Utility.LOGGEDIN_SHARED_PREF, false);
        if(loggedIn){
            Intent intent = new Intent(getApplicationContext(), UsersListActivity.class);
            startActivity(intent);

            finish();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.appCompatButtonLogin:

//                mEmail= EditTextEmail.getText().toString();
//                mPassword = EditTextPassword.getText().toString();
//                if(mEmail.equals(admin)&&mPassword.equals(password)) {

                    Intent intent = new Intent(AdminActivity.this, UsersListActivity.class);
                    intent.putExtra("email", mEmail);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
 //              }
// else {
//                    Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
//
//                }
                break;

        }

    }

}
