package eu.hansolo;


import java.time.Instant;
import java.util.Optional;


public class JarItem extends Item {
    private Optional<Instant> lastTimeSaved;


    public JarItem(final String name) {
        this(name, null);
    }
    public JarItem(final String name, final Instant lastTimeSaved) {
        super(0, Type.JAR, name);
        this.lastTimeSaved = null == lastTimeSaved ? Optional.empty() : Optional.of(lastTimeSaved);
    }


    public Optional<Instant> getLastTimeSaved() { return this.lastTimeSaved; }
    public void setLastTimeSaved(final Instant lastTimeSaved) {
        this.lastTimeSaved = Optional.of(lastTimeSaved);
    }
}
