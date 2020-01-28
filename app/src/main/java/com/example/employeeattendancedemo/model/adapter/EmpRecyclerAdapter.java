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
import com.example.employeeattendancedemo.model.EmpAttendanceDataActivity;
import com.example.employeeattendancedemo.model.RegisterActivity;
import com.example.employeeattendancedemo.model.helper.DatabaseHelper;
import com.example.employeeattendancedemo.model.model.User;

import java.util.List;

public class EmpRecyclerAdapter extends RecyclerView.Adapter<EmpRecyclerAdapter.EmpViewHolder>
{
    private List<User> listEmps;
    private Context context;
    private DatabaseHelper databaseHelper;

    public EmpRecyclerAdapter(List<User> listUsers, Context context,DatabaseHelper databaseHelper)
    {
        this.listEmps = listUsers;
        this.context = context;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public EmpViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emp_recycler, parent, false);
        return new EmpViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmpViewHolder holder, final int position)
    {
        holder.txt_emp_code.setText(listEmps.get(position).getEmp_code());
        holder.txt_emp_name.setText(listEmps.get(position).getName());
        holder.txt_emp_dob.setText(listEmps.get(position).getD_o_b());
        holder.txt_emp_mob.setText(listEmps.get(position).getMob());
        holder.txt_emp_email.setText(listEmps.get(position).getEmail());

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
                        databaseHelper.deleteUser(listEmps.get(position));
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

                positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
                positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));

                negativeButton.setTextColor(Color.parseColor("#FFFF0400"));
                negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"));
            }
        });

        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select your option.");
                builder.setMessage("Are you sure to update?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(context, RegisterActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("emp",listEmps.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
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

                positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
                positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));

                negativeButton.setTextColor(Color.parseColor("#FFFF0400"));
                negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"));

            }
        });

        holder.btn_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EmpAttendanceDataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("emp_code",listEmps.get(position).getEmp_code());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(EmpRecyclerAdapter.class.getSimpleName(),""+listEmps.size());
        return listEmps.size();
    }

    public class EmpViewHolder extends RecyclerView.ViewHolder
    {
        public AppCompatButton btn_update,btn_delete,btn_Report;
        public TextView txt_emp_code,txt_emp_name,txt_emp_dob,txt_emp_mob,txt_emp_email;

        public EmpViewHolder(View view) {
            super(view);
            txt_emp_code = (TextView) view.findViewById(R.id.txt_emp_code);
            txt_emp_name = (TextView) view.findViewById(R.id.txt_emp_name);
            txt_emp_dob = (TextView) view.findViewById(R.id.txt_emp_dob);
            txt_emp_mob = (TextView) view.findViewById(R.id.txt_emp_mob);
            txt_emp_email = (TextView) view.findViewById(R.id.txt_emp_email);

            btn_update = (AppCompatButton) view.findViewById(R.id.btn_update);
            btn_delete = (AppCompatButton) view.findViewById(R.id.btn_delete);
            btn_Report = (AppCompatButton) view.findViewById(R.id.btn_Report);
        }
    }


}