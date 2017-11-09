package edu.sjsu.cmpe275.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of the submission.
         */

    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        BlogService blogService = (BlogService) ctx.getBean("blogService");

        try {
            blogService.unshareBlog("Alice", "Alice");
        	System.out.println(blogService.readBlog("Alice", "Alice"));
        	blogService.shareBlog("Alice", "Alice", "Bob");
            System.out.println(blogService.readBlog("Bob", "Alice"));
            System.out.println(blogService.readBlog("Bob", "Alice"));
            blogService.shareBlog("Bob", "Alice", "Carl");
            System.out.println(blogService.readBlog("Carl", "Alice"));
            blogService.commentOnBlog("Bob", "Alice", "Nice work!");
            blogService.commentOnBlog("Carl", "Alice", "Nice work");
            blogService.unshareBlog("Alice", "Carl");

        } catch (Exception e) {
            e.printStackTrace();
        }
        ctx.close();
    }
}