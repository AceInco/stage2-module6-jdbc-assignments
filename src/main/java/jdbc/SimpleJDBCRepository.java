package jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository
{
    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myusers (firstname, lastname, age) VALUES (?,?,?)";
    private static final String updateUserSQL = "UPDATE myusers SET firstname=?, lastname=?, age=? WHERE id=?";
    private static final String deleteUser = "DELETE FROM myusers WHERE id=?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id=?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstname=?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    {
        try
        {
            connection = CustomDataSource.getInstance().getConnection();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public Long createUser(User createdUser)
    {
        //using Statement
        Long createdUserId = null;
        try
        {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            String firstName = createdUser.getFirstName();
            String lastName = createdUser.getLastName();
            int age = createdUser.getAge();

            String SQL = createUserSQL.replace("?,?,?", "'" + firstName + "'"
                    + "," + "'" + lastName + "'" + "," + age);
            st.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                createdUserId = rs.getLong(1);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return createdUserId;

        //using PreparedStatement
        /*try
        {
            st = connection.createStatement();
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, createdUser.getFirstName());
            ps.setString(2, createdUser.getFirstName());
            ps.setInt(3, createdUser.getAge());
            return (long) ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }*/
    }

    public User findUserById(Long userId)
    {
        //using Statement
        User user = null;

        try
        {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            String SQL = findUserByIdSQL.replace("?", "'" + userId + "'");
            ResultSet resultSet = st.executeQuery(SQL);

            while (resultSet.next())
            {
                user = new User();

                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getInt("age"));
            }

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return user;
        /*User user = null;
        try
        {
            st = connection.createStatement();
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            user = new User();
            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setAge(resultSet.getInt("age"));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return user;*/
    }

    public User findUserByName(String userName)
    {
        //using Statement
        User user = null;

        try
        {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            String SQL = findUserByNameSQL.replace("?", "'" + userName + "'");
            ResultSet resultSet = st.executeQuery(SQL);

            while (resultSet.next())
            {
                user = new User();

                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getInt("age"));
            }

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return user;
        /*User user = null;
        try
        {
            st = connection.createStatement();
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            user = new User();
            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setAge(resultSet.getInt("age"));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return user;*/
    }

    public List<User> findAllUser()
    {
        //using Statement
        List<User> users = new ArrayList<>();

        try
        {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(findAllUserSQL);

            while (resultSet.next())
            {
                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getInt("age"));

                users.add(user);
            }

        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }

        return users;

        //using PrepareStatement
        /*List<User> users = new ArrayList<>();
        try
        {
            st = connection.createStatement();
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findAllUserSQL);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getInt("age"));

                users.add(user);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return users;*/
    }

    public User updateUser(User updatedUser)
    {
        //using Statement
        User user = null;
        try
        {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            long id = updatedUser.getId();
            String firstName = updatedUser.getFirstName();
            String lastName = updatedUser.getLastName();
            int age = updatedUser.getAge();

            String SQL = updateUserSQL.replace("firstname=?", "firstname=" + "'" + firstName + "'" )
                    .replace("lastname=?", "lastname=" +"'" + lastName + "'" )
                    .replace("age=?", "age=" + age)
                    .replace("id=?", "id=" + id);

            st.executeUpdate(SQL);

            user = findUserById(id);
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return user;
        /*try
        {
            st = connection.createStatement();
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(updateUserSQL);

            ps.setString(1, updatedUser.getFirstName());
            ps.setString(2, updatedUser.getFirstName());
            ps.setInt(3, updatedUser.getAge());
            ps.setLong(4, updatedUser.getId());

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return updatedUser;*/
    }

    public void deleteUser(Long userId)
    {
        //using Statement
        try
        {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            String SQL = deleteUser.replace("?", String.valueOf(userId));
            st.executeUpdate(SQL);
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        //using PrepareStatement
        /*try
        {
            ps = connection.prepareStatement(deleteUser);

            ps.setLong(1, userId);

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }*/
    }
}