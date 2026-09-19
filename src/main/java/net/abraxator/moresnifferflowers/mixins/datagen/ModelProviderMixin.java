package net.abraxator.moresnifferflowers.mixins.datagen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(ModelProvider.BlockStateGeneratorCollector.class)
public class ModelProviderMixin {

    @WrapOperation(method = "Lnet/minecraft/client/data/models/ModelProvider$BlockStateGeneratorCollector;accept(Lnet/minecraft/client/data/models/blockstates/BlockModelDefinitionGenerator;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    public Object allowDuplicates(Map instance, Object block, Object model, Operation<BlockModelDefinitionGenerator> original){
        BlockModelDefinitionGenerator call = original.call(instance, block, model);
        if (call != null && ((Block) block).builtInRegistryHolder().key().identifier().getNamespace().equals(MoreSnifferFlowers.MOD_ID)){
            return null;
        }
        return call;
    }

    @WrapOperation(method = "validate", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
    public boolean validate(List<Identifier> instance, Operation<Boolean> original){
        boolean call = original.call(instance);
        if (call){
            return true;
        }
        ArrayList<Identifier> list = new ArrayList<>(instance);
        List<Identifier> list1 = list.stream().filter(id -> !id.getNamespace().equals(MoreSnifferFlowers.MOD_ID)).toList();

        return list1.isEmpty();
    }

    @Mixin(ModelProvider.SimpleModelCollector.class)
    public static class SimpleModelCollector {
        @WrapOperation(method = "accept*", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
        public Object allowDuplicates(Map instance, Object id, Object contents, Operation<ModelInstance> original){
            ModelInstance call = original.call(instance, id, contents);
            if (call != null && ((Identifier) id).getNamespace().equals(MoreSnifferFlowers.MOD_ID)){
                return null;
            }
            return call;
        }


    }
}
