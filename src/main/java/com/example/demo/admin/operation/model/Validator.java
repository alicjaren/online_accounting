package com.example.demo.admin.operation.model;

import java.util.logging.Logger;

public class Validator {

    protected final Logger LOGGER = Logger.getLogger(getClass().getName());

    public boolean loginIsUnique(String login){
        LOGGER.info("Validate login");
        return true;
    }
}
