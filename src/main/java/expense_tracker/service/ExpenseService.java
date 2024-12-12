package expense_tracker.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

import expense_tracker.model.*;
import expense_tracker.repository.*;
import expense_tracker.utility.JwtUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    public IncomeModel getIncome(int userId) {
        IncomeModel income = incomeRespo.findByUserId(userId);
        if (income != null) {
            return income;
        }
        throw new IllegalArgumentException("Please Add Income");
    }


    public void addExpense(Map<String, Object> requestBody) {
        int userId = Integer.parseInt((String) requestBody.get("userId"));
        int categoryId = Integer.parseInt((String) requestBody.get("categoryid"));
        int amount = (int) requestBody.get("amount");
        String description = (String) requestBody.get("description");
        String strDate = (String) requestBody.get("date");
        Date date = Date.valueOf(strDate);

        if(!expenseRespo.existsByUserIdAndCategoryId(userId, categoryId)) {

            ExpensesModel expense = new ExpensesModel();

            expense.setUserId(userId);
            expense.setCategoryId(categoryId);
            expense.setAmount((double) amount);
            expense.setDescription(description);
            expense.setDate(date);

            expenseRespo.save(expense);
        }

       else {
            throw new IllegalArgumentException("CategoryID Already Exist");
        }

    }

    public void addCategory(Map<String, Object> requestBody) {
        int userId = Integer.parseInt((String) requestBody.get("userId"));
        int categoryId = Integer.parseInt((String) requestBody.get("categoryid"));
        String name = (String) requestBody.get("name");
        String type = (String) requestBody.get("type");

        if(!categoryRespo.existsByUserIdAndCategoryId(userId, categoryId)) {

          CategoriesModel category = new CategoriesModel();

            category.setUserId(userId);
            category.setCategoryId(categoryId);
            category.setName(name);
            category.setType(type);

            categoryRespo.save(category);
        }

        else {
            throw new IllegalArgumentException("CategoryID Already Exist");
        }
    }

    public void updateExpense(Map<String, Object> requestBody) {
        try {
            int userId = Integer.parseInt((String) requestBody.get("userId"));
            int categoryId = Integer.parseInt((String) requestBody.get("categoryid"));
            int amount = (int) requestBody.get("amount");
            String description = (String) requestBody.get("description");
            String strDate = (String) requestBody.get("date");
            Date date = Date.valueOf(strDate);

            if(expenseRespo.existsByUserIdAndCategoryId(userId, categoryId)) {

                ExpensesModel expense = expenseRespo.findByUserIdAndCategoryId(userId,categoryId);

                expense.setUserId(userId);
                expense.setCategoryId(categoryId);
                expense.setAmount((double) amount);
                expense.setDescription(description);
                expense.setDate(date);

                expenseRespo.save(expense);
            }

            else {
                throw new IllegalArgumentException("CategoryID Already Exist");
            }
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Error While Updating");
        }

    }

    public void updateCategories(Map<String, Object> requestBody) {
        int userId = Integer.parseInt((String) requestBody.get("userId"));
        int categoryId = Integer.parseInt((String) requestBody.get("categoryid"));
        String name = (String) requestBody.get("name");
        String type = (String) requestBody.get("type");

        if(categoryRespo.existsByUserIdAndCategoryId(userId, categoryId)) {

            CategoriesModel category = categoryRespo.findByUserIdAndCategoryId(userId, categoryId);

            category.setUserId(userId);
            category.setCategoryId(categoryId);
            category.setName(name);
            category.setType(type);

            categoryRespo.save(category);
        }

        else {
            throw new IllegalArgumentException("CategoryID Already Exist");
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
                resultData.put("categoryid", expense.getCategoryId());
                resultData.put("amount", expense.getAmount());
                resultData.put("name", category.getName());
                resultData.put("description", expense.getDescription());
                resultData.put("type", category.getType());
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
                resultData.put("categoryid", expense.getCategoryId());
                resultData.put("amount", expense.getAmount());
                resultData.put("name", category.getName());
                resultData.put("description", expense.getDescription());
                resultData.put("type", category.getType());
                resultData.put("date", expense.getDate());
                response.add(resultData);
            }
        }

        return response;
    }

    public List<Map<String, String>> getCategoryReport(int userId) {
        List<Map<String, String>> response = new ArrayList();
        Double total = this.incomeRespo.findIncomeByUserId(userId);
        if (total != null && total > 0.0) {
            List<Object[]> results = this.expenseRespo.findByCategory(userId);
            Iterator iterator = results.iterator();

            while (iterator.hasNext()) {
                Object[] result = (Object[]) iterator.next();
                String type = (String) result[0];
                Double amount = (Double) result[1];
                String percentage = (int) (amount / total * 100.0) + "%";

                Map<String, String> data = new HashMap<>();
                data.put("type", type);
                data.put("percentage", percentage);

                response.add(data);
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
        LoginModel storedUser = loginRespo.findByUsernameAndPassword(login.getUsername(), login.getPassword());
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

