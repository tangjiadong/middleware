package com.debug.middleware.server;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * Created by Tang on 2020/7/28
 */
public class JApiDocsMain {
    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("/Users/Tang/JavaCode/JavaBooks/基于SpringBoot实现Java分布式中间件开发入门与实战/02/middleware"); // 项目根目录
        config.setProjectName("middleware"); // 项目名称
        config.setApiVersion("V1.0");       // 声明该API的版本
        config.setDocsPath("/Users/Tang/JavaCode/JavaBooks/基于SpringBoot实现Java分布式中间件开发入门与实战/02/middleware/server/src/main/java/com/debug/middleware/server/apidocs"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档
    }
}
