package com.debug.middleware.server.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * Created by Tang on 2020/7/15
 */
@Data
@ToString
public class PhoneUser {
    private String telephone;
    private Double money;

    public PhoneUser(String telephone, Double money) {
        this.telephone = telephone;
        this.money = money;
    }

    //手机号相同,代表充值记录重复(只适用于特殊的排名需要),所以要重写equals()和hashCode()方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneUser phoneUser = (PhoneUser) o;
        return telephone != null ? telephone.equals(phoneUser.telephone) : phoneUser.telephone == null;
    }

    @Override
    public int hashCode() {
        return telephone != null ? telephone.hashCode() : 0;
    }
}
