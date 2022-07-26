package anticope.tanuki;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import anticope.tanuki.modules.*;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Items;
import org.slf4j.Logger;


public class Tanuki extends MeteorAddon {
	public static final Logger LOG = LogUtils.getLogger();
	public static final Category CATEGORY = new Category("Tanuki", Items.BROWN_WOOL.getDefaultStack());

	@Override
	public void onInitialize() {
		LOG.info("Initializing Tanuki");

		Modules modules = Modules.get();
		modules.add(new AntiCrystal());
		modules.add(new AntiCrystalPhase());
		modules.add(new AutoGriffer());
		modules.add(new BedrockWalk());
		modules.add(new Confetti());
		modules.add(new FuckedDetector());
		modules.add(new PauseOnUnloaded());
        modules.add(new TanukiPacketFly());
	}

	@Override
	public void onRegisterCategories() {
		Modules.registerCategory(CATEGORY);
	}

    @Override
    public String getPackage() {
        return "anticope.tanuki";
    }

    @Override
    public String getWebsite() {
        return "https://github.com/AntiCope/tanuki";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("AntiCope", "tanuki");
    }

    @Override
    public String getCommit() {
        String commit = FabricLoader
            .getInstance()
            .getModContainer("tanuki")
            .get().getMetadata()
            .getCustomValue("github:sha")
            .getAsString();
        return commit.isEmpty() ? null : commit.trim();
    }
}
