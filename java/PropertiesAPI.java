import java.util.Properties;
import java.io.*;

public class PropertiesAPI {

    public static void main(String[] args) throws Exception {

        // CREATE
        Properties props = new Properties();
        props.setProperty("host", "localhost");
        props.setProperty("port", "8080");
        props.setProperty("env", "dev");

        // STORE
        File file = new File("app.properties");
        try (FileOutputStream out = new FileOutputStream(file)) {
            props.store(out, null);
        }
        System.out.println("stored: " + file.getAbsolutePath());

        // READ (from file)
        Properties loaded = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            loaded.load(in);
        }
        for (String key : loaded.stringPropertyNames()) {
            System.out.println(key + "=" + loaded.getProperty(key));
        }
        System.out.println("default: " + loaded.getProperty("missing", "N/A"));

        // SYSTEM
        System.out.println("java.version=" + System.getProperty("java.version"));
        System.out.println("os.name=" + System.getProperty("os.name"));
        System.out.println("user.home=" + System.getProperty("user.home"));

        System.setProperty("my.custom", "hello");
        System.out.println("my.custom=" + System.getProperty("my.custom"));

        file.delete();
    }
}
