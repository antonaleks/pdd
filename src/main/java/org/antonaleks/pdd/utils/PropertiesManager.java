package org.antonaleks.pdd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesManager {
    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getDbPort() {
        return dbPort;
    }

    private static final String dbUrl;
    private static final String dbPort;
    private static final String dbName;
    private static final String dbCollectionTopics;

    public static String getDbCollectionTopics() {
        return dbCollectionTopics;
    }

    private static final String pathToProperties = "config.properties";

    private static final String dbCollectionQuestion;

    private static final String dbCollectionUser;

    public static String getDbCollectionQuestion() {
        return dbCollectionQuestion;
    }

    public static String getDbCollectionUser() {
        return dbCollectionUser;
    }

    public static String getDbCollectionStatistic() {
        return dbCollectionStatistic;
    }

    private static final String dbCollectionStatistic;

    static {
        Properties props = new Properties();
        try {
            ResourceHelper app = new ResourceHelper();

            InputStream is = app.getFileFromResourceAsStream(pathToProperties);
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbUrl = props.getProperty("db_url");
        dbPort = props.getProperty("db_port");
        dbName = props.getProperty("db_name");

        dbCollectionQuestion = props.getProperty("db_question");
        dbCollectionUser = props.getProperty("db_user");
        dbCollectionTopics = props.getProperty("db_topics");

        dbCollectionStatistic = props.getProperty("db_statistic");


    }


    public static String getDbName() {
        return dbName;
    }
}