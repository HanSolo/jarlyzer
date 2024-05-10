package eu.hansolo;

import java.util.Objects;


public abstract class Item implements Comparable<Item> {
    protected Type   type;
    protected int    hierarchy;
    protected String name;
    protected double percentageUsed;


    public Item(final Type type, final String name) {
        this.type      = type;
        this.hierarchy = this.type.getHierarchy();
        this.name      = name;
    }


    public int getHierarchy() { return this.hierarchy; }

    public Type getType() { return this.type; }

    public String getName() { return this.name; }

    public double getPercentageUsed() { return this.percentageUsed; }
    public void setPercentageUsed(final double percentageUsed) { this.percentageUsed = percentageUsed; }

    public double getPercentageUnused() { return 100.0 - this.percentageUsed; }

    @Override public String toString() { return this.name; }

    @Override public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Item item = (Item) o;
        return type == item.type && Objects.equals(name, item.name);
    }
    @Override public int hashCode() {
        return Objects.hash(type, name);
    }

    @Override public int compareTo(final Item other) {
        return Integer.compare(this.hierarchy, other.hierarchy);
    }
}
