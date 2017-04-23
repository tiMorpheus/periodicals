package com.tolochko.periodicals.model.connection;

public interface ConnectionProxy extends AutoCloseable{

    /**
     * Defines begin of transaction.
     */
    void beginTransaction();

    /**
     * Saves transaction.
     */
    void commitTransaction();

    /**
     * rolls back transaction.
     */
    void rollbackTransaction();

    /**
     * Closes connection.
     */
    @Override
    void close();

}
