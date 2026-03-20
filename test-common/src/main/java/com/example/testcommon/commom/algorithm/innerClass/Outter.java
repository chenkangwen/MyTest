package com.example.testcommon.commom.algorithm.innerClass;

/**
 * @Author: chenkangwen
 * @CreateTime: 2026-03-13  09:25
 * @Description:
 * @Version: 1.0
 */
public class Outter {

    private String title = "i am class Outters title";

    public String getTitle() {
        return title;
    }

    public static class Inner {
        public void print() {
            System.out.println("-------------------------------------------------------");
        }
    }


    public class InnerClassFood extends Food {
        public String getName() {
            return super.getName();
        }
    }

    public class InnerClassPurFood extends PureFood {
        public int getPrice() {
            return super.getPrice();
        }
    }


    public String getName() {
        InnerClassFood innerClassFood = new InnerClassFood();
        return innerClassFood.getName();
    }

    public int getPrice() {
        InnerClassPurFood innerClassPurFood = new InnerClassPurFood();
        return innerClassPurFood.getPrice();
    }


}
