package com.example.testservice;

import com.example.testapi.dto.BaseEntityDTO;
import com.example.testapi.dto.UserDTO;
import com.example.testapi.es.TestUserEs;
import com.example.testcommon.commom.algorithm.innerClass.Outter;
import com.example.testcommon.commom.algorithm.sorts.BubbleSort;
import com.example.testcommon.commom.algorithm.sorts.HeapSort;
import com.example.testcommon.commom.algorithm.sorts.QuickSort;
import com.example.testcommon.commom.algorithm.sorts.SelectionSort;
import com.example.testcommon.commom.algorithm.sorts.TopKSort;
import com.example.testservice.boot.TestServiceApplication;
import com.example.testservice.utils.DateUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: chenkangwen
 * @CreateTime: 2023-08-10  10:29
 * @Description:
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestServiceApplication.class)
@EnableAutoConfiguration
public class CommonTest {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final UserDTO userDTO = new UserDTO();

    private static final String REGEXP = "huawei";

    private static final int[] arr = new int[]{19, 80, 95, 24, 78, 98, 10, 70, 100, 30};

    private static final int[] test_arr = new int[]{10, 19, 24, 30, 70, 78, 80, 95, 98, 100};


    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public static void main(String[] args) {
        String s1 = "runoob";
        String s2 = "runoob";
        System.out.println("s1== s2 is:" + s1 == s2);

        Outter.Inner inner = new Outter.Inner();

        Outter outter = new Outter();
        Outter.InnerClassFood innerClassFood = outter.new InnerClassFood();


    }

    @Test
    public void test() {
        Date now = DateUtils.parseDate("2024-07-29 19:34:41");
        System.out.println(now);
    }

    @Test
    public void test_1() {
        List<? extends BaseEntityDTO> list = new ArrayList<>();
        List<? super BaseEntityDTO> baseEntityDTOS = new ArrayList<>();
    }

    /**
     * @description: es
     * @author: chenkangwen
     * @date: 2024/11/7
     * @param: []
     */
    @Test
    public void test_2() {
        TestUserEs testUserEs = new TestUserEs();
        testUserEs.setId(20L);
        testUserEs.setName("xixihaha");
        testUserEs.setAge(30);
        testUserEs = elasticsearchRestTemplate.save(testUserEs);

    }


    @Test
    public void test_3() {
        String str = "google taobao jingdong tengxun huawei huawei";
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(str);

        System.out.println(matcher.find());
        System.out.println(matcher.matches());

        boolean matches = Pattern.compile("^1[3-9]\\d{9}$").matcher("13812345678").matches();
        System.out.println(matches);

        String s = matcher.replaceAll("google");
    }


    @Test
    public void test_4() {
        BubbleSort.bubbleSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "----");
        }
    }


    @Test
    public void test_5() {
        HeapSort.heapSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "----");
        }
    }


    @Test
    public void test_6() {
        QuickSort.quickSort(test_arr, 0, test_arr.length - 1);
        for (int i = 0; i < test_arr.length; i++) {
            System.out.print(test_arr[i] + "----");
        }
    }


    @Test
    public void test_7() {
        TopKSort.topKSort();
    }


    @Test
    public void test_8() {
        SelectionSort.selectionSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "----");
        }
    }


    /**
     * 有一个停车场收费逻辑如下
     * 当前时间到次日凌晨2点00分为一天 每天收费15元
     * 不满一天的部分按小时收费 每小时收费1元
     * 周六日收费翻倍
     **/


    @Test
    public void test_9() throws Exception {
        String dateStartStr = "2025-06-10 00:30:00";
        String dateEndStr = "2025-06-17 15:00:00";
        Date dayStart = sdf.parse(dateStartStr);
        Date dayEnd = this.getDayEnd(dayStart);
        BigDecimal timeDifference = this.timeDifference(dayStart, dayEnd);
        System.out.println(timeDifference);

    }

    @Test
    public void test_10() {
        BigDecimal num1 = new BigDecimal("5.003");
        BigDecimal num2 = new BigDecimal("123.4500");
        BigDecimal num3 = new BigDecimal("0.000");

        DecimalFormat df = new DecimalFormat("0.00");  // 最多保留2位小数，自动丢弃末尾0
        System.out.println(df.format(num1));  // 输出: 5
        System.out.println(df.format(num2));  // 输出: 123.45
        System.out.println(df.format(num3));  // 输出: 0


        char ch1 = 88;

        char ch2 = 'A';


    }


    public BigDecimal timeDifference(Date dayStart, Date dayEnd) {
        BigDecimal time = new BigDecimal(String.valueOf(dayEnd.getTime() - dayStart.getTime()));
        BigDecimal bigDecimal = new BigDecimal(1000 * 60 * 60);
        return time.divide(bigDecimal, 2, RoundingMode.HALF_UP);
    }


    /**
     * @description:
     * @author: chenkangwen
     * @date: 2025/6/17
     * @param: [dateStr]
     */
    public Date getDayEnd(Date dayStart) {
        Date dateEnd = null;
        try {

            Calendar instance1 = Calendar.getInstance();
            instance1.setTime(dayStart);

            Calendar instance2 = Calendar.getInstance();
            instance2.set(instance1.get(Calendar.YEAR), instance1.get(Calendar.MONTH), instance1.get(Calendar.DATE), 2, 0, 0);

            if (instance1.getTime().after(instance2.getTime())) {
                instance2.set(Calendar.DAY_OF_MONTH, instance2.get(Calendar.DAY_OF_MONTH) + 1);
            }
            dateEnd = instance2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateEnd;
    }
}