package dev.munebase.hexkeys.casting.environment;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;

public class NoeticBookshelfAmbitHook {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) {
            return;
        }

        CastingEnvironment.addCreateEventListener((env, ignored) -> env.addExtension(new NoeticBookshelfAmbitComponent(env)));
        initialized = true;
    }
}
