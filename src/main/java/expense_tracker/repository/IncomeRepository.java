package expense_tracker.repository;


import expense_tracker.model.IncomeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeModel, Integer> {
    IncomeModel findByUserId(int userId);

    @Query("SELECT i.income from IncomeModel i WHERE i.userId = :userId")
    Double findIncomeByUserId(int userId);
}