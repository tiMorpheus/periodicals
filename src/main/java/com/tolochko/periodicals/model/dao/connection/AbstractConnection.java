package com.tolochko.periodicals.model.dao.connection;

public interface AbstractConnection extends AutoCloseable {

    void beginTransaction();

    void commitTransaction();


    void rollbackTransaction();

    @Override
    void close();

}
