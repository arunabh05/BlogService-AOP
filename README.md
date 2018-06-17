# BlogService-AOP
Implemention of retry and authorization concerns for a blog service through Aspect Oriented Programming (AOP).
The blog service allows access to and sharing of blogs. One can share his blog with others, 
and can share  with others the blogs he is shared with too. 

The blog service is defined as follows:

```javascript
public interface BlogService {
	/**
 	* Read the blog of another user or oneself.
 	* @param userId the ID of the current user
 	* @param blogUserId the ID of user, whose blog is being requested
 	* @return the blog for blogUserId  
 	*/
	Blog readBlog(String userId, String blogUserId)  throws AccessDeniedExeption, NetworkException, IllegalArgumentException;
    
        /**
 	* Comment on another user’s blog with a message.
 	* @param userId the ID of the current user
 	* @param blogUserId the ID of user, whose blog is being commented
 	*/
	void commentOnBlog(String userId, String blogUserId, String message)  throws AccessDeniedExeption, NetworkException, IllegalArgumentException;
    
	/**
 	* Share a blog with another user. The blog may or may not belong to the current user.
 	* @param userId the ID of the current user
 	* @param blogUserId the ID of the user, whose blog is being shared
 	* @param targetUserId the ID of the user to share the blog with
 	*/
	void shareBlog(String userId, String blogUserId, String targetUserId)  throws AccessDeniedExeption, NetworkException, IllegalArgumentException;
    
	/**
 	* Unshare the current user's own blog with another user.
 	* @param userId
 	* @param targetUserId
 	*/
	void unshareBlog(String userId, String targetUserId)  throws AccessDeniedExeption, NetworkException,IllegalArgumentException;
}
```
  
## Features

Uses AOP to enforce the following authorization and retry policies,

1. Once can share his blog with anybody.
2. One can only read blogs that are shared with him, or his own blog. In any other case, an **AccessDeniedExeption** is thrown.

**Example:** If Alice shares her blog with Bob, Bob can further share Alice’s blog with Carl. If Alice attempts to share Bob’s blog with Carl while Bob’s blog is not shared with Alice in the first place, Alice gets an **AccessDeniedExeption**.

3. One can only unshare his own blog. 

**Example:** When unsharing a blog with Bob that the blog is not shared by any means with Bob in the first place, the operation throws an **AccessDeniedExeption**.

4. Both sharing and unsharing of Alice’s blog with Alice have no effect; i.e. Alice always has access to her own blog, and can share and unshare with herself without encountering any exception, even these operations do not take any effect.

5. For all the methods, every parameters for user ID must of a string of at least 3 and maximum 16 unicode characters, or an **IllegalArgumentException** is thrown.  
6. For any blog that is shared with Alice, she can comment on it with a message that is up to 100 unicode characters. If the message is longer than 100, or null, or empty, an **IllegalArgumentException** is thrown.

**Note**: _Access control assumes that authentication is already taken care of elsewhere, i.e., it’s outside the scope of the project to make sure only Alice can call readBlog with userId as “Alice”_.

7. All the methods in BlogService can also run into network failures, in which case, an NetworkException will be thrown, and the method takes no effect at all. The feature to automatically retry for up to two times for a network failure (indicated by an NetworkException).

**Note**: _Two retries are in addition to the original failed invocation; if the application still encounter NetworkException on your second (i.e., final) retry, the application will throw out this NetworkException so that the caller knows its occurrence_.
