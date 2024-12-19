package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {

    @Autowired
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet row = preparedStatement.executeQuery()) {


            while (row.next()) {
                categories.add(mapRow(row));
            }

        } catch (Exception e) {
            System.out.println("An error has occurred");
            e.printStackTrace();
        }


        return categories;
    }

    @Override
    public Category getById(int categoryId) {

        Category category = null;
        String query = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setInt(1, categoryId);
           try(ResultSet row = preparedStatement.executeQuery()) {
               if (row.next()) {
                   category =  mapRow(row);
                   return category;
               }
           }

        } catch (Exception e) {
            System.out.println("An error has occurred");
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public Category create(Category category) {
        String query = "INSERT INTO categories (category_id, name, description) VALUES (?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setInt(1, category.getCategoryId());
            preparedStatement.setString(2, category.getName());
            preparedStatement.setString(3, category.getDescription());
            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Insert failed, no rows were affected");
            }
            try (ResultSet keys = preparedStatement.getGeneratedKeys()){
                if (keys.next()){
                    int generatedId = keys.getInt(1);
                    category.setCategoryId(generatedId);
                }
            }

        } catch (Exception e) {
            System.out.println("An error has occurred");
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category) {
        String query = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2,category.getDescription());
            preparedStatement.setInt(3, categoryId);

            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new SQLException("Update failed, no rows were affected");
            }


        } catch (Exception e) {
            System.out.println("An error has occurred");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId) {
        String query = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, categoryId);
            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Delete failed, no rows were affected.");
            }

        } catch (Exception e) {
            System.out.println("An error has occurred");
            e.printStackTrace();
        }
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
