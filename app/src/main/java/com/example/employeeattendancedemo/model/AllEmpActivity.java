package com.example.employeeattendancedemo.model;

import android.Manifest;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeattendancedemo.R;
import com.example.employeeattendancedemo.databinding.ActivityEmpListBinding;
import com.example.employeeattendancedemo.model.adapter.EmpRecyclerAdapter;
import com.example.employeeattendancedemo.model.helper.CSVWriter;
import com.example.employeeattendancedemo.model.helper.DatabaseHelper;
import com.example.employeeattendancedemo.model.model.User;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllEmpActivity extends AppCompatActivity
{
    ActivityEmpListBinding binding;

    private List<User> listEmp;
    private EmpRecyclerAdapter empRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emp_list);

        getSupportActionBar().setTitle("All Employee");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();


        databaseHelper = new DatabaseHelper(this);

        listEmp = new ArrayList<>();
        empRecyclerAdapter = new EmpRecyclerAdapter(listEmp,this,databaseHelper);

        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        binding.recEmployee.setLayoutManager(manager);
        binding.recEmployee.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.recEmployee.setAdapter(empRecyclerAdapter);

        getDataFromSQLite();

        binding.btnExportEmployeeData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                    new ExportDatabaseCSVTaskEmployee().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {

                    new ExportDatabaseCSVTaskEmployee().execute();
                }

            }
        });

        binding.btnExportAttendanceData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                    new ExportDatabaseCSVTaskAttendance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {

                    new ExportDatabaseCSVTaskAttendance().execute();
                }

            }
        });
    }

    private void getDataFromSQLite()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                listEmp.clear();
                listEmp.addAll(databaseHelper.getAllEmp());

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                empRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }


    public class ExportDatabaseCSVTaskEmployee extends AsyncTask<String, Void, Boolean>
    {
        private final ProgressDialog dialog = new ProgressDialog(AllEmpActivity.this);
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args)
        {
            File exportDir = new File(Environment.getExternalStorageDirectory(), "/employeeData/");
            if (!exportDir.exists()) { exportDir.mkdirs(); }

            File file = new File(exportDir, "employee.csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                Cursor curCSV = databaseHelper.getEmpData();
                csvWrite.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext()) {
                    String arrStr[]=null;
                    String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                    for(int i=0;i<curCSV.getColumnNames().length;i++)
                    {
                        mySecondStringArray[i] =curCSV.getString(i);
                    }
                    csvWrite.writeNext(mySecondStringArray);
                }
                csvWrite.close();
                curCSV.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) { this.dialog.dismiss(); }
            if (success) {
                Toast.makeText(AllEmpActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AllEmpActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ExportDatabaseCSVTaskAttendance extends AsyncTask<String, Void, Boolean>
    {
        private final ProgressDialog dialog = new ProgressDialog(AllEmpActivity.this);
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args)
        {
            File exportDir = new File(Environment.getExternalStorageDirectory(), "/employeeData/");
            if (!exportDir.exists()) { exportDir.mkdirs(); }

            File file = new File(exportDir, "attendance.csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                Cursor curCSV = databaseHelper.getAttendanceData();
                csvWrite.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext()) {
                    String arrStr[]=null;
                    String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                    for(int i=0;i<curCSV.getColumnNames().length;i++)
                    {
                        mySecondStringArray[i] =curCSV.getString(i);
                    }
                    csvWrite.writeNext(mySecondStringArray);
                }
                csvWrite.close();
                curCSV.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) { this.dialog.dismiss(); }
            if (success) {
                Toast.makeText(AllEmpActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AllEmpActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
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
