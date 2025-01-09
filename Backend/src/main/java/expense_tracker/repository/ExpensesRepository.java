package expense_tracker.repository;

import java.util.List;

import expense_tracker.model.ExpensesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends JpaRepository<ExpensesModel, Integer> {
    boolean existsByUserIdAndCategoryId(int userId, int categoryId);

    ExpensesModel findByUserIdAndCategoryId(int userId, int categoryId);

    List<ExpensesModel> findByUserId(int userId);

    @Query("SELECT c.type , SUM(e.amount) AS Total FROM ExpensesModel e JOIN CategoriesModel c ON e.Id = c.Id where e.userId = :userId GROUP BY c.type")
    List<Object[]> findByCategory(@Param("userId") int userId);
}