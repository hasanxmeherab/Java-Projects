public class User {
    private int id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private boolean isAdmin;

    public User(int id, String username, String name, String email, String phone, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }
}
