package com.zbinyds.learn.validate.test;

import com.zbinyds.learn.validate.core.annotation.NaturalNumber;
import lombok.Data;

/**
 * @Author zbinyds
 * @Create 2024-11-18 14:35
 */

@Data
public class TestModel {
    @NaturalNumber(groups = {TestGroup.Test2.class})
    private Integer t1;

    @NaturalNumber(message = "不是自然数", groups = {TestGroup.Test2.class, TestGroup.Test1.class})
    private Integer t2;

    @NaturalNumber(groups = {TestGroup.Test1.class})
    private Integer t3;
}
