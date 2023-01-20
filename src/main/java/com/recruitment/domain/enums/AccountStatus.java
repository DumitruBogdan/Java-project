package com.recruitment.domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum AccountStatus {
    ACTIVE("ACTIVE"),
    ARCHIVED("ARCHIVED");

    private final String name;

    AccountStatus(String name) {
        this.name = name;
    }

    public static Optional<AccountStatus> get(String url) {
        return Arrays.stream(AccountStatus.values())
                .filter(env -> env.name.equals(url))
                .findFirst();
    }

    public String getName() {
        return name;
    }
}
