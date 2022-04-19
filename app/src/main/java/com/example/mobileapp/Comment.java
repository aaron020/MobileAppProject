package com.example.mobileapp;
/*
Comment class
 */
public class Comment implements Comparable<Comment> {
    private String username;
    private double timestamp;
    private String text;
    //Each comment needs a postId to identify which post the comment was made on
    private String postId;

    public Comment(){}

    public Comment(String username, String postId, String text)  {
        this.username = username;
        this.postId = postId;
        this.timestamp = System.currentTimeMillis();;
        this.text = text;
    }

    public String getPostId() {
        return postId;
    }

    public String getUsername() {
        return username;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    @Override
    public int compareTo(Comment comment) {
        if(this.timestamp > comment.timestamp){
            return -1;
        }else if (this.timestamp < comment.timestamp){
            return 1;
        }
        return 0;
    }
}
