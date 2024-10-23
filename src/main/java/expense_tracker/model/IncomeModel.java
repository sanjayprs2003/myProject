package expense_tracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "Income"
)
public class IncomeModel {
    @Id
    @Column(
            name = "userId"
    )
    private int userId;
    @Column(
            name = "income"
    )
    private double income;

    public IncomeModel() {
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getIncome() {
        return this.income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
