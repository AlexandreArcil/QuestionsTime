package fr.canardnocturne.questionstime.config;

public class ConfigMutable<T> extends Config<T> {

    public ConfigMutable(final T value) {
        super(value);
    }

    public void setValue(final T value) {
        this.value = value;
    }

}
