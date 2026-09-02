package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.networking.toClient.SyncSlipperyPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class SlipperyCapability{
    public static final Identifier ID = MoreSnifferFlowers.loc("slippery");

    public float lastYaw = 0;
    public float lastSpeed = 0;
    public boolean isFallen = false;
    public int fallenTicks = 0;
    public int maxFallenTicks = 0;

    public static final Codec<SlipperyCapability> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("lastYaw").forGetter(data -> data.lastYaw),
                    Codec.FLOAT.fieldOf("lastSpeed").forGetter(data -> data.lastSpeed),
                    Codec.BOOL.fieldOf("isFallen").forGetter(data -> data.isFallen),
                    Codec.INT.fieldOf("fallenTicks").forGetter(data -> data.fallenTicks),
                    Codec.INT.fieldOf("maxFallenTicks").forGetter(data -> data.maxFallenTicks)
            ).apply(instance, (a,b,c,d,e) -> {
                SlipperyCapability cap =  new SlipperyCapability();
                cap.lastYaw = a;
                cap.lastSpeed = b;
                cap.isFallen = c;
                cap.fallenTicks = d;
                cap.maxFallenTicks = e;
                return cap;
            }));


    public void onEffectEnd(LivingEntity player) {
        lastSpeed = 0;
        lastYaw = 0;
        if (isFallen) getUp(player);
    }

    public void tick(LivingEntity entity, int amplifier) {
        float yaw = entity.getYRot();
        Vec3 motion = entity.getDeltaMovement();
        float speed = (float) (motion.x + motion.y + motion.z);

        if (isFallen){
            fallenTicks--;
            entity.setJumping(false);
            entity.setDeltaMovement(motion.x, Math.min(motion.y, 0), motion.z);

            if (!entity.getPose().equals(Pose.SWIMMING)){
                if (entity instanceof Player player) {
                    player.setForcedPose(Pose.SWIMMING);
                } else {
                    entity.setPose(Pose.SLEEPING);
                }
            }


            if (fallenTicks <= 0){
                getUp(entity);
            }

        } else if (!entity.level().isClientSide && !(lastSpeed == 0 && lastYaw == 0)){

            boolean speedChange = Math.abs(speed - lastSpeed) > 0.60f; // this only works for falling down for some reason
            float rotationLimit = Math.max(90f - amplifier*10, 15f);
            boolean rotationChange = Math.abs(Mth.wrapDegrees(yaw - lastYaw)) > rotationLimit && entity.isSprinting();

            if (entity.onGround() && (speedChange || rotationChange)) {
                fallDown(entity, amplifier);
            }
        }

        lastSpeed = speed;
        lastYaw = yaw;
    }




    public void fallDown(LivingEntity entity, int amplifier) {
        isFallen = true;
        maxFallenTicks = 30 + amplifier * 10;
        fallenTicks = maxFallenTicks;

        AttributeInstance attribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute == null) return;

        attribute.removeModifier(ID);
        AttributeModifier mod = new AttributeModifier(ID, -100, AttributeModifier.Operation.ADD_VALUE);
        attribute.addTransientModifier(mod);

        entity.level().playSound(null, entity.blockPosition(), SoundEvents.SLIME_SQUISH, SoundSource.PLAYERS, 1f, 1f);

        sync(entity);
    }

    public void getUp(LivingEntity entity) {
        fallenTicks = 0;
        isFallen = false;

        AttributeInstance attribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) attribute.removeModifier(ID);

        if (entity instanceof Player player)
            player.setForcedPose(null);
        else entity.setPose(Pose.STANDING);

       if (!entity.level().isClientSide())
           sync(entity);
    }



    public void sync(LivingEntity entity){
        PacketDistributor.sendToAllPlayers(new SyncSlipperyPacket(this, entity.getId()));
    }

    public static SlipperyCapability get(LivingEntity entity) {
       return entity.getData(MSFDataAttachments.SLIPPERY);
    }
}
