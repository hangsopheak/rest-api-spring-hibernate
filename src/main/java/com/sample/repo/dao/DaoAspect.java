package com.sample.repo.dao;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sample.repo.domain.AbstractLongDomainEntity;
import com.sample.server.Tracer;

/**
 * @author sophea
 */
@Aspect
@Component
public class DaoAspect {
    private static final Logger LOG = LoggerFactory.getLogger(DaoAspect.class);
    /**un-comment below when real implementation*/
    @Around("bean(*ServiceImpl)")
    public Object logDaoInvocation(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getStaticPart().getSignature().getName();
        String name = pjp.getTarget().getClass().getSimpleName() + ":" + methodName;
        
        Tracer.mark(name + ":start");
        try {
            final Object[] args = pjp.getArgs();
            for (Object o : args) {
                LOG.info("=====object={}",  o);
                  //get param object
                if (o instanceof AbstractLongDomainEntity) {
                    final AbstractLongDomainEntity domainEntity = (AbstractLongDomainEntity) o;
                    final Date currentDate = new Date();
                    //set current date 
                    domainEntity.setUpdatedDate(currentDate);
                    
                    if ("add".equals(methodName) || "create".equals(methodName)) {
                            LOG.info("add object triggered");
                            //set current Date
                            domainEntity.setCreatedDate(currentDate);
                            //set version
                            domainEntity.setVersion(1L);
                            
                    } else if ("update".equals(methodName)) {
                            LOG.info("update object triggered");
                            if (domainEntity.getVersion() == null) {
                                domainEntity.setVersion(1L);
                            } else {
                                domainEntity.setVersion(domainEntity.getVersion() +1);
                            }
                    }
                }
            }

            return pjp.proceed();
        }
        finally {
            Tracer.markAndOutput(name + ":end");
        }
    }
}
