/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollcp2;

/**
 *
 * @author Amir
 */
public class Employee {
    private String employeeId;
    private String lastName, firstName, birthday, address, phoneNumber;
    private String sssNumber, philHealth, tinNumber, pagIbig, status, position, supervisor;
    private double basicSalary, riceSubsidy, phoneAllowance, clothingAllowance;
    private double grossSemiMonthlyRate, hourlyRate;
    private double sssContribution;
    private double philHealthContribution;
    private double pagIbigContribution;
    private double withholdingTax;

    public Employee(String employeeId, String lastName, String firstName, String birthday,
                    String address, String phoneNumber, String sssNumber, String philHealth,
                    String tinNumber, String pagIbig, String status, String position,
                    String supervisor, double basicSalary, double riceSubsidy,
                    double phoneAllowance, double clothingAllowance,
                    double grossSemiMonthlyRate, double hourlyRate) {
        
        // Validation
        if (!ValidationUtils.isValidEmployeeId(employeeId)) {
            throw new IllegalArgumentException("Invalid employee ID: " + employeeId);
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (basicSalary < 0 || hourlyRate < 0) {
            throw new IllegalArgumentException("Salary values cannot be negative");
        }
        
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philHealth = philHealth;
        this.tinNumber = tinNumber;
        this.pagIbig = pagIbig;
        this.status = status;
        this.position = position;
        this.supervisor = supervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        this.hourlyRate = hourlyRate;
    }

    public void calculateContributions() {
        double weeklySalary = basicSalary / 2;
        this.sssContribution = weeklySalary * 0.045;
        this.philHealthContribution = weeklySalary * 0.025;
        this.pagIbigContribution = weeklySalary * 0.02;
    }

    // Getters
    public double getSssContribution() { return sssContribution; }
    public double getPhilHealthContribution() { return philHealthContribution; }
    public double getPagIbigContribution() { return pagIbigContribution; }
    public double getHourlyRate() { return hourlyRate; }
    public String getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBirthday() { return birthday; }
    public String getPosition() { return position; }
    public String getStatus() { return status; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getWithholdingTax() { return withholdingTax; }
    public void setWithholdingTax(double withholdingTax) { this.withholdingTax = withholdingTax; }

    public void displayInfo() {
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Position: " + position);
        System.out.println("Status: " + status);
        System.out.printf("Hourly Rate: PHP %.2f%n", hourlyRate);
        System.out.printf("SSS Contribution: PHP %.2f%n", sssContribution);
        System.out.printf("PhilHealth Contribution: PHP %.2f%n", philHealthContribution);
        System.out.printf("Pag-IBIG Contribution: PHP %.2f%n", pagIbigContribution);
    }

    @Override
    public String toString() {
        return String.format("%s - %s, %s (%s)", employeeId, lastName, firstName, position);
    }
}