package com.oteller.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApigatewayApplicationTest {

    @Test
    void mainTest() {
        ApigatewayApplication.main(new String[]{});
    }

    @Test
    void contextLoads() {
        // Ensures application context loads correctly
    }
}
