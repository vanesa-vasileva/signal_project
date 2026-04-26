package com.main;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;

/**
 * Entry point that allows selecting which class to run
 * via a command-line argument.
 *
 * Usage:
 *   java -jar app.jar DataStorage      → runs DataStorage
 *   java -jar app.jar                  → runs HealthDataSimulator
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equalsIgnoreCase("DataStorage")) {
            DataStorage.main(new String[]{});
        } else {
            HealthDataSimulator.main(args);
        }
    }
}