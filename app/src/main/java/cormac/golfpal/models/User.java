package cormac.golfpal.models;

public class User {
    public String uId;
    public String email;

    public User(){}

    public User(String uId, String email) {
        this.uId = uId;
        this.email = email;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
