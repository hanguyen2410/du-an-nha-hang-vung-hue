package com.cg.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Component
public class AppUtils {

    static final String stringAB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public final String DOMAIN_SERVER = "localhost";
    public final long TOKEN_MAX_AGE = 1000 * 60 * 60 * 24;

    private static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    public ResponseEntity<?> mapErrorToResponse(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    public static  LocalDate convertStringToLocalDate(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD);
        return LocalDate.parse(str, formatter);
    }

    public String convertLocalDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD);
        return formatter.format(date);
    }

    public String convertDMYtoYMD(String dmy) {
        return dmy.substring(6, 10) + '-' + dmy.substring(3, 5) + "-" + dmy.substring(0, 2);
    }

    public int getMinute(String fullHour) {
        String hour = fullHour.substring(0, 2);
        String minutes = fullHour.substring(3);
        return Integer.parseInt(hour) * 60 + Integer.parseInt(minutes);
    }

    public String getMinuteNow() {
        LocalTime time = LocalTime.now();
        int hour = time.getHour();
        int minute = time.getMinute();

        String strHour = String.valueOf(hour);
        String strMinute = String.valueOf(minute);

        if (hour < 10) {
            strHour = "0" + hour;
        }

        if (minute < 10) {
            strMinute = "0" + minute;
        }

        return strHour + ":" + strMinute;
    }

    public String randomPassword(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            stringBuilder.append(stringAB.charAt(new SecureRandom().nextInt(stringAB.length())));
        return stringBuilder.toString();
    }

    public String randomCode(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            stringBuilder.append(stringAB.charAt(new SecureRandom().nextInt(stringAB.length())));
        return stringBuilder.toString();
    }

    public String getPrincipalUsername() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }

        return userName;
    }

    public String replaceNonEnglishChar(String str) {
        str = str.toLowerCase();
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("[^\\x00-\\x7F]", "");

        return str;
    }

    public String removeNonAlphanumeric(String str) {
        do {
            str = str.replace(" ","-");
            str = str.replaceAll("[^a-zA-Z0-9\\-]", "-");
            str = str.replaceAll("--", "-");

            while (str.charAt(0) == '-') {
                str = str.substring(1);
            }

            while (str.charAt(str.length() - 1) == '-') {
                str = str.substring(0, str.length() - 1);
            }
        }
        while (str.contains("--"));

        return str.trim().toLowerCase(Locale.ENGLISH);
    }

}
