package com.griso.shop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foo")
@Controller("foo")
public class FooController {

    @GetMapping
    public String getFoo() {
        return "foo";
    }

    @GetMapping("/public")
    public String getPublicContent() {
        return "Public";
    }

    @GetMapping("/private")
    public String getPrivateContent() {
        return "Private (Only logged users)";
    }

    @GetMapping("/admin")
    public String getAdminContent() {
        return "Private (Only ADMIN users)";
    }
}
