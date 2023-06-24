package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Util util = new Util();
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String query = "CREATE TABLE `UsersDB`.`users` (" +
                "  `ID` INT NOT NULL AUTO_INCREMENT," +
                "  `Name` VARCHAR(45) NOT NULL," +
                "  `LastName` VARCHAR(45) NOT NULL," +
                "  `Age` INT NULL," +
                "  PRIMARY KEY (`ID`));";

        try {
            Statement statement = util.getConnection().createStatement();
            statement.executeUpdate(query);
            statement.close();
            System.out.println("Таблица успешно создана. Имя таблицы: users Поля: ID, Name, LastName, Age");
        } catch (SQLException e) {
            System.out.println("Таблица с таким именем уже существует: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE users";
        try {
            Statement statement = util.getConnection().createStatement();
            statement.executeUpdate(query);
            statement.close();
            System.out.println("Таблица с именем: users удалена!");
        } catch (SQLException e) {
            System.out.println("Таблицы с таким именем не существует: " + e.getMessage());
        }

    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
       String query = "INSERT INTO users(Name, LastName, Age) VALUES (" + "'"+name+"'," + " '"+lastName+"',"+age +");";
       Statement statement = util.getConnection().createStatement();
       statement.execute(query);
       statement.close();
        System.out.println("Новая запись добавлена в таблицу." +
                " Имя: " + name +
                " Фамилия: " + lastName +
                " Возраст: " + age);
    }

    public void removeUserById(long id) throws SQLException {
        String query ="DELETE FROM users WHERE ID = " + id +";";
        Statement statement = util.getConnection().createStatement();
        statement.executeUpdate(query);
        statement.close();
        System.out.println("Запись с ID: " + id + " удалена!");
    }

    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT * from users;";
        Statement statement = util.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        List<User> allUsers = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId((long) resultSet.getInt("ID")); ;
            user.setName(resultSet.getString("Name"));
            user.setLastName(resultSet.getString("LastName"));
            user.setAge((byte) resultSet.getInt("Age")) ;
            allUsers.add(user);
        }
        statement.close();
        System.out.println("Список пользователей сформирован!");
        return allUsers;
    }

    public void cleanUsersTable() throws SQLException {
        String query = "DELETE FROM users";
        Statement statement = util.getConnection().createStatement();
        statement.executeUpdate(query);
        statement.close();
        System.out.println("Все пользователи удалены из таблицы");
    }
}
