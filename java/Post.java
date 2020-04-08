package com.example.android.meme;

/**
 * Created by rahul on 17-03-2018.
 */

public class Post {
    private String PhotoURL,title,description,UID,Type,Dept_Type,Batch_Year_Type,post_id,posted_by,date;

    public Post() {
    }

    public Post(String photoURL, String title, String description, String UID, String type, String dept_Type, String batch_Year_Type, String post_id, String posted_by, String date) {
        PhotoURL = photoURL;
        this.title = title;
        this.description = description;
        this.UID = UID;
        Type = type;
        Dept_Type = dept_Type;
        Batch_Year_Type = batch_Year_Type;
        this.post_id = post_id;
        this.posted_by = posted_by;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setPhotoURL(String image) {
        this.PhotoURL = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getType() {
        return Type;
    }

    public String getDept_Type() {
        return Dept_Type;
    }

    public String getBatch_Year_Type() {
        return Batch_Year_Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setDept_Type(String dept_Type) {
        Dept_Type = dept_Type;
    }

    public void setBatch_Year_Type(String batch_Year_Type) {
        Batch_Year_Type = batch_Year_Type;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }
}
