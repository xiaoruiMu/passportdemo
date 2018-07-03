package com.example.passportdemo.serial;

import java.io.Serializable;

/**
 * Person man
 *
 * @author muxiaorui
 * @create 2018-06-25 10:59
 **/
public class Man extends Person implements Serializable {

    private static final long serialVersionUID = -8500976234275133205L;
    public String sex;
    public String other;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Man(String sex, String other) {
        super();
        this.sex = sex;
        this.other = other;
    }

    @Override
    public String toString() {
        return "Man{" +
                "sex='" + sex + '\'' +
                ", other='" + other + '\'' +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
