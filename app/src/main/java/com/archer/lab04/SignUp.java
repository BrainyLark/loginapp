package com.archer.lab04;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private String username;
    private String password;

    private EditText repeatpassField;
    private EditText dateField;
    private EditText ageField;
    private RadioGroup genderGroup;
    private EditText numberField;

    public void initialize() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        repeatpassField = (EditText) findViewById(R.id.repeatpassField);
        dateField = (EditText) findViewById(R.id.dateField);
        ageField = (EditText) findViewById(R.id.ageField);
        genderGroup = (RadioGroup) findViewById(R.id.radioGroup);
        numberField = (EditText) findViewById(R.id.numberField);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize();
    }

    public void invokeSaveButton (View v) {
        if(!isValid()) {
            Toast.makeText(SignUp.this, "Шаардлагатай талбарууд бөглөгдөөгүй байна, сайтар нягтлаад дахин илгээнэ үү!", Toast.LENGTH_LONG).show();
            return;
        }
        if(!isPasswordOk()) {
            Toast.makeText(SignUp.this, "Таны дахин оруулсан нууц үг өмнөхтэй таарахгүй байна, дахин оролдоно уу!", Toast.LENGTH_LONG).show();
            return;
        }

        if(!isAcceptableName(username)) {
            Toast.makeText(SignUp.this, "Ийм нэртэй хэрэглэгч бүртгэлтэй байна! Өөр нэр сонгоно уу!", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = dateField.getText().toString();
        int age = Integer.parseInt(ageField.getText().toString());
        RadioButton genderBtn = (RadioButton) findViewById(genderGroup.getCheckedRadioButtonId());
        String sex = genderBtn.getText().toString();
        String phone = numberField.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERNAME, username);
        contentValues.put(DatabaseHelper.PASSWORD, password);
        contentValues.put(DatabaseHelper.DATE, date);
        contentValues.put(DatabaseHelper.AGE, age);
        contentValues.put(DatabaseHelper.GENDER, sex);
        contentValues.put(DatabaseHelper.PHONE, phone);

        getContentResolver().insert(UserProvider.CONTENT_URI, contentValues);
        Toast.makeText(SignUp.this, "Та амжилттай бүртгүүллээ", Toast.LENGTH_SHORT).show();
        finish();

    }

    public void invokeCancelButton (View v) {
        finish();
    }

    public boolean isPasswordOk() {
        if(!password.equals(repeatpassField.getText().toString()))
            return false;
        return true;
    }

    public boolean isValid() {
        if(repeatpassField.getText().toString().matches("") ||
                dateField.getText().toString().matches("") ||
                ageField.getText().toString().matches("") ||
                genderGroup.getCheckedRadioButtonId() == -1 ||
                numberField.getText().toString().matches("")) {
            return false;
        }
        return true;
    }

    public boolean isAcceptableName(String uname) {
        Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI, new String[] {"id"}, "username = ?", new String[] {uname}, null);
        if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        return true;
    }


}
