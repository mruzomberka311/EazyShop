package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{

    @Autowired
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        return null;
    }

    @Override
    public Category getById(int categoryId)
    {
        String query = "SELECT * FROM categories WHERE categoryId = ?";

      try (Connection connection = getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(query);){
           ResultSet row = preparedStatement.executeQuery();
           mapRow(row);


      }catch (Exception e){
          System.out.printf("An error has occurred");
          e.printStackTrace();
      }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        String query = "INSERT INTO categories (category_id, name, description) VALUES (?,?,?)";
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){


        }catch (Exception e){
            System.out.println("An error has occurred");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
       String query = "UPDATE categories SET category_id = ?, name = ?, description = ?";
       try  (Connection connection = getConnection();
       PreparedStatement preparedStatement = connection.prepareStatement(query);){
       int row = preparedStatement.executeUpdate();
       if (row == 0){
           throw new SQLException("Insert failed, no rows were affected");
       }


       }catch (Exception e){
           System.out.println("An error has occurred");
           e.printStackTrace();
       }
    }

    @Override
    public void delete(int categoryId)
    {
        String query = "DELETE FROM categories WHERE category_id = ?";
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
            preparedStatement.setInt(1, categoryId);
           int rows = preparedStatement.executeUpdate();
           if (rows == 0){
               throw new SQLException("Delete failed, no rows were affected.");
           }

        }catch (Exception e){
            System.out.println("An error has occurred");
            e.printStackTrace();
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
