package com.example.android.meme;

/**
 * Created by hp on 13-03-2018.
 */

public class Message {
    private String Content;
    private String dateTime;
    private String sender;
    private String receiver;

    public Message()
    {

    }
    public Message(String Content, String dateTime, String sender, String reciever)
    {
        this.Content = Content;
        this.dateTime = dateTime;
        this.sender = sender;
        this.receiver = reciever;
    }

    public String getContent()
    {
        return Content;
    }
    public void setContent(String Content)
    {
        this.Content = Content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}
