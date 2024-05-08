package eu.hansolo;

import com.azul.icclient.tools.Constants;


public class MethodItem extends Item {
    private boolean used;


    public MethodItem(final String name) {
        this(name.trim(), false);
    }
    public MethodItem(final String name, final boolean used) {
        super(3, Type.METHOD, name.trim());
        this.used = used;
    }


    public String getMethodName() { return this.name.substring(this.name.lastIndexOf(Constants.PACKAGE_SEPARATOR) + 1); }

    public boolean isUsed() { return this.used; }
    public void setUsed(final boolean used) { this.used = used; }
}