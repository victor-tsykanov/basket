package com.example.basket.core.ports.in.testcheckout;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestCheckoutCommand {
    public static TestCheckoutCommand of() {
        return new TestCheckoutCommand();
    }
}
