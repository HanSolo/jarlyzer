package eu.hansolo;

public enum Type {
    JAR(0),
    PACKAGE(1),
    CLASS(2),
    METHOD(3);

    private int hierarchy;

    Type(final int hierarchy) {
        this.hierarchy = hierarchy;
    }


    public int getHierarchy() { return this.hierarchy; }
}
