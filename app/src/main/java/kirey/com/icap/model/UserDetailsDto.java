package kirey.com.icap.model;

/**
 * Created by kitanoskan on 22/05/2017.
 */

public class UserDetailsDto {

    private String username;
    private String password;

    public UserDetailsDto(String user, String pass)
    {
        this.username = user;
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
