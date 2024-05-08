package eu.hansolo;


import java.util.Objects;


public class PackageItem extends Item {
    private String fullyQualifiedName;
    private long   numberOfClasses;


    public PackageItem(final String name, final String fullyQualifiedName) {
        this(name, fullyQualifiedName, 0);
    }
    public PackageItem(final String name, final String fullyQualifiedName, final long numberOfClasses) {
        super(1, Type.PACKAGE, name.trim());
        this.fullyQualifiedName = fullyQualifiedName;
        this.numberOfClasses    = numberOfClasses;
    }


    public String getFullyQualifiedName() { return this.fullyQualifiedName; }

    public long getNumberOfClasses() { return this.numberOfClasses; }
    public void setNumberOfClasses(final long numberOfMethods) { this.numberOfClasses = numberOfMethods; }

    public void setNumberOfClassesAndPercentageUsed(final long numberOfClasses, final double percentageUsed) {
        this.numberOfClasses = numberOfClasses;
        this.percentageUsed  = percentageUsed;
    }

    @Override public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
        PackageItem that = (PackageItem) o;
        return Objects.equals(fullyQualifiedName, that.fullyQualifiedName);
    }
    @Override public int hashCode() {
        return Objects.hash(super.hashCode(), this.fullyQualifiedName);
    }
}
