package com.wdiscute.starcatcher.registry;

import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishApi;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.AdjustLureTimeModifier;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.ExtraGoldenChanceModifier;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.NeverLoseModifier;
import net.mcexpanded.fancytabsections.FancyTabSections;
import net.mcexpanded.fancytabsections.Section.SectionAnimatedTextured;
import net.mcexpanded.fancytabsections.Section.SectionColored;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;
import java.util.List;

public interface SCCreativeModeTabs
{
    static void register(IEventBus eventBus)
    {
        //register creative mode tab
        FancyTabSections.registerCreativeModeTab(eventBus, Starcatcher.rl("starcatcher"), SCItems.ROD::toStack);

        //Must Have
        FancyTabSections.addSection(Starcatcher.rl("starcatcher"),
                new SectionAnimatedTextured(Starcatcher.rl("must_have"))
                        .setFrames(9)
                        .setFrameTimeInMS(200)
                        .setTextOutline(SCColors.BLACK)
                        .setTitle(Component.empty())
                        .setCollapsible(false)
                        .add(SCItems.ROD)
                        .add(() ->
                        {
                            ItemStack devRod = SCItems.ICEBORN_ROD.toStack();
                            SCDataComponents.set(devRod, SCDataComponents.MODIFIERS, List.of(
                                    new NeverLoseModifier(""),
                                    new AdjustLureTimeModifier(0.05f, 0.05f, 1f, "")
                            ));
                            return devRod;
                        })
                        .add(() ->
                        {
                            ItemStack devRod = SCItems.OBSIDIAN_ROD.toStack();
                            SCDataComponents.set(devRod, SCDataComponents.MODIFIERS, List.of(
                                    new ExtraGoldenChanceModifier(1, false, ""),
                                    new AdjustLureTimeModifier(0.05f, 0.05f, 1f, "")
                            ));
                            return devRod;
                        })
                        .add(SCItems.GUIDE)
                        .add(SCBlocks.STAND)
                        .add(SCBlocks.DISPLAY)
                        .add(SCBlocks.TACKLE_BOX)
                        .add(SCBlocks.AQUARIUM)
                        .add(SBBlocks.SELLING_BIN)
        );


        //hooks & bobbers
        FancyTabSections.addSection(Starcatcher.rl("starcatcher"),
                new SectionColored(Starcatcher.rl("hooks_bobbers"))
                        .setBannerColor(SCColors.BANNER_COLOR)

                        .addItemTag(SCTags.HOOKS)
                        .addItemTag(SCTags.BOBBERS)
                        .addItemTag(SCTags.BAITS)
        );

        //cosmetics
        FancyTabSections.addSection(Starcatcher.rl("starcatcher"),
                new SectionColored(Starcatcher.rl("cosmetics"))
                        .setBannerColor(SCColors.BANNER_COLOR)
                        .add((d) -> SCItems.RODS_REGISTRY.getEntries().stream().map(o -> o.getDelegate().value().getDefaultInstance()).toList())
                        .add((d) -> SCItems.TEMPLATES_REGISTRY.getEntries().stream().map(o -> o.getDelegate().value().getDefaultInstance()).toList())
                        .add((d) -> SCBlocks.HATS.getEntries().stream().map(o -> o.get().asItem().getDefaultInstance()).toList())
        );

        //tackle boxes
        FancyTabSections.addSection(Starcatcher.rl("starcatcher"),
                new SectionColored(Starcatcher.rl("tackle_boxes"))
                        .setBannerColor(SCColors.BANNER_COLOR)
                        .add((d) -> SCBlocks.TACKLE_BOXES.getEntries().stream().map(o -> o.get().asItem().getDefaultInstance()).toList())
        );

        //Trophies & Secrets
        FancyTabSections.addSection(Starcatcher.rl("starcatcher"),
                new SectionColored(Starcatcher.rl("trophies"))
                        .setBannerColor(SCColors.BANNER_COLOR)

                        .add(SCBlocks.TROPHY_COPPER)
                        .add(SCBlocks.TROPHY_IRON)
                        .add(SCBlocks.TROPHY_GOLD)
                        .add(SCBlocks.TROPHY_EMERALD)
                        .add(SCBlocks.TROPHY_DIAMOND)
                        .add(SCBlocks.TROPHY_OF_THE_OLDER_ANGLER)

                        .add(SCItems.LETTER)
                        .add(SCItems.BOTTLED_LETTER)
                        .add(SCItems.MESSAGE_IN_A_BOTTLE)
                        .add(SCItems.BROKEN_BOTTLE)
                        .add(SCItems.MESSAGE)

                        //secret messages
                        .add((registryAccess) ->
                                {
                                    List<ItemStack> list = new ArrayList<>();
                                    for (FishProperties fp : FishApi.getMessages(registryAccess))
                                    {
                                        ItemStack stack = fp.catchInfo().fish().toStack();

                                        if (stack.has(SCDataComponents.MESSAGE.get()))
                                        {
                                            SCDataComponents.set(stack, SCDataComponents.MESSAGE, stack.get(SCDataComponents.MESSAGE.get()));
                                            list.add(stack);
                                        }
                                    }
                                    return list;
                                }
                        )
        );

        //Fish
        FancyTabSections.addSection(Starcatcher.rl("starcatcher"),
                new SectionColored(Starcatcher.rl("fish"))
                        .setBannerColor(SCColors.BANNER_COLOR)
                        .add((d) -> SCItems.BUCKETABLE_FISHES_REGISTRY.getEntries().stream().map(o -> o.getDelegate().value().getDefaultInstance()).toList())
                        .add((d) -> SCItems.NON_BUCKETABLE_FISH_REGISTRY.getEntries().stream().map(o -> o.getDelegate().value().getDefaultInstance()).toList())
                        .add((d) -> SCItems.NON_FISH_FISH_REGISTRY.getEntries().stream().map(o -> o.getDelegate().value().getDefaultInstance()).toList())
                        .add(SCItems.LAVA_CRAB_CLAW)
                        .add(SCBlocks.CLAM)
                        .add(SCBlocks.CONCH)
        );

        //Miscellaneous
        FancyTabSections.addSection(Starcatcher.rl("starcatcher"),
                new SectionColored(Starcatcher.rl("miscellaneous"))
                        .setBannerColor(SCColors.BANNER_COLOR)
                        .add(SCItems.BOOT)
                        .add(SCItems.MOSSY_BOOT)
                        .add(SCItems.DRIED_SEAWEED)
                        .add(SCItems.LAVA_CRAB_CLAW)

                        .add(SCItems.FISH_BONES)

                        .add(SCItems.FISH_RADAR)
                        .add(SCItems.PEARL)
                        .add(SCItems.STARCATCHER_TWINE)
                        .add(SCItems.MISSINGNO)
                        .add(SCItems.UNKNOWN_FISH)
                        .add(SCItems.STARCAUGHT_BUCKET)
                        .add(SCItems.STARCAUGHT_LAVA_BUCKET)
                        .add(SCItems.STARCAUGHT_FISH)
                        .add(SCItems.COOKED_STARCAUGHT_FISH)
        );
    }
}
