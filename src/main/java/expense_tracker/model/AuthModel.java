package expense_tracker.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "Auth"
)
public class AuthModel {
    @Id
    @Column(
            name = "Id"
    )
    private int userId;

    @Column(
            name = "token"
    )
    private String token;

    public AuthModel(){

    }

    public AuthModel(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
