package com.wdiscute.starcatcher.secretnotes;

import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.io.SCDataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NoteContainer extends Item
{
    public final SecretNote.Note note;
    final Item turnsInto;

    public NoteContainer(SecretNote.Note noteName)
    {
        super(new Properties().stacksTo(1));
        this.note = noteName;
        this.turnsInto = SCItems.BROKEN_BOTTLE.get();
    }

    public NoteContainer(Properties p, SecretNote.Note noteName)
    {
        super(p);
        this.note = noteName;
        this.turnsInto = SCItems.BROKEN_BOTTLE.get();
    }

    public NoteContainer(Properties p, Item turnsInto, SecretNote.Note noteName)
    {
        super(p);
        this.note = noteName;
        this.turnsInto = turnsInto;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand)
    {
        //give note
        ItemStack is = new ItemStack(SCItems.SECRET_NOTE.get());
        SCDataComponents.set(is, SCDataComponents.SECRET_NOTE, note);
        player.addItem(is);

        //replace with broken bottle
        player.setItemInHand(usedHand, new ItemStack(turnsInto));

        return InteractionResult.SUCCESS;
    }
}
