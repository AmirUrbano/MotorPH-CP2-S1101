/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollcp2;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author Amir
 */
public class NewEmployeeFrame extends JFrame {
    private MainFrame parentFrame;
    private EmployeeDetails employeeDetails;
    private JTextField employeeIdField, lastNameField, firstNameField, birthdayField;
    private JTextField addressField, phoneField, sssField, philHealthField;
    private JTextField tinField, pagIbigField, statusField, positionField;
    private JTextField supervisorField, basicSalaryField, riceSubsidyField;
    private JTextField phoneAllowanceField, clothingAllowanceField;
    private JTextField grossSemiMonthlyField, hourlyRateField;
    private JButton saveBtn, cancelBtn, clearBtn;

    public NewEmployeeFrame(MainFrame parent, EmployeeDetails employeeDetails) {
        this.parentFrame = parent;
        this.employeeDetails = employeeDetails;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Add New Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        createFormPanel();
        createButtonPanel();

        setSize(600, 700);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
    }

    private void createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Employee ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1;
        employeeIdField = new JTextField(15);
        formPanel.add(employeeIdField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        lastNameField = new JTextField(15);
        formPanel.add(lastNameField, gbc);

        // First Name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(15);
        formPanel.add(firstNameField, gbc);

        // Birthday
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Birthday (MM/DD/YYYY):"), gbc);
        gbc.gridx = 1;
        birthdayField = new JTextField(15);
        formPanel.add(birthdayField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(15);
        formPanel.add(addressField, gbc);

        // Phone Number
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);

        // SSS Number
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("SSS Number:"), gbc);
        gbc.gridx = 1;
        sssField = new JTextField(15);
        formPanel.add(sssField, gbc);

