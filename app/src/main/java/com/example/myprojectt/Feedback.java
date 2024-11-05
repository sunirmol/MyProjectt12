package com.example.myprojectt;

import java.util.ArrayList;
import java.util.List;

public class Feedback {
    private String id;           // Firestore document ID
    private String userId;
    private String userName;      // User's name (set externally if required)
    private float rating;
    private String category;
    private String feedbackText;
    private boolean isAnonymous;
    private long timestamp;       // Timestamp of feedback submission
    private int likes;            // Number of likes on the feedback
    private List<Reply> replies;  // List of replies to the feedback

    // Constructor
    public Feedback(String userId, float rating, String category, String feedbackText, boolean isAnonymous, long timestamp) {
        this.userId = userId;
        this.rating = rating;
        this.category = category;
        this.feedbackText = feedbackText;
        this.isAnonymous = isAnonymous;
        this.timestamp = timestamp;
        this.likes = 0;
        this.replies = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public float getRating() {
        return rating;
    }

    public String getCategory() {
        return category;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getLikes() {
        return likes;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public void setAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    // Methods to increment likes and manage replies
    public void incrementLikes() {
        this.likes++;
    }

    public void addReply(Reply reply) {
        this.replies.add(reply);
    }

    // Nested Reply class to store replies
    public static class Reply {
        private String replyText;
        private String replierId;
        private long replyTimestamp;

        // Constructor
        public Reply(String replyText, String replierId, long replyTimestamp) {
            this.replyText = replyText;
            this.replierId = replierId;
            this.replyTimestamp = replyTimestamp;
        }

        // Getters
        public String getReplyText() {
            return replyText;
        }

        public String getReplierId() {
            return replierId;
        }

        public long getReplyTimestamp() {
            return replyTimestamp;
        }

        // Setters
        public void setReplyText(String replyText) {
            this.replyText = replyText;
        }

        public void setReplierId(String replierId) {
            this.replierId = replierId;
        }

        public void setReplyTimestamp(long replyTimestamp) {
            this.replyTimestamp = replyTimestamp;
        }
    }
}
