package com.rakshi.bank.domains.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;

public class CustomIdGenerator implements IdentifierGenerator {

    public static final AtomicLong ACCOUNT_ID_GENERATOR = new AtomicLong(0);
    private static final String PREFIX = "CUST";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        final int[] nextId = {1};

        session.doWork(connection -> {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(
                        "SELECT user_id FROM users ORDER BY created_at DESC LIMIT 1"
                );

                if (rs.next()) {
                    String lastId = rs.getString("user_id");
                    if (lastId != null && lastId.startsWith(PREFIX)) {
                        String numericPart = lastId.substring(PREFIX.length());
                        nextId[0] = Integer.parseInt(numericPart) + 1;
                    }
                }
            }
        });

        return String.format(PREFIX + "%08d", nextId[0]);
    }
}
