package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import java.sql.*;

public class Util {

    private static final String URL = "jdbc:mysql://localhost:3306/UsersDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    public static final SessionFactory sessionFactory;
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

    public Util() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Успешное подключение к базе данных");
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Connection getConnection() {
        return connection;
    }

}
