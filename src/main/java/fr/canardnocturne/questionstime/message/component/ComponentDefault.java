package fr.canardnocturne.questionstime.message.component;


import net.kyori.adventure.text.Component;

public class ComponentDefault<T> extends MessageComponent<T> {

    public ComponentDefault(final String name) {
        super(name);
    }

    @Override
    public Component process(final T type) {
        return Component.text(String.valueOf(type));
    }
}
