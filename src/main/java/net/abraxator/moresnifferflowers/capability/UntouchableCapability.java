package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UntouchableCapability {
    public static final Identifier ID_SPEED = MoreSnifferFlowers.loc("untouchable_speed");
    public static final Identifier ID_RESISTANCE = MoreSnifferFlowers.loc("untouchable_resistance");

    public double lastX = 0;
    public double lastZ = 0;
    public float speedModifier = 0;

    public static final Codec<UntouchableCapability> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.DOUBLE.fieldOf("lastX").forGetter(data -> data.lastX),
                    Codec.DOUBLE.fieldOf("lastZ").forGetter(data -> data.lastZ),
                    Codec.FLOAT.fieldOf("speedModifier").forGetter(data -> data.speedModifier)
            ).apply(instance, (a,b,c) -> {
                UntouchableCapability cap =  new UntouchableCapability();
                cap.lastX = a;
                cap.lastZ = b;
                cap.speedModifier = c;
                return cap;
            }));


    public void onAttacked(){
        speedModifier = speedModifier * 0.7f;
    }

    public void onEffectEnd(Player player){
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ID_SPEED);
        player.getAttribute(Attributes.ARMOR).removeModifier(ID_RESISTANCE);
        speedModifier = 0;
    }

    public void tick(Player player, int amplifier){
        Vec3 position = player.position();
        double posX = position.x;
        double posZ = position.z;

        if (lastX == 0 && lastZ == 0){
            lastX = posX;
            lastZ = posZ;

            return;
        }

        float speedAccumulation = 0.00005f;
        float maxSpeed = 150 + amplifier*15;
        speedAccumulation += (speedAccumulation / 3) * amplifier;

        double speedX = Math.abs(posX - lastX);
        double speedZ = Math.abs(posZ - lastZ);
        int speed = Math.round((float) (speedX * speedX + speedZ * speedZ) * 1000);

        float armorModifier = Mth.floor(speedModifier * 400);
        if (armorModifier < 0) armorModifier = 0;

        if (player.isSprinting() ){

           if (speed <= maxSpeed) speedModifier += speedAccumulation;

        } else {
            if (speedModifier > 0) speedModifier -= speedAccumulation * 15;
        }

        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ID_SPEED);
        player.getAttribute(Attributes.ARMOR).removeModifier(ID_RESISTANCE);

        AttributeModifier mod = new AttributeModifier(ID_SPEED, speedModifier, AttributeModifier.Operation.ADD_VALUE);
        AttributeModifier mod1 = new AttributeModifier(ID_RESISTANCE, armorModifier, AttributeModifier.Operation.ADD_VALUE);

        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(mod);
        player.getAttribute(Attributes.ARMOR).addTransientModifier(mod1);

        lastX = posX;
        lastZ = posZ;
    }

    public void debugPrint(Level level){
        System.out.println("Speed Modifier = " + speedModifier +" client = " + level.isClientSide());
    }
}
