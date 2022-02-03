package anticope.tanuki.modules;

import anticope.tanuki.Tanuki;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.util.math.Vec3d;

public class TanukiPacketFly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgAntiKick = settings.createGroup("Anti Kick"); //Pog

    public enum AntiKickMode {
        Normal,
        Packet,
        None
    }

    private final Setting<Double> packetFlySpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("packet-fly-speed")
        .description("How fast to fly with Packet Fly")
        .defaultValue(0.1)
        .min(0)
        .sliderMax(50)
        .build()
    );

    private final Setting<Double> verticalPacketSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("vertical-packet-speed")
        .description("How fast you should ascend with Packet Fly.")
        .defaultValue(1)
        .min(0)
        .sliderMax(20)
        .build()
    );

    private final Setting<Integer> tpConfirmCounter = sgGeneral.add(new IntSetting.Builder()
        .name("tp-confirm-counter")
        .description("How many instances of TeleportConfirm before a reset.")
        .defaultValue(20)
        .min(0)
        .sliderMax(1024)
        .build()
    );

    private final Setting<AntiKickMode> antiKickMode = sgAntiKick.add(new EnumSetting.Builder<AntiKickMode>()
        .name("anti-kick-mode")
        .description("The mode for anti kick.")
        .defaultValue(AntiKickMode.Packet)
        .build()
    );

    private final Setting<Integer> delay = sgAntiKick.add(new IntSetting.Builder()
        .name("delay")
        .description("The amount of delay, in ticks, between toggles in normal mode.")
        .defaultValue(80)
        .min(1)
        .max(5000)
        .sliderMax(200)
        .build()
    );

    private final Setting<Integer> offTime = sgAntiKick.add(new IntSetting.Builder()
        .name("off-time")
        .description("The amount of delay, in ticks, that Flight is toggled off for in normal mode.")
        .defaultValue(5)
        .min(1)
        .max(20)
        .sliderMax(10)
        .build()
    );

    private int delayLeft = delay.get();
    private int offLeft = offTime.get();
    private int tpConfirms = tpConfirmCounter.get();
    private boolean flip;
    private float lastYaw;

    public TanukiPacketFly() {
        super(Tanuki.CATEGORY, "tanuki-packet-fly", "FLYYYY! No Fall is recommended with this module.");
    }

    @Override
    public void onActivate() {
        delayLeft = delay.get();
        offLeft = offTime.get();
        tpConfirms = tpConfirmCounter.get();
        flip = false;
        lastYaw = 0;
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        tpConfirms++;

        float currentYaw = mc.player.getYaw();
        if (mc.player.fallDistance >= 3f && currentYaw == lastYaw && mc.player.getVelocity().length() < 0.003d) {
            mc.player.setYaw(mc.player.getYaw() + (flip ? 1 : -1));
            flip = !flip;
        }
        lastYaw = currentYaw;

        if(tpConfirms >= tpConfirmCounter.get()) {
            tpConfirms = 0;
        }
    }

    @EventHandler
    private void onPostTick(TickEvent.Post event) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;
        if (antiKickMode.get() == AntiKickMode.Normal && delayLeft > 0) delayLeft --;

        else if (antiKickMode.get() == AntiKickMode.Normal && delayLeft <= 0 && offLeft > 0) {
            offLeft --;
            return;
        }

        else if (antiKickMode.get() == AntiKickMode.Normal && delayLeft <=0 && offLeft <= 0) {
            delayLeft = delay.get();
            offLeft = offTime.get();
        }

        if (mc.player.getYaw() != lastYaw) mc.player.setYaw(lastYaw);

        PlayerEntity p = mc.player;
        if (mc.options.keyForward.isPressed()) {
            Vec3d forward = Vec3d.fromPolar(0, mc.player.getYaw());
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.getX() + forward.x * packetFlySpeed.get(), p.getY(), p.getZ() + forward.z * packetFlySpeed.get(), false));
        }

        if (mc.options.keyBack.isPressed()) {
            Vec3d forward = Vec3d.fromPolar(0, p.getYaw() - 180);
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.getX() + forward.x * packetFlySpeed.get(), p.getY(), p.getZ() + forward.z * packetFlySpeed.get(), false));
        }

        if (mc.options.keyLeft.isPressed()) {
            Vec3d forward = Vec3d.fromPolar(0, p.getYaw() - 90);
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.getX() + forward.x * packetFlySpeed.get(), p.getY(), p.getZ() + forward.z * packetFlySpeed.get(), false));
        }

        if (mc.options.keyRight.isPressed()) {
            Vec3d forward = Vec3d.fromPolar(0, p.getYaw() - 270);
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.getX() + forward.x * packetFlySpeed.get(), p.getY(), p.getZ() + forward.z * packetFlySpeed.get(), false));
        }

        if (mc.options.keyJump.isPressed()) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.getX(), p.getY() + 0.05 * verticalPacketSpeed.get(), p.getZ(), false));
        }

        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.getX(), p.getY() - 0.06, p.getZ(), false));

        mc.getNetworkHandler().sendPacket(new TeleportConfirmC2SPacket(tpConfirms - 1));
        mc.getNetworkHandler().sendPacket(new TeleportConfirmC2SPacket(tpConfirms));
        mc.getNetworkHandler().sendPacket(new TeleportConfirmC2SPacket(tpConfirms + 1));
    }

}
