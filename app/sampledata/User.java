public class User{

    int id;
    String firstName;
    String lastName;
    String email;
    String password;
    String user_name

    public User(int id, String firstName, String lastName, String email, String password, String user_name)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.user_name = user_name;
    }

}