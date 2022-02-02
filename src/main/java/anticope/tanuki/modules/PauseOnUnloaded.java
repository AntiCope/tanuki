package anticope.tanuki.modules;

import anticope.tanuki.Tanuki;
import baritone.api.BaritoneAPI;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;

public class PauseOnUnloaded extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> readahead = sgGeneral.add(new DoubleSetting.Builder()
            .name("Readahead")
            .description("How far the module should 'look ahead' for unloaded chunks.")
            .min(1)
            .max(40)
            .sliderMin(1)
            .sliderMax(40)
            .defaultValue(12)
            .build()
    );

    public PauseOnUnloaded() {
        super(Tanuki.CATEGORY, "pause-on-unloaded", "Pauses Baritone when attempting to enter an unloaded chunk.");
    }

    private boolean paused;
    private int pausedChunkX;
    private int pausedChunkZ;

    @Override
    public void onActivate() {
        paused = false;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {

        int chunkX = (int) ((mc.player.getX() + (mc.player.getVelocity().getX() * readahead.get())) / 32);
        int chunkZ = (int) ((mc.player.getZ() + (mc.player.getVelocity().getZ() * readahead.get())) / 32);

        if (!mc.world.getChunkManager().isChunkLoaded(chunkX, chunkZ) && BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing() && !paused) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
            info("Entering unloaded chunk, pausing Baritone.");
            paused = true;
            pausedChunkX = chunkX;
            pausedChunkZ = chunkZ;
        } else if(paused && mc.world.getChunkManager().isChunkLoaded(pausedChunkX, pausedChunkZ)) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
            info("Chunk was loaded, resuming Baritone.");
            paused = false;
        }
    }
}
