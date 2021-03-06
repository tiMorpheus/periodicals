package com.tolochko.periodicals.controller.view.jsp.tag;

import org.apache.taglibs.standard.tag.common.core.Util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Date;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class FormatDatetimeTag extends TagSupport{
    private transient Temporal value;
    private String pattern = "dd.MM.YYYY HH:mm:ss";
    private String var;
    private int scope;


    public FormatDatetimeTag() {
        super();
        this.scope = PageContext.PAGE_SCOPE;
    }

    public void setVar(final String var) {
        this.var = var;
    }

    public void setScope(final String scope) {
        this.scope = Util.getScope(scope);
    }


    public void setValue(final Temporal value) {
        this.value = value;
    }

    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }

    @Override
    public int doEndTag() throws JspException {

        String formatted;

        if (isNull(value)) {
            if (nonNull(var)) {
                pageContext.removeAttribute(var, scope);
            }

            return Tag.EVAL_PAGE;
        }

        Instant instant = Instant.from(value);
        formatted = new SimpleDateFormat(pattern).format(Date.from(instant));

        if (nonNull(var)) {
            pageContext.setAttribute(var, formatted, scope);
        } else {
            try {
                pageContext.getOut().print(formatted);

            } catch (final IOException ioe) {
                throw new JspTagException(ioe.toString(), ioe);
            }
        }

        return Tag.EVAL_PAGE;
    }

    @Override
    public void release() {
        value = null;
    }
}
