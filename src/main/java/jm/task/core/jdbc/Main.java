package jm.task.core.jdbc;


import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {
        UserDao userDao = new UserDaoJDBCImpl();
        UserDao userDaoHibernate = new UserDaoHibernateImpl();
        userDao.createUsersTable();

        userDaoHibernate.saveUser("Name1", "LastName1", (byte) 20);
        userDaoHibernate.saveUser("Name2", "LastName2", (byte) 25);
        userDaoHibernate.saveUser("Name3", "LastName3", (byte) 31);
        userDaoHibernate.saveUser("Name4", "LastName4", (byte) 38);

        userDaoHibernate.removeUserById(2);
        List<User> users = userDaoHibernate.getAllUsers();
        users.stream().forEach(System.out::println);
        userDaoHibernate.cleanUsersTable();
        userDaoHibernate.dropUsersTable();


    }
}
