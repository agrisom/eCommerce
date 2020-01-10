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

}
