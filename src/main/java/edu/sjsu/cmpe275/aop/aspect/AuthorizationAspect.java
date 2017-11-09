package edu.sjsu.cmpe275.aop.aspect;

import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;

import edu.sjsu.cmpe275.aop.BlogService;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Order(2)
@Aspect
public class AuthorizationAspect {

    /***
     * Authorization Aspect
     * User Authorization for all request before execution.
     */

    @Autowired
    BlogService blogService;

    /**
     * HashMap for in-memory persistence. HashSet for unique entries.
     */
    private HashMap<String, Set<String>> blogs = new HashMap<String, Set<String>>();

    /**
     * userAuthorizationAdvice: Check user authorization for the requested action. Allows blog owner to perform
     * any action. Allows shared users to perform requested actions.
     * Throws AccessDeniedException When user is not authorized to perform the requested action.
     */
    @Before("execution(* edu.sjsu.cmpe275.aop.BlogService.*(..))")
	public void userAuthorizationAdvice (JoinPoint joinPoint) throws AccessDeniedExeption {

	    System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().getName());
        String blogUserId, userId;

        if(joinPoint.getSignature().getName().equals("unshareBlog")){
            blogUserId = (String) joinPoint.getArgs()[0];
            userId = (String) joinPoint.getArgs()[1];
        }else{
            userId = (String) joinPoint.getArgs()[0];
            blogUserId = (String) joinPoint.getArgs()[1];
        }

        if(userId.equals(blogUserId) || isAuthorized(userId, blogUserId)){
            return;
        }
        throw new AccessDeniedExeption("Access Denied.");
	}

    /**
     * afterShareAdvice: Adds the requested User ID in in-memory data-store.
     */
    @After("execution(* edu.sjsu.cmpe275.aop.BlogService.shareBlog(..))")
    public void afterShareAdvice(JoinPoint joinPoint){

        String blogUserId = (String) joinPoint.getArgs()[1];
        String targetUserId = (String) joinPoint.getArgs()[2];
        addUserId(targetUserId, blogUserId);
    }

    /**
     * afterShareAdvice: Removes the requested User ID in in-memory data-store.
     */
    @After("execution(* edu.sjsu.cmpe275.aop.BlogService.unshareBlog(..))")
    public void afterUnshareAdvice(JoinPoint joinPoint){

        String blogUserId = (String) joinPoint.getArgs()[0];
        String targetUserId = (String) joinPoint.getArgs()[1];
        removeUserId(targetUserId, blogUserId);
    }

    /**
     * Performs user authorization with in-memory data-store.
     */
    private boolean isAuthorized(String userId, String blogUserId){
        if(blogs.containsKey(blogUserId)){
            Set<String> blog = blogs.get(blogUserId);
            return blog.contains(userId);
        } else {
            blogs.put(blogUserId, new HashSet<String>());
            return false;
        }
    }

    private void addUserId(String userId, String blogUserId){
        Set<String> blog = blogs.get(blogUserId);
        if(blogs.containsKey(blogUserId)){
           if(userId.equals(blogUserId)){
               return;
           } else{
               blog.add(userId);
               return;
           }
        }
        blogs.put(blogUserId, new HashSet<String>());
    }

    private void removeUserId(String userId, String blogUserId){
        Set<String> blog = blogs.get(blogUserId);
        if(blogs.containsKey(blogUserId)){
            if(userId.equals(blogUserId)){
                return;
            } else{
                blog.remove(userId);
                return;
            }
        }
        blogs.put(blogUserId, new HashSet<String>());
    }
}