package expense_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import expense_tracker.model.CategoriesModel;
import expense_tracker.model.ExpensesModel;
import expense_tracker.model.IncomeModel;
import expense_tracker.service.ExpenseService;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/expenses"})
@CrossOrigin(origins = "http://localhost:5500")

public class ExpenseController {
    @Autowired
    private ExpenseService service;
    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    public ExpenseController() {
    }

    @PostMapping("/add-income")
    public ResponseEntity<String> addIncome(@RequestBody IncomeModel income) {
        try {
             service.addIncome(income);
             return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Income Added Successfully\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/update-income/{userId}")
    public ResponseEntity<String> updateIncome(@PathVariable("userId") int userId, @RequestBody IncomeModel incomeObj) {
        try {
             service.updateIncome(userId, incomeObj);
             return ResponseEntity.ok("{\"message\": \"Income Updated Successfully\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/view-income/{userId}")
    public ResponseEntity<Object> getIncome(@PathVariable("userId") int userId) {
        IncomeModel view = this.service.getIncome(userId);
        return ResponseEntity.ok(view);
    }

    @GetMapping("/view-expense/{userid}")
    public ResponseEntity<Object> getExpense(@PathVariable("userid") int userid) {
        List<Map<String, Object>> result = service.getExpense(userid);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add-expense")
    public ResponseEntity<String> addExpense(@RequestBody Map<String, Object> requestBody) {
       try {
           Object userIdObj = Integer.parseInt(requestBody.get("userId").toString());
           Object categoryIdObj = Integer.parseInt(requestBody.get("categoryId").toString());
           int userId = ((Number) userIdObj).intValue();
           int categoryId = ((Number) categoryIdObj).intValue();
           ExpensesModel expenseObj = (ExpensesModel)this.objectMapper.convertValue(requestBody.get("expenses"), ExpensesModel.class);
           CategoriesModel categoryObj = (CategoriesModel)this.objectMapper.convertValue(requestBody.get("category"), CategoriesModel.class);
           this.service.addExpense(userId, categoryId, expenseObj);
           this.service.addCategory(userId, categoryId, categoryObj);
               return ResponseEntity.status(HttpStatus.CREATED).body("Expenses Added Successfully");
           }
       catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }

    }

    @PutMapping("/update-expense/{UserId}")
    public ResponseEntity<String> updateExpense(@PathVariable("UserId") int UserId, @RequestBody Map<String, Object> requestBody) {
        try {
            Object userIdObj = Integer.parseInt(requestBody.get("userId").toString());
            Object categoryIdObj = Integer.parseInt(requestBody.get("categoryId").toString());
            int userId = ((Number) userIdObj).intValue();
            int categoryId = ((Number) categoryIdObj).intValue();
            if(userId == UserId) {
                ExpensesModel expenseObj = (ExpensesModel) this.objectMapper.convertValue(requestBody.get("expenses"), ExpensesModel.class);
                CategoriesModel categoryObj = (CategoriesModel) this.objectMapper.convertValue(requestBody.get("category"), CategoriesModel.class);
                this.service.updateExpense(userId, categoryId, expenseObj);
                this.service.updateCategories(userId, categoryId,  categoryObj);
                return ResponseEntity.ok("Updated Successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Check The Details");
            }
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<String> deleteExpense(@PathVariable("userId") int userId) {
          try {
              service.deleteIncomeUser(userId);
              service.deleteExpenseUser(userId);
              service.deleteCategoriesUser(userId);
              return ResponseEntity.ok("Deleted Successfully");
          }
          catch (IllegalArgumentException e) {
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
          }
    }

    @GetMapping("/view-expenses")
    public ResponseEntity<Object> viewAllExpense() {
        List<Map<String, Object>> response = this.service.viewAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view-by-category")
    public ResponseEntity<Object> viewByCategory(@RequestParam String category) {
        List<Map<String, Object>> response = this.service.viewByCategory(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view-by-date")
    public ResponseEntity<Object> viewByDate(@RequestBody Map<String, Date> requestBody) {
        Date sdate = (Date)requestBody.get("StartDate");
        Date ldate = (Date)requestBody.get("LastDate");
        List<Map<String, Object>> response = this.service.viewByDate(sdate, ldate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-category-report/{userId}")
    public ResponseEntity<Map<String, String>> getCategoryReport(@PathVariable("userId") int userId) {
        Map<String, String> response = new HashMap<>();
       try {
           Map<String, String> report = this.service.getCategoryReport(userId);
           return ResponseEntity.ok(report);
       }
       catch (IllegalArgumentException e){
           response.put("Error",e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
       }
    }
}

