package com.wdiscute.starcatcher.bob;

import com.wdiscute.starcatcher.*;
import com.wdiscute.starcatcher.io.*;
import com.wdiscute.starcatcher.io.network.FishingPayload;
import com.wdiscute.starcatcher.registry.ModEntities;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FishingBobEntity extends Projectile
{
    private static final Logger log = LoggerFactory.getLogger(FishingBobEntity.class);


    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(FishingBobEntity.class, EntityDataSerializers.INT);


    public final Player player;
    private FishHookState currentState;
    public FishProperties fpToFish;
    public ItemStack rod = ItemStack.EMPTY;
    public ItemStack bobber = ItemStack.EMPTY;
    public ItemStack hook = ItemStack.EMPTY;
    public ItemStack bait = ItemStack.EMPTY;

    int minTicksToFish;
    int maxTicksToFish;
    int chanceToFishEachTick;

    int timeBiting;

    int ticksInFluid;

    enum FishHookState
    {
        FLYING,
        BOBBING,
        BITING,
        FISHING
    }

    public FishingBobEntity(EntityType<? extends FishingBobEntity> entityType, Level level)
    {
        super(entityType, level);
        player = null;
    }

    public FishingBobEntity(Level level, Player player, ItemStack rod)
    {
        super(ModEntities.FISHING_BOB.get(), level);

        this.player = player;
        this.rod = rod;
        this.bobber = rod.get(ModDataComponents.BOBBER).stack().copy();
        this.bait = rod.get(ModDataComponents.BAIT).stack().copy();
        this.hook = rod.get(ModDataComponents.HOOK).stack().copy();

        {
            this.setOwner(player);

            minTicksToFish = 100;
            maxTicksToFish = 300;
            chanceToFishEachTick = 100;

            if (bobber.is(ModItems.IMPATIENT_BOBBER)) chanceToFishEachTick = 20;
            if (bobber.is(ModItems.IMPATIENT_BOBBER)) minTicksToFish = 80;

            float f = player.getXRot();
            float f1 = player.getYRot();
            float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
            float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
            float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
            float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
            double d0 = player.getX() - (double) f3 * 0.3;
            double d1 = player.getEyeY();
            double d2 = player.getZ() - (double) f2 * 0.3;
            this.moveTo(d0, d1, d2, f1, f);
            Vec3 vec3 = new Vec3(-f3, Mth.clamp(-(f5 / f4), -5.0F, 5.0F), -f2);
            double d3 = vec3.length();
            vec3 = vec3.multiply(0.6 / d3 + this.random.triangle(0.5F, 0.0103365), 0.6 / d3 + this.random.triangle(0.5F, 0.0103365), 0.6 / d3 + this.random.triangle(0.5F, 0.0103365));
            this.setDeltaMovement(vec3);
            this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) 180.0F / (double) (float) Math.PI));
            this.setXRot((float) (Mth.atan2(vec3.y, vec3.horizontalDistance()) * (double) 180.0F / (double) (float) Math.PI));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (!level.isClientSide) player.setData(ModDataAttachments.FISHING.get(), this.uuid.toString());

        currentState = FishHookState.FLYING;
    }


    public void reel()
    {
        //server only
        List<FishProperties> available = new ArrayList<>(List.of());

        List<ResourceLocation> data = player.getData(ModDataAttachments.TROPHIES_CAUGHT);

        List<TrophyProperties> trophiesCaught = new ArrayList<>(U.getTpsFromRls(level(), data));

        //-1 on the common to account for the default "fish" unfortunately, theres probably a way to fix this
        TrophyProperties.RarityProgress all = new TrophyProperties.RarityProgress(0, player.getData(ModDataAttachments.FISHES_CAUGHT).size() - 1); //-1 to remove the default
        TrophyProperties.RarityProgress common = new TrophyProperties.RarityProgress(0, -1);
        TrophyProperties.RarityProgress uncommon = TrophyProperties.RarityProgress.DEFAULT;
        TrophyProperties.RarityProgress rare = TrophyProperties.RarityProgress.DEFAULT;
        TrophyProperties.RarityProgress epic = TrophyProperties.RarityProgress.DEFAULT;
        TrophyProperties.RarityProgress legendary = TrophyProperties.RarityProgress.DEFAULT;

        for (FishCaughtCounter fcc : player.getData(ModDataAttachments.FISHES_CAUGHT))
        {
            all = new TrophyProperties.RarityProgress(all.total() + fcc.count(), all.unique());

            if (U.getFpFromRl(level(), fcc.fp()).rarity() == FishProperties.Rarity.COMMON)
                common = new TrophyProperties.RarityProgress(common.total() + fcc.count(), common.unique() + 1);

            if (U.getFpFromRl(level(), fcc.fp()).rarity() == FishProperties.Rarity.UNCOMMON)
                uncommon = new TrophyProperties.RarityProgress(uncommon.total() + fcc.count(), uncommon.unique() + 1);

            if (U.getFpFromRl(level(), fcc.fp()).rarity() == FishProperties.Rarity.RARE)
                rare = new TrophyProperties.RarityProgress(rare.total() + fcc.count(), rare.unique() + 1);

            if (U.getFpFromRl(level(), fcc.fp()).rarity() == FishProperties.Rarity.EPIC)
                epic = new TrophyProperties.RarityProgress(epic.total() + fcc.count(), epic.unique() + 1);

            if (U.getFpFromRl(level(), fcc.fp()).rarity() == FishProperties.Rarity.LEGENDARY)
                legendary = new TrophyProperties.RarityProgress(legendary.total() + fcc.count(), legendary.unique() + 1);

        }

        for (TrophyProperties tp : level().registryAccess().registryOrThrow(Starcatcher.TROPHY_REGISTRY))
        {
            //if tp can be caught
            if (check(all, tp.all())
                    && check(common, tp.common())
                    && check(uncommon, tp.uncommon())
                    && check(rare, tp.rare())
                    && check(epic, tp.epic())
                    && check(legendary, tp.legendary())
                    && !trophiesCaught.contains(tp)
                    && FishProperties.getChance(tp.fp(), this, new ItemStack(ModItems.ROD.get())) > 0
                    && random.nextIntBetweenInclusive(0, 99) < tp.chanceToCatch()
            )
            {

                ItemStack is = new ItemStack(BuiltInRegistries.ITEM.get(tp.fp().fish().getKey()));
                is.set(ModDataComponents.TROPHY, tp);
                if (!tp.customName().isEmpty())
                    is.set(DataComponents.ITEM_NAME, Component.translatable(tp.customName()));

                Entity itemFished = new ItemEntity(
                        level(), position().x, position().y + 1.2f, position().z,
                        is);

                Vec3 vec3 = new Vec3(
                        Math.clamp((player.position().x - position().x) / 25, -1, 1),
                        0.7 + Math.clamp((player.position().y - position().y) / 20, -1, 1),
                        Math.clamp((player.position().z - position().z) / 25, -1, 1));

                itemFished.setDeltaMovement(vec3);
                level().addFreshEntity(itemFished);

                trophiesCaught.add(tp);

                player.setData(ModDataAttachments.TROPHIES_CAUGHT, U.getRlsFromTps(level(), trophiesCaught));
                player.setData(ModDataAttachments.FISHING, "");
                kill();
                return;
            }

        }


        for (FishProperties fp : level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            int chance = FishProperties.getChance(fp, this, rod);

            for (int i = 0; i < chance; i++)
            {
                available.add(fp);
            }
        }

        if (available.isEmpty())
        {
            player.setData(ModDataAttachments.FISHING, "");
            this.discard();
        }

        fpToFish = available.get(random.nextInt(available.size()));

        boolean skipsMinigame = fpToFish.skipMinigame() || (bobber.is(ModItems.CREEPER_BOBBER) && random.nextFloat() > 0.8);

        //skip minigame if server config says so
        if (!Config.ENABLE_MINIGAME.get())
            skipsMinigame = true;

        if (skipsMinigame)
        {

            ItemStack is = new ItemStack(fpToFish.fish());

            if (!Config.ENABLE_MINIGAME.get() && !fpToFish.skipMinigame())
            {
                int size = FishCaughtCounter.getRandomSize(fpToFish);
                int weight = FishCaughtCounter.getRandomWeight(fpToFish);
                is.set(ModDataComponents.SIZE_AND_WEIGHT, new SizeAndWeight(size, weight));
                FishCaughtCounter.AwardFishCaughtCounter(fpToFish, player, 0, size, weight, false);
            }

            Entity itemFished = new ItemEntity(
                    level(),
                    position().x,
                    position().y + 1.2f,
                    position().z,
                    is
            );


            double x = (player.position().x - position().x) / 25;
            double y = (player.position().y - position().y) / 20;
            double z = (player.position().z - position().z) / 25;

            x = Math.clamp(x, -1, 1);
            y = Math.clamp(y, -1, 1);
            z = Math.clamp(z, -1, 1);

            //override stack with a creeper and bigger deltaMovement to align creeper angle
            if (bobber.is(ModItems.CREEPER_BOBBER))
            {
                itemFished = new Creeper(EntityType.CREEPER, level());

                itemFished.setPos(position().add(0, 1.2f, 0));

                x *= 2.5;
                y *= 2;
                z *= 2.5;
            }


            Vec3 vec3 = new Vec3(x, 0.7 + y, z);

            itemFished.setDeltaMovement(vec3);

            level().addFreshEntity(itemFished);

            player.setData(ModDataAttachments.FISHING, "");

            kill();
        }
        else
        {
            PacketDistributor.sendToPlayer(
                    ((ServerPlayer) player),
                    new FishingPayload(fpToFish, rod)
            );
        }


        //consume bait
        if (fpToFish.br().consumesBait())
        {

            if (bobber.is(ModItems.FRUGAL_BOBBER))
            {
                if (random.nextFloat() > 0.8f) bait.setCount(bait.getCount() - 1);
            }
            else
            {
                bait.setCount(bait.getCount() - 1);
            }

            rod.set(ModDataComponents.BAIT, new SingleStackContainer(bait));
        }


    }


    private boolean shouldStopFishing(Player player)
    {
        if (level().isClientSide) return false;

        boolean holdingRod = player.getMainHandItem().is(ModItems.ROD)
                || player.getOffhandItem().is(ModItems.ROD);

        if (!player.isRemoved() && player.isAlive() && holdingRod && !(this.distanceToSqr(player) > 1024))
        {
            return false;
        }
        else
        {
            player.setData(ModDataAttachments.FISHING.get(), "");

            this.discard();
            return true;
        }
    }

    @Override
    public boolean fireImmune()
    {
        return hook.is(StarcatcherTags.HOOKS_SURVIVE_FIRE);
    }

    @Override
    public void lavaHurt()
    {
        super.lavaHurt();
        if (!hook.is(StarcatcherTags.HOOKS_SURVIVE_FIRE) && !level().isClientSide)
        {
            player.setData(ModDataAttachments.FISHING, "");
            kill();
        }
    }

    @Override
    public void tick()
    {
        super.tick();

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
            if(player != null) player.setData(ModDataAttachments.FISHING.get(), "");
        }

        BlockPos blockpos = this.blockPosition();
        FluidState fluid = this.level().getFluidState(blockpos);
        FluidState fluidBellow = this.level().getFluidState(blockpos.below());

        if (this.currentState == FishHookState.FLYING)
        {
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
                            ModParticles.FISHING_BITING_LAVA.get(),
                            position().x + random.nextFloat() - 0.5,
                            position().y + random.nextFloat() * 0.5 - 0.25,
                            position().z + random.nextFloat() - 0.5,
                            0, 0, 0);
                else
                    level().addParticle(
                            ModParticles.FISHING_BITING.get(),
                            position().x + random.nextFloat() - 0.5,
                            position().y + random.nextFloat() * 0.5 - 0.25,
                            position().z + random.nextFloat() - 0.5,
                            0, 0, 0);
            }

            if (timeBiting > 80)
            {
                player.setData(ModDataAttachments.FISHING, "");
                kill();
            }
        }
        else
        {
            timeBiting = 0;
        }

        //if theres no fluid on block or under, changes to FLYING
        if (fluid.isEmpty() && fluidBellow.isEmpty())
        {
            if (!level().isClientSide) currentState = FishHookState.FLYING;
        }

        //TODO check for water level instead of just blockstate to make the entity sit better in water
        if (this.currentState == FishHookState.BOBBING || this.currentState == FishHookState.FISHING)
        {
            checkForFish();

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
            int i = random.nextInt(chanceToFishEachTick);
            if ((i == 1 || ticksInFluid > maxTicksToFish) && ticksInFluid > minTicksToFish)
            {
                if (Config.SHOW_EXCLAMATION_MARK_PARTICLE.get())
                    ((ServerLevel) level()).sendParticles(
                            ModParticles.FISHING_NOTIFICATION.get(),
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
    }

    public static boolean check(TrophyProperties.RarityProgress current, TrophyProperties.RarityProgress restriction)
    {
        return current.total() >= restriction.total() && current.unique() >= restriction.unique();
    }

}
