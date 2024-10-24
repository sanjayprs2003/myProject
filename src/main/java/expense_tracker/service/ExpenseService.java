package expense_tracker.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import expense_tracker.model.CategoriesModel;
import expense_tracker.model.ExpensesModel;
import expense_tracker.model.IncomeModel;
import expense_tracker.repository.CategoriesRepository;
import expense_tracker.repository.ExpensesRepository;
import expense_tracker.repository.IncomeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    private IncomeRepository incomeRespo;
    @Autowired
    private ExpensesRepository expenseRespo;
    @Autowired
    private CategoriesRepository categoryRespo;
    private  final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    public ResponseEntity<String> addIncome(IncomeModel incomeObj) {
        if (incomeObj.getUserId() <= 0 || incomeObj.getIncome() <= 0) {
            throw new IllegalArgumentException("Invalid User ID or Income");
        } else if (!incomeRespo.existsById(incomeObj.getUserId())) {
            incomeRespo.save(incomeObj);
            logger.info("Income Added Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body("Income Added Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User ID Already Exists");
        }
    }

    public ResponseEntity<String> updateIncome(int userId, IncomeModel incomeObj) {
        if (incomeObj.getUserId() <= 0 || incomeObj.getIncome() <= 0) {
            throw new IllegalArgumentException("Invalid User ID or Income");
        }
        else if (this.incomeRespo.existsById(userId) && incomeObj.getUserId() == userId) {
            this.incomeRespo.save(incomeObj);
            logger.info("Income Updated Successfully");
            return ResponseEntity.ok("Income Updated Successfully");
        } else {
            logger.error("User Not Found");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given Details Does Not Exist");
        }


    }

    public List<IncomeModel> getIncome() {
        return this.incomeRespo.findAll();
    }

    public boolean addExpense(int userId, int categoryId, ExpensesModel expenseObj) {
        if (expenseObj == null  || userId <= 0 || categoryId <= 0) {
            throw new IllegalArgumentException("Invalid Details");
        }
        else if (!this.expenseRespo.existsByUserIdAndCategoryId(userId, categoryId)) {
            expenseObj.setUserId(userId);
            expenseObj.setCategoryId(categoryId);
            this.expenseRespo.save(expenseObj);
            logger.info("Expenses Added Successfully");
            return true;
        } else {
            logger.error("User Details Already Exist Please Try Update");
            throw new IllegalArgumentException("Already Exist");
        }
    }

    public void addCategory(int userId, int categoryId, CategoriesModel categoryObj) {
        if (categoryObj == null || userId <= 0 || categoryId <= 0) {
            throw new IllegalArgumentException("Invalid Details");
        }
        else if (!this.categoryRespo.existsByUserIdAndCategoryId(userId, categoryId)) {
            categoryObj.setUserId(userId);
            categoryObj.setCategoryId(categoryId);
            this.categoryRespo.save(categoryObj);
            logger.info("categories Added Successfully");
        } else {
            logger.error("User Details Already Exist Please Try Update");
            throw new IllegalArgumentException("Already Exist");
        }
    }

    public void updateExpense(int userId, int categoryId, ExpensesModel expenseObj) {
        if (expenseObj == null || userId <= 0 || categoryId <= 0) {
            throw new IllegalArgumentException("Invalid Details");
        }
        ExpensesModel existingExpense = this.expenseRespo.findByUserIdAndCategoryId(userId, categoryId);
            if (existingExpense != null) {
                existingExpense.setAmount(expenseObj.getAmount());
                existingExpense.setDescription(expenseObj.getDescription());
                existingExpense.setDate(expenseObj.getDate());
                this.expenseRespo.save(existingExpense);
            } else {
                logger.error("Given ID Does Not Exist");
                throw new IllegalArgumentException("Given ID Does Not Exist");
            }
        }

    public void updateCategories(int userId, int categoryId, CategoriesModel categoryObj) {
         if (categoryObj == null || userId <= 0 || categoryId <= 0) {
                throw new IllegalArgumentException("Invalid Details");
         }
         CategoriesModel existingCategory = this.categoryRespo.findByUserIdAndCategoryId(userId, categoryId);
            if (existingCategory != null) {
                existingCategory.setName(categoryObj.getName());
                existingCategory.setType(categoryObj.getType());
                this.categoryRespo.save(existingCategory);
                logger.info("Category Added Successfully");
            } else {
                logger.error("Given ID Does Not Exist");
                throw new IllegalArgumentException("Given ID Does Not Exist");
            }
        }

    public void deleteIncomeUser(int userId) {
        IncomeModel income = incomeRespo.findByUserId(userId);
        if (income != null) {
            incomeRespo.delete(income);
            logger.info("UserId Deleted Successfully");
        } else {
            logger.error("Check the Given UserId");
            throw new IllegalArgumentException("Check The Details");
        }

    }

    public void deleteExpenseUser(int userId) {
        List<ExpensesModel> deleteExpenses = this.expenseRespo.findByUserId(userId);
        if (deleteExpenses != null) {
            Iterator var4 = deleteExpenses.iterator();

            while(var4.hasNext()) {
                ExpensesModel deleteExpense = (ExpensesModel)var4.next();
                this.expenseRespo.delete(deleteExpense);
                logger.info("Expense UserId Deleted Successfully");
            }
        } else {
            logger.error("Check the Given UserId");
            throw new IllegalArgumentException("Check The Details");
        }

    }

    public void deleteCategoriesUser(int userId) {
        List<CategoriesModel> deleteCategories = this.categoryRespo.findByUserId(userId);
        if (deleteCategories != null) {
            Iterator var4 = deleteCategories.iterator();

            while(var4.hasNext()) {
                CategoriesModel deleteCategory = (CategoriesModel)var4.next();
                this.categoryRespo.delete(deleteCategory);
                logger.info("Categories UserId Deleted Successfully");
            }
        } else {
            logger.error("Check the Given UserId");
            throw new IllegalArgumentException("Check The Details");
        }

    }

    public List<Map<String, Object>> viewAll() {
        List<Map<String, Object>> response = new ArrayList();
        List<ExpensesModel> expenses = this.expenseRespo.findAll();
        List<CategoriesModel> categories = this.categoryRespo.findAll();
        Map<Integer, CategoriesModel> categoryMap = new LinkedHashMap();
        Iterator var6 = categories.iterator();

        while(var6.hasNext()) {
            CategoriesModel category = (CategoriesModel)var6.next();
            categoryMap.put(category.getId(), category);
        }

        var6 = expenses.iterator();

        while(var6.hasNext()) {
            ExpensesModel expense = (ExpensesModel)var6.next();
            CategoriesModel category = (CategoriesModel)categoryMap.get(expense.getId());
            if (category != null) {
                Map<String, Object> resultData = new LinkedHashMap();
                resultData.put("id", expense.getId());
                resultData.put("userId", expense.getUserId());
                resultData.put("categoryId", expense.getCategoryId());
                resultData.put("amount", expense.getAmount());
                resultData.put("categoryName", category.getName());
                resultData.put("description", expense.getDescription());
                resultData.put("categoryType", category.getType());
                resultData.put("date", expense.getDate());
                response.add(resultData);
            }
        }

        return response;
    }

    public List<Map<String, Object>> viewByCategory(String type) {
        List<Map<String, Object>> response = new ArrayList();
        List<ExpensesModel> expenses = this.expenseRespo.findAll();
        List<CategoriesModel> categories = this.categoryRespo.findAll();
        Map<Integer, CategoriesModel> categoryMap = new LinkedHashMap();
        Iterator var7 = categories.iterator();

        while(var7.hasNext()) {
            CategoriesModel category = (CategoriesModel)var7.next();
            categoryMap.put(category.getId(), category);
        }

        var7 = expenses.iterator();

        while(var7.hasNext()) {
            ExpensesModel expense = (ExpensesModel)var7.next();
            CategoriesModel category = (CategoriesModel)categoryMap.get(expense.getId());
            if (category != null && category.getType().equals(type)) {
                Map<String, Object> resultData = new LinkedHashMap();
                resultData.put("id", expense.getId());
                resultData.put("userId", expense.getUserId());
                resultData.put("categoryId", expense.getCategoryId());
                resultData.put("amount", expense.getAmount());
                resultData.put("categoryName", category.getName());
                resultData.put("description", expense.getDescription());
                resultData.put("categoryType", category.getType());
                resultData.put("date", expense.getDate());
                response.add(resultData);
            }
        }

        return response;
    }

    public List<Map<String, Object>> viewByDate(Date sdate, Date ldate) {
        List<Map<String, Object>> response = new ArrayList();
        List<ExpensesModel> expenses = this.expenseRespo.findAll();
        List<CategoriesModel> categories = this.categoryRespo.findAll();
        Map<Integer, CategoriesModel> categoryMap = new LinkedHashMap();
        Iterator var8 = categories.iterator();

        while(var8.hasNext()) {
            CategoriesModel category = (CategoriesModel)var8.next();
            categoryMap.put(category.getId(), category);
        }

        var8 = expenses.iterator();

        while(var8.hasNext()) {
            ExpensesModel expense = (ExpensesModel)var8.next();
            CategoriesModel category = (CategoriesModel)categoryMap.get(expense.getId());
            if (category != null && !expense.getDate().before(sdate) && !expense.getDate().after(ldate)) {
                Map<String, Object> resultData = new LinkedHashMap();
                resultData.put("id", expense.getId());
                resultData.put("userId", expense.getUserId());
                resultData.put("categoryId", expense.getCategoryId());
                resultData.put("amount", expense.getAmount());
                resultData.put("categoryName", category.getName());
                resultData.put("description", expense.getDescription());
                resultData.put("categoryType", category.getType());
                resultData.put("date", expense.getDate());
                response.add(resultData);
            }
        }

        return response;
    }

    public Map<String, String> getCategoryReport(int userId) {
        Map<String, String> response = new LinkedHashMap();
        Double total = this.incomeRespo.findIncomeByUserId(userId);
        if (total != null && total > 0.0) {
            List<Object[]> results = this.expenseRespo.findByCategory(userId);
            Iterator var6 = results.iterator();

            while(var6.hasNext()) {
                Object[] result = (Object[])var6.next();
                String type = (String)result[0];
                Double amount = (Double)result[1];
                String percentage = (int)(amount / total * 100.0) + "%";
                response.put(type, percentage);
            }
        }

        return response;
    }

}

