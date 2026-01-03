// package com.fanhoufang.solver.common.config;

// import java.text.SimpleDateFormat;
// import java.time.format.DateTimeFormatter;
// import java.util.TimeZone;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import tools.jackson.databind.cfg.DateTimeFeature;
// import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
// import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
// import tools.jackson.databind.json.JsonMapper;
// import tools.jackson.databind.ObjectMapper;

// // @RequiredArgsConstructor // 为所有final字段生成构造器注入

// @Configuration
// public class LocalDateTimeSerializerConfig {

//     @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
//     private String pattern;

//     ObjectMapper mapper = JsonMapper.builder()
//             .defaultDateFormat(new SimpleDateFormat(pattern))
//             .defaultTimeZone(TimeZone.getDefault())
//             .build();

//     @Bean
//     public LocalDateTimeSerializer localDateTimeSerializer() {
//         return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
//     }

//     @Bean
//     public LocalDateTimeDeserializer localDateTimeDeserializer() {
//         return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern));
//     }

//     @Bean
//     JsonMapperBuilderCustomizer jacksonCustomizer() {
//         return builder -> builder
//                 .defaultDateFormat(new SimpleDateFormat(pattern))
//                 .defaultTimeZone(TimeZone.getDefault())
//                 .enable(DateTimeFeature.WRITE_DATES_WITH_ZONE_ID);
//     }
// }