        // PhilHealth
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("PhilHealth Number:"), gbc);
        gbc.gridx = 1;
        philHealthField = new JTextField(15);
        formPanel.add(philHealthField, gbc);

        // TIN
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("TIN:"), gbc);
        gbc.gridx = 1;
        tinField = new JTextField(15);
        formPanel.add(tinField, gbc);

        // Pag-IBIG
        gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(new JLabel("Pag-IBIG Number:"), gbc);
        gbc.gridx = 1;
        pagIbigField = new JTextField(15);
        formPanel.add(pagIbigField, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 10;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusField = new JTextField(15);
        formPanel.add(statusField, gbc);

        // Position
        gbc.gridx = 0; gbc.gridy = 11;
        formPanel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1;
        positionField = new JTextField(15);
        formPanel.add(positionField, gbc);

        // Supervisor
        gbc.gridx = 0; gbc.gridy = 12;
        formPanel.add(new JLabel("Supervisor:"), gbc);
        gbc.gridx = 1;
        supervisorField = new JTextField(15);
        formPanel.add(supervisorField, gbc);

        // Basic Salary
        gbc.gridx = 0; gbc.gridy = 13;
        formPanel.add(new JLabel("Basic Salary:"), gbc);
        gbc.gridx = 1;
        basicSalaryField = new JTextField(15);
        formPanel.add(basicSalaryField, gbc);

        // Rice Subsidy
        gbc.gridx = 0; gbc.gridy = 14;
        formPanel.add(new JLabel("Rice Subsidy:"), gbc);
        gbc.gridx = 1;
        riceSubsidyField = new JTextField(15);
        formPanel.add(riceSubsidyField, gbc);

        // Phone Allowance
        gbc.gridx = 0; gbc.gridy = 15;
        formPanel.add(new JLabel("Phone Allowance:"), gbc);
        gbc.gridx = 1;
        phoneAllowanceField = new JTextField(15);
        formPanel.add(phoneAllowanceField, gbc);

        // Clothing Allowance
        gbc.gridx = 0; gbc.gridy = 16;
        formPanel.add(new JLabel("Clothing Allowance:"), gbc);
        gbc.gridx = 1;
        clothingAllowanceField = new JTextField(15);
        formPanel.add(clothingAllowanceField, gbc);

        // Gross Semi-Monthly Rate
        gbc.gridx = 0; gbc.gridy = 17;
        formPanel.add(new JLabel("Gross Semi-Monthly Rate:"), gbc);
        gbc.gridx = 1;
        grossSemiMonthlyField = new JTextField(15);
        formPanel.add(grossSemiMonthlyField, gbc);

        // Hourly Rate
        gbc.gridx = 0; gbc.gridy = 18;
        formPanel.add(new JLabel("Hourly Rate:"), gbc);
        gbc.gridx = 1;
        hourlyRateField = new JTextField(15);
        formPanel.add(hourlyRateField, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        saveBtn = new JButton("Save Employee");
        saveBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        saveBtn.setBackground(new Color(40, 167, 69));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(new SaveEmployeeListener());

        clearBtn = new JButton("Clear Form");
        clearBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        clearBtn.addActionListener(e -> clearForm());

        cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(cancelBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void clearForm() {
        employeeIdField.setText("");
        lastNameField.setText("");
        firstNameField.setText("");
        birthdayField.setText("");
        addressField.setText("");
        phoneField.setText("");
        sssField.setText("");
        philHealthField.setText("");
        tinField.setText("");
        pagIbigField.setText("");
        statusField.setText("");
        positionField.setText("");
        supervisorField.setText("");
        basicSalaryField.setText("");
        riceSubsidyField.setText("");
        phoneAllowanceField.setText("");
        clothingAllowanceField.setText("");
        grossSemiMonthlyField.setText("");
        hourlyRateField.setText("");
    }

    private class SaveEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Validate required fields
                if (employeeIdField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() ||
                    firstNameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(NewEmployeeFrame.this,
                        "Employee ID, Last Name, and First Name are required",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate Employee ID format
                String employeeId = employeeIdField.getText().trim();
                if (!ValidationUtils.isValidEmployeeId(employeeId)) {
                    JOptionPane.showMessageDialog(NewEmployeeFrame.this,
                        "Employee ID must be 5 digits (e.g., 10035)",
                        "Invalid Employee ID", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if employee ID already exists
                if (employeeDetails.findEmployeeById(employeeId) != null) {
                    JOptionPane.showMessageDialog(NewEmployeeFrame.this,
                        "Employee ID " + employeeId + " already exists",
                        "Duplicate Employee ID", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Parse numeric fields
                double basicSalary = parseDouble(basicSalaryField.getText(), "Basic Salary");
                double riceSubsidy = parseDouble(riceSubsidyField.getText(), "Rice Subsidy");
                double phoneAllowance = parseDouble(phoneAllowanceField.getText(), "Phone Allowance");
                double clothingAllowance = parseDouble(clothingAllowanceField.getText(), "Clothing Allowance");
                double grossSemiMonthly = parseDouble(grossSemiMonthlyField.getText(), "Gross Semi-Monthly Rate");
                double hourlyRate = parseDouble(hourlyRateField.getText(), "Hourly Rate");

                // Create new employee
                Employee newEmployee = new Employee(
                    employeeId,
                    lastNameField.getText().trim(),
                    firstNameField.getText().trim(),
                    birthdayField.getText().trim(),
                    addressField.getText().trim(),
                    phoneField.getText().trim(),
                    sssField.getText().trim(),
                    philHealthField.getText().trim(),
                    tinField.getText().trim(),
                    pagIbigField.getText().trim(),
                    statusField.getText().trim(),
                    positionField.getText().trim(),
                    supervisorField.getText().trim(),
                    basicSalary,
                    riceSubsidy,
                    phoneAllowance,
                    clothingAllowance,
                    grossSemiMonthly,
                    hourlyRate
                );

                // Add to employee list
                employeeDetails.addEmployee(newEmployee);

                // Save to CSV file
                saveEmployeeToCSV(newEmployee);

                // Refresh parent table
                parentFrame.refreshTable();

                JOptionPane.showMessageDialog(NewEmployeeFrame.this,
                    "Employee " + employeeId + " has been successfully added!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(NewEmployeeFrame.this,
                    ex.getMessage(),
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(NewEmployeeFrame.this,
                    "Error saving employee: " + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private double parseDouble(String text, String fieldName) throws NumberFormatException {
            if (text.trim().isEmpty()) {
                return 0.0;
            }
            try {
                double value = Double.parseDouble(text.trim());
                if (value < 0) {
                    throw new NumberFormatException(fieldName + " cannot be negative");
                }
                return value;
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid number format for " + fieldName + ": " + text);
            }
        }

        private void saveEmployeeToCSV(Employee employee) throws IOException {
            String csvLine = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f",
                employee.getEmployeeId(),
                employee.getLastName(),
                employee.getFirstName(),
                employee.getBirthday(),
                employee.getAddress(),
                employee.getPhoneNumber(),
                employee.getSssNumber(),
                employee.getPhilHealth(),
                employee.getTinNumber(),
                employee.getPagIbig(),
                employee.getStatus(),
                employee.getPosition(),
                employee.getSupervisor(),
                employee.getBasicSalary(),
                employee.getRiceSubsidy(),
                employee.getPhoneAllowance(),
                employee.getClothingAllowance(),
                employee.getGrossSemiMonthlyRate(),
                employee.getHourlyRate()
            );

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("employees.csv", true))) {
                writer.write(csvLine);
                writer.newLine();
            }
        }
    }
}
