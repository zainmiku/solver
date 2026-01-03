package com.fanhoufang.solver.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * <p>
 * 系统配置
 * </p>
 */
@TableName("system_configurations")
@Data
public class SystemConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置键
     */
    private String configKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置值类型
     */
    private String configType;

    /**
     * 配置描述
     */
    private String description;

    /**
     * 配置类别
     */
    private String category;

    /**
     * 是否加密存储
     */
    private Integer isEncrypted;

    /**
     * 是否为系统配置
     */
    private Integer isSystem;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * 最后更新用户ID
     */
    private Long updatedBy;

}