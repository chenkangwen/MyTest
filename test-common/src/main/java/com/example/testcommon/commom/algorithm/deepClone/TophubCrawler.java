package com.example.testcommon.commom.algorithm.deepClone;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;

public class TophubCrawler {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120 Safari/537.36";
    private static final String DEFAULT_URL = "https://tophub.today/n/KMZd7X3erO";

    public static void main(String[] args) {


        // 创建 Playwright 对象
        try (Playwright playwright = Playwright.create()) {
            // 启动浏览器（这里使用 Chromium，支持 headless 模式）
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            // 创建上下文，可以设置用户代理
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent(USER_AGENT));
            Page page = context.newPage();

            // 导航到目标 URL，等待页面加载完成
            page.navigate(DEFAULT_URL, new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));

            // 定位榜单表格，等待表格出现（最多 10 秒）
            ElementHandle table = page.waitForSelector("table", new Page.WaitForSelectorOptions().setTimeout(10000));
            if (table == null) {
                System.out.println("未找到热榜表格.");
                return;
            }

            // 获取所有表格行（tr），限定在 tbody 内
            ElementHandle tbody = table.querySelector("tbody");
            if (tbody == null) {
                System.out.println("未找到表格主体.");
                return;
            }
            ElementHandle[] rows = tbody.querySelectorAll("tr").toArray(new ElementHandle[0]);
            if (rows.length == 0) {
                System.out.println("未找到榜单内容.");
                return;
            }

            // 打印历史上的今天
            System.out.println("打印历史上的今天（Playwright）：");
            for (int i = 0; i < Math.min(20, rows.length); i++) {
                ElementHandle row = rows[i];
                ElementHandle[] tds = row.querySelectorAll("td").toArray(new ElementHandle[0]);
                // 榜单名、第3个td（一般是“热度”），第2个td内a标签的内容和href
                if (tds.length >= 2) {
                    ElementHandle titleA = tds[1].querySelector("a");
                    if (titleA != null) {
                        String title = titleA.textContent();
                        String itemUrl = titleA.getAttribute("href");
                        // 处理相对链接，转换为绝对 URL
                        if (itemUrl != null && !itemUrl.startsWith("http")) {
                            itemUrl = page.url().replaceAll("/[^/]*$", "/") + itemUrl;
                        }
                        System.out.println((i + 1) + ". " + title + " | " + itemUrl);
                    }
                }
            }

            // 关闭资源（browser 关闭时会自动关闭 context 和 page）
            browser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}