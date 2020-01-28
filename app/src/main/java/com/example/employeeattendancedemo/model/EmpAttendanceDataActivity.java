package com.example.employeeattendancedemo.model;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeattendancedemo.R;
import com.example.employeeattendancedemo.databinding.ActivityEmpAttendanceListBinding;
import com.example.employeeattendancedemo.model.adapter.AttendanceRecyclerAdapter;
import com.example.employeeattendancedemo.model.helper.DatabaseHelper;
import com.example.employeeattendancedemo.model.model.Attendance;

import java.util.ArrayList;
import java.util.List;

public class EmpAttendanceDataActivity extends AppCompatActivity
{
    private ActivityEmpAttendanceListBinding binding;
    private List<Attendance> attendanceList;
    private AttendanceRecyclerAdapter attendanceRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emp_attendance_list);

        getSupportActionBar().setTitle("Attendance Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseHelper = new DatabaseHelper(this);

        attendanceList = new ArrayList<>();
        attendanceRecyclerAdapter = new AttendanceRecyclerAdapter(attendanceList,this,databaseHelper);

        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        binding.recAttendanceList.setLayoutManager(manager);
        binding.recAttendanceList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.recAttendanceList.setAdapter(attendanceRecyclerAdapter);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            String emp_code = bundle.getString("emp_code");
            getDataFromSQLite(emp_code);
        }
        else
        {
            finish();
        }

    }

    private void getDataFromSQLite(final String emp_code)
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                attendanceList.clear();
                attendanceList.addAll(databaseHelper.getAllAttendanceDataEmployeeBased(emp_code));

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                attendanceRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
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
