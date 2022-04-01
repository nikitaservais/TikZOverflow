package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;

/**
 * Enables to connect to SQLite database
 */
public abstract class Database{
    ConnectionDBSingleton connectionDB;

    public Database() throws DatabaseConnectionException {
        try {
            this.connectionDB = ConnectionDBSingleton.getInstance();
        } catch (InvalidDriverException | DatabaseException e) {
            throw new DatabaseConnectionException(e);
        }
    }
}