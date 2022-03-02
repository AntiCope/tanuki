package anticope.tanuki;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import java.lang.invoke.MethodHandles;
import anticope.tanuki.modules.*;
import anticope.tanuki.modules.hud.*;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Tanuki extends MeteorAddon {
	public static final Logger LOG = LoggerFactory.getLogger("Tanuki");
	public static final Category CATEGORY = new Category("Tanuki", Items.BROWN_WOOL.getDefaultStack());

	@Override
	public void onInitialize() {
		LOG.info("Initializing Tanuki");

		// Required when using @EventHandler
		MeteorClient.EVENT_BUS.registerLambdaFactory("anticope.tanuki", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

		Modules modules = Modules.get();
		modules.add(new AntiCrystal());
		modules.add(new AntiCrystalPhase());
		modules.add(new AutoGriffer());
		modules.add(new BedrockWalk());
		modules.add(new Confetti());
		modules.add(new FuckedDetector());
		modules.add(new PauseOnUnloaded());
        modules.add(new TanukiPacketFly());

        HUD hud = Systems.get(HUD.class);
        hud.elements.add(new ProfileHud(hud));
	}

	@Override
	public void onRegisterCategories() {
		Modules.registerCategory(CATEGORY);
	}
}
