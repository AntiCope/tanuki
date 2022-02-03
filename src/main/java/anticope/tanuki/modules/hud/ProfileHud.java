package anticope.tanuki.modules.hud;

import net.minecraft.nbt.NbtCompound;

import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.hud.modules.DoubleTextHudElement;
import meteordevelopment.meteorclient.systems.hud.modules.HudElement;

public class ProfileHud extends DoubleTextHudElement {

    public ProfileHud(HUD hud) {
        super(hud, "profile", "Displays the last loaded profile.", "Profile: ");
    }

    public static String lastLoadedProfile = "-";

    @Override
    protected String getRight() {
        return lastLoadedProfile;
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = super.toTag();
        tag.putString("lastProfile", "lastLoadedProfile");
        return tag;
    }

    @Override
    public HudElement fromTag(NbtCompound tag) {
        lastLoadedProfile = tag.getString("lastProfile");
        return super.fromTag(tag);
    }
}
