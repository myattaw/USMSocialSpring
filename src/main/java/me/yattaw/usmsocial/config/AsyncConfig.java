package me.yattaw.usmsocial.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
/**
 * Configuration class for asynchronous task execution, enabling asynchronous
 * processing.
 *
 * @version 17 April 2024
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * Retrieves the asynchronous task executor bean.
     * @return An instance of ThreadPoolTaskExecutor configured for asynchronous task execution.
     */
    @Override
    @Bean(name = "emailExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("EmailExecutor-");
        executor.initialize();
        return executor;
    }

    /**
     * Retrieves the asynchronous uncaught exception handler.
     * @return An instance of SimpleAsyncUncaughtExceptionHandler for handling uncaught exceptions.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}