package com.zbinyds.learn.validate.test;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zbinyds
 * @Create 2024-11-18 14:37
 */

@CrossOrigin
@RestController
@RequestMapping
public class TestController {
    @GetMapping("/test1")
    public String test1(@Validated({TestGroup.Test1.class}) TestModel testModel) {
        return testModel.getClass().toString();
    }

    @GetMapping("/test2")
    public String test2(@Validated({TestGroup.Test2.class}) TestModel testModel) {
        return testModel.getClass().toString();
    }
}
