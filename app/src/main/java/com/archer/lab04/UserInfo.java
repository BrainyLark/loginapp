package com.archer.lab04;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfo extends AppCompatActivity {

    private String username;
    private String password;
    private int age;
    private String num;
    private String birthdate;
    private String gender;

    private TextView usernameView;
    private EditText ageEditText;
    private TextView genderView;
    private EditText numberEditText;
    private EditText dateEditText;

    public void initialize() {
        usernameView = (TextView) findViewById(R.id.usernameView);
        ageEditText = (EditText) findViewById(R.id.ageUserInfo);
        genderView = (TextView) findViewById(R.id.genderUserInfo);
        numberEditText = (EditText) findViewById(R.id.numberUserInfo);
        dateEditText = (EditText) findViewById(R.id.dateUserInfo);

        Intent data = getIntent();
        username = data.getStringExtra("username");
        password = data.getStringExtra("password");

        setDataFromDatabase();
    }

    public void setDataFromDatabase() {

        Cursor result = getContentResolver().query(UserProvider.CONTENT_URI, null, "username = ?", new String[] {username}, null);
        if(result.moveToFirst()) {
            age = result.getInt(result.getColumnIndex("age"));
            num = result.getString(result.getColumnIndex("phone"));
            birthdate = result.getString(result.getColumnIndex("date"));
            gender = result.getString(result.getColumnIndex("gender"));
        }
        usernameView.setText(username);
        ageEditText.setText(Integer.toString(age));
        genderView.setText(gender);
        numberEditText.setText(num);
        dateEditText.setText(birthdate);

    }

    public void getDataFromFields() {
        age = Integer.parseInt(ageEditText.getText().toString());
        num = numberEditText.getText().toString();
        birthdate = dateEditText.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initialize();

    }

    public void updateInformationButton(View v) {
        getDataFromFields();
        ContentValues updateValues = new ContentValues();
        updateValues.put(DatabaseHelper.AGE, age);
        updateValues.put(DatabaseHelper.DATE, birthdate);
        updateValues.put(DatabaseHelper.PHONE, num);

        getContentResolver().update(UserProvider.CONTENT_URI, updateValues, "username = ?", new String[] {username});
        Toast.makeText(this, "Амжилттай хадгалагдлаа!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void changePasswordButton(View v) {
        Intent data = new Intent(UserInfo.this, ChangePassword.class);
        data.putExtra("username", username);
        data.putExtra("password", password);
        startActivity(data);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exitMenuItem:
                finish();
                return true;
        }
        return false;
    }
}
