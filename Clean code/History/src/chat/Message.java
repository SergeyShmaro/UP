package chat;

import java.util.Date;

public class Message {
    private String id;
    private String author;
    private Date time;
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Message(String id, String author, String message, Date time) {

        this.id = id;
        this.author = author;
        this.message = message;
        this.time = time;
    }

    public String toString() {
        return "id : " + id + "\n Who: " + author + "\n " + message + "\n\t\t\t" + time;
    }
}
