package main.java.model;

public class User {
    private int id;
    private String username;
    private int highScore;
    
    public User(int id, String username, int highScore) {
        this.id = id;
        this.username = username;
        this.highScore = highScore;
    }
    
    public User(String username) {
        this.username = username;
        this.highScore = 0;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getHighScore() {
        return highScore;
    }
    
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", highScore=" + highScore + "]";
    }
}
