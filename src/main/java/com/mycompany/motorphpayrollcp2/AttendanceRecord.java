/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollcp2;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class AttendanceRecord {
    private String employeeId;
    private String date;
    private LocalTime logIn;
    private LocalTime logOut;
    private boolean isLate;
    private int lateMinutes;
    private double hoursWorked;
    private int weekNumber;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Full constructor
    public AttendanceRecord(String employeeId, String date, LocalTime logIn, LocalTime logOut,
                             boolean isLate, int lateMinutes, double hoursWorked, int weekNumber) {
        this.employeeId = employeeId;
        this.date = date;
        this.logIn = logIn;
        this.logOut = logOut;
        this.isLate = isLate;
        this.lateMinutes = lateMinutes;
        this.hoursWorked = hoursWorked;
        this.weekNumber = weekNumber;
    }

    // Constructor for CSV loading with validation
    public AttendanceRecord(String employeeId, String date, String logIn, String logOut) {
        if (!ValidationUtils.isValidEmployeeId(employeeId)) {
            throw new IllegalArgumentException("Invalid employee ID: " + employeeId);
        }
        if (!ValidationUtils.isValidTime(logIn)) {
            throw new IllegalArgumentException("Invalid login time: " + logIn);
        }
        if (!ValidationUtils.isValidTime(logOut)) {
            throw new IllegalArgumentException("Invalid logout time: " + logOut);
        }
        
        this.employeeId = employeeId;
        this.date = date;
        this.logIn = LocalTime.parse(logIn);
        this.logOut = LocalTime.parse(logOut);
        calculateLateAndHours();
    }

    // Calculate lateness and hours worked
    private void calculateLateAndHours() {
        this.isLate = logIn.isAfter(Config.EXPECTED_LOGIN);
        this.lateMinutes = isLate ? (int) Duration.between(Config.EXPECTED_LOGIN, logIn).toMinutes() : 0;
        this.hoursWorked = Duration.between(logIn, logOut).toMinutes() / 60.0;
    }

    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getDate() { return date; }
    public LocalTime getLogInTime() { return logIn; }
    public LocalTime getLogOutTime() { return logOut; }
    public boolean isLate() { return isLate; }
    public int getLateMinutes() { return lateMinutes; }
    public double getHoursWorked() { return hoursWorked; }
    public int getWeekNumber() { return weekNumber; }

    @Override
    public String toString() {
        return "Employee ID: " + employeeId + ", Date: " + date + ", Log In: " + logIn + ", Log Out: " + logOut +
               ", Late: " + (isLate ? lateMinutes + " mins" : "No") + ", Hours Worked: " + hoursWorked;
    }
}
