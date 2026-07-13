package fr.canardnocturne.questionstime.command.set.question.permissions;

import net.luckperms.api.LuckPermsProvider;
import org.spongepowered.api.command.parameter.Parameter;

public class LuckPermsPermissionsParameter {

    public static Parameter.Value<String> create() {
        // I don't know why but the service is not available using the service provider
        return Parameter.choices(String.class,
                                s -> s,
                                () -> LuckPermsProvider.get().getPlatform().getKnownPermissions())
                        .key("permissions").consumeAllRemaining().build();
    }

}
