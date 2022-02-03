package anticope.tanuki.modules;

import anticope.tanuki.Tanuki;
import meteordevelopment.meteorclient.events.entity.DropItemsEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class AntiDrop extends Module {

    private final SettingGroup General = settings.getDefaultGroup();

    private final Setting<List<Item>> items = General.add(new ItemListSetting.Builder()
        .name("items")
        .description("The items to prevent from being dropped.")
        .defaultValue(new ArrayList<>())
        .build()
    );

    public AntiDrop() {
        super(Tanuki.CATEGORY, "anti-drop", "Stops you from dropping specified items with Q.");
    }

    @EventHandler
    private void onDropItem(DropItemsEvent dropItemEvent) {
        assert mc.player != null;

        int hotbarSlot = mc.player.getInventory().selectedSlot;

        Item item = mc.player.getInventory().getStack(hotbarSlot).getItem();

        if (items.get().contains(item)) {
            dropItemEvent.cancel();
        }
    }
}
