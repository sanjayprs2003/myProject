package expense_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Date;
import java.util.*;

import expense_tracker.model.CategoriesModel;
import expense_tracker.model.ExpensesModel;
import expense_tracker.model.IncomeModel;
import expense_tracker.model.LoginModel;
import expense_tracker.service.ExpenseService;
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

    @PostMapping("/add-user")
    public ResponseEntity<String> addUser(@RequestBody LoginModel login) {
        try{
            service.addUser(login);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"User Added Successfully\"}");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginModel login) {
        try {
            LoginModel result = service.checkUser(login);

            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"Invalid username or password\"}");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
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

    @GetMapping("/view-income/{userId}")
    public ResponseEntity<Object> getIncome(@PathVariable("userId") int userId) {
       try{
           IncomeModel view = this.service.getIncome(userId);
           return ResponseEntity.ok(view);
       }
       catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
       }
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
               return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Expenses Added Successfully\"}");
           }
       catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
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
                return ResponseEntity.ok("{\"message\": \"Updated Successfully\"}");
            }
            else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\": \"" + "Check The Details" + "\"}");
            }
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/delete-user/{userid}")
    public ResponseEntity<String> deleteExpense(@PathVariable("userid") int userId, @RequestParam("categoryid") int categoryid) {
          try {
              service.deleteExpenseUser(userId, categoryid);
              service.deleteCategoriesUser(userId, categoryid);
              return ResponseEntity.ok("{\"message\": \"Deleted Successfully\"}");
          }
          catch (IllegalArgumentException e) {
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
          }
    }

    @GetMapping("/view-expenses")
    public ResponseEntity<Object> viewAllExpense() {
        List<Map<String, Object>> response = this.service.viewAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view-category/{userid}")
    public ResponseEntity<Object> viewCategory(@PathVariable("userid") int userid) {
        Set<String> result = service.getCategory(userid);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/view-by-category/{userid}")
    public ResponseEntity<Object> viewByCategory(@PathVariable("userid") int userid, @RequestParam String category) {
        List<Map<String, Object>> response = this.service.viewByCategory(userid, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view-by-date/{userid}")
    public ResponseEntity<Object> viewByDate(@PathVariable("userid") int userid, @RequestParam Date sdate, @RequestParam Date ldate) {
        List<Map<String, Object>> response = this.service.viewByDate(userid, sdate, ldate);
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

