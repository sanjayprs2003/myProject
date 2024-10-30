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

    public void addIncome(IncomeModel incomeObj) {
        if (incomeObj.getUserId() <= 0 || incomeObj.getIncome() <= 0) {
            throw new IllegalArgumentException("Invalid User ID or Income");
        } else if (!incomeRespo.existsById(incomeObj.getUserId())) {
            incomeRespo.save(incomeObj);
            logger.info("Income Added Successfully");
        } else {
            throw new IllegalArgumentException("User ID Already Exist");
        }
    }

    public void updateIncome(int userId, IncomeModel incomeObj) {
        if (incomeObj.getUserId() <= 0 || incomeObj.getIncome() <= 0) {
            throw new IllegalArgumentException("Invalid User ID or Income");
        }
        else if (this.incomeRespo.existsById(userId) && incomeObj.getUserId() == userId) {
            incomeRespo.save(incomeObj);
            logger.info("Income Updated Successfully");
        } else {
            logger.error("User Not Found");
            throw new IllegalArgumentException("User Details Does Not Exist");
        }


    }

    public IncomeModel getIncome(int userid) {
        IncomeModel response = incomeRespo.findByUserId(userid);
        return response;
    }

    public void addExpense(int userId, int categoryId, ExpensesModel expenseObj) {
        if (userId <= 0 || categoryId <= 0 || expenseObj.getAmount() <=0 || expenseObj.getDescription() == null || expenseObj.getDate() == null) {
            throw new IllegalArgumentException("Invalid Details");
        }
        else if (!this.expenseRespo.existsByUserIdAndCategoryId(userId, categoryId)) {
            expenseObj.setUserId(userId);
            expenseObj.setCategoryId(categoryId);
            expenseRespo.save(expenseObj);
            logger.info("Expenses Added Successfully");
        } else {
            logger.error("User Details Already Exist Please Try Update");
            throw new IllegalArgumentException("Already Exist");
        }
    }

    public void addCategory(int userId, int categoryId, CategoriesModel categoryObj) {
        if (userId <= 0 || categoryId <= 0 || categoryObj.getName() == null || categoryObj.getType() == null) {
            throw new IllegalArgumentException("Invalid Details");
        }
        else if (!categoryRespo.existsByUserIdAndCategoryId(userId, categoryId)) {
            categoryObj.setUserId(userId);
            categoryObj.setCategoryId(categoryId);
            categoryRespo.save(categoryObj);
            logger.info("categories Added Successfully");
        } else {
            logger.error("User Details Already Exist Please Try Update");
            throw new IllegalArgumentException("Already Exist");
        }
    }

    public void updateExpense(int userId, int categoryId, ExpensesModel expenseObj) {
        if (expenseObj == null || userId <= 0 || categoryId <= 0 || expenseObj.getAmount() <=0 || expenseObj.getDescription() == null || expenseObj.getDate() == null) {
            throw new IllegalArgumentException("Invalid Details");
        }
        ExpensesModel existingExpense = expenseRespo.findByUserIdAndCategoryId(userId, categoryId);
        if (existingExpense != null) {
            existingExpense.setAmount(expenseObj.getAmount());
            existingExpense.setDescription(expenseObj.getDescription());
            existingExpense.setDate(expenseObj.getDate());
            expenseRespo.save(existingExpense);
        } else {
            logger.error("Given ID Does Not Exist");
            throw new IllegalArgumentException("Given ID Does Not Exist");
            }
        }

    public void updateCategories(int userId, int categoryId, CategoriesModel categoryObj) {
         if (categoryObj == null || userId <= 0 || categoryId <= 0 || categoryObj.getName() == null || categoryObj.getType() == null) {
                throw new IllegalArgumentException("Invalid Details");
         }
         CategoriesModel existingCategory = categoryRespo.findByUserIdAndCategoryId(userId, categoryId);
         if (existingCategory != null) {
                existingCategory.setName(categoryObj.getName());
                existingCategory.setType(categoryObj.getType());
                categoryRespo.save(existingCategory);
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
            throw new IllegalArgumentException("Check The Income UserId Details");
        }

    }

    public void deleteExpenseUser(int userId) {
        List<ExpensesModel> deleteExpenses = expenseRespo.findByUserId(userId);
        if (deleteExpenses != null) {
            Iterator  iterator = deleteExpenses.iterator();

            while(iterator.hasNext()) {
                ExpensesModel deleteExpense = (ExpensesModel)iterator.next();
                expenseRespo.delete(deleteExpense);
                logger.info("Expense UserId Deleted Successfully");
            }
        } else {
            logger.error("Check the Given UserId");
            throw new IllegalArgumentException("Check The Details");
        }

    }

    public void deleteCategoriesUser(int userId) {
        List<CategoriesModel> deleteCategories = categoryRespo.findByUserId(userId);
        if (deleteCategories != null) {
            Iterator iterator = deleteCategories.iterator();

            while(iterator.hasNext()) {
                CategoriesModel deleteCategory = (CategoriesModel)iterator.next();
                categoryRespo.delete(deleteCategory);
                logger.info("Categories UserId Deleted Successfully");
            }
        } else {
            logger.error("Check the Given UserId");
            throw new IllegalArgumentException("Check The Details");
        }

    }

    public List<Map<String, Object>> viewAll() {
        List<Map<String, Object>> response = new ArrayList();
        List<ExpensesModel> expenses = expenseRespo.findAll();
        List<CategoriesModel> categories = categoryRespo.findAll();
        Map<Integer, CategoriesModel> categoryMap = new LinkedHashMap();
        Iterator iterator = categories.iterator();

        while(iterator.hasNext()) {
            CategoriesModel category = (CategoriesModel)iterator.next();
            categoryMap.put(category.getId(), category);
        }

        iterator = expenses.iterator();

        while(iterator.hasNext()) {
            ExpensesModel expense = (ExpensesModel)iterator.next();
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
        List<ExpensesModel> expenses = expenseRespo.findAll();
        List<CategoriesModel> categories = categoryRespo.findAll();
        Map<Integer, CategoriesModel> categoryMap = new LinkedHashMap();
        Iterator iterator = categories.iterator();

        while(iterator.hasNext()) {
            CategoriesModel category = (CategoriesModel) iterator.next();
            categoryMap.put(category.getId(), category);
        }

        iterator = expenses.iterator();

        while(iterator.hasNext()) {
            ExpensesModel expense = (ExpensesModel) iterator.next();
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
        List<ExpensesModel> expenses = expenseRespo.findAll();
        List<CategoriesModel> categories = categoryRespo.findAll();
        Map<Integer, CategoriesModel> categoryMap = new LinkedHashMap();
        Iterator iterator = categories.iterator();

        while(iterator.hasNext()) {
            CategoriesModel category = (CategoriesModel) iterator.next();
            categoryMap.put(category.getId(), category);
        }

        iterator = expenses.iterator();

        while(iterator.hasNext()) {
            ExpensesModel expense = (ExpensesModel) iterator.next();
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
            Iterator iterator = results.iterator();

            while(iterator.hasNext()) {
                Object[] result = (Object[]) iterator.next();
                String type = (String)result[0];
                Double amount = (Double)result[1];
                String percentage = (int)(amount / total * 100.0) + "%";
                response.put(type, percentage);
            }
        }
        else {
            throw new IllegalArgumentException("Given ID Does Not Exist");
        }

        return response;
    }

    public List<Map<String, Object>> getExpense(int userid) {

        List<Map<String, Object>> result = new ArrayList<>();

        List<ExpensesModel> expenses = expenseRespo.findByUserId(userid);

        for(ExpensesModel expense : expenses) {

           CategoriesModel category = categoryRespo.findById(expense.getId());

           Map<String, Object> response = new LinkedHashMap<>();

           response.put("id", expense.getId());
           response.put("userid", expense.getUserId());
           response.put("categoryid", expense.getCategoryId());
           response.put("amount", expense.getAmount());
           response.put("name", category.getName());
           response.put("description", expense.getDescription());
           response.put("type", category.getType());
           response.put("date", expense.getDate());
           result.add(response);
        }

        return result;

    }
}

