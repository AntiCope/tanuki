package anticope.tanuki.modules.hud;

import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.elements.MeteorTextHud;
import meteordevelopment.meteorclient.systems.hud.elements.TextHud;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.starscript.value.Value;

public class ProfileHud {
    public static final HudElementInfo<TextHud>.Preset PROFILE = addPreset("profile", "Profile: #1{last_loaded_profile}", 20);

    private static HudElementInfo<TextHud>.Preset addPreset(String title, String text, int updateDelay) {
        return MeteorTextHud.INFO.addPreset(title, (textHud) -> {
            if (text != null) {
                textHud.text.set(text);
            }

            if (updateDelay != -1) {
                textHud.updateDelay.set(updateDelay);
            }
        });
    }

    public static String lastLoadedProfile = "-";

    @PreInit
    public static void init() {
        MeteorStarscript.ss.set("last_loaded_profile", () -> Value.string(lastLoadedProfile));
    }
}
