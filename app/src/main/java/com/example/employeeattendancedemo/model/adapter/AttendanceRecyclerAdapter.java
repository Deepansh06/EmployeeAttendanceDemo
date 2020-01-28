package com.example.employeeattendancedemo.model.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeattendancedemo.R;
import com.example.employeeattendancedemo.model.AllEmpActivity;
import com.example.employeeattendancedemo.model.RegisterActivity;
import com.example.employeeattendancedemo.model.helper.DatabaseHelper;
import com.example.employeeattendancedemo.model.model.Attendance;
import com.example.employeeattendancedemo.model.model.User;

import java.util.List;

public class AttendanceRecyclerAdapter extends RecyclerView.Adapter<AttendanceRecyclerAdapter.AttendanceViewHolder>
{
    private List<Attendance> attendanceList;
    private Context context;
    private DatabaseHelper databaseHelper;

    public AttendanceRecyclerAdapter(List<Attendance> attendanceList, Context context, DatabaseHelper databaseHelper)
    {
        this.attendanceList = attendanceList;
        this.context = context;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_recycler, parent, false);
        return new AttendanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, final int position)
    {
        holder.txt_emp_code.setText(attendanceList.get(position).getEmp_code());
        holder.txt_time.setText(attendanceList.get(position).getTime());
        holder.txt_date.setText(attendanceList.get(position).getDate());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select your option.");
                builder.setMessage("Are you sure to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        databaseHelper.deleteAttendanceData(attendanceList.get(position));
                        Toast.makeText(context, "Successfully Deleted",Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, AllEmpActivity.class));
                        ((Activity)context).finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,
                                "Cancelled",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                // Change the alert dialog buttons text and background color
                positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
                positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));

                negativeButton.setTextColor(Color.parseColor("#FFFF0400"));
                negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"));
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(AttendanceRecyclerAdapter.class.getSimpleName(),""+attendanceList.size());
        return attendanceList.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder
    {
        public AppCompatButton btn_delete;
        public TextView txt_emp_code,txt_time,txt_date;

        public AttendanceViewHolder(View view) {
            super(view);
            txt_emp_code = (TextView) view.findViewById(R.id.txt_emp_code);
            txt_time = (TextView) view.findViewById(R.id.txt_time);
            txt_date = (TextView) view.findViewById(R.id.txt_date);

            btn_delete = (AppCompatButton) view.findViewById(R.id.btn_delete);
        }
    }


}