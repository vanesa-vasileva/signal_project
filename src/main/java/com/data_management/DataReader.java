package com.data_management;

/**
 * Defines a contract for reading patient data from different sources.
 * Implementations can read from files, WebSocket streams, databases, etc.
 */
public interface DataReader {

    /**
     * Reads data from the source and stores it in the provided DataStorage.
     *
     * @param dataStorage the storage where the read data will be stored
     * @throws Exception if an error occurs during reading (e.g., connection issues, parsing errors)
     */
    void readData(DataStorage dataStorage) throws Exception;
}