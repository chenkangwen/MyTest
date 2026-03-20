package com.example.testapi.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: chenkangwen
 * @CreateTime: 2026-03-05  11:21
 * @Description:
 * @Version: 1.0
 */
@Data
public class Money implements Cloneable, Serializable {

    private BigDecimal money;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
