package com.example.testcommon.commom.skipList;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.ContentType;

public class BrowserSimulateClient {

    public static void main(String[] args) {
        String url = "https://www.adguider.com/sv1/calendar/getCalendarAjax";
        String jsonBody = "{\"startTime\":\"2026-02-23\",\"endTime\":\"2026-03-01\",\"fdIdList\":[13,4,3,10,5,6,7,8,9,11]}";

        // 创建 Cookie 存储，自动管理 Cookies
        BasicCookieStore cookieStore = new BasicCookieStore();

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build()) {

            HttpPost httpPost = new HttpPost(url);

            // ========== 模拟浏览器请求头 ==========
            // 设置 User-Agent 为最新 Chrome
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            httpPost.setHeader("Accept", "application/json, text/plain, */*");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            // 如果接口需要来源页，可设置 Referer（通常为网站首页或当前页）
            httpPost.setHeader("Referer", "https://www.adguider.com/");
            httpPost.setHeader("Origin", "https://www.adguider.com");
            httpPost.setHeader("Connection", "keep-alive");
            // 如果存在特定的 Cookie，可以手动设置（从浏览器复制）
            // httpPost.setHeader("Cookie", "your_cookie_key=your_cookie_value; another=value");

            // 设置请求体
            httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            // 创建上下文，用于携带 Cookie 存储
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(cookieStore);

            // 执行请求
            httpClient.execute(httpPost, context, response -> {
                System.out.println("Response Code: " + response.getCode());

                // 输出所有响应头（便于调试）
                System.out.println("Response Headers:");


                // 读取响应体
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("Response Body: " + responseBody);

                // 输出当前存储的 Cookies（自动管理）
                System.out.println("Stored Cookies: " + cookieStore.getCookies());

                return null;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}