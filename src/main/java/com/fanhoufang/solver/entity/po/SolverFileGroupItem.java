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
 * 文件组项关系
 * </p>
 */
@TableName("solver_file_group_items")
@Data
public class SolverFileGroupItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关系ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件组ID
     */
    private Long groupId;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文件在组中的顺序
     */
    private Integer orderIndex;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}