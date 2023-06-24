package jm.task.core.jdbc.dao;

import com.mysql.cj.Session;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Transaction;

import javax.persistence.Id;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    Util util = new Util();

    public UserDaoHibernateImpl() {

    }


    @Override
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

    @Override
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

    @Override
    public void saveUser(String name, String lastName, byte age) {

        User user = new User(name, lastName, age);
        Util.saveUser(user);
    }

    @Override
    public void removeUserById(long id) {
        Util.deleteUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return Util.getAllUsers("User");
    }

    @Override
    public void cleanUsersTable() {
        Util.clearTable("User");
    }
}
