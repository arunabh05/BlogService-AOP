package edu.sjsu.cmpe275.aop.aspect;
import edu.sjsu.cmpe275.aop.BlogService;
import edu.sjsu.cmpe275.aop.exceptions.NetworkException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

@Order(3)
@Aspect
public class RetryAspect {
    /***
     * RetryAspect
     * Reattempts to execute in case of request failure due to {@link NetworkException}
     */

    @Autowired
    private BlogService blogService;

    private static int retryCounter = 0;

    /**
     * RetryAdvice: AfterThrowing retryAdvice to retry request in case of {@link NetworkException}. Retry limit set to 2.
     */
    @AfterThrowing(pointcut = "execution(* edu.sjsu.cmpe275.aop.BlogService.*(..))", throwing = "e")
	public void retryAdvice(JoinPoint joinPoint, NetworkException e) throws Throwable{
	    System.out.printf("After throwing exception in execution of the method %s\n", joinPoint.getSignature().getName());

        retryCounter++;
        try {
            System.out.println("");
            System.out.println("Retry Counter:"+retryCounter);
            System.out.println("");

            if(retryCounter == 2){
                retryCounter = 0;
                throw new NetworkException("Network Unavailable: " + e.getMessage());
            }

            if(joinPoint.getSignature().getName().equals("readBlog")){
                blogService.readBlog((String)joinPoint.getArgs()[0], (String)joinPoint.getArgs()[1]);
            }
            else if(joinPoint.getSignature().getName().equals("shareBlog")){
                blogService.shareBlog((String)joinPoint.getArgs()[0], (String)joinPoint.getArgs()[1],
                        (String)joinPoint.getArgs()[2]);
            }
            else if(joinPoint.getSignature().getName().equals("unshareBlog")){
                blogService.readBlog((String)joinPoint.getArgs()[0], (String)joinPoint.getArgs()[1]);
            }
            else if(joinPoint.getSignature().getName().equals("commentOnBlog")){
                blogService.commentOnBlog((String)joinPoint.getArgs()[0], (String)joinPoint.getArgs()[1],
                        (String)joinPoint.getArgs()[2]);
            }
        } catch (Throwable throwable) {
			//throwable.printStackTrace();
		}
	}
}