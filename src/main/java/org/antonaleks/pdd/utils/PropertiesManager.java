package org.antonaleks.pdd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    public static String getAppTitle() {
        return appTitle;
    }

    private static final String appTitle;

    public static final String FOCUS_COLOR = "#8d9bd7";
    public static final String DEFAULT_TEXT_COLOR = "WHITE";
    public static final String RIGHT_BUTTON_COLOR = "Green";
    public static final String FAIL_BUTTON_COLOR = "Red";
    public static final String CHECKED_EXAM_BUTTON_COLOR = "Blue";

    public static final String PASSIVE_COLOR = "#5264AE";


    public static String getDbCollectionTopics() {
        return dbCollectionTopics;
    }

    private static final String pathToProperties = "config.properties";

    private static final String dbCollectionQuestion;

    private static final String dbCollectionUser;

    public static String getDbCollectionLicense() {
        return dbCollectionLicense;
    }

    private static final String dbCollectionLicense;


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
            props.load(new InputStreamReader(is, StandardCharsets.UTF_8));

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
        appTitle = props.getProperty("app_title");
        dbCollectionLicense = props.getProperty("db_license");


    }


    public static String getDbName() {
        return dbName;
    }
}