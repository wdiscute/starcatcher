package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.TrophyProperties;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

@EmiEntrypoint
public class StarcatcherEmiPlugin implements EmiPlugin
{
    public static final ResourceLocation MY_SPRITE_SHEET = Starcatcher.rl("textures/gui/emi_simplified_textures.png");
    public static final EmiStack MY_WORKSTATION = EmiStack.of(ModItems.ROD);
    public static final EmiRecipeCategory STARCATCHER_CATEGORY
            = new EmiRecipeCategory(
            Starcatcher.rl("fishing"),
            MY_WORKSTATION);

    @Override
    public void register(EmiRegistry registry)
    {
        // Tell EMI to add a tab for your category
        registry.addCategory(STARCATCHER_CATEGORY);

        // Add all the workstations your category uses
        registry.addWorkstation(STARCATCHER_CATEGORY, MY_WORKSTATION);

        Registry<FishProperties> fps = Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY);

        for (FishProperties fp : fps)
        {
            registry.addRecipe(new StarcatcherEmiRecipe(fps.getKey(fp), fp));
        }


        Registry<TrophyProperties> trophies = Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.TROPHY_REGISTRY);

        for (TrophyProperties fp : trophies)
        {
            if(fp.trophyType().equals(TrophyProperties.TrophyType.TROPHY) || fp.trophyType().equals(TrophyProperties.TrophyType.SECRET))
                registry.addRecipe(new StarcatcherEmiRecipe(trophies.getKey(fp), fp));
        }
    }
}
