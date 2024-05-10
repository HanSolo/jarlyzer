package eu.hansolo;


import java.time.Instant;
import java.util.Optional;


public class JarItem extends Item {
    private long numberOfClasses;


    public JarItem(final String name) {
        this(name, 0);
    }
    public JarItem(final String name, final long numberOfClasses) {
        super(Type.JAR, name);
        this.numberOfClasses = numberOfClasses;
    }


    public long getNumberOfClasses() { return this.numberOfClasses; }
    public void setNumberOfClasses(final long numberOfMethods) { this.numberOfClasses = numberOfMethods; }

    public void setNumberOfClassesAndPercentageUsed(final long numberOfClasses, final double percentageUsed) {
        this.numberOfClasses = numberOfClasses;
        this.percentageUsed  = percentageUsed;
    }
}
