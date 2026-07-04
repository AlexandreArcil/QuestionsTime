package fr.canardnocturne.questionstime.config;

public class Config<T> {

    protected T value;

    public Config(final T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
