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
 * 求解文件
 * </p>
 */
@TableName("solver_files")
@Data
public class SolverFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件路径（共享存储）
     */
    private String filePath;

    /**
     * 文件描述
     */
    private String description;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型（如CASE, CONFIG等）
     */
    private String fileType;

    /**
     * 文件标签（JSON数组）
     */
    private String tags;

    /**
     * 上传用户ID
     */
    private Long uploadedBy;

    /**
     * 是否启用
     */
    private Integer isActive;

    /**
     * 使用次数
     */
    private Integer usageCount;

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