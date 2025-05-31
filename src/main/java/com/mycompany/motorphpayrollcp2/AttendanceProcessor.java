/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollcp2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AttendanceProcessor {
    private static final Logger logger = Logger.getLogger(AttendanceProcessor.class.getName());
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();

    // For parsing CSV values
    private static final DateTimeFormatter CSV_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Singleton instance
    private static AttendanceProcessor instance;

    public static AttendanceProcessor getInstance() {
        if (instance == null) {
            instance = new AttendanceProcessor();
        }
        return instance;
    }

    // Load attendance records from CSV file with improved error handling
    public void loadAttendance(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Attendance file not found: " + filePath);
        }
        
        attendanceRecords.clear();
        int lineNumber = 0;
        int recordsLoaded = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                try {
                    String[] data = line.split(",");

                    if (data.length >= 6) {
                        String employeeId = data[0].trim();
                        LocalDate date = LocalDate.parse(data[3].trim(), CSV_DATE_FORMAT);
                        LocalTime timeIn = parseTime(data[4].trim());
                        LocalTime timeOut = parseTime(data[5].trim());

                        if (date != null && timeIn != null && timeOut != null) {
                            AttendanceRecord record = new AttendanceRecord(
                                employeeId,
                                date.toString(),
                                timeIn.toString(),
                                timeOut.toString()
                            );
                            attendanceRecords.add(record);
                            recordsLoaded++;
                        }
                    }
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error parsing line " + lineNumber + ": " + line, e);
                }
            }
            
            logger.info("Successfully loaded " + recordsLoaded + " attendance records from " + filePath);
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading attendance file: " + filePath, e);
            throw new RuntimeException("Error reading attendance file: " + e.getMessage());
        }
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            logger.warning("Invalid date format: " + dateStr);
            return null;
        }
    }

    private LocalTime parseTime(String timeStr) {
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            logger.warning("Invalid time format: " + timeStr);
            return null;
        }
    }

    public double getHoursWorked(String employeeId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        
        return attendanceRecords.stream()
                .filter(record -> record.getEmployeeId().equals(employeeId))
                .filter(record -> {
                    LocalDate date = LocalDate.parse(record.getDate(), DATE_FORMATTER);
                    return !date.isBefore(startDate) && !date.isAfter(endDate);
                })
                .mapToDouble(AttendanceRecord::getHoursWorked)
                .sum();
    }

    public int getMinutesLate(String employeeId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        
        return attendanceRecords.stream()
                .filter(record -> record.getEmployeeId().equals(employeeId))
                .filter(record -> {
                    LocalDate date = LocalDate.parse(record.getDate(), DATE_FORMATTER);
                    return !date.isBefore(startDate) && !date.isAfter(endDate);
                })
                .mapToInt(AttendanceRecord::getLateMinutes)
                .sum();
    }

    public int getDaysLate(String employeeId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        
        return (int) attendanceRecords.stream()
                .filter(record -> record.getEmployeeId().equals(employeeId))
                .filter(record -> {
                    LocalDate date = LocalDate.parse(record.getDate(), DATE_FORMATTER);
                    return !date.isBefore(startDate) && !date.isAfter(endDate);
                })
                .filter(AttendanceRecord::isLate)
                .count();
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    public int getWeekNumber(LocalDate date) {
        return date.get(WeekFields.of(Locale.getDefault()).weekOfMonth());
    }

    public LocalDate getStartOfWeek(LocalDate date) {
        return date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
    }

    public LocalDate getEndOfWeek(LocalDate date) {
        return date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7);
    }
    
    // Get total number of records for debugging
    public int getTotalRecords() {
        return attendanceRecords.size();
    }
}
