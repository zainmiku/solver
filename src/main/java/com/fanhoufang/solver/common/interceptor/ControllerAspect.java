package  com.fanhoufang.solver.common.interceptor;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

/**
 * 类描述: 日志拦截器，打印controller层的入参和出参
 *
 * @author 
 */

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor // 为所有final字段生成构造器注入
public class ControllerAspect {
    // //用来记录请求进入的时间，防止多线程时出错，这里用了ThreadLocal
    // ThreadLocal<Long> startTime = new ThreadLocal<>();

    private final JsonMapper jsonMapper;

    /**
     * 定义切入点，controller下面的所有类的所有公有方法，这里需要更改成自己项目的
     */
    @Pointcut("execution(public * com.fanhoufang.solver.controller..*.*(..))")
    public void controllerAspect(){};

    /**
     * Around 手动控制调用核心业务逻辑，以及调用前和调用后的处理,
     * <p>
     * 注意：当核心业务抛异常后，立即退出，转向AfterAdvice 执行完AfterAdvice，再转到ThrowingAdvice
     *
     * @param pjp the pjp
     * @return object
     * @throws Throwable the throwable
     */
    @Around(value = "controllerAspect()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        //防止不是http请求的方法，例如：scheduled
        if (ra == null) {
            return pjp.proceed();
        }
        HttpServletRequest request = sra.getRequest();
        log.info(pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName() + "===== START ===========================================");
        log.info("URL           : {}" , request.getRequestURL().toString());
        log.info("HTTP_METHOD   : {}" , request.getMethod());
        log.info("IP            : {}" , request.getRemoteAddr());
        Object[] args = pjp.getArgs();
        List<Object> logArgs = new ArrayList<>();
        for (Object arg : args) {
            if (!(arg instanceof MultipartFile)&&!(arg instanceof HttpServletResponse)) {
                logArgs.add(arg);
            }
        }
        log.info("REQUEST ARGS  : {}", jsonMapper.writeValueAsString(logArgs));
        long startTime = System.currentTimeMillis();
        try {
            Object response = pjp.proceed();
            // 3.出参打印
            log.info("RESPONSE       :{}", response != null ? jsonMapper.writeValueAsString(response) : "");
            return response;
        } catch (Throwable e) {
            if (e instanceof MethodArgumentNotValidException) {
                log.info("RESPONSE ERROR:{}", e.getMessage());
            } else {
                log.error("RESPONSE ERROR:{}", Arrays.toString(e.getStackTrace()));
            }
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("SPEND TIME   : {}ms", (endTime - startTime));
            log.info(pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName() + "===== END ===========================================");
        }
    }
}