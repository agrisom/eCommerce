package com.griso.shop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShopApplicationTest {

    @Test
    void main() {
        ShopApplication.main(new String[] {});
        assert(true);
    }

}