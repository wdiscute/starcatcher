package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxMenu;
import com.wdiscute.starcatcher.registry.items.rod.FishingRodMenu;
import com.wdiscute.starcatcher.tournament.StandMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.registry.DeferredHolder;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;
import net.nikdo53.neobackports.utils.IMenuTypeExtension;

import java.util.function.Supplier;

public class SCMenuTypes
{
    public static final DeferredRegisterTyped<MenuType<?>> MENUS =
            DeferredRegisterTyped.create(Registries.MENU, Starcatcher.MOD_ID);

    public static final Supplier<MenuType<FishingRodMenu>> FISHING_ROD_MENU =
            registerMenuType("fishing_rod_menu", FishingRodMenu::new);

    public static final Supplier<MenuType<StandMenu>> STAND_MENU =
            registerMenuType("stand_menu", StandMenu::new);

    public static final Supplier<MenuType<TackleBoxMenu>> TACKLE_BOX =
            registerMenuType("tackle_box.png", TackleBoxMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                               IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
