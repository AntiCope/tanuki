package anticope.tanuki.modules;

import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

import anticope.tanuki.Tanuki;
import anticope.tanuki.events.PushOutOfBlockEvent;


public class AntiCrystalPhase extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> clipDistance = sgGeneral.add(new DoubleSetting.Builder()
            .name("Clip Distance")
            .description("The distance per clip.")
            .defaultValue(.01)
            .min(0)
            .max(1)
            .build()
    );

    public AntiCrystalPhase() {
        super(Tanuki.CATEGORY, "anti-crystal-phase", "Allows you to shield the lower half of your hitbox.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        ClientPlayerEntity p = mc.player;
        double blocks = clipDistance.get();

        if (!p.isOnGround()) return;

            if(mc.options.forwardKey.isPressed()){
                Vec3d forward = Vec3d.fromPolar(0, p.getYaw());
                p.updatePosition(p.getX() + forward.x * blocks, p.getY(), p.getZ() + forward.z * blocks);
            }

            if(mc.options.backKey.isPressed()){
                Vec3d forward = Vec3d.fromPolar(0, p.getYaw() - 180);
                p.updatePosition(p.getX() + forward.x * blocks, p.getY(), p.getZ() + forward.z * blocks);
            }

            if(mc.options.leftKey.isPressed()){
                Vec3d forward = Vec3d.fromPolar(0, p.getYaw() - 90);
                p.updatePosition(p.getX() + forward.x * blocks, p.getY(), p.getZ() + forward.z * blocks);
            }

            if(mc.options.rightKey.isPressed()) {
                Vec3d forward = Vec3d.fromPolar(0, p.getYaw() - 270);
                p.updatePosition(p.getX() + forward.x * blocks, p.getY(), p.getZ() + forward.z * blocks);
            }

            if (mc.options.jumpKey.isPressed()) {
                p.updatePosition(p.getX(), p.getY() + 0.05, p.getZ());
            }

            if (mc.options.sneakKey.isPressed()) {
                p.updatePosition(p.getX(), p.getY() - 0.05, p.getZ());
            }
    }

    @EventHandler
    private void onPushOutOfBlock(PushOutOfBlockEvent event) {
        event.cancel();
    }

}
