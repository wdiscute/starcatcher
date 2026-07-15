package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.data.MessagesSavedData;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FishMessagesModifier extends AbstractCatchModifier
{
    private final float chance;

    public static final MapCodec<FishMessagesModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("chance").forGetter(o -> o.chance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, FishMessagesModifier::new));

    public FishMessagesModifier(float chance, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
    }

    @Override
    public void onAdd(FishingBobEntity fishingBobEntity)
    {
        super.onAdd(fishingBobEntity);
    }

    @Override
    public Pair<FishProperties, ResourceLocation> forceSelectFishIfNoNonFishAvailable(FishingBobEntity fbe)
    {
        //if passes the chance
        if (fbe.level().random.nextFloat() > chance)
            return null;

        List<Message> messages = MessagesSavedData.get(((ServerLevel) fbe.level())).getMessages();

        //if there are any messages
        List<Message> list = messages.stream().filter(o -> o.dimension().equals(fbe.level().dimension().location()) && !o.sender().equals(fbe.player.getUUID())).toList();

        if (!list.isEmpty())
        {

            //get random message
            Message message = list.get(fbe.getRandom().nextInt(list.size()));

            //remove message from server saved data
            MessagesSavedData.get(((ServerLevel) fbe.level())).removeMessage(message);

            //save message to the stack
            ItemStack is = new ItemStack(SCItems.MESSAGE_IN_A_BOTTLE.get());
            SCDataComponents.set(is, SCDataComponents.MESSAGE, message);

            //add message caught to data attachment to display on guide
            List<Message> messagesCaught = new ArrayList<>(SCDataAttachments.get(fbe.player, SCDataAttachments.MESSAGES_CAUGHT));
            messagesCaught.add(message);
            SCDataAttachments.set(fbe.player, SCDataAttachments.MESSAGES_CAUGHT, messagesCaught);

            //return custom FP for the message-in-a-bottle
            return Pair.of(FishProperties.empty().withFish(new MaybeStack(is))
                    .withDifficulty(Difficulty.TRASH), Starcatcher.MISSINGNO);
        }

        return null;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("fish_messages");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
