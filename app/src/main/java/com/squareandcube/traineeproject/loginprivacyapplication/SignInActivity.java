package com.squareandcube.traineeproject.loginprivacyapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SignInActivity extends AppCompatActivity {
    DatabaseHelper db;
    ImageView imageView;
    TextView firstname, lastname, dob, email, phone;
    String intent_email;
    String mFirstName, mLastName, mDob, mEmail, mPhone, mPassword,mimage;
    CondidateRegisterModel candidateModel;
    DatePickerDialog mDatePickerDialog;
    Bitmap mbimtap;
    private static final int PICK_IMAGE_REQUEST = 234;

    Button updateBtn, editBtn;
    ToggleButton mTbtn;

    private boolean loggedIn = false;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      // setSupportActionBar(toolbar);

        db = new DatabaseHelper(this);
        firstname = (TextView) findViewById(R.id.firstnamesign);
        lastname = (TextView) findViewById(R.id.lastnamesign);
        dob = (TextView) findViewById(R.id.dobnamesign);
        email = (TextView) findViewById(R.id.emailnamesign);
        phone = (TextView) findViewById(R.id.phonenamesign);
        imageView = (ImageView) findViewById(R.id.imageViewsignIn);

        disableEditText();
        candidateModel = new CondidateRegisterModel();

        updateBtn = (Button) findViewById(R.id.updatebtn);
        editBtn = (Button) findViewById(R.id.editbtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtn.setVisibility(View.INVISIBLE);
                enableEditText();
                updateBtn.setVisibility(View.VISIBLE);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatecandidate_profile();
                checkEmpty();
            }
        });
        dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (dob.getRight() - dob.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Calendar calendar = Calendar.getInstance();
                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                        mDatePickerDialog = new DatePickerDialog(SignInActivity.this, R.style.datepickertheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dob.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                        mDatePickerDialog.show();
                    }
                }

                return false;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(Utility.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Utility.LOGGEDIN_SHARED_PREF, false);

        intent_email= sharedPreferences.getString(Utility.SHARED_PREF_CANDIDATE_EMAIL,"");

        getCandidateDetails();


    }


    public void disableEditText() {
        firstname.setEnabled(false);
        lastname.setEnabled(false);
        dob.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
      //  imageView.setEnabled(false);
    }

    public void enableEditText() {
        firstname.setEnabled(true);
        lastname.setEnabled(true);
        dob.setEnabled(true);
        email.setEnabled(true);
        phone.setEnabled(true);
      //  imageView.setEnabled(true);
    }


    public void getCandidateDetails() {
        List<CondidateRegisterModel> list = new ArrayList<>();
        list = db.getUserDetail(intent_email);

        user_id = list.get(0).getUser_Id();
        firstname.setText(list.get(0).getFirstName());
        lastname.setText(list.get(0).getLastName());
        dob.setText(list.get(0).getDOB());
        email.setText(list.get(0).getEmail());
        phone.setText(list.get(0).getPhoneNo());
       // byte[] imagecode = Base64.decode(list.get(0).getImage(), Base64.DEFAULT);
//        Bitmap decodebitmap = BitmapFactory.decodeByteArray(imagecode, 0, imagecode.length);
   //    imageView.setImageBitmap(decodebitmap);
        byte[] imagecode = Base64.decode(list.get(0).getImage(), Base64.DEFAULT);
        setUpImage(imagecode);

       // byte[] imagecode = Base64.decode(list.get(0).getImage(), Base64.DEFAULT);
       // Bitmap decodebitmap = BitmapFactory.decodeByteArray(imagecode, 0, imagecode.length);
       // imageView.setImageBitmap(decodebitmap);
      //  setUpImage(imagecode);

    }

    public void updatecandidate_profile() {

        mFirstName = firstname.getText().toString();
        mLastName = lastname.getText().toString();
        mDob = dob.getText().toString();
        mEmail = email.getText().toString();
        mPhone = phone.getText().toString();


        candidateModel.setUser_Id(user_id);
        candidateModel.setFirstName(mFirstName);
        candidateModel.setLastName(mLastName);
        candidateModel.setDOB(mDob);
        candidateModel.setEmail(mEmail);
        candidateModel.setPhoneNo(mPhone);
       // candidateModel.setImage(mimage);


        db.updateUser(candidateModel);

        editBtn.setVisibility(View.VISIBLE);
        disableEditText();

    }

//    public String getStringImage(Bitmap bmp){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 85, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }
//
//    private void requestCameraPermission() {
//
//        ActivityCompat.requestPermissions(SignInActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                0);
//
//    }
//    public void selectProfileImage() {
//
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            requestCameraPermission();
//            gallery();
//        } else {
//            gallery();
//        }
//    }
//
//        public void gallery()
//        {
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//        }

    private void setUpImage(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        imageView.setImageBitmap(circleBitmap);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder d = new AlertDialog.Builder(SignInActivity.this, R.style.alertdialogtheme);
        d.setTitle("Exit");
        d.setMessage("are you sure want to exit?");
        d.setIcon(R.drawable.ic_warning);
        d.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SignInActivity.this, "Cancelled", Toast.LENGTH_LONG).show();

            }
        });
        d.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();

            }
        });
        d.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.logout1:
                new AlertDialog.Builder(this, R.style.alertdialogtheme)
                        .setTitle("Logout")
                        .setMessage("Do you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences preferences = getSharedPreferences(Utility.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putBoolean(Utility.LOGGEDIN_SHARED_PREF, false);

                                editor.putString(Utility.SHARED_PREF_CANDIDATE_EMAIL, "");

                                editor.commit();

                                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                break;

//            case R.id.imageViewsignIn:
//                isImageClicked = true;
//                selectProfileImage();
//                break;
                }

        return super.onOptionsItemSelected(item);
    }

    public void checkEmpty(){
        mFirstName = firstname.getText().toString();
        mLastName=lastname.getText().toString();
        mDob=dob.getText().toString();
        mEmail=email.getText().toString();
        mPhone=phone.getText().toString();

        if (TextUtils.isEmpty(mFirstName)){
            firstname.setError("Yout First Name");
            updateBtn.setEnabled(false);

        }
        if (TextUtils.isEmpty(mLastName)){
            lastname.setError("Your Last Name");
            updateBtn.setEnabled(false);

        }
        if (TextUtils.isEmpty(mDob)){
            dob.setError("Your Date of birth");
            updateBtn.setEnabled(false);
        }
        if (TextUtils.isEmpty(mEmail)){
            email.setError("your Email Address");
            updateBtn.setEnabled(false);
        }
        if (TextUtils.isEmpty(mPhone)){
            phone.setError("Your Phone Number");
            updateBtn.setEnabled(false);
        }

        else {
            updateBtn.setEnabled(true);
        }
    }

}
