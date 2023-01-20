package com.recruitment.domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum HiredStatus {
    GO("GO"),
    NO_GO("NO_GO");
    private final String name;

    HiredStatus(String name) {
        this.name = name;
    }

    public static Optional<HiredStatus> get(String url) {
        return Arrays.stream(HiredStatus.values())
                .filter(env -> env.name.equals(url))
                .findFirst();
    }

    public String getName() {
        return name;
    }
}
