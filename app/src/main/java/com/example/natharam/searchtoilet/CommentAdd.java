package com.example.natharam.searchtoilet;

import java.sql.Timestamp;

public class CommentAdd {

    String comment;
    String rating;
    String timestamp;

    public CommentAdd() {
    }

    public CommentAdd(String comment, String rating,String timestamp1) {
        this.comment = comment;
        this.rating = rating;
        timestamp=timestamp1;

    }



    public String getTimestamp() {
        return timestamp;
    }

    public String getComment() {
        return comment;
    }

    public String getRating() {
        return rating;
    }
}
