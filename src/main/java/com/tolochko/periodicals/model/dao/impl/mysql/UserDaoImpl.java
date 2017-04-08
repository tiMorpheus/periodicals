package com.tolochko.periodicals.model.dao.impl.mysql;

import com.tolochko.periodicals.model.dao.impl.mysql.helper.JdbcTemplates;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import com.tolochko.periodicals.model.dao.util.UserMapper;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.pool.ConnectionPoolProvider;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.isNull;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);

    private JdbcTemplates templates = new JdbcTemplates(ConnectionPoolProvider.getInstance());


    @Override
    public User findUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE users.email = ?";

        User user = templates.findObject(query, UserMapper::map, email);

        if (user != null){
            user.setRole(readRole(user.getId()));
        }

        return user;
    }

    @Override
    public Long findRoleId(User.Role role) {
        String query = "SELECT id FROM role WHERE role = ?";

        return templates.findObject(query, (rs) -> {
            try {
                return rs.getLong("id");
            } catch (SQLException e) {
                logger.error("cannot map object");
                throw new IllegalStateException(e);
            }
        }, role.toString());
    }


    @Override
    public User findOneById(Long id)  {
        String query = "SELECT * FROM users WHERE users.id = ?";

        User user = templates.findObject(query, UserMapper::map, id);

        if (user != null){
            user.setRole(readRole(user.getId()));
        }

        return user;

    }

    @Override
    public User.Role readRole(Long id){
        String query = "SELECT role.role FROM role "+
                        "JOIN users ON users.role = role.id AND "+
                        "users.id = ?";

        return templates.findObject(query, (rs) -> {
            try {
                return User.Role.getRole(rs.getString("role"));
            } catch (SQLException e) {
                logger.error("cannot map object");
                throw new IllegalStateException(e);
            }
        }, id);
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users";

        List<User> users = templates.findObjects(query, UserMapper::map);

        users.forEach(e -> e.setRole(readRole(e.getId())));

        return users;
    }

    @Override
    public long add(User user) {
        if (isNull(user)){
            String msg = "user cannot be null";
            logger.info(msg);
            throw new IllegalArgumentException(msg);
        }
        String query = "INSERT INTO users " +
                "(first_name, last_name, email, address, password, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";


        return templates.insert(query,
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getAddress(),
                            user.getPassword(),
                            findRoleId(user.getRole()));

    }

    @Override
    public void delete(Long id){
        String query ="DELETE FROM users WHERE id = ?";

        templates.remove(query, id);
    }

    @Override
    public void update(Long id, User user) {
        String query = "UPDATE users " +
                "SET " +
                "first_name = ?," +
                "last_name = ?," +
                "email = ?," +
                "password = ?," +
                "address = ?," +
                "role = ? " +
                "WHERE id=?";

        templates.update(query,
               user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getAddress(),
                findRoleId(user.getRole()),
                id);
    }
}

