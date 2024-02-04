package dev.munebase.hexkeys;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.nio.file.Path;

public class HexkeysAbstractions {
    /**
     * This explanation is mostly from Architectury's template project.
     * One thing I would like to add for those familiar with Hex Casting's layout:
     * this is a somewhat simpler and more intuitive (imo) way to accomplish what they accomplish with Xplat abstractions.
     * <p>
     * We can use {@link Platform#getConfigFolder()} but this is just an example of {@link ExpectPlatform}.
     * <p>
     * This must be a <b>public static</b> method. The platform-implemented solution must be placed under a
     * platform sub-package, with its class suffixed with {@code Impl}.
     * <p>
     * Example:
     * <p>
     * Expect: dev.munebase.hexkeys.HexkeysAbstractions#getConfigDirectory()
     * <p>
     * Actual Fabric: dev.munebase.hexkeys.fabric.HexkeysAbstractionsImpl#getConfigDirectory()
     * <p>
     * Actual Forge: dev.munebase.hexkeys.forge.HexkeysAbstractionsImpl#getConfigDirectory()
     * <p>
     * <a href="https://plugins.jetbrains.com/plugin/16210-architectury">You should also get the IntelliJ plugin to help with @ExpectPlatform.</a>
     */
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void processAddingDim(MinecraftServer server, ServerWorld world) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void commonSetup() {
        throw new AssertionError();
    }
}
