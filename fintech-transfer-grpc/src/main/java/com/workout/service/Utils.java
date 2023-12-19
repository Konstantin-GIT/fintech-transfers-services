package com.workout.service;

import java.util.regex.Pattern;
public class Utils {

    public static Boolean containsOnlyDigitsAndNotEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
       return Pattern.matches("\\d+", str);
    }

}
