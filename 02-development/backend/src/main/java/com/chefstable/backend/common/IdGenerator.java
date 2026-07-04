package com.chefstable.backend.common;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    public String prefixed(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().replace("-", "");
    }
}
