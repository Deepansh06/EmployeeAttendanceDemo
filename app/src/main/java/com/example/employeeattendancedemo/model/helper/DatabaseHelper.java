package com.example.employeeattendancedemo.model.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.employeeattendancedemo.model.model.Attendance;
import com.example.employeeattendancedemo.model.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "EmpManager.db";

    private static final String TABLE_EMP = "emp";
    private static final String TABLE__ATTENDANCE_ = "attendance";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMP_CODE = "emp_code";
    private static final String COLUMN_EMP_NAME = "emp_name";
    private static final String COLUMN_EMP_DOB = "emp_dob";
    private static final String COLUMN_EMP_PASSWORD = "emp_password";
    private static final String COLUMN_EMP_MOBILE = "emp_mobile";
    private static final String COLUMN_EMP_EMAIL = "emp_email";


    private static final String COLUMN_ATTENDANCE_ID = "id";
    private static final String COLUMN_ATTENDANCE_EMP_CODE = "emp_code";
    private static final String COLUMN__ATTENDANCE__ENTRY_TIME = "entry_time";
    private static final String COLUMN__ATTENDANCE__ENTRY_DATE = "entry_date";


    private String CREATE_EMP_TABLE = "CREATE TABLE " + TABLE_EMP + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_EMP_CODE + " TEXT,"
            + COLUMN_EMP_NAME + " TEXT," + COLUMN_EMP_DOB + " TEXT," + COLUMN_EMP_PASSWORD + " TEXT," +
            COLUMN_EMP_MOBILE + " TEXT," + COLUMN_EMP_EMAIL + " TEXT" + ")";


    private String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE__ATTENDANCE_ + "("
            + COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ATTENDANCE_EMP_CODE + " TEXT,"
            + COLUMN__ATTENDANCE__ENTRY_TIME + " TEXT," + COLUMN__ATTENDANCE__ENTRY_DATE + " TEXT" + ")";


    private String DROP_EMP_TABLE = "DROP TABLE IF EXISTS " + TABLE_EMP;
    private String DROP_DATA_TABLE = "DROP TABLE IF EXISTS " + TABLE__ATTENDANCE_;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMP_TABLE);
        db.execSQL(CREATE_DATA_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EMP_TABLE);
        db.execSQL(DROP_DATA_TABLE);
        onCreate(db);
    }

    /*********************************For Employee data**************************************/
    /*********************************Start**************************************/
    public void addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMP_CODE, user.getEmp_code());
        values.put(COLUMN_EMP_NAME, user.getName());
        values.put(COLUMN_EMP_DOB, user.getD_o_b());
        values.put(COLUMN_EMP_PASSWORD, user.getPassword());
        values.put(COLUMN_EMP_MOBILE, user.getMob());
        values.put(COLUMN_EMP_EMAIL, user.getEmail());
        db.insert(TABLE_EMP, null, values);
        db.close();
    }

    public List<User> getAllEmp()
    {
        String[] columns = {
                COLUMN_ID,
                COLUMN_EMP_CODE,
                COLUMN_EMP_NAME,
                COLUMN_EMP_DOB,
                COLUMN_EMP_PASSWORD,
                COLUMN_EMP_MOBILE,
                COLUMN_EMP_EMAIL
        };
        String sortOrder = COLUMN_EMP_CODE + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EMP,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst())
        {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                user.setEmp_code(cursor.getString(cursor.getColumnIndex(COLUMN_EMP_CODE)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EMP_NAME)));
                user.setD_o_b(cursor.getString(cursor.getColumnIndex(COLUMN_EMP_DOB)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_EMP_PASSWORD)));
                user.setMob(cursor.getString(cursor.getColumnIndex(COLUMN_EMP_MOBILE)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMP_EMAIL)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }

    public void updateUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMP_NAME, user.getName());
        values.put(COLUMN_EMP_DOB, user.getD_o_b());
        values.put(COLUMN_EMP_PASSWORD, user.getPassword());
        values.put(COLUMN_EMP_MOBILE, user.getMob());
        values.put(COLUMN_EMP_EMAIL, user.getEmail());

        db.update(TABLE_EMP, values, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void deleteUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMP, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public boolean checkEmp(String emp_code)
    {
        String[] columns = {COLUMN_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_EMP_CODE + " = ?";
        String[] selectionArgs = {emp_code};
        Cursor cursor = db.query(TABLE_EMP,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0)
        {
            return true;
        }
        return false;
    }

    public boolean authenticateEmp(String emp_code, String password) //for login
    {
        String[] columns = {COLUMN_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_EMP_CODE + " = ?" + " AND " + COLUMN_EMP_PASSWORD + " = ?";
        String[] selectionArgs = {emp_code, password};
        Cursor cursor = db.query(TABLE_EMP,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public Cursor getEmpData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EMP , new String[]{});

        return res;
    }

    /*********************************END**************************************/



    /*********************************For attendance data**************************************/
    /*********************************Start**************************************/
    public void insertAttendanceData(Attendance attendance)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ATTENDANCE_EMP_CODE, attendance.getEmp_code());
        values.put(COLUMN__ATTENDANCE__ENTRY_TIME, attendance.getTime());
        values.put(COLUMN__ATTENDANCE__ENTRY_DATE, attendance.getDate());

        db.insert(TABLE__ATTENDANCE_, null, values);
        db.close();
    }

    public List<Attendance> getAllAttendanceDataEmployeeBased(String emp_code)
    {
        String[] columns = {
                COLUMN_ATTENDANCE_ID,
                COLUMN_ATTENDANCE_EMP_CODE,
                COLUMN__ATTENDANCE__ENTRY_DATE,
                COLUMN__ATTENDANCE__ENTRY_TIME
        };
        String sortOrder = COLUMN_ATTENDANCE_EMP_CODE + " ASC";
        List<Attendance> attendanceList = new ArrayList<Attendance>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_EMP_CODE + " = ?";
        String[] selectionArgs = {emp_code};


        Cursor cursor = db.query(TABLE__ATTENDANCE_,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst())
        {
            do {
                Attendance attendance = new Attendance();
                attendance.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ATTENDANCE_ID))));
                attendance.setEmp_code(cursor.getString(cursor.getColumnIndex(COLUMN_ATTENDANCE_EMP_CODE)));
                attendance.setTime(cursor.getString(cursor.getColumnIndex(COLUMN__ATTENDANCE__ENTRY_TIME)));
                attendance.setDate(cursor.getString(cursor.getColumnIndex(COLUMN__ATTENDANCE__ENTRY_DATE)));
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return attendanceList;
    }

    public void deleteAttendanceData(Attendance attendance)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE__ATTENDANCE_, COLUMN_ATTENDANCE_ID + " = ?", new String[]{String.valueOf(attendance.getId())});
        db.close();
    }

    public Cursor getAttendanceData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE__ATTENDANCE_ , new String[]{});

        return res;
    }

    /*********************************END**************************************/
}
