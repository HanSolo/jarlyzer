package eu.hansolo;


public class MethodItem extends Item {
    private boolean used;


    public MethodItem(final String name) {
        this(name.trim(), false);
    }
    public MethodItem(final String name, final boolean used) {
        super(Type.METHOD, name.trim());
        this.used = used;
    }


    public String getMethodName() { return this.name.substring(this.name.lastIndexOf(Constants.PACKAGE_SEPARATOR) + 1); }

    public boolean isUsed() { return this.used; }
    public void setUsed(final boolean used) { this.used = used; }
}
