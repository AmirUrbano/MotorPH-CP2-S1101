/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollcp2;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PayrollProcessor {
    private static final Logger logger = Logger.getLogger(PayrollProcessor.class.getName());
    private AttendanceProcessor attendanceProcessor = AttendanceProcessor.getInstance();
    private EmployeeDetails employeeDetails = new EmployeeDetails();

    public void processMonthlyPayroll(String employeeId, int month) {
        // Validation
        if (!ValidationUtils.isValidEmployeeId(employeeId)) {
            throw new IllegalArgumentException("Invalid employee ID format: " + employeeId);
        }
        if (!ValidationUtils.isValidMonth(month)) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        try {
            attendanceProcessor.loadAttendance(Config.ATTENDANCE_FILE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load attendance data", e);
            throw new RuntimeException("Failed to load attendance data: " + e.getMessage());
        }

        Employee employee = employeeDetails.getEmployeeById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
        }

        employee.calculateContributions();

        System.out.println("\nMonth of " + getMonthName(month) + ":");
        System.out.println("Employee: " + employee.getFirstName() + " " + employee.getLastName());
        System.out.println("========================================");

        LocalDate startOfMonth = LocalDate.of(2024, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        double totalMonthlyGrossSalary = 0;
        double totalMonthlyNetSalary = 0;
        double totalSSS = 0;
        double totalPhilHealth = 0;
        double totalPagIbig = 0;
        double totalWithholdingTax = 0;
        double totalLateDeductions = 0;
        int totalLateDays = 0;

        int weekCount = 1;
        LocalDate currentWeekStart = startOfMonth.with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());

        while (!currentWeekStart.isAfter(endOfMonth)) {
            LocalDate currentWeekEnd = currentWeekStart.plusDays(6);
            if (currentWeekEnd.isAfter(endOfMonth)) currentWeekEnd = endOfMonth;

            double totalHoursWorked = attendanceProcessor.getHoursWorked(employeeId, currentWeekStart, currentWeekEnd);

            if (totalHoursWorked == 0) {
                currentWeekStart = currentWeekStart.plusDays(7);
                continue;
            }

            int totalMinutesLate = attendanceProcessor.getMinutesLate(employeeId, currentWeekStart, currentWeekEnd);
            int daysLate = attendanceProcessor.getDaysLate(employeeId, currentWeekStart, currentWeekEnd);

            double overtimeHours = Math.max(totalHoursWorked - Config.STANDARD_WORK_HOURS, 0);
            double regularHours = Math.min(totalHoursWorked, Config.STANDARD_WORK_HOURS);

            double grossSalary = (regularHours * employee.getHourlyRate()) + 
                               (overtimeHours * employee.getHourlyRate() * Config.OVERTIME_MULTIPLIER);
            double lateDeductions = totalMinutesLate * (employee.getHourlyRate() / 60);

            // Monthly deductions divided by 4 for weekly display
            double monthlySSS = getSSSContribution(grossSalary);
            double monthlyPhilHealth = getPhilHealthContribution(grossSalary);
            double monthlyPagIbig = getPagIbigContribution(grossSalary);
            double monthlyWithholdingTax = getWithholdingTax(grossSalary);

            double weeklySSS = monthlySSS / 4;
            double weeklyPhilHealth = monthlyPhilHealth / 4;
            double weeklyPagIbig = monthlyPagIbig / 4;
            double weeklyWithholdingTax = monthlyWithholdingTax / 4;

            double totalDeductions = lateDeductions + weeklySSS + weeklyPhilHealth + weeklyPagIbig + weeklyWithholdingTax;
            double netSalary = grossSalary - totalDeductions;

            displayWeeklyReport(weekCount, currentWeekStart, currentWeekEnd, regularHours, overtimeHours, 
                              totalHoursWorked, daysLate, totalMinutesLate, grossSalary, lateDeductions,
                              weeklySSS, weeklyPhilHealth, weeklyPagIbig, weeklyWithholdingTax, netSalary);

            totalMonthlyGrossSalary += grossSalary;
            totalMonthlyNetSalary += netSalary;
             totalSSS += weeklySSS;
            totalPhilHealth += weeklyPhilHealth;
            totalPagIbig += weeklyPagIbig;
            totalWithholdingTax += weeklyWithholdingTax;
            totalLateDeductions += lateDeductions;
            totalLateDays += daysLate;

            currentWeekStart = currentWeekStart.plusDays(7);
            weekCount++;
        }

        displayMonthlySummary(totalMonthlyGrossSalary, totalLateDeductions, totalSSS, 
                            totalPhilHealth, totalPagIbig, totalWithholdingTax, 
                            totalLateDays, totalMonthlyNetSalary);
    }

    private void displayWeeklyReport(int weekCount, LocalDate start, LocalDate end, 
                                   double regularHours, double overtimeHours, double totalHours,
                                   int daysLate, int minutesLate, double grossSalary, 
                                   double lateDeductions, double sss, double philHealth, 
                                   double pagIbig, double tax, double netSalary) {
        System.out.println("\n----------------------------------------");
        System.out.printf("WEEK %d: %s - %s\n", weekCount, formatDate(start), formatDate(end));
        System.out.println("----------------------------------------");
        System.out.printf("-> Regular Hours Worked: %.2f\n", regularHours);
        System.out.printf("-> Overtime Hours: %.2f\n", overtimeHours);
        System.out.printf("-> Total Hours Worked: %.2f\n", totalHours);
        System.out.printf("-> Days Late: %d\n", daysLate);
        System.out.printf("-> Minutes Late: %d\n", minutesLate);
        System.out.printf("-> Gross Salary: PHP %.2f\n", grossSalary);
        System.out.printf("-> Late Deductions: PHP %.2f\n", lateDeductions);
        System.out.printf("-> SSS Deduction: PHP %.2f\n", sss);
        System.out.printf("-> PhilHealth Deduction: PHP %.2f\n", philHealth);
        System.out.printf("-> Pag-IBIG Deduction: PHP %.2f\n", pagIbig);
        System.out.printf("-> Withholding Tax: PHP %.2f\n", tax);
        System.out.printf("-> Net Salary: PHP %.2f\n", netSalary);
    }

    private void displayMonthlySummary(double grossSalary, double lateDeductions, double sss,
                                     double philHealth, double pagIbig, double tax,
                                     int lateDays, double netSalary) {
        System.out.println("\n========================================");
        System.out.println("TOTAL MONTHLY SUMMARY:");
        System.out.printf("-> Total Gross Salary: PHP %.2f\n", grossSalary);
        System.out.printf("-> Total Late Deductions: PHP %.2f\n", lateDeductions);
        System.out.printf("-> Total SSS Deduction: PHP %.2f\n", sss);
        System.out.printf("-> Total PhilHealth Deduction: PHP %.2f\n", philHealth);
        System.out.printf("-> Total Pag-IBIG Deduction: PHP %.2f\n", pagIbig);
        System.out.printf("-> Total Withholding Tax: PHP %.2f\n", tax);
        System.out.printf("-> Total Days Late: %d\n", lateDays);
        System.out.printf("-> Total Net Salary: PHP %.2f\n", netSalary);
        System.out.println("========================================");
    }

    private String getMonthName(int month) {
        return LocalDate.of(2024, month, 1).getMonth().getDisplayName(
            java.time.format.TextStyle.FULL, Locale.ENGLISH);
    }

    private String formatDate(LocalDate date) {
        return date.format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    // Your existing contribution calculation methods remain the same
    private double getSSSContribution(double salary) {
        if (salary < 3250) {
            return 135.00;
        } else if (salary >= 3250 && salary < 3750) {
            return 157.50;
        } else if (salary >= 3750 && salary < 4250) {
            return 180.00;
        } else if (salary >= 4250 && salary < 4750) {
            return 202.50;
        } else if (salary >= 4750 && salary < 5250) {
            return 225.00;
        } else if (salary >= 5250 && salary < 5750) {
            return 247.50;
        } else if (salary >= 5750 && salary < 6250) {
            return 270.00;
        } else if (salary >= 6250 && salary < 6750) {
            return 292.50;
        } else if (salary >= 6750 && salary < 7250) {
            return 315.00;
        } else if (salary >= 7250 && salary < 7750) {
            return 337.50;
        } else if (salary >= 7750 && salary < 8250) {
            return 360.00;
        } else if (salary >= 8250 && salary < 8750) {
            return 382.50;
        } else if (salary >= 8750 && salary < 9250) {
            return 405.00;
        } else if (salary >= 9250 && salary < 9750) {
            return 427.50;
        } else if (salary >= 9750 && salary < 10250) {
            return 450.00;
        } else if (salary >= 10250 && salary < 10750) {
            return 472.50;
        } else if (salary >= 10750 && salary < 11250) {
            return 495.00;
        } else if (salary >= 11250 && salary < 11750) {
            return 517.50;
        } else if (salary >= 11750 && salary < 12250) {
            return 540.00;
        } else if (salary >= 12250 && salary < 12750) {
            return 562.50;
        } else if (salary >= 12750 && salary < 13250) {
            return 585.00;
        } else if (salary >= 13250 && salary < 13750) {
            return 607.50;
        } else if (salary >= 13750 && salary < 14250) {
            return 630.00;
        } else if (salary >= 14250 && salary < 14750) {
            return 652.50;
        } else if (salary >= 14750 && salary < 15250) {
            return 675.00;
        } else if (salary >= 15250 && salary < 15750) {
            return 697.50;
        } else if (salary >= 15750 && salary < 16250) {
            return 720.00;
        } else if (salary >= 16250 && salary < 16750) {
            return 742.50;
        } else if (salary >= 16750 && salary < 17250) {
            return 765.00;
        } else if (salary >= 17250 && salary < 17750) {
            return 787.50;
        } else if (salary >= 17750 && salary < 18250) {
            return 810.00;
        } else if (salary >= 18250 && salary < 18750) {
            return 832.50;
        } else if (salary >= 18750 && salary < 19250) {
            return 855.00;
        } else if (salary >= 19250 && salary < 19750) {
            return 877.50;
        } else if (salary >= 19750 && salary < 20250) {
            return 900.00;
        } else if (salary >= 20250 && salary < 20750) {
            return 922.50;
        } else if (salary >= 20750 && salary < 21250) {
            return 945.00;
        } else if (salary >= 21250 && salary < 21750) {
            return 967.50;
        } else if (salary >= 21750 && salary < 22250) {
            return 990.00;
        } else if (salary >= 22250 && salary < 22750) {
            return 1012.50;
        } else if (salary >= 22750 && salary < 23250) {
            return 1035.00;
        } else if (salary >= 23250 && salary < 23750) {
            return 1057.50;
        } else if (salary >= 23750 && salary < 24250) {
            return 1080.00;
        } else if (salary >= 24250 && salary < 24750) {
            return 1102.50;
        } else if (salary >= 24750) {
            return 1125.00;
        }
        return 0;
    }

    private double getPhilHealthContribution(double salary) {
        double premium = salary * 0.03;
        if (premium > 1800) premium = 1800;
        return premium / 2;
    }

    private double getPagIbigContribution(double salary) {
        double contribution = salary * 0.02;
        if (contribution > 100) return 100;
        return contribution;
    }

    private double getWithholdingTax(double salary) {
        if (salary <= 20833) return 0;
        else if (salary <= 33332) return (salary - 20833) * 0.2;
        else if (salary <= 66666) return 2500 + (salary - 33333) * 0.25;
        else if (salary <= 166666) return 10833 + (salary - 66667) * 0.3;
        else if (salary <= 666666) return 40833.33 + (salary - 166667) * 0.32;
        else return 200833.33 + (salary - 666667) * 0.35;
    }
}


