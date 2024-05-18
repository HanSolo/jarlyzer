package eu.hansolo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;


public class Scanner {

    public static final TreeNode<Item> getClassesAndMethods(final String filename) {
        TreeNode<Item>  jarNode = new TreeNode<>(new JarItem(filename));
        try {
            List<String>   commands  = List.of("/bin/sh", "-c", "jar tf " + filename + " | grep '.class$' | tr / . | sed 's/\\.class$//' | xargs javap -p -classpath " + filename);
            ProcessBuilder builder   = new ProcessBuilder(commands).redirectErrorStream(true);
            Process        process   = builder.start();
            List<String>   lines     = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().toList();
            String         className = "";
            TreeNode<Item> classNode = null;
            for (String line : lines) {
                if (line.startsWith("Compiled") || line.contains("META-INF")) { continue; }
                if (!line.endsWith(";")) { // Classes
                    Constants.CLASS_MATCHER.reset(line);
                    className = "";
                    final List<MatchResult> classResults = Constants.CLASS_MATCHER.results().collect(Collectors.toList());
                    if (!classResults.isEmpty()) {
                        MatchResult result = classResults.get(0);
                        className = result.group(6).strip().replaceAll("\\$[0-9]+", "").replaceAll("\\.", Constants.PACKAGE_SEPARATOR);
                        List<String> pkgs = List.of(className.split("\\" + Constants.PACKAGE_SEPARATOR));
                        TreeNode<Item> currentRootNode = jarNode;
                        for (int i = 0 ; i < pkgs.size() - 1 ; i++) {
                            String pkgName = pkgs.get(i);
                            StringBuilder fullyQualifiedNameBuilder = new StringBuilder();
                            for (int j = 0  ; j < i ; j++) {
                                fullyQualifiedNameBuilder.append(pkgs.get(j)).append(Constants.PACKAGE_SEPARATOR);
                            }
                            fullyQualifiedNameBuilder.append(pkgName);
                            TreeNode<Item> pkgNode = new TreeNode<>(new PackageItem(pkgName, fullyQualifiedNameBuilder.toString()), currentRootNode);
                            currentRootNode = pkgNode;
                        }
                        String                   classNameToFind = className;
                        Optional<TreeNode<Item>> optNode         = jarNode.getHierarchicalNodeList().stream().filter(n -> n.getItem().getType() == Type.CLASS).filter(n -> n.getItem().getName().equals(classNameToFind)).findFirst();
                        classNode = optNode.isPresent() ? optNode.get() : new TreeNode<>(new ClassItem(className), currentRootNode);
                    }
                } else { // Methods
                    Constants.METHOD_MATCHER.reset(line);
                    final List<MatchResult> methodResults = Constants.METHOD_MATCHER.results().collect(Collectors.toList());
                    if (!methodResults.isEmpty()) {
                        MatchResult result = methodResults.get(0);
                        String      methodName = result.group(1);
                        methodName = methodName.substring(0, methodName.indexOf("(")).replaceAll("\\.", Constants.PACKAGE_SEPARATOR) + methodName.substring(methodName.indexOf("("));
                            if (methodName.startsWith("lambda$")) { methodName = methodName.replace("lambda$", ""); }
                            methodName = methodName.replaceAll("\\$[0-9]+", "");
                        if (!methodName.isEmpty() && !methodName.contains(className)) {
                            String fullyQualifiedName = className + Constants.PACKAGE_SEPARATOR + methodName;
                            ClassItem classItem = (ClassItem) (classNode.getItem());
                                MethodItem methodItem = new MethodItem(fullyQualifiedName);
                                if (classNode.getChildren().stream().filter(n -> n.getItem().equals(methodItem)).count() == 0) {
                                    new TreeNode<>(methodItem, classNode);
                                    classItem.setNumberOfMethods(classItem.getNumberOfMethods() + 1);
                                }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return null;
        }
        JarItem jarItem = (JarItem) jarNode.getItem();
        jarItem.setNumberOfClasses(jarNode.getHierarchicalNodeList().stream().filter(n -> n.getItem().getType() == Type.CLASS).count());
        return jarNode;
    }
}
