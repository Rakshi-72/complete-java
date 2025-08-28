package com.rakshi.bank.domains.utils;

import java.util.concurrent.atomic.AtomicLong;

public class CustomIdGenerator {

    public static final AtomicLong USER_ID_GENERATOR = new AtomicLong(0);
    public static final AtomicLong ACCOUNT_ID_GENERATOR = new AtomicLong(0);

}
