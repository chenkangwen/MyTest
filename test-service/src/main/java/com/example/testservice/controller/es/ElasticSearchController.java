package com.example.testservice.controller.es;

import com.example.testservice.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chenkangwen
 * @CreateTime: 2026-03-05  09:50
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RequestMapping(value = "/user")
@RestController
public class ElasticSearchController extends BaseController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
}
