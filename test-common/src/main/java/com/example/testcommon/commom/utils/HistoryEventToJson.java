package com.example.testcommon.commom.utils;

import com.example.testcommon.entity.HistoryEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HistoryEventToJson {

    public static List<HistoryEvent> crawlHistoryEvents(String url) throws Exception {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();

        List<HistoryEvent> events = new ArrayList<>();
        Elements dtElements = doc.select("dt");
        Elements descElements = doc.select("div.desc");

        for (int i = 0; i < dtElements.size(); i++) {
            String dtText = dtElements.get(i).text().trim();

            if (dtText.matches("^\\d+\\.\\s+\\d{4}年.*")) {
                String titleFull = dtText.replaceFirst("^\\d+\\.\\s+", "");

                // 分离年份和标题
                String year = "";
                String title = "";
                int separatorIndex = titleFull.indexOf('-');
                if (separatorIndex == -1) separatorIndex = titleFull.indexOf('—');

                if (separatorIndex > 0) {
                    year = titleFull.substring(0, separatorIndex);
                    title = titleFull.substring(separatorIndex + 1);
                }

                String content = i < descElements.size() ? descElements.get(i).text().trim().replaceAll("\\s+", " ") : "";

                events.add(new HistoryEvent(year, title, content));
            }
        }

        return events;
    }
}