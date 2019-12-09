package com.imau.brms.aspect;

import com.imau.brms.dto.ResourceDTO;
import com.imau.brms.entity.DownloadLog;
import com.imau.brms.entity.ReaderCard;
import com.imau.brms.mapper.DownloadLogMapper;
import com.imau.brms.service.ResourseService;
import com.imau.brms.utils.HttpContextUtils;
import com.imau.brms.utils.IPUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class DownloadLogAspect {

    @Autowired
    private DownloadLogMapper downloadLogMapper;

    @Autowired
    private ResourseService resourseService;


    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation( com.imau.brms.annotation.DownloadControllerLog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        //保存日志
        DownloadLog downloadLog = new DownloadLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //请求的参数
        Object[] args = joinPoint.getArgs();
        Integer resId = (Integer) args[0];
        ResourceDTO resourceDTO = resourseService.selectResourceByResId(resId);
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        ReaderCard reader = (ReaderCard) request.getSession().getAttribute("READER");
        downloadLog.setReaderId(reader.getReaderId());
        downloadLog.setIp((IPUtils.getIpAddr(request)));
        downloadLog.setBookId(resourceDTO.getBookId());
        downloadLog.setBookName(resourceDTO.getBookName());
        downloadLog.setResName(resourceDTO.getResName());
        downloadLog.setDownloadTime(new Date());

        downloadLogMapper.insert(downloadLog);
    }

}
