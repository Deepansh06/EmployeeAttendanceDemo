package com.example.employeeattendancedemo.model;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.employeeattendancedemo.R;
import com.example.employeeattendancedemo.databinding.ActivityLoginBinding;
import com.example.employeeattendancedemo.model.helper.DatabaseHelper;
import com.example.employeeattendancedemo.model.model.Attendance;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity
{
    private final AppCompatActivity activity = LoginActivity.this;

    ActivityLoginBinding binding;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        databaseHelper = new DatabaseHelper(activity);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFromSQLite();
            }
        });
        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        binding.txtEmpList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(LoginActivity.this, AllEmpActivity.class);
                startActivity(intentRegister);
            }
        });
    }

    private void verifyFromSQLite()
    {
        if (TextUtils.isEmpty(binding.etEmpCode.getText().toString()))
        {
            binding.etEmpCode.setError("Enter emp code");
        }
        if (TextUtils.isEmpty(binding.etPassword.getText().toString()))
        {
            binding.etEmpCode.setError("Enter emp code");
        }
        if (databaseHelper.authenticateEmp(binding.etEmpCode.getText().toString(), binding.etPassword.getText().toString()))
        {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

            Attendance attendance = new Attendance();
            attendance.setEmp_code(binding.etEmpCode.getText().toString().trim());
            attendance.setTime(time);
            attendance.setDate(date);

            databaseHelper.insertAttendanceData(attendance);

            MediaPlayer mPlayer = MediaPlayer.create(LoginActivity.this, R.raw.thank);
            mPlayer.start();

            Toast.makeText(activity, "Thank you!", Toast.LENGTH_SHORT).show();
            emptyInputEditText();
        }
        else
        {
            MediaPlayer mPlayer = MediaPlayer.create(LoginActivity.this, R.raw.try_again);
            mPlayer.start();
            Toast.makeText(activity, "Invalid user", Toast.LENGTH_SHORT).show();
        }
    }
    private void emptyInputEditText() {
        binding.etEmpCode.setText(null);
        binding.etPassword.setText(null);
    }

}
