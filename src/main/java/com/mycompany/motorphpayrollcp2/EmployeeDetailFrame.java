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
/**
 *
 * @author Amir
 */
public class EmployeeDetailFrame extends JFrame {
    private Employee employee;
    private EmployeeDetails employeeDetails;
    private PayrollProcessor payrollProcessor;
    private JComboBox<String> monthComboBox;
    private JTextArea employeeInfoArea;
    private JTextArea payrollResultArea;
    private JButton computeBtn;
    private JButton closeBtn;

    public EmployeeDetailFrame(Employee employee, EmployeeDetails employeeDetails) {
        this.employee = employee;
        this.employeeDetails = employeeDetails;
        this.payrollProcessor = new PayrollProcessor();
        initializeGUI();
        displayEmployeeInfo();
        loadAttendanceData();
    }

    private void initializeGUI() {
        setTitle("Employee Details - " + employee.getFirstName() + " " + employee.getLastName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        createEmployeeInfoPanel();
        createPayrollPanel();
        createButtonPanel();

        setSize(900, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void createEmployeeInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));

        employeeInfoArea = new JTextArea(12, 40);
        employeeInfoArea.setEditable(false);
        employeeInfoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        employeeInfoArea.setBackground(new Color(248, 248, 248));

        JScrollPane scrollPane = new JScrollPane(employeeInfoArea);
        infoPanel.add(scrollPane, BorderLayout.CENTER);

        add(infoPanel, BorderLayout.NORTH);
    }

    private void createPayrollPanel() {
        JPanel payrollPanel = new JPanel(new BorderLayout());
        payrollPanel.setBorder(BorderFactory.createTitledBorder("Payroll Computation"));

        // Month selection panel
        JPanel monthPanel = new JPanel(new FlowLayout());
        monthPanel.add(new JLabel("Select Month:"));

        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(5); // Default to June
        monthPanel.add(monthComboBox);

        computeBtn = new JButton("Compute Payroll");
        computeBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        computeBtn.setBackground(new Color(0, 123, 255));
        computeBtn.setForeground(Color.WHITE);
        computeBtn.addActionListener(new ComputePayrollListener());
        monthPanel.add(computeBtn);

        payrollPanel.add(monthPanel, BorderLayout.NORTH);

        // Payroll results area
        payrollResultArea = new JTextArea(20, 70);
        payrollResultArea.setEditable(false);
        payrollResultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        payrollResultArea.setBackground(new Color(248, 248, 248));

        JScrollPane resultScrollPane = new JScrollPane(payrollResultArea);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        payrollPanel.add(resultScrollPane, BorderLayout.CENTER);

        add(payrollPanel, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        closeBtn = new JButton("Close");
        closeBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        closeBtn.addActionListener(e -> dispose());

        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void displayEmployeeInfo() {
        StringBuilder info = new StringBuilder();
        info.append("EMPLOYEE DETAILS\n");
        info.append("=====================================\n");
        info.append("Employee ID: ").append(employee.getEmployeeId()).append("\n");
        info.append("Name: ").append(employee.getFirstName()).append(" ").append(employee.getLastName()).append("\n");
        info.append("Birthday: ").append(employee.getBirthday()).append("\n");
        info.append("Address: ").append(employee.getAddress()).append("\n");
        info.append("Phone: ").append(employee.getPhoneNumber()).append("\n");
        info.append("SSS Number: ").append(employee.getSssNumber()).append("\n");
        info.append("PhilHealth: ").append(employee.getPhilHealth()).append("\n");
        info.append("TIN: ").append(employee.getTinNumber()).append("\n");
        info.append("Pag-IBIG: ").append(employee.getPagIbig()).append("\n");
        info.append("Status: ").append(employee.getStatus()).append("\n");
        info.append("Position: ").append(employee.getPosition()).append("\n");
        info.append("Supervisor: ").append(employee.getSupervisor()).append("\n");
        info.append("Basic Salary: PHP ").append(String.format("%.2f", employee.getBasicSalary())).append("\n");
        info.append("Rice Subsidy: PHP ").append(String.format("%.2f", employee.getRiceSubsidy())).append("\n");
        info.append("Phone Allowance: PHP ").append(String.format("%.2f", employee.getPhoneAllowance())).append("\n");
        info.append("Clothing Allowance: PHP ").append(String.format("%.2f", employee.getClothingAllowance())).append("\n");
        info.append("Gross Semi-Monthly Rate: PHP ").append(String.format("%.2f", employee.getGrossSemiMonthlyRate())).append("\n");
        info.append("Hourly Rate: PHP ").append(String.format("%.2f", employee.getHourlyRate())).append("\n");

        employeeInfoArea.setText(info.toString());
        employeeInfoArea.setCaretPosition(0);
    }

    private void loadAttendanceData() {
        try {
            AttendanceProcessor.getInstance().loadAttendance(Config.ATTENDANCE_FILE);
            payrollResultArea.setText("✓ Attendance data loaded successfully!\n");
            payrollResultArea.append("✓ Total records: " + AttendanceProcessor.getInstance().getTotalRecords() + "\n\n");
            payrollResultArea.append("Instructions:\n");
            payrollResultArea.append("1. Select month from dropdown above\n");
            payrollResultArea.append("2. Click 'Compute Payroll' button\n\n");
            payrollResultArea.append("Available months with data: June (month 6)\n");
        } catch (Exception e) {
            payrollResultArea.setText("✗ Error loading attendance data: " + e.getMessage());
            computeBtn.setEnabled(false);
        }
    }

    private class ComputePayrollListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int month = monthComboBox.getSelectedIndex() + 1;
            
            computeBtn.setEnabled(false);
            computeBtn.setText("Computing...");
            
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    PrintStream old = System.out;
                    System.setOut(ps);
                    
                    try {
                        payrollProcessor.processMonthlyPayroll(employee.getEmployeeId(), month);
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
                        payrollResultArea.setText(result);
                        payrollResultArea.setCaretPosition(0);
                    } catch (Exception ex) {
                        payrollResultArea.setText("Error computing payroll: " + ex.getMessage());
                        JOptionPane.showMessageDialog(EmployeeDetailFrame.this,
                            "Error computing payroll: " + ex.getMessage(),
                            "Computation Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        computeBtn.setEnabled(true);
                        computeBtn.setText("Compute Payroll");
                    }
                }
            };
            
            worker.execute();
        }
    }
}
