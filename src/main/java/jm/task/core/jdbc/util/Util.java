package jm.task.core.jdbc.util;


import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


import javax.persistence.Id;

import org.hibernate.query.Query;

import java.io.Serializable;
import java.sql.*;
import java.util.List;
import java.util.stream.IntStream;


public class Util {


    private static final String URL = "jdbc:mysql://localhost:3306/UsersDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static SessionFactory sessionFactory;
    private Connection connection;

    static {
        try {
            Configuration configuration = new Configuration();

            configuration.addAnnotatedClass(jm.task.core.jdbc.model.User.class);
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/UsersDB");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", "root");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }


    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Util() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Успешное подключение к базе данных");
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void saveUser(User user) {
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


    public static void deleteUserById(long Id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                User user = session.get(User.class, Id);
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

    public static void clearTable(String className) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

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

    public static List<User> getAllUsers(String className) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

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

}
