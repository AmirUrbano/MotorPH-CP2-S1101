/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollcp2;

import java.time.LocalDate;

/**
 *
 * @author Amir
 */
public class ValidationUtils {
    public static boolean isValidEmployeeId(String employeeId) {
        return employeeId != null && employeeId.matches("\\d{5}");
    }
    
    public static boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }
    
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isValidTime(String timeStr) {
        try {
            java.time.LocalTime.parse(timeStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}