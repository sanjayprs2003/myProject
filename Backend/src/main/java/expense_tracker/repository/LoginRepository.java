package expense_tracker.repository;

import expense_tracker.model.LoginModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginModel, Integer> {

    boolean existsByUsername(String username);

    @Query("SELECT l FROM LoginModel l WHERE l.username = :username AND l.password = :password")
    LoginModel findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

   // LoginModel findByUsername(String username);
}
