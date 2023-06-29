package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Id;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.sessionFactory;

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
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                Serializable generatedId = session.save(user);
                user.setId((Long) generatedId);


                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                User user = session.get(User.class, id);
                if (user != null) {
                    session.delete(user);
                }

                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String className = "User";
                String hql = "FROM " + className;
                Query<User> query = session.createQuery(hql, User.class);
                List<User> users = query.getResultList();

                transaction.commit();

                return users;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String className = "User";
                String hql = "DELETE FROM " + className;
                Query<User> query = session.createQuery(hql);
                query.executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }
}
