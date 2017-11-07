package com.archer.lab04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    private EditText usernameField;
    private EditText passwordField;

    public void initialize() {
        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String lastUser = sharedPreferences.getString("lastUsername", null);
        usernameField.setText(lastUser);
    }

    public void onClickLoginBtn(View v) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Шаардлагатай талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isNameThere(username)) {
            Toast.makeText(this, "Ийм нэртэй хэрэглэгч бүртгэлгүй байна. Та дахин оролдоно уу!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isPassMatch(username, password)) {
            Toast.makeText(this, "Таны нууц үг буруу байна. Дахин оролдоно уу!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastUsername", username);
        editor.commit();

        Intent data = new Intent(MainActivity.this, UserInfo.class);
        data.putExtra("username", username);
        data.putExtra("password", password);

        startActivity(data);
    }

    public void onClickSignupBtn(View v) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Шаардлагатай талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent(MainActivity.this, SignUp.class);
        data.putExtra("username", username);
        data.putExtra("password", password);
        startActivity(data);
    }

    public boolean isPassMatch(String username, String password) {
        Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI, new String[] {"password"}, "username = ?", new String[] {username}, null);

        if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            return (password.equals(cursor.getString(0))) ? true:false;
        }

        return false;
    }

    public boolean isNameThere(String username) {
        Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI, new String[] {"id"}, "username = ?", new String[] {username}, null);
        return (cursor != null && cursor.getCount() > 0) ? true : false;
    }
}
