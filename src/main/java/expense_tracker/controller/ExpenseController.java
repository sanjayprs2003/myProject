package expense_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import expense_tracker.model.CategoriesModel;
import expense_tracker.model.ExpensesModel;
import expense_tracker.model.IncomeModel;
import expense_tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/expenses"})
public class ExpenseController {
    @Autowired
    private ExpenseService service;
    @Autowired
    private ObjectMapper objectMapper;

    public ExpenseController() {
    }

    @PostMapping({"/add-income"})
    public void addIncome(@RequestBody IncomeModel income) {
        this.service.addIncomeService(income);
    }

    @PutMapping({"/update-income/{userId}"})
    public void updateIncome(@PathVariable("userId") int userId, @RequestBody IncomeModel incomeObj) {
        this.service.updateIncomeService(userId, incomeObj);
    }

    @GetMapping({"/view-income"})
    public ResponseEntity<Object> getIncome() {
        List<IncomeModel> view = this.service.getIncome();
        return ResponseEntity.ok(view);
    }

    @PostMapping({"/add-expense"})
    public void addExpense(@RequestBody Map<String, Object> requestBody) {
        ExpensesModel expenseObj = (ExpensesModel)this.objectMapper.convertValue(requestBody.get("expenses"), ExpensesModel.class);
        CategoriesModel categoryObj = (CategoriesModel)this.objectMapper.convertValue(requestBody.get("category"), CategoriesModel.class);
        this.service.addCategory(categoryObj);
        this.service.addExpense(expenseObj);
    }

    @PutMapping({"/update-expense/{userId}"})
    public void updateExpense(@PathVariable("userId") int userId, @RequestBody Map<String, Object> requestBody) {
        ExpensesModel expenseObj = (ExpensesModel)this.objectMapper.convertValue(requestBody.get("expenses"), ExpensesModel.class);
        CategoriesModel categoryObj = (CategoriesModel)this.objectMapper.convertValue(requestBody.get("category"), CategoriesModel.class);
        this.service.updateExpense(userId, expenseObj);
        this.service.updateCategories(userId, categoryObj);
    }

    @DeleteMapping({"/delete-user/{userId}"})
    public void deleteExpense(@PathVariable("userId") int userId) {
        this.service.deleteIncomeUser(userId);
        this.service.deleteExpenseUser(userId);
        this.service.deleteCategoriesUser(userId);
    }

    @GetMapping({"/view-expenses"})
    public ResponseEntity<Object> viewAllExpense() {
        List<Map<String, Object>> response = this.service.viewAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/view-by-category"})
    public ResponseEntity<Object> viewByCategory(@RequestBody Map<String, String> requestBody) {
        String category = (String)requestBody.get("type");
        List<Map<String, Object>> response = this.service.viewByCategory(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/view-by-date"})
    public ResponseEntity<Object> viewByDate(@RequestBody Map<String, Date> requestBody) {
        Date sdate = (Date)requestBody.get("StartDate");
        Date ldate = (Date)requestBody.get("LastDate");
        List<Map<String, Object>> response = this.service.viewByDate(sdate, ldate);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/get-category-report/{userId}"})
    public ResponseEntity<Map<String, String>> getCategoryReport(@PathVariable("userId") int userId) {
        Map<String, String> report = this.service.getCategoryReport(userId);
        return ResponseEntity.ok(report);
    }
}

