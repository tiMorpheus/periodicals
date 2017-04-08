package com.tolochko.periodicals.model.domain.user;

import java.io.Serializable;
import java.util.Set;

public class User implements Serializable {
    private static final long serialVersionUID = 1232131888L;

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String passwordHash;

    /**
     * Roles define specific system functionality available to a user.
     */
    private Role role;

    public enum Status {
        ACTIVE, BLOCKED
    }

    public enum Role {
        ADMIN, USER;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public static Role getRole(String role) {
            switch (role) {
                case "user":
                    return USER;
                case "admin":
                    return ADMIN;
                default:
                    throw new IllegalArgumentException("This role doesn't exist");
            }
        }
    }

    public static class Builder {
        private User user;

        public Builder() {
            this.user = new User();
        }

        public Builder setId(long id) {
            user.setId(id);
            return this;
        }

        public Builder setFirstName(String firstName) {
            user.setFirstName(firstName);
            return this;
        }

        public Builder setLastName(String lastName) {
            user.setLastName(lastName);
            return this;
        }

        public Builder setEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder setAddress(String address) {
            user.setAddress(address);
            return this;
        }


        public Builder setRole(Role role) {
            user.setRole(role);
            return this;
        }

        public Builder setPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public User build() {
            return user;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, firstName='%s', lastName='%s', " +
                        "email='%s', address='%s',  role=%s}",
                id, firstName, lastName,
                email, address, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (id != user.id) {
            return false;
        }
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) {
            return false;
        }
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }


}
