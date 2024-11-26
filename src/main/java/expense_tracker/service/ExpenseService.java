package expense_tracker.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

import expense_tracker.model.*;
import expense_tracker.repository.*;
import expense_tracker.utility.JwtUtil;
import expense_tracker.utility.SecurityConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExpenseService {

    @Autowired
    private IncomeRepository incomeRespo;
    @Autowired
    private ExpensesRepository expenseRespo;
    @Autowired
    private CategoriesRepository categoryRespo;
    @Autowired
    private LoginRepository loginRespo;
    @Autowired
    private AuthRepository authRespo;
    @Autowired
    private PhotoRepository photoRespo;

    private final MinioClient minioClient;
    private final String bucketName;

    private JwtUtil jwtUtil;

    public ExpenseService(JwtUtil jwtUtil, @Value("${minio.url}") String minioUrl,  @Value("${minio.access-key}") String accessKey,
                          @Value("${minio.secret-key}") String secretKey, @Value("${minio.bucket-name}") String bucketName) {

        this.jwtUtil = jwtUtil;
        this.minioClient = MinioClient.builder().endpoint(minioUrl).credentials(accessKey, secretKey).build();
        this.bucketName = bucketName;
    }

    private final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    public void addIncome(IncomeModel incomeObj) {
        if (incomeObj.getUserId() <= 0 || incomeObj.getIncome() <= 0) {
            throw new IllegalArgumentException("Invalid User ID or Income");
        } else {
            incomeRespo.save(incomeObj);
            logger.info("Income Added Successfully");
        }
    }

    public IncomeModel getIncome(int userid) {
        Optional<IncomeModel> incomeOptional = incomeRespo.findByUserId(userid);
        if (incomeOptional.isPresent()) {
            return incomeOptional.get();
        } else {
            throw new IllegalArgumentException("Give Income");
        }
    }


    public void addExpense(int userId, int categoryId, ExpensesModel expenseObj) {
        if (userId <= 0 || categoryId <= 0 || expenseObj.getAmount() <= 0 || expenseObj.getDescription() == null || expenseObj.getDate() == null) {
            throw new IllegalArgumentException("Invalid Details");
        } else if (incomeRespo.existsById(userId)) {
            if (!this.expenseRespo.existsByUserIdAndCategoryId(userId, categoryId)) {
                expenseObj.setUserId(userId);
                expenseObj.setCategoryId(categoryId);
                expenseRespo.save(expenseObj);
                logger.info("Expenses Added Successfully");
            } else {
                logger.error("User Details Already Exist Please Try Update");
                throw new IllegalArgumentException("Given ID Details Already Exist");
            }
        } else {
            logger.error("User Id Not Exist In Income Please Try Update");
            throw new IllegalArgumentException("Give The User Income First");
        }
    }

    public void addCategory(int userId, int categoryId, CategoriesModel categoryObj) {
        if (userId <= 0 || categoryId <= 0 || categoryObj.getName() == null || categoryObj.getType() == null) {
            throw new IllegalArgumentException("Invalid Details");
        } else if (!categoryRespo.existsByUserIdAndCategoryId(userId, categoryId)) {
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
        if (expenseObj == null || userId <= 0 || categoryId <= 0 || expenseObj.getAmount() <= 0 || expenseObj.getDescription() == null || expenseObj.getDate() == null) {
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

    public void deleteExpenseUser(int userId, int categoryId) {
        ExpensesModel deleteExpenses = expenseRespo.findByUserIdAndCategoryId(userId, categoryId);
        if (deleteExpenses != null) {
            expenseRespo.delete(deleteExpenses);
        } else {
            logger.error("Check the Given UserId");
            throw new IllegalArgumentException("Check The Details");
        }

    }

    public void deleteCategoriesUser(int userId, int categoryId) {
        CategoriesModel deleteCategories = categoryRespo.findByUserIdAndCategoryId(userId, categoryId);
        if (deleteCategories != null) {
            categoryRespo.delete(deleteCategories);
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

        while (iterator.hasNext()) {
            CategoriesModel category = (CategoriesModel) iterator.next();
            categoryMap.put(category.getId(), category);
        }

        iterator = expenses.iterator();

        while (iterator.hasNext()) {
            ExpensesModel expense = (ExpensesModel) iterator.next();
            CategoriesModel category = (CategoriesModel) categoryMap.get(expense.getId());
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

    public Set<String> getCategory(int userid) {
        List<CategoriesModel> categories = categoryRespo.findByUserId(userid);
        Set<String> result = new LinkedHashSet<>();
        for (CategoriesModel category : categories) {
            result.add(category.getType());
        }
        return result;
    }

    public List<Map<String, Object>> viewByCategory(int userid, String type) {
        List<Map<String, Object>> response = new ArrayList();
        List<ExpensesModel> expenses = expenseRespo.findByUserId(userid);
        List<CategoriesModel> categories = categoryRespo.findByUserId(userid);
        Map<Integer, CategoriesModel> categoryMap = new LinkedHashMap();
        Iterator iterator = categories.iterator();

        while (iterator.hasNext()) {
            CategoriesModel category = (CategoriesModel) iterator.next();
            categoryMap.put(category.getId(), category);
        }

        iterator = expenses.iterator();

        while (iterator.hasNext()) {
            ExpensesModel expense = (ExpensesModel) iterator.next();
            CategoriesModel category = (CategoriesModel) categoryMap.get(expense.getId());
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

    public List<Map<String, Object>> viewByDate(int userid, Date sdate, Date ldate) {
        List<Map<String, Object>> response = new ArrayList();
        List<ExpensesModel> expenses = expenseRespo.findByUserId(userid);
        List<CategoriesModel> categories = categoryRespo.findByUserId(userid);
        Map<Integer, CategoriesModel> categoryMap = new LinkedHashMap();
        Iterator iterator = categories.iterator();

        while (iterator.hasNext()) {
            CategoriesModel category = (CategoriesModel) iterator.next();
            categoryMap.put(category.getId(), category);
        }

        iterator = expenses.iterator();

        while (iterator.hasNext()) {
            ExpensesModel expense = (ExpensesModel) iterator.next();
            CategoriesModel category = (CategoriesModel) categoryMap.get(expense.getId());
            if (category != null && !expense.getDate().before(sdate) && !expense.getDate().after(ldate)) {
                Map<String, Object> resultData = new LinkedHashMap();
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

            while (iterator.hasNext()) {
                Object[] result = (Object[]) iterator.next();
                String type = (String) result[0];
                Double amount = (Double) result[1];
                String percentage = (int) (amount / total * 100.0) + "%";
                response.put(type, percentage);
            }
        } else {
            throw new IllegalArgumentException("Given ID Does Not Exist");
        }

        return response;
    }

    public List<Map<String, Object>> getExpense(int userid) {

        List<Map<String, Object>> result = new ArrayList<>();

        List<ExpensesModel> expenses = expenseRespo.findByUserId(userid);

        for (ExpensesModel expense : expenses) {

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

    public void addUser(LoginModel login) {
        if (!loginRespo.existsByUsername(login.getUsername())) {
            loginRespo.save(login);
        } else {
            throw new IllegalArgumentException("Given Username Already Exist");
        }
    }

    public AuthModel checkUser(LoginModel login) {
        LoginModel storedUser = loginRespo.findByUsername(login.getUsername());
        if (storedUser != null) {
            AuthModel auth = authRespo.findById(storedUser.getId());
            if (auth != null) {
                String token = auth.getToken();
                if (jwtUtil.isTokenValid(token)) {
                    return auth;
                } else {
                    authRespo.delete(auth);
                    String newToken = jwtUtil.generateToken(storedUser.getUsername(), storedUser.getId());
                    AuthModel newTokenAuth = new AuthModel(storedUser.getId(), newToken);
                    authRespo.save(newTokenAuth);
                    return newTokenAuth;
                }
            } else {
                String newToken = jwtUtil.generateToken(storedUser.getUsername(), storedUser.getId());
                AuthModel newAuthModel = new AuthModel(storedUser.getId(), newToken);
                authRespo.save(newAuthModel);
                return newAuthModel;
            }
        }
            throw new IllegalArgumentException("Invalid username or password");
        }


    public String uploadImage(int userId, MultipartFile photo) throws IOException {
        try{
            InputStream inputStream = photo.getInputStream();
            String fileName = photo.getOriginalFilename();
            long fileSize = photo.getSize();
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(inputStream, fileSize, -1).build());
            PhotoModel newphoto = new PhotoModel();
            newphoto.setId(userId);
            newphoto.setFileName(fileName);
            newphoto.setFileUrl("http://localhost:9000/" + bucketName + "/" + fileName);
            newphoto.setFileSize(String.valueOf(fileSize));
            newphoto.setUploadDate(LocalDateTime.now());
            photoRespo.save(newphoto);
            return fileName;
        }
        catch (Exception e) {
            return "Error uploading file";
        }
    }


    public byte[] getImage(int userId) throws Exception {
        try  {
            Optional<PhotoModel> photoModel = photoRespo.findById(userId);
            String imageName = photoModel.get().getFileName();
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(imageName).build());
            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new Exception("Error reading image from MinIO", e);
        }
    }

}

