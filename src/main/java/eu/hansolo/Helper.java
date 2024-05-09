package eu.hansolo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.QUOTES;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_OPEN;


public class Helper {
    public static final String toJsonString(final String jarFilename, final TreeNode<Item> treeNode, final String reportFrom, final String reportTo, final String appEnv) {
        List<String>  items         = new ArrayList<>();
        StringBuilder jarBuilder    = new StringBuilder();
        StringBuilder classBuilder  = new StringBuilder();
        StringBuilder methodBuilder = new StringBuilder();

        String sourceName = jarFilename;

        jarBuilder.append(CURLY_BRACKET_OPEN)
                  .append(QUOTES).append(Constants.JAR_FILENAME_FIELD).append(QUOTES).append(COLON).append(QUOTES).append(sourceName).append(QUOTES).append(COMMA)
                  .append(QUOTES).append(Constants.LAST_TIME_SAVED_FIELD).append(QUOTES).append(COLON).append(Instant.now().getEpochSecond()).append(COMMA)
                  .append(QUOTES).append(Constants.FROM_FIELD).append(QUOTES).append(COLON).append(QUOTES).append(reportFrom).append(QUOTES).append(COMMA)
                  .append(QUOTES).append(Constants.TO_FIELD).append(QUOTES).append(COLON).append(QUOTES).append(reportTo).append(QUOTES).append(COMMA)
                  .append(QUOTES).append(Constants.APP_ENV_FIELD).append(QUOTES).append(COLON).append(QUOTES).append(appEnv).append(QUOTES).append(COMMA)
                  .append(QUOTES).append(Constants.CLASSES_FIELD).append(QUOTES).append(COLON).append(SQUARE_BRACKET_OPEN);

        // Iterate over all classes
        treeNode.getAll().stream().filter(n -> n.getItem().getType() == Type.CLASS).forEach(cn -> {
            ClassItem classItem = (ClassItem) cn.getItem();
            if (!items.contains(classItem.getName())) {
                classBuilder.setLength(0);
                classBuilder.append(CURLY_BRACKET_OPEN)
                            .append(QUOTES).append(Constants.CLASS_NAME_FIELD).append(QUOTES).append(COLON).append(QUOTES).append(classItem.getFullyQualifiedName()).append(QUOTES).append(COMMA)
                            .append(QUOTES).append(Constants.NUMBER_OF_METHODS_FIELD).append(QUOTES).append(COLON).append(classItem.getNumberOfMethods()).append(COMMA)
                            .append(QUOTES).append(Constants.PERCENTAGE_USED_FIELD).append(QUOTES).append(COLON).append(classItem.getPercentageUsed()).append(COMMA)
                            .append(QUOTES).append(Constants.METHODS_FIELD).append(QUOTES).append(COLON).append(SQUARE_BRACKET_OPEN);

                // Iterate over all methods in class
                cn.getChildren().stream().filter(n -> n.getItem().getType() == Type.METHOD).forEach(mn -> {
                    MethodItem methodItem = (MethodItem) mn.getItem();
                    if (!items.contains(classItem.getName() + Constants.PACKAGE_SEPARATOR + methodItem.getName())) {
                        methodBuilder.setLength(0);
                        methodBuilder.append(CURLY_BRACKET_OPEN)
                                     .append(QUOTES).append(Constants.METHOD_NAME_FIELD).append(QUOTES).append(COLON).append(QUOTES).append(methodItem.getMethodName()).append(QUOTES).append(COMMA)
                                     .append(QUOTES).append(Constants.USED_FIELD).append(QUOTES).append(COLON).append(methodItem.isUsed() ? "true" : "false").append(CURLY_BRACKET_CLOSE);

                        classBuilder.append(methodBuilder).append(COMMA);
                        items.add(classItem.getName() + Constants.PACKAGE_SEPARATOR + methodItem.getName());
                    }
                });
                if (!cn.getChildren().isEmpty()) { classBuilder.setLength(classBuilder.length() - 1); }

                classBuilder.append(SQUARE_BRACKET_CLOSE)
                            .append(CURLY_BRACKET_CLOSE);

                jarBuilder.append(classBuilder).append(COMMA);
                items.add(classItem.getName());
            }
        });
        jarBuilder.setLength(jarBuilder.length() - 1);

        jarBuilder.append(SQUARE_BRACKET_CLOSE)
                  .append(CURLY_BRACKET_CLOSE);
        return jarBuilder.toString();
    }

    public static final void saveTextFile(final String text, final String filename) {
        try {
            Files.write(Paths.get(filename), text.getBytes());
            //System.out.println("Successfully exported text to file " + filename);
        } catch (IOException e) {
            //System.out.println("Error saving text file " + filename + ". " + e);
        }
    }
}
