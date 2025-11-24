package k23cnt2_nguyenmanhtuan_day5.entity;

public class Info {

    private String name;
    private String nickName;
    private String email;
    private String website;

    public Info(String name, String nickName, String email, String website) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.website = website;
    }

    public String getName() { return name; }
    public String getNickName() { return nickName; }
    public String getEmail() { return email; }
    public String getWebsite() { return website; }

    public void setName(String name) { this.name = name; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public void setEmail(String email) { this.email = email; }
    public void setWebsite(String website) { this.website = website; }
}
