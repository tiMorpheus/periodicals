package com.tolochko.periodicals.model.dao.impl.mysql;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.pool.ConnectionPoolProvider;
import com.tolochko.periodicals.utils.DaoUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);


    @Override
    public User findByFirstName(String username) throws DaoException {
        String query = "SELECT * FROM users WHERE first_name = ?";


        try (Connection connection = ConnectionPoolProvider.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? DaoUtil.getUserFromResultSet(rs) : null;
            }

        } catch (SQLException e) {
            String msg = "Exception during finding a user with username = " + username;
            logger.error(msg, e);
            throw new DaoException(msg, e);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE users.email = ?";

        try (Connection connection = ConnectionPoolProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? DaoUtil.getUserFromResultSet(rs) : null;
            }

        } catch (SQLException e) {
            String msg = "Exception during finding a user with email = " + email;
            logger.error(msg, e);
            throw new DaoException(msg, e);
        }

    }

    @Override
    public boolean isEmailExistsInDb(String email){
        String query = "SELECT COUNT(id) FROM users " +
                "WHERE users.email = ?";

        try (Connection connection = ConnectionPoolProvider.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            String msg = e.getMessage();
            logger.error(msg, e);
            throw new DaoException(msg, e);
        }
    }

    @Override
    public User findOneById(long id)  {
        String query = "SELECT * FROM users WHERE users.id = ?";

        try (Connection connection = ConnectionPoolProvider.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? DaoUtil.getUserFromResultSet(rs) : null;
            }

        } catch (SQLException e) {
            String msg = "Exception during finding a user with id = " + id;
            logger.error(msg, e);
            throw new DaoException(msg, e);
        }
    }

    @Override
    public List<User> findAll() throws DaoException {


        String query = "SELECT * FROM users";



        List<User> users = null;
        try (Connection connection = ConnectionPoolProvider.getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
                  ResultSet rs = ps.executeQuery()) {
            users = new ArrayList<>();

            while (rs.next()) {
                users.add(DaoUtil.getUserFromResultSet(rs));
            }


            return users;
        } catch (SQLException e) {
            String msg = "Exception during finding all users.";
            logger.error(msg, e);
            throw new DaoException(msg, e);
        }
    }

    @Override
    public long createNew(User user) {
        if (isNull(user)){
            String msg = "user cannot be null";
            logger.info(msg);
            throw new IllegalArgumentException(msg);
        }
        String query = "INSERT INTO users " +
                "(first_name, last_name, email, address, password_hash, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";


        try (Connection connection = ConnectionPoolProvider.getConnection();
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getAddress());
            ps.setString(5,user.getPassword());
            ps.setString(6, user.getStatus().name().toLowerCase());

            tryExecuteUpdate(ps);

            return tryRetrieveId(ps);

        } catch (SQLException e) {
            String msg = String.format("Exception during creating a new user: %s", user);
            logger.error(msg, e);
            throw new DaoException(msg, e);
        }
    }

    private void tryExecuteUpdate(PreparedStatement st) throws SQLException {
        int affectedRows = st.executeUpdate();

        if (affectedRows == 0) {
            logger.info("Exception during creating a new user");
            throw new DaoException("Exception during creating a new user");
        }
    }

    private long tryRetrieveId(PreparedStatement st) throws SQLException {

        try (ResultSet generatedKeys = st.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                logger.info("Creating user failed, no rows affected");
                throw new DaoException("Creating user failed, no rows affected");
            }
        }
    }

    @Override
    public int deleteUserFromDb(String email){
        String query ="DELETE FROM users WHERE email = ?";

        try (Connection connection = ConnectionPoolProvider.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1,email);

            return ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Exception during deleting user from db");
        }

        return 0;
    }

    @Override
    public int update(User entity) throws DaoException {
        logger.info("U cannot update user " + entity);
        throw new UnsupportedOperationException();
    }
}

