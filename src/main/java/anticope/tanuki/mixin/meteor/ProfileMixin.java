package anticope.tanuki.mixin.meteor;

import anticope.tanuki.modules.hud.ProfileHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import meteordevelopment.meteorclient.systems.profiles.Profile;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Profile.class)
public class ProfileMixin {
    @Inject(method = "load()V", at = @At("TAIL"), remap = false)
    private void onLoad(CallbackInfo ci) {
        Profile thisObject = (Profile) (Object) this;
        ProfileHud.lastLoadedProfile = thisObject.name.get();
    }
}
