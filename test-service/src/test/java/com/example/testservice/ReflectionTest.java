package com.example.testservice;

import com.alibaba.fastjson.JSON;
import com.example.testapi.dto.UserDTO;
import com.example.testapi.service.MyInterface;
import com.example.testcommon.commom.mode.proxyMode.jdkDynamicProxy.SuperMarketInvocationHandler;
import com.example.testcommon.entity.Result;
import com.example.testservice.boot.TestServiceApplication;
import com.example.testservice.service.impl.MyInterfaceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.Scanner;

/**
 * @Author: chenkangwen
 * @CreateTime: 2025-06-19  11:00
 * @Description:
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestServiceApplication.class)
@EnableAutoConfiguration
public class ReflectionTest {


    @Test
    public void test() {
        Result result = new Result();


        Class stuClass = result.getClass();//获取Class对象
        System.out.println(stuClass.getName());

        //第二种方式获取Class对象
        Class stuClass2 = Result.class;
        System.out.println(stuClass == stuClass2);//判断第一种方式获取的Class对象和第二种方式获取的是否是同一个

        //第三种方式获取Class对象
        try {
            Class stuClass3 = Class.forName("com.example.testcommon.entity.Result");//注意此字符串必须是真实路径，就是带包名的类路径，包名.类名
            Constructor[] list = stuClass3.getDeclaredConstructors();
            for (Constructor constructor : list) {
                constructor.setAccessible(true);
                int parameterCount = constructor.getParameterCount();
                if (1 == parameterCount) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUserName("sxx");
                    result = (Result) constructor.newInstance(userDTO);
                }
            }
            System.out.println(JSON.toJSONString(result));
            System.out.println(stuClass3 == stuClass2);//判断三种方式是否获取的是同一个Class对象
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入key");
        String key = scanner.next();
        switch (key) {
            case "1":
                UserDTO userDTO = new UserDTO();
                userDTO.setUserName("sxx");
                System.out.println(JSON.toJSONString(userDTO));
                break;
            default:
                System.out.println("nothing");
                break;
        }
    }

    @Test
    public void proxy() {
        MyInterface myInterface = new MyInterfaceImpl();

        MyInterface proxyInstance = (MyInterface) Proxy.newProxyInstance(
                MyInterface.class.getClassLoader(),
                new Class<?>[]{MyInterface.class},
                new SuperMarketInvocationHandler(myInterface));

        proxyInstance.doSomething(); // 输出将会包括代理逻辑的输出。

        proxyInstance.notDoSomething(); // 输出将会包括代理逻辑的输出。

    }
}
