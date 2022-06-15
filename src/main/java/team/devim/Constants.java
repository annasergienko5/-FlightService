package team.devim;

public class Constants {
    public static final String DB_URL = System.getenv("DB_URL");
    public static final String DB_USERNAME = System.getenv("DB_USERNAME");
    public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    public static final String DB_DRIVER = System.getenv("DB_DRIVER");

    public  static final boolean REDIS_FLAG = Boolean.parseBoolean(System.getenv("REDIS_FLAG"));

    public  static final String REDIS_HOST = System.getenv("REDIS_HOST");
}