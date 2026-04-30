public abstract class User {
    private String id;
    private String name;
    private String email;
    private boolean isSuspended;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isSuspended = false;
    }

    public abstract String getRole();

    public String getId()            { return id; }
    public String getName()          { return name; }
    public String getEmail()         { return email; }
    public boolean isSuspended()     { return isSuspended; }
    public void setSuspended(boolean suspended) { this.isSuspended = suspended; }
}
