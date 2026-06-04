package com.example.adventure.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Terminal {

    private static final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    private Terminal() {}

    private static String cleanText(String text) {
        return text.trim().toUpperCase();
    }

    private static boolean isInvalid(String text) {
        return text == null || text.isBlank();
    }

    public static boolean isNumeric(String text) {
        return !isInvalid(text) && text.chars().allMatch(Character::isDigit);
    }

    public static void inputNull() {
        System.out.print("Press Enter to continue ");
        
        try {
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input.", e);
        }
    }

    public static String inputText(String prompt) {
        System.out.print(prompt);

        try {
            String line = reader.readLine();

            if (line == null) {
                throw new IllegalStateException("Input stream closed.");
            }

            return line;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input.", e);
        }
    }

    public static int inputNumber(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(inputText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number!");
            }
        }
    }

    public static boolean inputConfirm(String prompt, boolean defaultValue) {
        while (true) {
            String text = inputText(prompt);

            if (isInvalid(text)) {
                System.out.println("Input cannot be empty!");
                continue;
            }

            return switch (text.charAt(0)) {
                case 'Y' | 'y' -> true;
                case 'N' | 'n' -> false;
                default -> defaultValue;
            };
        }
    }

    public static String inputOptions(String prompt, String[] options) {
        while (true) {
            String text = inputText(prompt);
            
            if (isInvalid(text)) {
                System.out.println("Input cannot be empty!");
                continue;
            }
            
            text = cleanText(text);

            if (isNumeric(text)) {
                int index = Integer.parseInt(text);

                if (index < 1 || index > options.length) {
                    System.out.printf("Numeric input must be between 1 and %d!\n", options.length);
                    continue;
                }

                return options[index - 1];
            }

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

    public static <E extends Enum<E>> E inputOptions(String prompt, Class<E> enumClass) {
        return inputOptions(prompt, enumClass.getEnumConstants());
    }

    public static <E extends Enum<E>> E inputOptions(String prompt, E[] options) {
        while (true) {
            String text = inputText(prompt);
            
            if (isInvalid(text)) {
                System.out.println("Input cannot be empty!");
                continue;
            }
            
            text = cleanText(text);

            if (isNumeric(text)) {
                int index = Integer.parseInt(text);

                if (index < 1 || index > options.length) {
                    System.out.printf("Numeric input must be between 1 and %d!\n", options.length);
                    continue;
                }

                return options[index - 1];
            }

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