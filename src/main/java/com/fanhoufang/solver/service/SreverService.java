package com.fanhoufang.solver.service;

import java.util.Iterator;

import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhoufang.solver.common.exception.BusinessException;
import com.fanhoufang.solver.entity.po.ComputeServer;
import com.fanhoufang.solver.mapper.ServerMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor // 为所有final字段生成构造器注入
public class SreverService {
    private final ServerMapper sMapper;
    /**
     * MyBatis Plus 游标查询示例
     */
    public void getByCursor() {
        LambdaQueryWrapper<ComputeServer> lqWrapper = new LambdaQueryWrapper<ComputeServer>();
        lqWrapper.ge(ComputeServer::getGpuCount, 1);
        Long serverCount = sMapper.selectCount(lqWrapper);
        sMapper.selectList(lqWrapper, resultContext -> {
            try {
                ComputeServer cServer = resultContext.getResultObject();
                log.info("当前处理第" + resultContext.getResultCount() + "条记录: " + cServer);
                if (resultContext.getResultCount() == 2) {
                    log.error("第2条记录手动抛出测试异常，以测试异常处理机制");
                    throw new BusinessException("测试异常");
                }
                if (resultContext.getResultCount() == serverCount) {
                    log.info("第 " + serverCount + " 条记录是最后一条记录，可做额外处理！");
                }
                // 业务处理逻辑
            } catch (Exception e) {
                log.error("当前处理第" + resultContext.getResultCount() + "条记录异常");
            }

        });
    }
    /**
     * MyBatis 游标查询示例
     */
    @Transactional
    public void getByCursor1() {
        try (Cursor<ComputeServer> cursor = sMapper.getCursorMapper(1L)) {
            Iterator<ComputeServer> iterator = cursor.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                ComputeServer cServer = iterator.next();
                count++;
                boolean isLast = !iterator.hasNext();

                if (isLast) {
                    log.info("第 {} 条记录是最后一条记录，可做额外处理！", count);
                } else {
                    log.info("当前处理第 {} 条记录: {}", count, cServer);
                }
            }
        } catch (Exception e) {
            log.error("游标处理异常", e);
            throw new RuntimeException("游标处理失败", e);
        }
    }

    public String getErr() {
        throw new BusinessException("测试异常");
    }
}