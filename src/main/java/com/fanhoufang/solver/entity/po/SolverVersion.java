package com.fanhoufang.solver.entity.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * <p>
 * 求解器版本
 * </p>
 */
@TableName("solver_versions")
@Data
public class SolverVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 求解器版本ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 求解器名称
     */
    private String name;

    /**
     * 版本号
     */
    private String version;

    /**
     * 完整版本标识
     */
    private String fullVersion;

    /**
     * 求解器文件路径（共享存储）
     */
    private String filePath;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 上传用户ID
     */
    private Long uploadedBy;

    /**
     * 是否启用
     */
    private Integer isActive;

    /**
     * 总运行次数
     */
    private Integer totalRuns;

    /**
     * 成功次数
     */
    private Integer successCount;

    /**
     * 失败次数
     */
    private Integer failureCount;

    /**
     * 平均执行时间（秒）
     */
    private BigDecimal avgExecutionTime;

    /**
     * 成功率（%）
     */
    private BigDecimal successRate;

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

}