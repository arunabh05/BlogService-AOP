package edu.sjsu.cmpe275.aop.aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;  // if needed
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

@Order(1)
@Aspect
public class ValidationAspect {
    /***
     * ValidationAspect
     * Validates the user ID length and comment length.
     */

	@Before("execution(* edu.sjsu.cmpe275.aop.BlogService.*(..))")
	public void userIdValidation(JoinPoint joinPoint) {
		System.out.printf("Before to the execution of the method %s\n", joinPoint.getSignature().getName());

		String userId = (String) joinPoint.getArgs()[0];
		String blogUserId = (String) joinPoint.getArgs()[1];

		int userIdCodePointCount = userId.codePointCount(0,userId.length());
		int blogUserIdCodePointCount = blogUserId.codePointCount(0,blogUserId.length());

		if(userIdCodePointCount < 3 || userIdCodePointCount > 16){
		    throw new IllegalArgumentException("Invalid User Id");
        }

        if(blogUserIdCodePointCount < 3 || blogUserIdCodePointCount > 16){
            throw new IllegalArgumentException("Invalid Blog User Id");
        }
    }

    @Before("execution(* edu.sjsu.cmpe275.aop.BlogService.shareBlog(..))")
    public void targetUserIdValidation(JoinPoint joinPoint) {
        System.out.printf("Before to the execution of the method %s\n", joinPoint.getSignature().getName());

        String targetId = (String) joinPoint.getArgs()[2];
        int targetUserIdCodePointCount = targetId.codePointCount(0, targetId.length());

        if(targetUserIdCodePointCount < 3 || targetUserIdCodePointCount > 16){
            throw new IllegalArgumentException("Invalid Target User Id");
        }
    }

    @Before("execution(* edu.sjsu.cmpe275.aop.BlogService.commentOnBlog(..))")
    public void commentValidation(JoinPoint joinPoint) {
        System.out.printf("Prior to the execution of the method %s\n", joinPoint.getSignature().getName());

        String comment = (String) joinPoint.getArgs()[2];

        if(comment== null || comment.trim().equals("") || comment.codePointCount(0, comment.length()) > 100){
            throw new IllegalArgumentException("Invalid blog user Id");
        }
    }
}