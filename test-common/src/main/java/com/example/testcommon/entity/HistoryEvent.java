package com.example.testcommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 历史事件实体类
 */
@Data
@AllArgsConstructor
public class HistoryEvent {

    // 年份，如 "1949年"
    private String year;
    // 标题，如 "平津战役结束，北平和平解放"
    private String title;
    // 内容描述
    private String content;
}