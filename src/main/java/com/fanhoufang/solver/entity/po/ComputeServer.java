package com.fanhoufang.solver.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 计算服务器
 */
@TableName("compute_servers")
@Data
public class ComputeServer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务器ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 服务器主机/IP
     */
    private String host;

    /**
     * SSH端口
     */
    private Integer port;

    /**
     * SSH用户名
     */
    private String sshUsername;

    /**
     * SSH密码（加密存储）
     */
    private String sshPassword;

    /**
     * SSH私钥路径
     */
    private String sshPrivateKeyPath;

    /**
     * 服务器描述
     */
    private String description;

    /**
     * 服务器状态
     */
    private String status;

    /**
     * GPU数量
     */
    private Integer gpuCount;

    /**
     * 最大并发任务数
     */
    private Integer maxConcurrentTasks;

    /**
     * 服务器标签（JSON数组）
     */
    private String tags;

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
     * 最后心跳时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastHeartbeat;

}