package expense_tracker.repository;

import expense_tracker.model.PhotoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoModel, Integer> {
}
