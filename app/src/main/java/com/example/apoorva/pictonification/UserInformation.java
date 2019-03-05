package com.example.apoorva.pictonification;

public class UserInformation {

    public String username,email_id,year,div,batch,department,password,confirm_password;


    public UserInformation()
    {

    }

    public UserInformation(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserInformation(String username, String email_id, String year, String div, String batch, String department, String password, String confirm_password) {
        this.username = username;
        this.email_id = email_id;
        this.year = year;
        this.div = div;
        this.batch = batch;
        this.department = department;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getYear() {
        return year;
    }

    public String getDiv() {
        return div;
    }

    public String getBatch() {
        return batch;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }
}
