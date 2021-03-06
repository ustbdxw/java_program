package com.concurrent.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Description: 线程池配置
 *
 * @author dxw
 * @date 2021/3/2 15:06
 */
@Configuration
@ConfigurationProperties(prefix = "threadpool")
public class ThreadPoolConfig {
 
    /**
     * 核心线程数
     */
    private Integer corePoolSize;
 
    /**
     * 最大线程数
     */
    private Integer maxPoolSize;
 
    /**
     * 队列容量
     */
    private Integer queueCapacity;
 
    /**
     * 线程活跃时间（秒）
     */
    private Integer keepAliveSeconds;

    /**
     * 线程池中任务的等待时间
     */
    private Integer awaitTerminationSeconds;
 
    public Integer getCorePoolSize() {
        return corePoolSize;
    }
 
    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
 
    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }
 
    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
 
    public Integer getQueueCapacity() {
        return queueCapacity;
    }
 
    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
 
    public Integer getKeepAliveSeconds() {
        return keepAliveSeconds;
    }
 
    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public Integer getAwaitTerminationSeconds() {
        return awaitTerminationSeconds;
    }

    public void setAwaitTerminationSeconds(Integer awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }
}