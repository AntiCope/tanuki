package anticope.tanuki.modules;

import anticope.tanuki.Tanuki;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerManager;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class AutoGriffer extends Module {

    private final SettingGroup sgPlace = settings.createGroup("Place");

    public AutoGriffer() {
        super(Tanuki.CATEGORY, "auto-griffer", "Places lava on people.");
    }

    private final Setting<Double> range = sgPlace.add(new DoubleSetting.Builder()
        .name("range")
        .description("How far away the target can be to be affected.")
        .defaultValue(4)
        .min(0)
        .build());

    private static PlayerEntity target;
    private int stage;

    @Override
    public void onActivate() {
        target = null;
        stage = 1;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {

        if (target != null && (mc.player.distanceTo(target) > range.get() || !target.isAlive()))
            target = null;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || !Friends.get().shouldAttack(player) || !player.isAlive()
                || mc.player.distanceTo(player) > range.get())
                continue;

            if (target == null)
                target = player;
            else if (mc.player.distanceTo(target) > mc.player.distanceTo(player))
                target = player;
        }

        if (target == null) {
            FakePlayerManager.forEach(player -> {
                if (!Friends.get().shouldAttack(player) || !player.isAlive()
                    || mc.player.distanceTo(player) > range.get())
                    return;

                if (target == null)
                    target = player;
                else if (mc.player.distanceTo(target) > mc.player.distanceTo(player))
                    target = player;
            });
        }
        if (target == null) {
            stage = 1;
        }
        if (target != null) {
            if (mc.player.canSee(target)) {
                switch (stage) {
                    case 1:
                        FindItemResult lava = InvUtils.find(Items.LAVA_BUCKET);
                        if (!lava.found()) {
                            toggle();
                            return;
                        } else {
                            BlockPos anvil1 = target.getBlockPos().add(0, (int) 1.3, 0);
                            mc.player.setPitch((float) Rotations.getPitch(anvil1));
                            mc.player.setYaw((float) Rotations.getYaw(anvil1));
                            InvUtils.swap(lava.slot(), false);
                        }
                        stage++;
                        break;
                    case 2:
                        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                        toggle();
                        break;
                }
            } else {
                toggle();
            }
        }
    }

    @Override
    public String getInfoString() {
        if (target != null && target instanceof PlayerEntity)
            return target.getEntityName();
        if (target != null)
            return target.getType().getName().getString();
        return null;
    }

}
