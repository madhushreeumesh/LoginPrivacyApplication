package com.squareandcube.traineeproject.loginprivacyapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout inputFirstName,inputLastName,inputDOB,inputEmail,inputPassword,inputPhoneNo;

    TextInputEditText FirstName,LastName,Dob,email,password,phoneno;
    String mFirstName,mLastName,mDOB,mEmail,mPassword,mPhone,mImage;
    ImageView imageView;
    Button register;
    DatePickerDialog mDatePickerDialog;

    private Uri filePath;
    private InputValidation inputValidation;
    Bitmap bitmap;
    CondidateRegisterModel user = new CondidateRegisterModel();
    private static final int PICK_IMAGE_REQUEST = 234;

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Dob.setOnTouchListener(this);

        db = new DatabaseHelper(this);

        inputFirstName = (TextInputLayout)findViewById(R.id.textInputLayoutFirstName);
        inputLastName = (TextInputLayout)findViewById(R.id.textInputLayoutLastName);
        inputDOB = (TextInputLayout)findViewById(R.id.textInputLayoutDOB);
        inputEmail = (TextInputLayout)findViewById(R.id.textInputLayoutEmail);
        inputPassword = (TextInputLayout)findViewById(R.id.textInputLayoutPassword);
        inputPhoneNo = (TextInputLayout)findViewById(R.id.textInputLayoutPhoneNo);
//        inputFirstName = (TextInputLayout)findViewById(R.id.textInputLayoutFirstName);

        FirstName = (TextInputEditText) findViewById(R.id.textInputEditTextFirstName);
        LastName = (TextInputEditText) findViewById(R.id.textInputEditTextLastName);
        Dob= (TextInputEditText) findViewById(R.id.textInputEditTextDOB);
        email = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        password = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        phoneno = (TextInputEditText) findViewById(R.id.textInputEditTextPhoneNo);
        imageView = (ImageView)findViewById(R.id.imageView);
        register =(Button)findViewById(R.id.appCompatButtonRegister);
        inputValidation = new InputValidation(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfileImage();
            }
        });

        Dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final  int DRAWABLE_RIGHT=2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=(Dob.getRight()-Dob.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        Calendar calendar = Calendar.getInstance();
                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                        mDatePickerDialog = new DatePickerDialog(RegisterActivity.this,R.style.datepickertheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Dob.setText(day+"/"+(month+1)+"/"+year);
                            }
                        },mYear,mMonth,mDay);
                        mDatePickerDialog.show();
                    }
                }

                return false;
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCandidate();
//                Intent i2=new Intent(RegisterActivity.this,SignInActivity.class);
//                startActivity(i2);
//                finish();
            }
        });

//        Intent intent= new Intent(RegisterActivity.this,SignInActivity.class);
//        startActivity(intent);
    }



    private void requestCameraPermission() {


        // Camera permission has not been granted yet. Request it directly.
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                0);

    }

    public void selectProfileImage(){

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestCameraPermission();
        }
        else {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 4;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.d("filepath",filePath.getPath());
                Log.d("Bitmap", String.valueOf(bitmap));

                Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);

                imageView.setImageBitmap(circleBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void registerCandidate()
    {


        if (!inputValidation.isInputEditTextFilled(FirstName, inputFirstName, getString(R.string.error_message_first_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(LastName, inputLastName, getString(R.string.error_message_last_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(Dob, inputDOB, getString(R.string.error_message_date_of_birth))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(email, inputEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(email, inputEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(phoneno, inputPhoneNo, getString(R.string.error_message_phone))) {
            return;
        }
        if (!inputValidation.isInputEditTextPhone(phoneno, inputPhoneNo, getString(R.string.error_message_phone))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(password, inputPassword, getString(R.string.error_message_password))) {
            return;
        }
        if(bitmap==null){
            Toast.makeText(getApplicationContext(),"select image",Toast.LENGTH_SHORT).show();
            return;
        }



        if(db.checkUser(email.getText().toString()))
        {
            Log.d("Email check","Already exits");
            Toast.makeText(getApplicationContext(),"Already exits",Toast.LENGTH_SHORT).show();
        }else {
            user.setFirstName(FirstName.getText().toString());
            user.setLastName(LastName.getText().toString());
            user.setDOB(Dob.getText().toString());
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());
            user.setPhoneNo(phoneno.getText().toString());
            user.setImage(getStringImage(bitmap));
            db.addUser(user);
            Toast.makeText(getApplicationContext(),"Register Successfull",Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);

        }
    }
}
