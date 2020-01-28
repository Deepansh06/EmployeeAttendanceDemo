package com.example.employeeattendancedemo.model.model;

import java.io.Serializable;

public class User implements Serializable
{
    private int id;
    private String emp_code;
    private String name;
    private String d_o_b;
    private String password;
    private String mob;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getD_o_b() {
        return d_o_b;
    }

    public void setD_o_b(String d_o_b) {
        this.d_o_b = d_o_b;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}