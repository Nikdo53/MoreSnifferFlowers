package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class ComboMealCapability {
    public static final Codec<ComboMealCapability> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("speed").forGetter(ComboMealCapability::getSpeed),
                    Codec.INT.fieldOf("duration").forGetter(ComboMealCapability::getDuration)
            ).apply(instance, ComboMealCapability::new));

    public static final Identifier ID = MoreSnifferFlowers.loc("combo_meal");
    public float speed = 1;
    public int duration = 0;

    public float getSpeed() { return speed; }
    public int getDuration() { return duration; }

    public void setSpeed(float speed) { this.speed = speed; }
    public void setDuration(int duration) { this.duration = duration; }

    public static ComboMealCapability getCapability(Player player) {
       return player.getData(MSFDataAttachments.COMBO_MEAL.get());
    }

    public ComboMealCapability(float speed, int duration) {
        this.speed = speed;
        this.duration = duration;
    }

    public void onEffectEnd(Player player) {
        speed = 1;
        duration = 0;
        player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ID);
    }

    public void onAttack(Player player, boolean isCharged) {
        int amplifier = Objects.requireNonNull(player.getEffect(MSFEffects.COMBO_MEAL)).getAmplifier();

        if (isCharged){
            speed *= 1 + (amplifier / 4f + 1) / 10f;
            duration = (int) (150 / (speed * 2));

            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ID);
            AttributeModifier mod = new AttributeModifier(ID, speed - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(mod);

        } else {

            duration /= 2;

        }
    }

    public void tick(Player player) {
        if (duration <= 0){
            speed = 1;
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ID);
        }
        if (duration > 0) {
            duration--;
        }

    }

    public void debugPrint(){
        System.out.println("lastSpeed: " + speed + " duration: " + duration);
    }
}
