package eu.hansolo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Constants {
    public static final String PACKAGE_SEPARATOR = "/";

    public static final Pattern CLASS_PATTERN         = Pattern.compile("(public|protected|private)?\\s?(static)?\\s?(final)?\\s?(abstract)?\\s?(class|enum|interface)+\\s+(([0-9a-zA-Z\\$_]*\\.)*([a-zA-Z0-9\\$\\.]+))\\s+");
    public static final Matcher CLASS_MATCHER         = CLASS_PATTERN.matcher("");
    public static final Pattern METHOD_PATTERN        = Pattern.compile("([a-zA-Z0-9<>_\\-\\\\.\\?$?a-zA-Z0-9_\\-]+\\s?\\([a-zA-Z0-9<>_,\\?\\-\\.\\s]*\\));");
    public static final Matcher METHOD_MATCHER        = METHOD_PATTERN.matcher("");

    public static final String JAR_FILENAME_FIELD      = "jar_filename";
    public static final String CLASS_NAME_FIELD        = "class_name";
    public static final String METHOD_NAME_FIELD       = "method_name";
    public static final String CLASSES_FIELD           = "classes";
    public static final String NUMBER_OF_CLASSES_FIELD = "number_of_classes";
    public static final String NUMBER_OF_METHODS_FIELD = "number_of_methods";
    public static final String PERCENTAGE_USED_FIELD   = "percentage_used";
    public static final String METHODS_FIELD           = "methods";
    public static final String USED_FIELD              = "used";
}
