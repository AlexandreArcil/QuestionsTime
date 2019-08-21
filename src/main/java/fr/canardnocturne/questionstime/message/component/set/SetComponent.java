package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponent;

public interface SetComponent {

    <V> void setComponent(MessageComponent<V> component, V value);

}
