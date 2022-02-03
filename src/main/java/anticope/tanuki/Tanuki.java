package anticope.tanuki;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import java.lang.invoke.MethodHandles;
import anticope.tanuki.modules.*;
import net.minecraft.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Tanuki extends MeteorAddon {
	public static final Logger LOG = LogManager.getLogger();
	public static final Category CATEGORY = new Category("Tanuki", Items.BROWN_WOOL.getDefaultStack());

	@Override
	public void onInitialize() {
		LOG.info("Initializing Tanuki");

		// Required when using @EventHandler
		MeteorClient.EVENT_BUS.registerLambdaFactory("anticope.tanuki", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

		Modules modules = Modules.get();
		modules.add(new AntiCrystal());
		modules.add(new AntiCrystalPhase());
        modules.add(new AntiDrop());
		modules.add(new AutoGriffer());
		modules.add(new BedrockWalk());
		modules.add(new Confetti());
		modules.add(new FuckedDetector());
		modules.add(new PauseOnUnloaded());
	}

	@Override
	public void onRegisterCategories() {
		Modules.registerCategory(CATEGORY);
	}
}
