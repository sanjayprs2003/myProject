package expense_tracker.repository;

import expense_tracker.model.AuthModel;
import expense_tracker.model.CategoriesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthModel, Integer>  {
    AuthModel findById(int userid);
}
