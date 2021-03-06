package xyz.lihang.blog.web.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.lihang.blog.quartz.quartz.AccessRecordQuartz;

import javax.servlet.http.HttpServletRequest;


/**
 * Controller 日志  Aop
 */
@Aspect
@Component
public class ControllerAspectJ {

	private  final Logger log = LoggerFactory.getLogger(getClass());

	public ControllerAspectJ(){
        log.info("Controller 日志  Aop创建");
	}

    //Controller层切点    
    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public  void controllerAspect() {

    }


    /**
     * 前置通知（Before advice） ：在某连接点（JoinPoint）之前执行的通知，但这个通知不能阻止连接点前的执行。
     * @param joinPoint
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint)
    {
        try{
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            log.info("RequestURI:" + request.getRequestURI()  + "\tClassName:" + targetName +"\t Method:"+ methodName);
            AccessRecordQuartz.createAccessRecord(request);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

}
