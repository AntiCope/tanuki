package anticope.tanuki.modules;

import com.google.common.collect.Streams;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;

import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import anticope.tanuki.Tanuki;

import java.util.Comparator;
import java.util.Optional;

public class AntiCrystal extends Module {

    public enum Mode {
        PressurePlate,
        Button
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("The mode at which AntiCrystal operates.")
            .defaultValue(Mode.PressurePlate)
            .build()
    );

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
            .name("range")
            .description("The range to place Pressure Plates/Buttons.")
            .min(1)
            .max(10)
            .defaultValue(1)
            .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
            .name("rotate")
            .description("Rotate.")
            .defaultValue(true)
            .build()
    );

    public AntiCrystal() {
        super(Tanuki.CATEGORY, "anti-crystal", "Stops End Crystals from doing damage to you.");
    }

    private static final Item[] BUTTONS = { Items.ACACIA_BUTTON, Items.BIRCH_BUTTON, Items.CRIMSON_BUTTON, Items.DARK_OAK_BUTTON, Items.JUNGLE_BUTTON, Items.OAK_BUTTON, Items.POLISHED_BLACKSTONE_BUTTON, Items.SPRUCE_BUTTON, Items.STONE_BUTTON, Items.WARPED_BUTTON };
    private static final Item[] PLATES =  { Items.ACACIA_PRESSURE_PLATE, Items.BIRCH_PRESSURE_PLATE, Items.CRIMSON_PRESSURE_PLATE, Items.DARK_OAK_PRESSURE_PLATE, Items.JUNGLE_PRESSURE_PLATE, Items.OAK_PRESSURE_PLATE, Items.POLISHED_BLACKSTONE_PRESSURE_PLATE, Items.SPRUCE_PRESSURE_PLATE, Items.STONE_PRESSURE_PLATE, Items.WARPED_PRESSURE_PLATE};

    @EventHandler
    private void onTick(TickEvent.Post event) {
        assert mc.world != null;
        assert mc.player != null;

        Optional<EndCrystalEntity> crystalTarget = Streams.stream(mc.world.getEntities())
                .filter(e -> (e instanceof EndCrystalEntity))
                .filter(e -> e.distanceTo(mc.player) <= range.get() * 2)
                .filter(e -> mc.world.getBlockState(e.getBlockPos()).isAir())
                .min(Comparator.comparingDouble(o -> o.distanceTo(mc.player)))
                .map(e -> (EndCrystalEntity) e);

        crystalTarget.ifPresent(crystal -> {

            if (mode.get() == Mode.PressurePlate) {
                FindItemResult result = InvUtils.find(PLATES);
                if (!result.found()) return;
                BlockUtils.place(crystal.getBlockPos(), result, rotate.get(), 100);
            } else if (mode.get() == Mode.Button) {
                FindItemResult result = InvUtils.find(BUTTONS);
                if (!result.found()) return;
                BlockUtils.place(crystal.getBlockPos(), result, rotate.get(), 100);
            }
        });

    }
}

