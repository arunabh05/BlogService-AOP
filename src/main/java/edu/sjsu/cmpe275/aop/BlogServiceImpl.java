package edu.sjsu.cmpe275.aop;

import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;
import edu.sjsu.cmpe275.aop.exceptions.NetworkException;

import java.util.HashMap;

public class BlogServiceImpl implements BlogService{

    private HashMap<String, Blog> blogs = new HashMap<String, Blog>();

    public Blog readBlog(String userId, String blogUserId) throws AccessDeniedExeption, NetworkException, IllegalArgumentException {
        System.out.printf("User %s requests to read user %s's blog\n", userId, blogUserId);

        if(!blogs.containsKey(blogUserId)){
            blogs.put(blogUserId, new Blog());
        }
        return blogs.get(blogUserId);
    }

	public void shareBlog(String userId, String blogUserId, String targetUserId)
			throws AccessDeniedExeption, NetworkException {

        System.out.printf("User %s shares user %s's blog with user %s\n", userId, blogUserId, targetUserId);

        if(!blogs.containsKey(blogUserId)){
            blogs.put(blogUserId, new Blog());
        }

        Blog blog = blogs.get(blogUserId);
        blog.addSharedUserId(targetUserId);

        printUsers(blog);
	}

	public void unshareBlog(String userId, String targetUserId) throws AccessDeniedExeption, NetworkException {

        System.out.printf("User %s unshares his/her own blog with user %s\n", userId, targetUserId);

        if(!blogs.containsKey(userId)){
            blogs.put(userId, new Blog());
        }

        Blog blog = blogs.get(userId);
        blog.removeSharedUserId(targetUserId);

        printUsers(blog);
    }

	public void commentOnBlog(String userId, String blogUserId, String message)
			throws AccessDeniedExeption, IllegalArgumentException, NetworkException {

        System.out.printf("User %s commented on %s's blog\n", userId, blogUserId);

        if(!blogs.containsKey(userId)){
            blogs.put(userId, new Blog());
        }

        Blog blog = blogs.get(blogUserId);
        blog.addNewComment(userId, message);

        printComments(blog);
    }

    private void printUsers(Blog blog){
        System.out.println("****************** Users ******************");
        for(String users: blog.getSharedUserIds()){
            System.out.print(" ["+ users +"]");
        }
        System.out.println("");
    }

    private void printComments(Blog blog){
        System.out.println("****************** Comments ******************");
        for(String user: blog.getSharedUserIds()){
            System.out.print(" ["+ user +"]");
            if(blog.getComments().get(user) != null){
                for(String comment : blog.getComments().get(user)){
                    System.out.println("--> "+ comment);
                }
            }
        }
        System.out.println("");
    }
}