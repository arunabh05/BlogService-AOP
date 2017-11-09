package edu.sjsu.cmpe275.aop;

import java.util.*;

public class Blog {

    private List<String> sharedUserIds;
    private Map<String, List<String>> comments;

    public Blog(){
        this.sharedUserIds = new ArrayList<String>();
        this.comments = new HashMap<String, List<String>>();
    }

    public List<String> getSharedUserIds() {
        return sharedUserIds;
    }

    public Map<String, List<String>> getComments() {
        return comments;
    }

    public void addSharedUserId(String targetUserId){
        for(String sharedUsers : sharedUserIds){
            if(sharedUsers.equalsIgnoreCase(targetUserId)){
                return;
            }
        }
        this.sharedUserIds.add(targetUserId);
    }

    public void removeSharedUserId(String sharedUserId){
        this.sharedUserIds.remove(sharedUserId);
    }

    public void addNewComment(String sharedUserId , String comment){
        if(comments.containsKey(sharedUserId)){
            comments.get(sharedUserId).add(comment);
            return;
        }

        ArrayList<String> commentList = new ArrayList<String>();
        commentList.add(comment);
        comments.put(sharedUserId, commentList);
    }
}
