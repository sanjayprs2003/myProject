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
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    public ExpenseService() {
    }

    public void addIncomeService(IncomeModel incomeObj) {
        if (!this.incomeRespo.existsById(incomeObj.getUserId())) {
            this.incomeRespo.save(incomeObj);
            logger.info("Income Added Successfully");
        } else {
            logger.error("UserId Already Exist Please Try Update");
        }

    }

    public void updateIncomeService(int userId, IncomeModel incomeObj) {
        if (this.incomeRespo.existsById(userId)) {
            this.incomeRespo.save(incomeObj);
            logger.info("Income Updated Successfully");
        } else {
            logger.error("User Not Found");
        }

    }

    public List<IncomeModel> getIncome() {
        return this.incomeRespo.findAll();
    }

    public void addExpense(ExpensesModel expenseObj) {
        if (expenseObj != null) {
            if (!this.expenseRespo.existsByUserIdAndCategoryId(expenseObj.getUserId(), expenseObj.getCategoryId())) {
                this.expenseRespo.save(expenseObj);
                logger.info("Expenses Added Successfully");
            } else {
                logger.error("User Details Already Exist Please Try Update");
            }
        } else {
            logger.error("Given Details Not Valid");
        }

    }

    public void addCategory(CategoriesModel categoryObj) {
        if (categoryObj != null) {
            if (!this.categoryRespo.existsByUserIdAndCategoryId(categoryObj.getUserId(), categoryObj.getCategoryId())) {
                this.categoryRespo.save(categoryObj);
                logger.info("Expenses Added Successfully");
            } else {
                logger.error("User Details Already Exist Please Try Update");
            }
        } else {
            logger.error("Given Details Not Valid");
        }

    }

    public void updateExpense(int userId, ExpensesModel expenseObj) {
        if (expenseObj != null) {
            ExpensesModel existingExpense = this.expenseRespo.findByUserIdAndCategoryId(userId, expenseObj.getCategoryId());
            if (existingExpense != null) {
                existingExpense.setAmount(expenseObj.getAmount());
                existingExpense.setDescription(expenseObj.getDescription());
                existingExpense.setDate(expenseObj.getDate());
                this.expenseRespo.save(existingExpense);
            } else {
                logger.error("Given ID Does Not Exist");
            }
        } else {
            logger.error("Given Details Not Valid");
        }

    }

    public void updateCategories(int userId, CategoriesModel categoryObj) {
        if (categoryObj != null) {
            CategoriesModel existingCategory = this.categoryRespo.findByUserIdAndCategoryId(userId, categoryObj.getCategoryId());
            if (existingCategory != null) {
                existingCategory.setName(categoryObj.getName());
                existingCategory.setType(categoryObj.getType());
                this.categoryRespo.save(existingCategory);
                logger.info("Category Added Successfully");
            } else {
                logger.error("Given ID Does Not Exist");
            }
        } else {
            logger.error("Given Credientials Not Valid");
        }

    }

    public void deleteIncomeUser(int userId) {
        IncomeModel income = this.incomeRespo.findByUserId(userId);
        if (income != null) {
            this.incomeRespo.delete(income);
            logger.info("UserId Deleted Successfully");
        } else {
            logger.error("Check the Given UserId");
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
