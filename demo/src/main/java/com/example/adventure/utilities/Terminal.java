package com.example.adventure.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Terminal {
    private final static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private String clean(String text) {
        return text.trim().toUpperCase();
    }

    private boolean test(String text) {
        return text == null || text.isBlank();
    }

    public String inputText(String prompt) throws IOException {
        System.out.print(prompt);
        return reader.readLine();
    }

    public int inputNumber(String prompt) throws IOException {
        System.out.print(prompt);
        while (true) {
            try {
                String text = reader.readLine();
                return Integer.parseInt(text);
            }  catch (NumberFormatException e) {
                System.out.println("Not a valid number!");
            }
        }
    }

    public boolean inputConfirm(String prompt, boolean defaultValue) throws IOException {
        System.out.print(prompt);

        if (defaultValue) {
            System.out.print(" [Y/n] ");
        } else {
            System.out.print(" [y/N] ");
        }

        while (true) {
            try {
                String text = reader.readLine();

                text = clean(text);
                if (text.equals("Y")) {
                    return true;
                } else if (text.equals("N")) {
                    return false;
                }
                return defaultValue;
            }  catch (NumberFormatException e) {
                System.out.println("Not a valid option!");
            }
        }
    }

    public String inputOptions(String prompt, String[] options) throws IOException {
        while (true) {
            System.out.print(prompt);
            String text = reader.readLine();
            
            if (test(text)) {
                System.out.println("Input cannot be empty!");
                continue;
            }
            
            text = clean(text);
            String match = null;
            int matches = 0;

            for (String option : options) {
                String upper = option.toUpperCase();
                if (upper.equals(text)) {
                    return option;
                } else if (upper.startsWith(text)) {
                    match = option;
                    matches++;
                }
            }

            if (matches == 0) {
                System.out.println("Not a valid option!");
            } else if (matches > 1) {
                System.out.println("Multiple options match!");
            } else {
                return match;
            }
        }
    }

    public <E extends Enum<E>> E inputOptions(String prompt, Class<E> enumClass) throws IOException {
        E[] options = enumClass.getEnumConstants();
        
        while (true) {
            System.out.print(prompt);
            String text = reader.readLine();
            
            if (test(text)) {
                System.out.println("Input cannot be empty!");
                continue;
            }
            
            text = clean(text);
            E match = null;
            int matches = 0;

            for (E option : options) {
                String name = option.name();

                if (name.equals(text)) {
                    return option;
                } else if (name.startsWith(text)) {
                    match = option;
                    matches++;
                }
            }

            if (matches == 0) {
                System.out.println("Not a valid option!");
            } else if (matches > 1) {
                System.out.println("Multiple options match!");
            } else {
                return match;
            }
        }
    }
}
