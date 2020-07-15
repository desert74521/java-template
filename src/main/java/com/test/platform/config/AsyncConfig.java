package com.test.platform.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        taskExecutor.setCorePoolSize(20);
        //最大线程数
        taskExecutor.setMaxPoolSize(60);
        //队列大小
        taskExecutor.setQueueCapacity(500);
        taskExecutor.setThreadNamePrefix("async-service-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}


