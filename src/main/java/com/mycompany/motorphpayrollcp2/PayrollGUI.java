/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollcp2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PayrollGUI extends JFrame {
    private JTextField employeeIdField;
    private JComboBox<String> monthComboBox;
    private JTextArea resultArea;
    private PayrollProcessor payrollProcessor;
    private EmployeeDetails employeeDetails;
    private JButton processButton;
    private JButton clearButton;

    public PayrollGUI() {
        payrollProcessor = new PayrollProcessor();
        employeeDetails = new EmployeeDetails();
        initializeGUI();
        loadAttendanceData();
    }

    private void initializeGUI() {
        setTitle("MotoPH Payroll System - GUI Version");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create main panels
        createInputPanel();
        createResultPanel();
        createButtonPanel();

        // Set window properties
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Employee Payroll Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Employee ID
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Employee ID:"), gbc);
        
        gbc.gridx = 1;
        employeeIdField = new JTextField(15);
        employeeIdField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        inputPanel.add(employeeIdField, gbc);

        // Month selection
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Select Month:"), gbc);
        
        gbc.gridx = 1;
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        monthComboBox.setSelectedIndex(5); // Default to June (index 5)
        inputPanel.add(monthComboBox, gbc);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Payroll Results"));

        resultArea = new JTextArea(25, 70);
        resultArea.setEditable(false);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        resultArea.setBackground(new Color(248, 248, 248));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        processButton = new JButton("Process Payroll");
        processButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        processButton.setBackground(new Color(0, 123, 255));
        processButton.setForeground(Color.WHITE);
        processButton.addActionListener(new ProcessButtonListener());
        
        clearButton = new JButton("Clear Results");
        clearButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        clearButton.addActionListener(e -> {
            resultArea.setText("");
            employeeIdField.setText("");
        });

        buttonPanel.add(processButton);
        buttonPanel.add(clearButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAttendanceData() {
        try {
            AttendanceProcessor.getInstance().loadAttendance(Config.ATTENDANCE_FILE);
            resultArea.setText("✓ System Ready - Attendance data loaded successfully!\n");
            resultArea.append("✓ Total records loaded: " + AttendanceProcessor.getInstance().getTotalRecords() + "\n\n");
            resultArea.append("Instructions:\n");
            resultArea.append("1. Enter Employee ID (e.g., 10001)\n");
            resultArea.append("2. Select month from dropdown\n");
            resultArea.append("3. Click 'Process Payroll' button\n\n");
            resultArea.append("Available Employee IDs: 10001 to 10034\n");
            resultArea.append("Available months with data: June (month 6)\n");
        } catch (Exception e) {
            resultArea.setText("✗ Error loading attendance data: " + e.getMessage());
            processButton.setEnabled(false);
        }
    }

    private class ProcessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeId = employeeIdField.getText().trim();
            int month = monthComboBox.getSelectedIndex() + 1;
            
            // Validation
            if (employeeId.isEmpty()) {
                JOptionPane.showMessageDialog(PayrollGUI.this, 
                    "Please enter an Employee ID (e.g., 10001)", 
                    "Input Required", JOptionPane.WARNING_MESSAGE);
                employeeIdField.requestFocus();
                return;
            }
            
            if (!ValidationUtils.isValidEmployeeId(employeeId)) {
                JOptionPane.showMessageDialog(PayrollGUI.this, 
                    "Invalid Employee ID format. Please use 5 digits (e.g., 10001)", 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                employeeIdField.requestFocus();
                return;
            }

            // Check if employee exists
            Employee employee = employeeDetails.findEmployeeById(employeeId);
            if (employee == null) {
                JOptionPane.showMessageDialog(PayrollGUI.this, 
                    "Employee not found with ID: " + employeeId + "\n" +
                    "Available Employee IDs: 10001 to 10034", 
                    "Employee Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Process payroll and capture output
            processButton.setEnabled(false);
            processButton.setText("Processing...");
            
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    PrintStream old = System.out;
                    System.setOut(ps);
                    
                    try {
                        payrollProcessor.processMonthlyPayroll(employeeId, month);
                        System.out.flush();
                        return baos.toString();
                    } finally {
                        System.setOut(old);
                    }
                }
                
                @Override
                protected void done() {
                    try {
                        String result = get();
                        resultArea.setText(result);
                        resultArea.setCaretPosition(0); // Scroll to top
                    } catch (Exception ex) {
                        resultArea.setText("Error processing payroll: " + ex.getMessage());
                        JOptionPane.showMessageDialog(PayrollGUI.this, 
                            "Error processing payroll: " + ex.getMessage(), 
                            "Processing Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        processButton.setEnabled(true);
                        processButton.setText("Process Payroll");
                    }
                }
            };
            
            worker.execute();
        }
    }

    // FIXED MAIN METHOD - REMOVED THE PROBLEMATIC getSystemLookAndFeel()
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PayrollGUI().setVisible(true);
        });
    }
}