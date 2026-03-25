package com.example.testcommon.commom.algorithm.deepClone;


import com.alibaba.fastjson.JSON;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DouyinHotListCrawler {

    private static final Logger log = LoggerFactory.getLogger(DouyinHotListCrawler.class);

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120 Safari/537.36";

    private static final String DEFAULT_URL = "https://so-landing.douyin.com/landings/hotlist";

    private static final int MAX_HOT_ITEMS = 50;

    private static final int MIN_TEXT_LENGTH = 2;

    private static final int MAX_TEXT_LENGTH = 200;

    private static final long PAGE_LOAD_WAIT_MS = 1500L;

    private static final long RETRY_WAIT_MS = 1000L;

    /**
     * 在页面上执行 JavaScript，提取热榜条目
     */
    private static List<String> extractHotlistFromDom(Page page) {
        // 使用普通字符串拼接，避免文本块兼容性问题
        String js =
                "() => {" +
                        "  const containsChinese = s => /[\\u4e00-\\u9fff]/.test(s);" +
                        "  const normalize = s => s.replace(/\\s+/g, ' ').trim();" +
                        "  const nodes = Array.from(document.querySelectorAll('h1,h2,h3,div,span,section'));" +
                        "  const heading = nodes.find(n => /热点榜|热榜/.test(n.innerText));" +
                        "  if (!heading) return [];" +
                        "  let container = heading.closest('section') || heading.parentElement || document.body;" +
                        "  const listEl = container.querySelector('ul,ol') || container;" +
                        "  const candidates = Array.from(listEl.querySelectorAll('li, a, div, p, span'));" +
                        "  const items = [];" +
                        "  const seen = new Set();" +
                        "  for (const c of candidates) {" +
                        "    let t = c.innerText || '';" +
                        "    t = normalize(t);" +
                        "    if (!t) continue;" +
                        "    if (/更新于|发布时间|发布时间：|查看更多|全部|换一换/.test(t)) continue;" +
                        "    t = t.split('\\n')[0].trim();" +
                        "    t = t.replace(/^[\\s\\d\\.\\u2460-\\u2473\\u3007\\uFF10-\\uFF19]+[\\s\\.\\u3000]*/, '').trim();" +
                        "    t = t.replace(/[\\u2014\\-–—]\\s*$/,'').trim();" +
                        "    if (t.length < 2 || t.length > 200) continue;" +
                        "    if (!containsChinese(t)) continue;" +
                        "    if (seen.has(t)) continue;" +
                        "    seen.add(t);" +
                        "    items.push(t);" +
                        "    if (items.length >= 50) break;" +
                        "  }" +
                        "  return items;" +
                        "}";

        try {
            Object result = page.evaluate(js);
            if (result instanceof List<?>) {
                List<String> items = new ArrayList<>();
                for (Object obj : (List<?>) result) {
                    if (obj != null) {
                        String s = obj.toString().trim();
                        if (!s.isEmpty()) {
                            items.add(s);
                        }
                    }
                }
                return items;
            }
        } catch (Exception e) {
            log.error("执行 JS 提取失败", e);
        }
        return Collections.emptyList();
    }

    /**
     * 使用 Playwright 抓取热榜
     */
    private static List<String> fetchHotlistPlaywright(String url, boolean headless) {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium()
                     .launch(new BrowserType.LaunchOptions().setHeadless(headless));
             BrowserContext context = browser.newContext(
                     new Browser.NewContextOptions().setUserAgent(USER_AGENT));
             Page page = context.newPage()) {

            page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));

            page.waitForTimeout(PAGE_LOAD_WAIT_MS);

            List<String> items = extractHotlistFromDom(page);

            if (items.isEmpty()) {
                page.waitForTimeout(RETRY_WAIT_MS);
                items = extractHotlistFromDom(page);
            }

            return items;
        } catch (Exception e) {
            log.error("抓取热榜失败", e);
            return Collections.emptyList();
        }
    }

    public static void main(String[] args) {
        CrawlConfig config = parseArgs(args);

        List<String> items = fetchHotlistPlaywright(config.url, config.headless);

        String json = JSON.toJSONString(items);
        log.info("热榜数据：{}", json);
    }

    /**
     * 命令行参数配置
     */
    private static class CrawlConfig {
        String url = DEFAULT_URL;
        boolean headless = true;
    }

    /**
     * 解析命令行参数
     */
    private static CrawlConfig parseArgs(String[] args) {
        CrawlConfig config = new CrawlConfig();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--url":
                    if (i + 1 < args.length) {
                        config.url = args[++i];
                    }
                    break;
                case "--no-headless":
                    config.headless = false;
                    break;
                default:
                    log.debug("忽略未知参数：{}", args[i]);
            }
        }

        return config;
    }
}