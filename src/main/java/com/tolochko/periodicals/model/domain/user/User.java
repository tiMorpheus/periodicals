package com.tolochko.periodicals.model.domain.user;

import java.io.Serializable;
import java.util.Set;

public class User implements Serializable {
    private static final long serialVersionUID = -888L;

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String passwordHash;
    /**
     * Only {@code active} users can sign into the system.
     */
    private Status status;

    /**
     * Roles define specific system functionality available to a user.
     */
    private Set<Role> roles;

    public enum Status {
        ACTIVE, BLOCKED
    }

    public enum Role {
        ADMIN, SUBSCRIBER;

        @Override
        public String toString() {
            return name().toLowerCase();
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

        public Builder setStatus(Status status) {
            user.setStatus(status);
            return this;
        }

        public Builder setRoles(Set<Role> roles) {
            user.setRoles(roles);
            return this;
        }

        public Builder setPassword(String password){
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public boolean hasRole(String role) {
        return hasRole(Role.valueOf(role.toUpperCase()));
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
                        "email='%s', address='%s', status=%s, roles=%s}",
                id, firstName, lastName,
                email, address, status, roles);
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