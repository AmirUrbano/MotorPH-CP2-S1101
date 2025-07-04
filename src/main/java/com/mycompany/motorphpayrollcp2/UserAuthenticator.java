/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollcp2;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Handles user authentication by reading credentials from CSV file
 * Manages login validation and security features
 * @author Amir
 */
public class UserAuthenticator {
    private static final String CREDENTIALS_FILE = "user_credentials.csv";
    private static final Logger logger = Logger.getLogger(UserAuthenticator.class.getName());
    private Map<String, String> userCredentials;

    public UserAuthenticator() {
        userCredentials = new HashMap<>();
        loadCredentials();
    }

    /**
     * Loads user credentials from CSV file
     * Expected format: username,password
     */
    private void loadCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // Skip empty lines and comments
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }
                
                // Split by comma
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    
                    // Basic validation
                    if (!username.isEmpty() && !password.isEmpty()) {
                        userCredentials.put(username, password);
                        logger.info("Loaded credentials for user: " + username);
                    } else {
                        logger.warning("Invalid credentials format on line " + lineNumber);
                    }
                } else {
                    logger.warning("Invalid line format on line " + lineNumber + ": " + line);
                }
            }
            
            logger.info("Successfully loaded " + userCredentials.size() + " user accounts");
            
        } catch (FileNotFoundException e) {
            logger.severe("Credentials file not found: " + CREDENTIALS_FILE);
            logger.severe("Please ensure the user_credentials.csv file exists in the project directory");
            
            // Create sample credentials file
            createSampleCredentialsFile();
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading credentials file", e);
        }
    }

    /**
     * Creates a sample credentials file if none exists
     */
    private void createSampleCredentialsFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            writer.write("# MotorPH Employee System - User Credentials");
            writer.newLine();
            writer.write("# Format: username,password");
            writer.newLine();
            writer.write("# Add your authorized users below:");
            writer.newLine();
            writer.write("admin,admin123");
            writer.newLine();
            writer.write("manager,manager456");
            writer.newLine();
            writer.write("employee,emp789");
            writer.newLine();
            writer.write("hr,hr2024");
            writer.newLine();
            
            logger.info("Sample credentials file created: " + CREDENTIALS_FILE);
            
            // Load the newly created file
            loadCredentials();
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create sample credentials file", e);
        }
    }

    /**
     * Authenticates user with provided credentials
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return true if authentication successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            logger.warning("Authentication attempt with null credentials");
            return false;
        }

        // Trim whitespace
        username = username.trim();
        password = password.trim();

        // Check if user exists and password matches
        if (userCredentials.containsKey(username)) {
            String storedPassword = userCredentials.get(username);
            if (storedPassword.equals(password)) {
                logger.info("Successful login for user: " + username);
                return true;
            } else {
                logger.warning("Failed login attempt for user: " + username + " (incorrect password)");
                return false;
            }
        } else {
            logger.warning("Failed login attempt for non-existent user: " + username);
            return false;
        }
    }

    /**
     * Gets the number of loaded user accounts
     * @return number of user accounts
     */
    public int getUserCount() {
        return userCredentials.size();
    }

    /**
     * Checks if a username exists in the system
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    public boolean userExists(String username) {
        return userCredentials.containsKey(username != null ? username.trim() : "");
    }

    /**
     * Reloads credentials from file (useful for runtime updates)
     */
    public void reloadCredentials() {
        userCredentials.clear();
        loadCredentials();
    }
}
