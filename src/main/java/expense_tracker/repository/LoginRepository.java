package expense_tracker.repository;

import expense_tracker.model.LoginModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginModel, Integer> {

    boolean existsByUsername(String username);

    LoginModel findByUsernameAndPassword(String username, String password);

    LoginModel findByUsername(String username);
}
