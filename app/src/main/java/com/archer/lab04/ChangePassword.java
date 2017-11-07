package com.archer.lab04;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    private String username;
    private String password;

    private String prevpass;
    private String newpass;
    private String reppass;

    private EditText prevpassField;
    private EditText newpassField;
    private EditText reppassField;

    public void initialize() {
        Intent data = getIntent();
        username = data.getStringExtra("username");
        password = data.getStringExtra("password");

        prevpassField = (EditText) findViewById(R.id.prevPassField);
        newpassField = (EditText) findViewById(R.id.newPassField);
        reppassField = (EditText) findViewById(R.id.repPassField);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initialize();
    }

    public void changePasswordButton(View v) {
        prevpass = prevpassField.getText().toString();
        newpass = newpassField.getText().toString();
        reppass = reppassField.getText().toString();

        if(!prevpass.equals(password)) {
            Toast.makeText(this, "Хуучин нууц үг тохирохгүй байна, дахин оролдоно уу!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!newpass.equals(reppass)) {
            Toast.makeText(this, "Шинэ нууц үгийг буруу давтаж оруулсан байна, дахин хянана уу!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(newpass.equals(password)) {
            Toast.makeText(this, "Шинэ нууц үг хуучин нууц үг хоёр ялгаагүй байна, нууц үгэнд өөрчлөлт орохгүй!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues updateValues = new ContentValues();
        updateValues.put(DatabaseHelper.PASSWORD, newpass);

        getContentResolver().update(UserProvider.CONTENT_URI, updateValues, "username = ?", new String[] {username});
        Toast.makeText(this, "Нууц үг амжилттай солигдлоо!", Toast.LENGTH_SHORT).show();
        finish();

    }
}
