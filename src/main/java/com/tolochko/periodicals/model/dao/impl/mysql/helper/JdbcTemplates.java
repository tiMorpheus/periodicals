package com.tolochko.periodicals.model.dao.impl.mysql.helper;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.util.EntityMapper;
import com.tolochko.periodicals.model.pool.ConnectionPoolProvider;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplates {
    private static final Logger logger = Logger.getLogger(JdbcTemplates.class);

    private Connection connection;

    private void executeQuery(String query, ResultSetExecutor executor, Object... params) {
        connection = ConnectionPoolProvider.getConnection();
        ResultSet rs = null;

        if (connection == null) {
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            rs = statement.executeQuery();

            executor.execute(rs);
        } catch (SQLException e) {
            logger.error("Cannot get db items");
            throw new DaoException(e);
        } finally {
            closeResultSet(rs);
            closeConnection(connection);
        }
    }

    public Long insert(String query, Object... params) {
        connection = ConnectionPoolProvider.getConnection();
        ResultSet rs = null;

        if (connection == null) {
            return null;
        }

        try (PreparedStatement statement = connection
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();

            rs = statement.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Cannot insert into db!!!",e );
            throw new DaoException(e);
        } finally {
            closeResultSet(rs);
            closeConnection(connection);
        }

        return null;
    }

    public <T> T findObject(String query, EntityMapper<T> mapper, Object... params) {
        Object[] result = new Object[1];

        executeQuery(query, (rs) -> {
            while (rs.next()) {
                result[0] = mapper.map(rs);
            }
        }, params);

        return (T) result[0];
    }

    public <T> List<T> findObjects(String query, EntityMapper<T> mapper, Object... params) {
        List<T> result = new ArrayList<>();
        executeQuery(query, (rs) -> {
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
        }, params);

        return result;
    }

    public int update(String query, Object... params) {
        connection = ConnectionPoolProvider.getConnection();

        if (connection == null) {
            return 0;
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("cannot execute updateById query", e);
            throw new DaoException(e);
        } finally {
            closeConnection(connection);
        }
    }

    public int remove(String query, Object... params) {
        return update(query, params);
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Cannot close result set!!!");
            }
        }

    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Cannot close connection!!!");
        }
    }


    @FunctionalInterface
    private interface ResultSetExecutor {
        void execute(ResultSet rs) throws SQLException;
    }
}
