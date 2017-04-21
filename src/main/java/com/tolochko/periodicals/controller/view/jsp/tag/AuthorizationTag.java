package com.tolochko.periodicals.controller.view.jsp.tag;

import com.tolochko.periodicals.model.domain.user.User;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Allows specifying two sets of roles that a user must have and must not have in order to
 * see the content of this tag.
 */
public class AuthorizationTag extends TagSupport {
    private static final Logger logger = Logger.getLogger(AuthorizationTag.class);
    private String mustHaveRole;
    private String mustNotHaveRole;
    private User user;

    @Override
    public int doStartTag() throws JspException {
        user = getUserFromSession();
        logger.debug(user);

        if (nonNull(user)
                && hasUserLegitRole()
                /*&& hasUserNoProhibitedRole()*/) {

            return Tag.EVAL_BODY_INCLUDE;
        }

        return Tag.SKIP_BODY;
    }

    private User getUserFromSession() {
        return (User) pageContext.getSession().getAttribute("currentUser");
    }

    private boolean hasUserLegitRole() {
        if ("*".equals(mustHaveRole)) {
            return true;

        } else {
            User.Role legitRole = parseLegitRole();
            User.Role userRole = user.getRole();

            return legitRole.equals(userRole);
        }
    }

    private User.Role parseLegitRole() {
        return nonNull(mustHaveRole) ? User.Role.valueOf(mustHaveRole.toUpperCase()) : null;
    }

    private boolean hasUserNoProhibitedRole() {
        if ("*".equals(mustNotHaveRole)) {
            return false;

        } else {
            User.Role prohibitedRole = parseProhibitedRole();
            User.Role userRole = user.getRole();

            return prohibitedRole.equals(userRole);
        }
    }

    private User.Role parseProhibitedRole() {

        return nonNull(mustNotHaveRole) ? User.Role.valueOf(mustNotHaveRole.toUpperCase()) : null;
    }

    public String getMustHaveRole() {
        return mustHaveRole;
    }

    public void setMustHaveRole(String mustHaveRole) {
        this.mustHaveRole = mustHaveRole;
    }

    public String getMustNotHaveRole() {
        return mustNotHaveRole;
    }

    public void setMustNotHaveRole(String mustNotHaveRole) {
        this.mustNotHaveRole = mustNotHaveRole;
    }
}
