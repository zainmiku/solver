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
 * 求解器性能比较
 * </p>
 */
@TableName("solver_performance_comparisons")
@Data
public class SolverPerformanceComparison implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 比较ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 基准求解器ID
     */
    private Long baseSolverId;

    /**
     * 对比求解器ID
     */
    private Long compareSolverId;

    /**
     * 对比任务数量
     */
    private Integer taskCount;

    /**
     * 平均加速比（对比/基准）
     */
    private BigDecimal avgSpeedup;

    /**
     * 平均执行时间差异（秒）
     */
    private BigDecimal avgExecutionTimeDiff;

    /**
     * 成功率差异（%）
     */
    private BigDecimal successRateDiff;

    /**
     * 结果准确度（%）
     */
    private BigDecimal resultAccuracy;

    /**
     * 更快次数
     */
    private Integer fasterCount;

    /**
     * 更慢次数
     */
    private Integer slowerCount;

    /**
     * 更稳定次数
     */
    private Integer moreStableCount;

    /**
     * 比较时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime comparedAt;

    /**
     * 创建用户ID
     */
    private Long createdBy;

}