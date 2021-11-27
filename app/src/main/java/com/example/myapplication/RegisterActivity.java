package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Sign up");
    }

    public void btnRegisterOnClick(View view) {
        EditText txtName = (EditText) findViewById(R.id.txtName);
        EditText txtAge = (EditText) findViewById(R.id.txtAge);
        EditText txtEmail = (EditText) findViewById(R.id.txtEmail);

        String name = txtName.getText().toString();
        String age = txtAge.getText().toString();
        String email = txtEmail.getText().toString();

        boolean isValid = true;

        if (name.trim().isEmpty()) {
            txtName.setError("Name is required.");
            isValid = false;
        }

        if (age.trim().isEmpty()) {
            txtAge.setError("Age is required.");
            isValid = false;
        }

        if (email.trim().isEmpty()) {
            txtEmail.setError("Email is required.");
            isValid = false;
        }
        else if (!email.matches("^[\\w+\\\\.?\\w+]+@[a-zA-Z_]+?\\.[a-zA-Z]+$")) {
            txtEmail.setError("Email format is invalid.");
            isValid = false;
        }

        if (!isValid) return;

        Gson gson = new Gson();
        RegisterData registerData = new RegisterData();
        registerData.name = name;
        registerData.age = Integer.parseInt(age);
        registerData.email = email;
        String jsonRegisterData = gson.toJson(registerData);

        Toast.makeText(getApplicationContext(), jsonRegisterData, Toast.LENGTH_LONG).show();

        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}