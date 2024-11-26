package expense_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.*;

import expense_tracker.model.*;
import expense_tracker.service.ExpenseService;
import expense_tracker.utility.JwtUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/api/expenses"})
@CrossOrigin(origins = "http://localhost:5500")

public class ExpenseController {
    @Autowired
    private ExpenseService service;
    @Autowired
    private ObjectMapper objectMapper;

    private final JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    public ExpenseController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
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
               AuthModel result = service.checkUser(login);
                return ResponseEntity.ok().body(result);
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

    @GetMapping("/view-expense/{userId}")
    public ResponseEntity<Object> getExpense(@PathVariable("userId") int userId) {
        List<Map<String, Object>> result = service.getExpense(userId);
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

    @GetMapping("/view-category/{userId}")
    public ResponseEntity<Object> viewCategory(@PathVariable("userId") int userId) {
        Set<String> result = service.getCategory(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/view-by-category/{userId}")
    public ResponseEntity<Object> viewByCategory(@PathVariable("userId") int userId, @RequestParam String category) {
        List<Map<String, Object>> response = this.service.viewByCategory(userId, category);
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

    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadFile(@PathVariable("userId") int userId, @RequestParam("photo") MultipartFile photo) {
        if (photo.isEmpty()) {
            return ResponseEntity.status(400).body("{\"success\": false, \"message\": \"No file found\"}");
        }
        try {
            String fileName = service.uploadImage(userId, photo);
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"File uploaded successfully\", \"fileName\": \"" + fileName + "\"}");
        } catch (IOException e) {
            logger.error("Error uploading file: " + e.getMessage(), e);
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"Error uploading file: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("images/{userId}")
    public ResponseEntity<?> getImage(@PathVariable int userId) {
        try {
            byte[] imageBytes = service.getImage(userId);

            if (imageBytes != null && imageBytes.length > 0) {
                return ResponseEntity.ok()
                        .body(imageBytes);
            } else {
                return ResponseEntity.status(404).body("{\"message\": \"Image not found\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body("{\"message\": \"Error Uploading File\"}");
        }
    }
}

