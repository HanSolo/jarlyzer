package eu.hansolo;


import java.util.List;


public class ClassItem extends Item {
    private String packageName;
    private long   numberOfMethods;


    public ClassItem(final String name) {
        this(name.trim(), 0, 0);
    }
    public ClassItem(final String name, final long numberOfMethods, final double percentageUsed) {
        super(2, Type.CLASS, name.trim());
        this.packageName     = name.contains(Constants.PACKAGE_SEPARATOR) ? name.substring(0, name.lastIndexOf(Constants.PACKAGE_SEPARATOR)) : "";
        this.numberOfMethods = numberOfMethods;
        this.percentageUsed  = percentageUsed;
    }


    public String getFullyQualifiedName() { return this.name; }

    public String getClassName() { return this.name.substring(this.name.lastIndexOf(Constants.PACKAGE_SEPARATOR) + 1); }

    public String getPackageName() { return this.packageName; }

    public List<String> getPackageParts() { return List.of(this.name.split(Constants.PACKAGE_SEPARATOR)); }

    public long getNumberOfMethods() { return this.numberOfMethods; }
    public void setNumberOfMethods(final long numberOfMethods) { this.numberOfMethods = numberOfMethods; }
}
