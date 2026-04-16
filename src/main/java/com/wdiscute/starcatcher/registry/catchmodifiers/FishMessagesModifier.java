package com.wdiscute.starcatcher.registry.catchmodifiers;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.MessagesSavedData;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.secretnotes.LetterItem;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FishMessagesModifier extends AbstractCatchModifier
{
    private boolean messageFished = false;

    @Override
    public void afterChoosingTheCatch(List<FishProperties> immutableAvailable)
    {
        if (U.r.nextDouble() > SCConfig.FISH_PLAYER_MESSAGES_CHANCE.get())
        {
            return;
        }
        List<LetterItem.Message> messages = MessagesSavedData.get(((ServerLevel) instance.level())).getMessages();

        //if there are any messages
        List<LetterItem.Message> list = messages.stream().filter(o -> o.dimension().equals(instance.level().dimension().location()) && !o.sender().equals(instance.player.getUUID())).toList();

        if (!list.isEmpty())
        {
            messageFished = true;
            ItemStack is = new ItemStack(SCItems.MESSAGE_IN_A_BOTTLE.get());

            LetterItem.Message message = list.get(U.r.nextInt(list.size()));
            MessagesSavedData.get(((ServerLevel) instance.level())).removeMessage(message);

            SCDataComponents.set(is, SCDataComponents.MESSAGE, message);

            //make ItemEntities for fish item stack
            ItemEntity messageInABottle = new ItemEntity(instance.level(), instance.position().x, instance.position().y + 1.2f, instance.position().z, is);

            //assign delta movement so fish flies towards player
            double x = Mth.clamp((instance.player.position().x - instance.position().x) / 25, -1, 1);
            double y = Mth.clamp((instance.player.position().y - instance.position().y) / 20, -1, 1);
            double z = Mth.clamp((instance.player.position().z - instance.position().z) / 25, -1, 1);
            Vec3 vec3 = new Vec3(x, 0.7 + y, z);
            messageInABottle.setDeltaMovement(vec3);
            instance.level().addFreshEntity(messageInABottle);
        }
    }

    @Override
    public boolean shouldCancelBeforeSkipsMinigameCheck()
    {
        return messageFished;
    }
}
