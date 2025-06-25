/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollcp2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author Amir
 */
public class MainFrame extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeDetails employeeDetails;
    private JButton viewEmployeeBtn;
    private JButton newEmployeeBtn;
    private JButton updateEmployeeBtn;
    private JButton deleteEmployeeBtn;
    private JButton refreshBtn;
    
    // Text fields for employee data editing
    private JTextField employeeIdField, lastNameField, firstNameField, birthdayField;
    private JTextField addressField, phoneField, sssField, philHealthField;
    private JTextField tinField, pagIbigField, statusField, positionField;
    private JTextField supervisorField, basicSalaryField, riceSubsidyField;
    private JTextField phoneAllowanceField, clothingAllowanceField;
    private JTextField grossSemiMonthlyField, hourlyRateField;
    
    private Employee selectedEmployee = null;

    public MainFrame() {
        employeeDetails = new EmployeeDetails();
        initializeGUI();
        loadEmployeeData();
    }

    private void initializeGUI() {
        setTitle("MotoPH Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create menu bar
        createMenuBar();

        // Create main content
        createMainPanel();

        // Set window properties
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void createMainPanel() {
        // Create split pane with table on left and form on right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        // Left panel - Employee table
        JPanel tablePanel = createTablePanel();
        splitPane.setLeftComponent(tablePanel);
        
        // Right panel - Employee form
        JPanel formPanel = createFormPanel();
        splitPane.setRightComponent(formPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Button panel at bottom
        createButtonPanel();
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Employee Records"));

        // Create table columns
        String[] columnNames = {
            "Employee #", "Last Name", "First Name", "SSS Number", 
            "PhilHealth Number", "TIN", "Pag-IBIG Number"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(150);

        // Add selection listener to populate form when row is selected
        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    populateFormFromSelectedRow();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Employee ID
        gbc.gridx = 0; gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1;
        employeeIdField = new JTextField(15);
        employeeIdField.setEditable(false); // ID should not be editable
        fieldsPanel.add(employeeIdField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 1;
        fieldsPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        lastNameField = new JTextField(15);
        fieldsPanel.add(lastNameField, gbc);

        // First Name
        gbc.gridx = 0; gbc.gridy = 2;
        fieldsPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(15);
        fieldsPanel.add(firstNameField, gbc);

        // Birthday
        gbc.gridx = 0; gbc.gridy = 3;
        fieldsPanel.add(new JLabel("Birthday:"), gbc);
        gbc.gridx = 1;
        birthdayField = new JTextField(15);
        fieldsPanel.add(birthdayField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 4;
        fieldsPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(15);
        fieldsPanel.add(addressField, gbc);

        // Phone Number
        gbc.gridx = 0; gbc.gridy = 5;
        fieldsPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        fieldsPanel.add(phoneField, gbc);

        // SSS Number
        gbc.gridx = 0; gbc.gridy = 6;
        fieldsPanel.add(new JLabel("SSS Number:"), gbc);
        gbc.gridx = 1;
        sssField = new JTextField(15);
        fieldsPanel.add(sssField, gbc);

        // PhilHealth
        gbc.gridx = 0; gbc.gridy = 7;
        fieldsPanel.add(new JLabel("PhilHealth:"), gbc);
        gbc.gridx = 1;
        philHealthField = new JTextField(15);
        fieldsPanel.add(philHealthField, gbc);

        // TIN
        gbc.gridx = 0; gbc.gridy = 8;
        fieldsPanel.add(new JLabel("TIN:"), gbc);
        gbc.gridx = 1;
        tinField = new JTextField(15);
        fieldsPanel.add(tinField, gbc);

        // Pag-IBIG
        gbc.gridx = 0; gbc.gridy = 9;
        fieldsPanel.add(new JLabel("Pag-IBIG:"), gbc);
        gbc.gridx = 1;
        pagIbigField = new JTextField(15);
        fieldsPanel.add(pagIbigField, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 10;
        fieldsPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusField = new JTextField(15);
        fieldsPanel.add(statusField, gbc);

        // Position
        gbc.gridx = 0; gbc.gridy = 11;
        fieldsPanel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1;
        positionField = new JTextField(15);
        fieldsPanel.add(positionField, gbc);

        // Supervisor
        gbc.gridx = 0; gbc.gridy = 12;
        fieldsPanel.add(new JLabel("Supervisor:"), gbc);
        gbc.gridx = 1;
        supervisorField = new JTextField(15);
        fieldsPanel.add(supervisorField, gbc);

        // Basic Salary
        gbc.gridx = 0; gbc.gridy = 13;
        fieldsPanel.add(new JLabel("Basic Salary:"), gbc);
        gbc.gridx = 1;
        basicSalaryField = new JTextField(15);
        fieldsPanel.add(basicSalaryField, gbc);

        // Rice Subsidy
        gbc.gridx = 0; gbc.gridy = 14;
        fieldsPanel.add(new JLabel("Rice Subsidy:"), gbc);
        gbc.gridx = 1;
        riceSubsidyField = new JTextField(15);
        fieldsPanel.add(riceSubsidyField, gbc);

        // Phone Allowance
        gbc.gridx = 0; gbc.gridy = 15;
        fieldsPanel.add(new JLabel("Phone Allowance:"), gbc);
        gbc.gridx = 1;
        phoneAllowanceField = new JTextField(15);
        fieldsPanel.add(phoneAllowanceField, gbc);

        // Clothing Allowance
        gbc.gridx = 0; gbc.gridy = 16;
        fieldsPanel.add(new JLabel("Clothing Allowance:"), gbc);
        gbc.gridx = 1;
        clothingAllowanceField = new JTextField(15);
        fieldsPanel.add(clothingAllowanceField, gbc);

        // Gross Semi-Monthly Rate
        gbc.gridx = 0; gbc.gridy = 17;
        fieldsPanel.add(new JLabel("Gross Semi-Monthly:"), gbc);
        gbc.gridx = 1;
        grossSemiMonthlyField = new JTextField(15);
        fieldsPanel.add(grossSemiMonthlyField, gbc);

        // Hourly Rate
        gbc.gridx = 0; gbc.gridy = 18;
        fieldsPanel.add(new JLabel("Hourly Rate:"), gbc);
        gbc.gridx = 1;
        hourlyRateField = new JTextField(15);
        fieldsPanel.add(hourlyRateField, gbc);

        JScrollPane formScrollPane = new JScrollPane(fieldsPanel);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formPanel.add(formScrollPane, BorderLayout.CENTER);
        
        // Initially disable all form fields
        setFormFieldsEnabled(false);
        
        return formPanel;
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        viewEmployeeBtn = new JButton("View Employee");
        viewEmployeeBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        viewEmployeeBtn.setBackground(new Color(0, 123, 255));
        viewEmployeeBtn.setForeground(Color.WHITE);
        viewEmployeeBtn.addActionListener(new ViewEmployeeListener());
        
        newEmployeeBtn = new JButton("New Employee");
        newEmployeeBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        newEmployeeBtn.setBackground(new Color(40, 167, 69));
        newEmployeeBtn.setForeground(Color.WHITE);
        newEmployeeBtn.addActionListener(new NewEmployeeListener());
        
        updateEmployeeBtn = new JButton("Update Employee");
        updateEmployeeBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        updateEmployeeBtn.setBackground(new Color(255, 193, 7));
        updateEmployeeBtn.setForeground(Color.BLACK);
        updateEmployeeBtn.addActionListener(new UpdateEmployeeListener());
        updateEmployeeBtn.setEnabled(false);
        
        deleteEmployeeBtn = new JButton("Delete Employee");
        deleteEmployeeBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        deleteEmployeeBtn.setBackground(new Color(220, 53, 69));
        deleteEmployeeBtn.setForeground(Color.WHITE);
        deleteEmployeeBtn.addActionListener(new DeleteEmployeeListener());
        deleteEmployeeBtn.setEnabled(false);
        
        refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        refreshBtn.addActionListener(e -> loadEmployeeData());

        buttonPanel.add(viewEmployeeBtn);
        buttonPanel.add(newEmployeeBtn);
        buttonPanel.add(updateEmployeeBtn);
        buttonPanel.add(deleteEmployeeBtn);
        buttonPanel.add(refreshBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            selectedEmployee = null;
            clearForm();
            setFormFieldsEnabled(false);
            updateEmployeeBtn.setEnabled(false);
            deleteEmployeeBtn.setEnabled(false);
            return;
        }

        String employeeId = (String) tableModel.getValueAt(selectedRow, 0);
        selectedEmployee = employeeDetails.findEmployeeById(employeeId);
        
        if (selectedEmployee != null) {
            populateForm(selectedEmployee);
            setFormFieldsEnabled(true);
            updateEmployeeBtn.setEnabled(true);
            deleteEmployeeBtn.setEnabled(true);
        }
    }

    private void populateForm(Employee employee) {
        employeeIdField.setText(employee.getEmployeeId());
        lastNameField.setText(employee.getLastName());
        firstNameField.setText(employee.getFirstName());
        birthdayField.setText(employee.getBirthday());
        addressField.setText(employee.getAddress());
        phoneField.setText(employee.getPhoneNumber());
        sssField.setText(employee.getSssNumber());
        philHealthField.setText(employee.getPhilHealth());
        tinField.setText(employee.getTinNumber());
        pagIbigField.setText(employee.getPagIbig());
        statusField.setText(employee.getStatus());
        positionField.setText(employee.getPosition());
        supervisorField.setText(employee.getSupervisor());
        basicSalaryField.setText(String.valueOf(employee.getBasicSalary()));
        riceSubsidyField.setText(String.valueOf(employee.getRiceSubsidy()));
        phoneAllowanceField.setText(String.valueOf(employee.getPhoneAllowance()));
        clothingAllowanceField.setText(String.valueOf(employee.getClothingAllowance()));
        grossSemiMonthlyField.setText(String.valueOf(employee.getGrossSemiMonthlyRate()));
        hourlyRateField.setText(String.valueOf(employee.getHourlyRate()));
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

    private void setFormFieldsEnabled(boolean enabled) {
        lastNameField.setEditable(enabled);
        firstNameField.setEditable(enabled);
        birthdayField.setEditable(enabled);
        addressField.setEditable(enabled);
        phoneField.setEditable(enabled);
        sssField.setEditable(enabled);
        philHealthField.setEditable(enabled);
        tinField.setEditable(enabled);
        pagIbigField.setEditable(enabled);
        statusField.setEditable(enabled);
        positionField.setEditable(enabled);
        supervisorField.setEditable(enabled);
        basicSalaryField.setEditable(enabled);
        riceSubsidyField.setEditable(enabled);
        phoneAllowanceField.setEditable(enabled);
        clothingAllowanceField.setEditable(enabled);
        grossSemiMonthlyField.setEditable(enabled);
        hourlyRateField.setEditable(enabled);
    }

    private void loadEmployeeData() {
        tableModel.setRowCount(0); // Clear existing data
        
        List<Employee> employees = employeeDetails.getAllEmployees();
        for (Employee emp : employees) {
            Object[] rowData = {
                emp.getEmployeeId(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getSssNumber(),
                emp.getPhilHealth(),
                emp.getTinNumber(),
                emp.getPagIbig()
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "MotoPH Employee Management System\n" +
            "Version 2.0\n" +
            "Employee record management with update and delete functionality",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }

    // Action Listeners
    private class ViewEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Please select an employee from the table",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String employeeId = (String) tableModel.getValueAt(selectedRow, 0);
            Employee employee = employeeDetails.findEmployeeById(employeeId);
            
            if (employee != null) {
                new EmployeeDetailFrame(employee, employeeDetails).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Employee not found: " + employeeId,
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class NewEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new NewEmployeeFrame(MainFrame.this, employeeDetails).setVisible(true);
        }
    }

    private class UpdateEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Please select an employee to update",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Validate form data
                if (!validateForm()) {
                    return;
                }

                // Create updated employee object
                Employee updatedEmployee = new Employee(
                    selectedEmployee.getEmployeeId(), // Keep original ID
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
                    Double.parseDouble(basicSalaryField.getText().trim()),
                    Double.parseDouble(riceSubsidyField.getText().trim()),
                    Double.parseDouble(phoneAllowanceField.getText().trim()),
                    Double.parseDouble(clothingAllowanceField.getText().trim()),
                    Double.parseDouble(grossSemiMonthlyField.getText().trim()),
                    Double.parseDouble(hourlyRateField.getText().trim())
                );

                // Update employee in the system
                if (employeeDetails.updateEmployee(updatedEmployee)) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Employee updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh the table
                    loadEmployeeData();
                    
                    // Update the selected employee reference
                    selectedEmployee = updatedEmployee;
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Failed to update employee",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Invalid number format in salary fields",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Error updating employee: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Please select an employee to delete",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int result = JOptionPane.showConfirmDialog(MainFrame.this,
                "Are you sure you want to delete employee:\n" +
                selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + 
                " (ID: " + selectedEmployee.getEmployeeId() + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                if (employeeDetails.deleteEmployee(selectedEmployee.getEmployeeId())) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Employee deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh the table
                    loadEmployeeData();
                    
                    // Clear form and reset selection
                    selectedEmployee = null;
                    clearForm();
                    setFormFieldsEnabled(false);
                    updateEmployeeBtn.setEnabled(false);
                    deleteEmployeeBtn.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Failed to delete employee",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean validateForm() {
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            lastNameField.requestFocus();
            return false;
        }
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            firstNameField.requestFocus();
            return false;
        }
        
        // Validate numeric fields
        try {
            Double.parseDouble(basicSalaryField.getText().trim());
            Double.parseDouble(riceSubsidyField.getText().trim());
            Double.parseDouble(phoneAllowanceField.getText().trim());
            Double.parseDouble(clothingAllowanceField.getText().trim());
            Double.parseDouble(grossSemiMonthlyField.getText().trim());
            Double.parseDouble(hourlyRateField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "All salary fields must contain valid numbers", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    public void refreshTable() {
        loadEmployeeData();
    }
}