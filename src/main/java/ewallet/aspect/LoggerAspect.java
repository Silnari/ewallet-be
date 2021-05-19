package ewallet.aspect;

import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Log
@Aspect
@Component
public class LoggerAspect {

    /**
     * Method to log used methods in rest and repository packages
     */
    @Before("execution(* ewallet.rest..*.*(..)) || execution(* ewallet.repository..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info(joinPoint.getTarget().getClass().getSimpleName()
                + " invokes method " + joinPoint.getSignature().getName()
                + Arrays.toString(joinPoint.getArgs()));
    }
}
