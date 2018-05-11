package com.squareandcube.traineeproject.loginprivacyapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mlogin=(Button)findViewById(R.id.login);
        Button mregister=(Button)findViewById(R.id.register);
        Button madmin=(Button)findViewById(R.id.admin);

        mlogin.setOnClickListener(this);
        mregister.setOnClickListener(this);
        madmin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Toast.makeText(this, "first button click", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.register:
                Toast.makeText(this, "first button click", Toast.LENGTH_LONG).show();
                Intent inten = new Intent(this, RegisterActivity.class);
                startActivity(inten);
                break;
            case R.id.admin:
                Toast.makeText(this, "first button click", Toast.LENGTH_LONG).show();
                Intent inte = new Intent(this, AdminActivity.class);
                startActivity(inte);
                break;
        }
    }

    @Override
    public void onBackPressed(){

        finishAffinity();
    }

}
