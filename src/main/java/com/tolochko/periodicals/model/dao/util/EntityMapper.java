package com.tolochko.periodicals.model.dao.util;

import java.sql.ResultSet;

@FunctionalInterface
public interface EntityMapper<T> {

    T map(ResultSet rs);
}
