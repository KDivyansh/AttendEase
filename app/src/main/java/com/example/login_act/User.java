package com.example.login_act;

public class User {
    private String email;
    private String name;
    private String rollNo;
    private String deviceId;
    private long latestAccess;
    private int CS321;
    private int CS402;
    private int CS324;

    private int CMP608;
    private int CMP618;
    private int PS315;


    public User() {
        // Default constructor required for Firebase
    }

    public User(String email, String name, String rollNo, String deviceId) {
        this.email = email;
        this.name = name;
        this.rollNo = rollNo;
        this.deviceId = deviceId;
        this.CS321 = 0;
        this.CS402 = 0;
        this.CS324 = 0;
        this.CMP608 = 0;
        this.CMP618 = 0;
        this.PS315 = 0;// Initialize more fields as needed for additional subject codes
        this.latestAccess = 0;
    }

    // Getter and setter methods for fields
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getCS321() {
        return CS321;
    }

    public void setCS321(int CS321) {
        this.CS321 = CS321;
    }

    public int getCS402() {
        return CS402;
    }

    public void setCS402(int CS402) {
        this.CS402 = CS402;
    }

    public int getCS324() {
        return CS324;
    }

    public void setCS324(int CS324) {
        this.CS324 = CS324;
    }

    public int getCMP608() {return CMP608;}

    public void setCMP608(int CMP608) {this.CMP608 = CMP608;}

    public int getCMP618() {return CMP618;}

    public void setCMP618(int CMP618) {this.CMP618 = CMP618;}

    public int getPS315() {return PS315;}

    public void setPS315(int PS315) {this.PS315 = PS315;}
    public long getLatestAccess() {
        return latestAccess;
    }

    public void setlatestAccess(long latestAccess) {
        this.latestAccess = latestAccess;
    }


    // Add more getter and setter methods for additional subject codes as needed
}
