package expense_tracker.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "Login"
)
public class LoginModel {
    @Id
    @Column(
            name = "Id"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int Id;
    @Column(
            name = "username"
    )
    private String username;

    private String password;

    public LoginModel() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
