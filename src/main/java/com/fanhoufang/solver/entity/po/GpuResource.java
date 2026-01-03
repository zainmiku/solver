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
 * GPU 资源
 * </p>
 */
@TableName("gpu_resources")
@Data
public class GpuResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * GPU资源ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属服务器ID
     */
    private Long serverId;

    /**
     * GPU索引（从0开始）
     */
    private Integer gpuIndex;

    /**
     * GPU名称（如NVIDIA A100）
     */
    private String name;

    /**
     * 显存大小（MB）
     */
    private Integer memoryMb;

    /**
     * GPU状态
     */
    private String status;

    /**
     * 当前运行的任务ID
     */
    private Long currentTaskId;

    /**
     * GPU利用率（0-100）
     */
    private BigDecimal utilization;

    /**
     * GPU温度
     */
    private BigDecimal temperature;

    /**
     * 最后检查时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChecked;

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