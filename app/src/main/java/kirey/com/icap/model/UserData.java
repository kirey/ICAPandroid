package kirey.com.icap.model;

/**
 * Created by kitanoskan on 11/07/2018.
 */

public class UserData {

    private static final UserData ourInstance = new UserData();

    private String mail;
    private String firstName;
    private String lastName;
    private String username;
    //TODO if needed add here another User data....


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
