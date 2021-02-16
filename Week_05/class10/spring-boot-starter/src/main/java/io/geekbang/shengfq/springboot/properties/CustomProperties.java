package io.geekbang.shengfq.springboot.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自动化配置属性读取
 *
 * */
@Data
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
    private int age;
    private String name;
    private String schoolName;
    private String klassName;
    private boolean enabled;
}
