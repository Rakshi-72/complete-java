package com.rakshi.bank.userservice.utils.logging;

public class MaskingUtil {

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@", 2);
        String username = parts[0];
        String domain = parts[1];
        String masked = username.charAt(0) + "***" + username.charAt(username.length() - 1);
        return masked + "@" + domain;
    }

    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return phone;
        return "****" + phone.substring(phone.length() - 4);
    }

    public static String maskCustomerId(String customerId) {
        if (customerId == null || customerId.length() < 4) return customerId;
        return "****" + customerId.substring(customerId.length() - 4);
    }
}