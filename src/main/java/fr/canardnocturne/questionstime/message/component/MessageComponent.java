package fr.canardnocturne.questionstime.message.component;

import net.kyori.adventure.text.Component;

public abstract class MessageComponent<T> {

    private final String name;

    public MessageComponent(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Component process(T type);

}
