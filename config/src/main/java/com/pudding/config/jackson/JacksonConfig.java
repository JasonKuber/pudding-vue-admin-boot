package com.pudding.config.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 注册 Java8 时间模块，支持 LocalDateTime、LocalDate 等
        objectMapper.registerModule(new JavaTimeModule());

        // 可自定义模块
        SimpleModule customModule = new SimpleModule();
        // customModule.addSerializer(...);
        // customModule.addDeserializer(...);
        objectMapper.registerModule(customModule);

        // 忽略未知属性，防止解析错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 忽略空 Bean 转 JSON 时的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 禁用转义字符输出
        objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);

        // 时间戳转为 ISO 标准时间格式（如：yyyy-MM-dd'T'HH:mm:ss）
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 统一时间格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 不序列化值为 null 的字段，减少数据传输量
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 支持驼峰命名法和下划线转换
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // 解决 Long 类型精度丢失问题，将 Long 转为 String
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

        return objectMapper;
    }


}
