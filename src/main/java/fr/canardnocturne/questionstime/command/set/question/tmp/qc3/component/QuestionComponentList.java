package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class QuestionComponentList<W> extends QuestionComponentCollection<W, List<W>> {

    public QuestionComponentList(final String name, final String singular, final String plural,
                                 final Function<String, W> valueMapper,
                                 final Function<W, String> valueUnmapper) {
        super(name, singular, plural, ArrayList::new, valueMapper, valueUnmapper);
    }

    @Override
    public List<W> load(final ConfigurationNode node) throws SerializationException {
        final List<String> values = node.node(this.name).getList(String.class, Collections.emptyList());
        return values.stream().map(this.valueMapper).toList();
    }

}
