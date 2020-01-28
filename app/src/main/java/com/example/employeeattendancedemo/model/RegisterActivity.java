package com.example.employeeattendancedemo.model;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.employeeattendancedemo.R;
import com.example.employeeattendancedemo.databinding.ActivitySignupBinding;
import com.example.employeeattendancedemo.model.helper.DatabaseHelper;
import com.example.employeeattendancedemo.model.model.User;

import java.util.Calendar;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity
{

    private final AppCompatActivity activity = RegisterActivity.this;
    ActivitySignupBinding binding;
    private DatabaseHelper databaseHelper;
    private User user;
    private User user_intent;
    private Bundle bundle;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseHelper = new DatabaseHelper(activity);

        bundle = getIntent().getExtras();
        if(bundle != null)
        {
            getSupportActionBar().setTitle("Update Employee");

            user_intent = (User) bundle.getSerializable("emp");
            binding.etEmpCode.setText(user_intent.getEmp_code());

            binding.etEmpCode.clearFocus();
            binding.etEmpCode.setFocusable(false);
            binding.etEmpCode.setEnabled(false);

            binding.etEmpName.setText(user_intent.getName());
            binding.etEmpDob.setText(user_intent.getD_o_b());
            binding.etPassword.setText(user_intent.getPassword());
            binding.etConfPassword.setText(user_intent.getPassword());
            binding.etMobile.setText(user_intent.getMob());
            binding.etEmail.setText(user_intent.getEmail());

            binding.btnSignUp.setText("Update");
            binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateDataToSQLite();
                }
            });

        }
        else
        {
            getSupportActionBar().setTitle("Register Employee");

            user = new User();
            binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postDataToSQLite();
                }
            });
        }


        binding.etEmpDob.clearFocus();
        binding.etEmpDob.setFocusable(false);

        binding.etEmpDob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                binding.etEmpDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void postDataToSQLite()
    {
        if (TextUtils.isEmpty(binding.etEmpCode.getText().toString()))
        {
            binding.etEmpCode.setError("Please enter EMP code");
        }
        else if (TextUtils.isEmpty(binding.etEmpName.getText().toString()))
        {
            binding.etEmpName.setError("Please enter EMP name");
        }
        else if (TextUtils.isEmpty(binding.etEmpDob.getText().toString()))
        {
            binding.etEmpDob.setError("Please enter EMP date of birth");
        }
        else if (TextUtils.isEmpty(binding.etPassword.getText().toString()))
        {
            binding.etPassword.setError("Please enter EMP password");
        }
        else if (TextUtils.isEmpty(binding.etConfPassword.getText().toString()))
        {
            binding.etConfPassword.setError("Please retype password");
        }
        else if (!binding.etPassword.getText().toString().equals(binding.etConfPassword.getText().toString()))
        {
            binding.etConfPassword.setError("Please not matching");
        }
        else if (TextUtils.isEmpty(binding.etMobile.getText().toString()))
        {
            binding.etMobile.setError("Please enter EMP mobile no");
        }
        else if (!isValidMobile(binding.etMobile.getText().toString()))
        {
            binding.etMobile.setError("Please enter valid mobile no");
        }
        else if (TextUtils.isEmpty(binding.etEmail.getText().toString()))
        {
            binding.etEmail.setError("Please enter EMP email ID");
        }
        else if (!isValidMail(binding.etEmail.getText().toString()))
        {
            binding.etEmail.setError("Please enter valid email ID");
        }
        else if (!databaseHelper.checkEmp(binding.etEmpCode.getText().toString().trim()))
        {
            user.setEmp_code(binding.etEmpCode.getText().toString().trim());
            user.setName(binding.etEmpName.getText().toString().trim());
            user.setD_o_b(binding.etEmpDob.getText().toString().trim());
            user.setPassword(binding.etPassword.getText().toString().trim());
            user.setMob(binding.etMobile.getText().toString().trim());
            user.setEmail(binding.etEmail.getText().toString().trim());

            databaseHelper.addUser(user);

            Toast.makeText(activity, "Successfully register", Toast.LENGTH_SHORT).show();
            emptyInputEditText();


        } else {
            Toast.makeText(activity, "User alreday exists", Toast.LENGTH_SHORT).show();
        }


    }

    private void UpdateDataToSQLite()
    {
        if (TextUtils.isEmpty(binding.etEmpCode.getText().toString()))
        {
            binding.etEmpCode.setError("Please enter EMP code");
        }
        else if (TextUtils.isEmpty(binding.etEmpName.getText().toString()))
        {
            binding.etEmpName.setError("Please enter EMP name");
        }
        else if (TextUtils.isEmpty(binding.etEmpDob.getText().toString()))
        {
            binding.etEmpDob.setError("Please enter EMP date of birth");
        }
        else if (TextUtils.isEmpty(binding.etPassword.getText().toString()))
        {
            binding.etPassword.setError("Please enter EMP password");
        }
        else if (TextUtils.isEmpty(binding.etConfPassword.getText().toString()))
        {
            binding.etConfPassword.setError("Please retype password");
        }
        else if (!binding.etPassword.getText().toString().equals(binding.etConfPassword.getText().toString()))
        {
            binding.etConfPassword.setError("Please not matching");
        }
        else if (TextUtils.isEmpty(binding.etMobile.getText().toString()))
        {
            binding.etMobile.setError("Please enter EMP mobile no");
        }
        else if (!isValidMobile(binding.etMobile.getText().toString()))
        {
            binding.etMobile.setError("Please enter valid mobile no");
        }
        else if (TextUtils.isEmpty(binding.etEmail.getText().toString()))
        {
            binding.etEmail.setError("Please enter EMP email ID");
        }
        else if (!isValidMail(binding.etEmail.getText().toString()))
        {
            binding.etEmail.setError("Please enter valid email ID");
        }
        else if (databaseHelper.checkEmp(binding.etEmpCode.getText().toString().trim()))
        {
            user_intent.setId(user_intent.getId());
            user_intent.setEmp_code(binding.etEmpCode.getText().toString().trim());
            user_intent.setName(binding.etEmpName.getText().toString().trim());
            user_intent.setD_o_b(binding.etEmpDob.getText().toString().trim());
            user_intent.setPassword(binding.etPassword.getText().toString().trim());
            user_intent.setMob(binding.etMobile.getText().toString().trim());
            user_intent.setEmail(binding.etEmail.getText().toString().trim());
            databaseHelper.updateUser(user_intent);
            Toast.makeText(activity, "Successfully update", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this,AllEmpActivity.class));
            finish();
            emptyInputEditText();
        } else {
            Toast.makeText(activity, "Cannot update", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidMail(String email) {

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }

    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    private void emptyInputEditText() {
        binding.etEmpCode.setText(null);
        binding.etEmpName.setText(null);
        binding.etEmpDob.setText(null);
        binding.etPassword.setText(null);
        binding.etConfPassword.setText(null);
        binding.etMobile.setText(null);
        binding.etEmail.setText(null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
