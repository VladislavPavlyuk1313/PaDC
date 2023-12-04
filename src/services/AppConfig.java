package services;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final String CONFIG_FILE = "config.properties";

    private static Properties properties;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                return;
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getA() {
        return Double.parseDouble(properties.getProperty("a"));
    }

    public static int getDimension() {
        return Integer.parseInt(properties.getProperty("dimension"));
    }


    public static double getMinValue() {
        return Double.parseDouble(properties.getProperty("min_value"));
    }

    public static double getMaxValue() {
        return Double.parseDouble(properties.getProperty("max_value"));
    }

    public static Boolean isKahanAlgorithmMustBeUsed() {
        return Boolean.parseBoolean(properties.getProperty("use_kahan_alg"));
    }

    public static String getVectorBFilePath() {
        return properties.getProperty("vectorB_file_path");
    }

    public static String getVectorCFilePath() {
        return properties.getProperty("vectorC_file_path");
    }

    public static String getMatrixDFilePath() {
        return properties.getProperty("matrixD_file_path");
    }

    public static String getMatrixTFilePath() {
        return properties.getProperty("matrixT_file_path");
    }

    public static String getMatrixZFilePath() {
        return properties.getProperty("matrixZ_file_path");
    }

    public static String getMatrixBFilePath() {
        return properties.getProperty("matrixB_file_path");
    }

    public static String getResultFilePath() {
        return properties.getProperty("result_file_path");
    }
}

