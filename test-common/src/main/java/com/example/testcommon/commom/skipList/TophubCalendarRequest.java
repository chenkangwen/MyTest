package com.example.testcommon.commom.skipList;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TophubCalendarRequest {


    public static void main(String[] args) {
        String url = "https://tophub.today/calendar/events";
        String start = getDateString(2026, 2, 23);
        String end = getDateString(2026, 4, 06);

        // 创建表单参数列表
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("start", start));
        formParams.add(new BasicNameValuePair("end", end));


        // 创建 HttpClient 实例
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            // 设置请求头
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // 设置表单实体（自动进行 URL 编码）
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);

            // 执行请求并处理响应
            try (ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
                System.out.println("Status code: " + statusCode);
                System.out.println("Response body: " + responseBody);
            }
        } catch (Exception e) {
            System.err.println("HTTP 请求异常：" + e.getMessage());
            e.printStackTrace();
        }
    }


    public static String getDateString(Integer year, Integer month, Integer day) {
        OffsetDateTime dateTime = OffsetDateTime.of(
                LocalDate.of(year, month, day),
                LocalTime.MIDNIGHT,
                ZoneOffset.ofHours(8)  // 东八区 +08:00
        );
        String formatted = dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return formatted;
    }

}