package up.history;

public class Message {
    private String id;
    private String author;
    private int time;
    private String message;

    public Message(String id, String author, String message, int time) {
        this.id = id;
        this.author = author;
        this.message = message;
        this.time = time;
    }

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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", time=" + time +
                ", message='" + message + '\'' +
                '}';
    }
}
