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
import jakarta.persistence.criteria.CriteriaBuilder;
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
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"User Added Successfully\"}");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");

        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginModel login) {
        try {
               AuthModel result = service.checkUser(login);
               Map<String , Object> response = new HashMap<>();
               response.put("success", true);
               response.put("token", result.getToken());
               response.put("userId", result.getUserId());
               return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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

    @PostMapping("/view-income")
    public ResponseEntity<Object> getIncome(@RequestBody Map<String, Object> requestBody) {
        try {
            String userIdStr = (String) requestBody.get("userId");
            if (userIdStr == null || userIdStr.isEmpty()) {
                throw new IllegalArgumentException("userId is missing.");
            }

            int userId = Integer.parseInt(userIdStr);
            IncomeModel incomeModel = this.service.getIncome(userId);
            return ResponseEntity.ok(incomeModel);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Invalid userId format.\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/view-expense")
    public ResponseEntity<Object> getExpense(@RequestBody Map<String, Object> requestBody) {
        try {
            int userId = Integer.parseInt((String) requestBody.get("userId"));
            List<Map<String, Object>> result = service.getExpense(userId);
            return ResponseEntity.ok(result);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"Invalid User ID format\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"An error occurred while processing the request: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/add-expense")
    public ResponseEntity<String> addExpense(@RequestBody Map<String, Object> requestBody) {
       try {
           this.service.addExpense(requestBody);
           this.service.addCategory(requestBody);
           return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Expenses Added Successfully\"}");
           }
       catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
       }

    }

    @PostMapping("/update-expense")
    public ResponseEntity<String> updateExpense(@RequestBody Map<String, Object> requestBody) {
        try {
           this.service.updateExpense(requestBody);
           this.service.updateCategories(requestBody);
            return ResponseEntity.ok().body("{\"message\": \"Expenses Updated Successfully\"}");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/delete-expense")
    public ResponseEntity<String> deleteExpense(@RequestBody Map<String, Object> requestBody) {
        try {
            String userIdStr = (String) requestBody.get("userId");
            String categoryIdStr = (String) requestBody.get("categoryid");
            if (userIdStr == null || userIdStr.isEmpty() || categoryIdStr == null || categoryIdStr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\": \"userId and categoryId must be provided\"}");
            }
            int userId = Integer.parseInt(userIdStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            service.deleteExpenseUser(userId, categoryId);
            service.deleteCategoriesUser(userId, categoryId);
            return ResponseEntity.ok("{\"message\": \"Deleted Successfully\"}");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }



    @PostMapping("/view-category")
    public ResponseEntity<Object> viewCategory(@RequestBody Map<String, Object> requestBody) {
        int userId = Integer.parseInt((String) requestBody.get("userId"));
        Set<String> result = service.getCategory(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/view-by-category")
    public ResponseEntity<Object> viewByCategory(@RequestBody Map<String, String> requestBody) {
        int userId = Integer.parseInt(requestBody.get("userId"));
        String category = requestBody.get("categoryType");
        List<Map<String, Object>> response = this.service.viewByCategory(userId, category);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/view-by-date")
    public ResponseEntity<Object> viewByDate(@RequestBody Map<String, Object> requestBody) {
       try {
           int userid = Integer.parseInt((String) requestBody.get("userId"));
           Date sdate =  Date.valueOf(((String) requestBody.get("startDate")));
           Date ldate = Date.valueOf((String) requestBody.get("lastDate"));
           List<Map<String, Object>> response = this.service.viewByDate(userid, sdate, ldate);
           return ResponseEntity.ok(response);
       }
       catch (IllegalArgumentException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
       }
    }

    @PostMapping("/get-category-report")
    public ResponseEntity<Object> getCategoryReport(@RequestBody Map<String, String> requestBody ) {
        int userId = Integer.parseInt(requestBody.get("userId"));
       try {
           List<Map<String, String>> report = this.service.getCategoryReport(userId);
           return ResponseEntity.ok(report);
       }
       catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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

