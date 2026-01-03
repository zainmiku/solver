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
 * 任务执行日志
 * </p>
 */
@TableName("task_execution_logs")
@Data
public class TaskExecutionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 日志级别
     */
    private String logLevel;

    /**
     * 日志消息
     */
    private String logMessage;

    /**
     * 日志时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime logTimestamp;

    /**
     * 日志来源（如SSH, Monitor, Scheduler等）
     */
    private String source;

    /**
     * 额外元数据（JSON）
     */
    private String metadata;

}