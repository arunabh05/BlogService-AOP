package edu.sjsu.cmpe275.aop;

import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;
import edu.sjsu.cmpe275.aop.exceptions.NetworkException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

public class BlogServiceTest {

    /***
     * These are dummy test cases. You may add test cases based on your own need.
     */
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
    BlogService blogService = (BlogService) ctx.getBean("blogService");

    @Test
    public void testOne() {
        try {
            Assert.assertEquals(blogService.readBlog("Alice", "Alice"),
                    blogService.readBlog("Alice", "Alice"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTwo() {
        try {
            blogService.shareBlog("Alice", "Alice", "Bob");
            Assert.assertEquals(blogService.readBlog("Bob", "Alice"),
                    blogService.readBlog("Alice", "Alice"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}