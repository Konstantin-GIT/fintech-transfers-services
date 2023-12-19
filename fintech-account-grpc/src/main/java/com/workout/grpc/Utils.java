package com.workout.grpc;

import java.util.regex.Pattern;
public class Utils {

    public static Boolean containsOnlyDigitsAndNotEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
       return Pattern.matches("[+-]?\\d+", str);
    }

    public static Boolean isDebitAmount(String amountOfBalanceChange) {
        if (amountOfBalanceChange == null || amountOfBalanceChange.isEmpty()) {
            return false;
        }
        char firstChar = amountOfBalanceChange.charAt(0);
        return firstChar == '-';
    }
}
