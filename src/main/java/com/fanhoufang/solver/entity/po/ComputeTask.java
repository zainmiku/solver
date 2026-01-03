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
 * 计算任务
 * </p>
 */
@TableName("compute_tasks")
@Data
public class ComputeTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 求解器版本ID
     */
    private Long solverVersionId;

    /**
     * 单个求解文件ID
     */
    private Long solverFileId;

    /**
     * 求解文件组ID
     */
    private Long solverFileGroupId;

    /**
     * 指定服务器ID（为空则系统分配）
     */
    private Long serverId;

    /**
     * 分配的GPU ID
     */
    private Long gpuId;

    /**
     * 分配的GPU索引
     */
    private Integer gpuIndex;

    /**
     * 求解参数（JSON格式）
     */
    private String parameters;

    /**
     * 任务优先级
     */
    private String priority;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 远程进程PID
     */
    private String remotePid;

    /**
     * 在队列中的位置
     */
    private Integer queuePosition;

    /**
     * 预估持续时间（秒）
     */
    private Integer estimatedDuration;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;

    /**
     * 进入队列时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime queuedAt;

    /**
     * 开始执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishedAt;

    /**
     * 最后心跳时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastHeartbeat;

    /**
     * 超时时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeoutAt;

    /**
     * 退出代码
     */
    private Integer exitCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 日志文件路径
     */
    private String logFilePath;

    /**
     * 结果文件路径
     */
    private String resultFilePath;

    /**
     * 执行时间（秒）
     */
    private BigDecimal executionTimeSeconds;

    /**
     * 峰值GPU显存使用（MB）
     */
    private Integer peakGpuMemoryMb;

    /**
     * 平均GPU利用率（%）
     */
    private BigDecimal avgGpuUtilization;

    /**
     * 提交用户ID
     */
    private Long submittedBy;

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