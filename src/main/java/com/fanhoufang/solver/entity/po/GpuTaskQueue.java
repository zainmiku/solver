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
 * GPU 任务队列项
 * </p>
 */
@TableName("gpu_task_queues")
@Data
public class GpuTaskQueue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 队列项ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * GPU ID
     */
    private Long gpuId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 在队列中的位置
     */
    private Integer queuePosition;

    /**
     * 队列状态
     */
    private String status;

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