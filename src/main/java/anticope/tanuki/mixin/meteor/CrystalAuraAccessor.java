package anticope.tanuki.mixin.meteor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.player.PlayerEntity;

import meteordevelopment.meteorclient.systems.modules.combat.CrystalAura;

@Mixin(CrystalAura.class)
public interface CrystalAuraAccessor {
    @Accessor("bestTarget")
    PlayerEntity bestTarget();

    @Accessor("bestTargetDamage")
    double bestTargetDamage();
}
