package expense_tracker.repository;

import java.util.List;

import expense_tracker.model.CategoriesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesModel, Integer> {
    boolean existsByUserIdAndCategoryId(int userId, int categoryId);

    CategoriesModel findByUserIdAndCategoryId(int userId, int categoryId);

    List<CategoriesModel> findByUserId(int userId);
}

