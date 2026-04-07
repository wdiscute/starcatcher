package com.wdiscute.starcatcher.bobberentity;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.*;
import com.wdiscute.starcatcher.io.network.FishingStartedPayload;
import com.wdiscute.starcatcher.registry.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.catchmodifiers.FishMessagesModifier;
import com.wdiscute.starcatcher.registry.catchmodifiers.SCCatchModifiers;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCParticles;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FishingBobEntity extends Projectile
{
    private static final Logger log = LoggerFactory.getLogger(FishingBobEntity.class);
    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(FishingBobEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> VOID = SynchedEntityData.defineId(FishingBobEntity.class, EntityDataSerializers.BOOLEAN);

    public final Player player;
    private FishHookState currentState;
    public FishProperties fpToFish;
    public ItemStack rod = ItemStack.EMPTY;
    public final List<AbstractCatchModifier> modifiers;

    public boolean survivesLava = false;

    public int minTicksToFish;
    public int maxTicksToFish;
    public float chanceToFishEachTick;

    public int timeBiting;

    public int ticksInFluid;

    boolean voidHook = false;

    enum FishHookState
    {
        FLYING,
        BOBBING,
        BITING,
        FISHING
    }

    //client
    public FishingBobEntity(EntityType<? extends FishingBobEntity> entityType, Level level)
    {
        super(entityType, level);
        this.player = getPlayer();
        this.modifiers = SCCatchModifiers.getCatchModifiers(player);
        modifiers.forEach(acm -> acm.onAdd(this));
    }

    @OnlyIn(Dist.CLIENT)
    public static Player getPlayer()
    {
        return Minecraft.getInstance().player;
    }

    //server
    public FishingBobEntity(Level level, Player player, ItemStack rod)
    {
        super(SCEntities.FISHING_BOB.get(), level);

        this.setOwner(player);
        this.player = player;
        this.rod = rod;
        this.modifiers = SCCatchModifiers.getCatchModifiers(player);

        //add fish messages modifier
        modifiers.add(new FishMessagesModifier());

        SingleStackContainer ssc = SCDataComponents.getOrDefault(rod, SCDataComponents.HOOK, SingleStackContainer.empty());
        voidHook = BuiltInRegistries.ITEM.getKey(ssc.stack().getItem()).equals(U.rl("tide", "void_fishing_hook"));

        entityData.set(VOID, voidHook);

        survivesLava = SCDataComponents.getOrDefault(rod, SCDataComponents.NETHERITE_UPGRADE, false) || modifiers.stream().anyMatch(AbstractCatchModifier::survivesLava);

        minTicksToFish = SCConfig.BASE_MIN_TICKS_TO_FISH.getAsInt();
        maxTicksToFish = SCConfig.BASE_MAX_TICKS_TO_FISH.getAsInt();
        chanceToFishEachTick = (float) SCConfig.BASE_CHANCE_TO_FISH.getAsDouble();

        //modify base chances
        for (AbstractCatchModifier acm : modifiers)
        {
            minTicksToFish = acm.adjustMinTicksToFish(minTicksToFish);
            maxTicksToFish = acm.adjustMaxTicksToFish(maxTicksToFish);
            chanceToFishEachTick = acm.adjustChanceToFishEachTick(chanceToFishEachTick);
        }

        minTicksToFish = Math.max(1, minTicksToFish);
        maxTicksToFish = Math.max(1, maxTicksToFish);

        //trigger onBobSummon
        modifiers.forEach(acm -> acm.onAdd(this));

        float playerXRot = player.getXRot();
        float playerYRot = player.getYRot();
        float f2 = Mth.cos(-playerYRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-playerYRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-playerXRot * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-playerXRot * ((float) Math.PI / 180F));
        double d0 = player.getX() - (double) f3 * 0.3;
        double d1 = player.getEyeY();
        double d2 = player.getZ() - (double) f2 * 0.3;
        this.moveTo(d0, d1, d2, playerYRot, playerXRot);
        Vec3 vec3 = new Vec3(-f3, Mth.clamp(-(f5 / f4), -5.0F, 5.0F), -f2);
        double d3 = vec3.length();
        vec3 = vec3.multiply(0.6 / d3 + this.random.triangle(0.5F, 0.0103365), 0.6 / d3 + this.random.triangle(0.5F, 0.0103365), 0.6 / d3 + this.random.triangle(0.5F, 0.0103365));
        this.setDeltaMovement(vec3);
        this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) 180.0F / (double) (float) Math.PI));
        this.setXRot((float) (Mth.atan2(vec3.y, vec3.horizontalDistance()) * (double) 180.0F / (double) (float) Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        if (!level.isClientSide)
            SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB).setUuid(player, this.uuid);

        currentState = FishHookState.FLYING;
    }

    public void reel()
    {
        modifiers.forEach(AbstractCatchModifier::onReelStart);

        //server only
        List<FishProperties> available = new ArrayList<>();

        //if no trophy is available, get chances of getting each fish
        for (FishProperties fp : level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            int chance = fp.calculateChance(this, level(), rod, AbstractFishRestriction.Context.FISHING);

            for (int i = 0; i < chance; i++)
            {
                available.add(fp);
            }
        }

        //if no fish is available, reset player fishing data and award nothing
        if (available.isEmpty())
        {
            player.displayClientMessage(Component.translatable("gui.starcatcher.reel_no_fish"), true);
            this.kill();
        }

        //trigger modifiers for which fish to get based on available
        for (AbstractCatchModifier acm : modifiers)
        {
            available = acm.modifyAvailablePool(available);
        }

        //get random fish from available pool
        fpToFish = available.get(random.nextInt(available.size()));

        //trigger modifiers for which fish to get based on available
        List<FishProperties> immutableAvailable = List.copyOf(available);
        modifiers.forEach(acm -> acm.afterChoosingTheCatch(immutableAvailable));

        //should cancel to prevent normal minigame/item fished (vanilla bobber & messages)
        if (modifiers.stream().anyMatch(AbstractCatchModifier::shouldCancelBeforeSkipsMinigameCheck))
        {
            this.kill();
            return;
        }

        //load treasure itemstack
        fpToFish = fpToFish.loadTreasure(((ServerPlayer) player));

        //skips minigame if (skipsminigame() or server config of minigame enabled = false) OR any modifier wants to
        if ((fpToFish.skipMinigame() || !SCConfig.ENABLE_MINIGAME.get())
                || modifiers.stream().anyMatch(m -> m.forceSkipMinigame(SCConfig.ENABLE_MINIGAME.get())))
        {
            FishProperties.spawnFishFromPlayerFishing(((ServerPlayer) player), 0, false, false, 0);
        }
        else
        {
            //send fishing minigame payload to client with FP
            FishingStartedPayload payload = new FishingStartedPayload(fpToFish, rod);
            PacketDistributor.sendToPlayer(((ServerPlayer) player), payload);
        }
    }

    private boolean shouldStopFishing(Player player)
    {
        if (level().isClientSide) return false;

        //if any modifier wants to stop fishing
        if (modifiers.stream().anyMatch(AbstractCatchModifier::shouldStopFishing)) return true;

        boolean holdingRod = player.getMainHandItem().is(SCTags.RODS)
                || player.getOffhandItem().is(SCTags.RODS);

        if (!player.isRemoved() && player.isAlive() && holdingRod && !(this.distanceToSqr(player) > 1024))
        {
            return false;
        }
        else
        {
            this.kill();
            return true;
        }
    }

    @Override
    public boolean fireImmune()
    {
        return survivesLava;
    }

    @Override
    public void lavaHurt()
    {
        super.lavaHurt();
        if (!survivesLava && !level().isClientSide)
        {
            kill();
        }
    }

    @Override
    public void kill()
    {
        SCDataAttachments.remove(player, SCDataAttachments.FISHING_BOB);
        super.kill();
    }

    @Override
    public void tick()
    {
        super.tick();

        voidHook = entityData.get(VOID);

        if (!level().isClientSide)
        {
            if (currentState == FishHookState.FLYING) entityData.set(STATE, 1);
            if (currentState == FishHookState.BOBBING) entityData.set(STATE, 2);
            if (currentState == FishHookState.BITING) entityData.set(STATE, 3);
            if (currentState == FishHookState.FISHING) entityData.set(STATE, 4);
        }
        else
        {
            if (entityData.get(STATE) == 1) currentState = FishHookState.FLYING;
            if (entityData.get(STATE) == 2) currentState = FishHookState.BOBBING;
            if (entityData.get(STATE) == 3) currentState = FishHookState.BITING;
            if (entityData.get(STATE) == 4) currentState = FishHookState.FISHING;
        }

        Player player = ((Player) this.getOwner());
        if (player == null || this.shouldStopFishing(player))
        {
            this.discard();
            if (player != null) SCDataAttachments.remove(player, SCDataAttachments.FISHING_BOB);
        }

        BlockPos blockpos = this.blockPosition();
        FluidState fluid = this.level().getFluidState(blockpos);
        FluidState fluidBellow = this.level().getFluidState(blockpos.below());

        if (this.currentState == FishHookState.FLYING)
        {
            //set voidhook fishing for overworld/nether/end negative offset (based on tide) and always for any other dimension
            ResourceLocation dim = level().dimension().location();
            if (voidHook && position().y < -71 && dim.equals(Level.OVERWORLD.location()))
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;

            if (voidHook && position().y < -5 && dim.equals(Level.NETHER.location()))
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;

            if (voidHook && position().y < 50 && dim.equals(Level.END.location()))
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;

            if (!dim.equals(Level.OVERWORLD.location()) && !dim.equals(Level.NETHER.location()) && !dim.equals(Level.END.location()))
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;


            if (getDeltaMovement().y < 1.2f)
                this.setDeltaMovement(this.getDeltaMovement().add(0, -0.02, 0));

            if (!fluid.isEmpty())
            {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.3, 0.3, 0.3));
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;
                return;
            }
        }

        if (this.currentState == FishHookState.BITING)
        {
            timeBiting++;
            for (int i = 0; i < 5; i++)
            {
                if (level().getFluidState(blockpos).is(Fluids.LAVA))
                    level().addParticle(
                            SCParticles.FISHING_BITING_LAVA.get(),
                            position().x + random.nextFloat() - 0.5,
                            position().y + random.nextFloat() * 0.5 - 0.25,
                            position().z + random.nextFloat() - 0.5,
                            0, 0, 0);
                else
                    level().addParticle(
                            SCParticles.FISHING_BITING.get(),
                            position().x + random.nextFloat() - 0.5,
                            position().y + random.nextFloat() * 0.5 - 0.25,
                            position().z + random.nextFloat() - 0.5,
                            0, 0, 0);
            }

            if (timeBiting > 80)
            {
                SCDataAttachments.remove(player, SCDataAttachments.FISHING_BOB);
                SCTackleSkins.get(level(), rod).onMissed(player);

                ItemStack bait = SCDataComponents.getOrDefault(rod, SCDataComponents.BAIT, new SingleStackContainer(ItemStack.EMPTY)).stack();
                if (!bait.is(Items.BUCKET))
                {
                    bait.shrink(1);
                    SCDataComponents.set(rod, SCDataComponents.BAIT, new SingleStackContainer(bait));
                }

                kill();
            }
        }
        else
        {
            timeBiting = 0;
        }

        //if theres no fluid on block or under, changes to FLYING
        if (fluid.isEmpty() && fluidBellow.isEmpty() && !voidHook)
        {
            if (!level().isClientSide) currentState = FishHookState.FLYING;
        }

        //TODO check for water level instead of just blockstate to make the entity sit better in water
        if (this.currentState == FishHookState.BOBBING || this.currentState == FishHookState.FISHING)
        {
            checkForFish();

            if (!voidHook)
            {
                if (!fluid.isEmpty())
                {
                    setDeltaMovement(this.getDeltaMovement().add(0.0F, 0.01, 0.0F));
                }
                else
                {
                    if (random.nextFloat() > 0.02)
                    {
                        setDeltaMovement(this.getDeltaMovement().add(0.0F, -0.03, 0.0F));
                    }
                    else
                    {
                        setDeltaMovement(this.getDeltaMovement().add(0.0F, -0.01, 0.0F));
                    }
                }
            }
            else
            {
                setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y * 0.9, getDeltaMovement().z);
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        //this.updateRotation();

        if (this.onGround() || this.horizontalCollision)
        {
            this.setDeltaMovement(Vec3.ZERO);
        }

        this.setDeltaMovement(this.getDeltaMovement().scale(0.92));
        this.reapplyPosition();
    }

    public boolean checkBiting()
    {

        if (currentState == FishHookState.BITING)
        {
            currentState = FishHookState.FISHING;
            reel();
            return true;
        }
        else
        {
            return false;
        }
    }

    private void checkForFish()
    {
        if (!level().isClientSide && currentState == FishHookState.BOBBING)
        {
            ticksInFluid++;
            boolean fish = U.r.nextFloat() < chanceToFishEachTick;
            if ((fish || ticksInFluid > maxTicksToFish) && ticksInFluid > minTicksToFish)
            {
                if (SCConfig.SHOW_EXCLAMATION_MARK_PARTICLE.get())
                    ((ServerLevel) level()).sendParticles(
                            SCParticles.FISHING_NOTIFICATION.get(),
                            position().x, position().y + 1, position().z,
                            1, 0, 0, 0, 0);

                this.setPos(position().x, position().y - 0.5f, position().z);
                if (!level().isClientSide) currentState = FishHookState.BITING;
                this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            }
        }


    }

    @Override
    public AABB getBoundingBoxForCulling()
    {
        AABB box = new AABB(-10, -10, -10, 10, 10, 10);
        return box.move(position());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        builder.define(STATE, 0);
        builder.define(VOID, false);
    }
}
