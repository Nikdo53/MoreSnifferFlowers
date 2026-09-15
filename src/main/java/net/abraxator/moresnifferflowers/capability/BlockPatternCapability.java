package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.client.MSFClientUtils;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.networking.toClient.RebuildChunkSectionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;
import java.util.function.Consumer;

public class BlockPatternCapability {
    protected Map<BlockPos, PatternData> patterns;
    public static final Codec<Long> LONG_STRING_CODEC = Codec.STRING.xmap(Long::parseLong, Object::toString);

    public static final Codec<BlockPos> BLOCKPOS_LONG_CODEC = LONG_STRING_CODEC.xmap(
            BlockPos::of,
            BlockPos::asLong
    );

    public static final Codec<BlockPatternCapability> CODEC =RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(BLOCKPOS_LONG_CODEC, PatternData.CODEC).fieldOf("patterns").forGetter(cap -> cap.patterns))
            .apply(instance, BlockPatternCapability::new));

    public static final StreamCodec<? super ByteBuf, BlockPatternCapability> STREAM_CODEC =
            StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    BlockPos.STREAM_CODEC,
                    PatternData.STREAM_CODEC
            ),
                    (cap -> cap.patterns),
                    BlockPatternCapability::new
            );

    public BlockPatternCapability(Map<BlockPos, PatternData> patterns) {
        this.patterns = new HashMap<>(patterns);
    }

    public static Map<BlockPos, PatternData> getPatterns(Level level, BlockPos pos) {
        return new HashMap<>(getBlockPatterns(level.getChunkAt(pos)).patterns);
    }

    protected static BlockPatternCapability getBlockPatterns(Level level, BlockPos pos){
        return getBlockPatterns(level.getChunkAt(pos));
    }

    protected static BlockPatternCapability getBlockPatterns(LevelChunk chunk){
        return chunk.getData(MSFDataAttachments.BLOCK_PATTERNS.get());
    }

   //Doesnt rebuild rendering
    protected static void operation(LevelChunk chunk, Consumer<Map<BlockPos, PatternData>> updater) {
        BlockPatternCapability data = chunk.getData(MSFDataAttachments.BLOCK_PATTERNS);
        updater.accept(data.patterns);
        chunk.setData(MSFDataAttachments.BLOCK_PATTERNS.get(), data);
    }

    protected static void operation(Level level, BlockPos pos, Consumer<Map<BlockPos, PatternData>> updater) {
        operation(level.getChunkAt(pos), updater);
        if (level.isClientSide()){
            MSFClientUtils.rebuildChunkSection(pos);
        } else if (level instanceof ServerLevel serverLevel){
            SectionPos sectionPos = SectionPos.of(pos);
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(pos), new RebuildChunkSectionPacket(sectionPos.x(), sectionPos.y(), sectionPos.z()));
        }
    }


    public static void setPattern(Level level, BlockPos pos, PatternData pattern) {
        operation(level, pos, patterns -> patterns.put(pos, pattern));
    }

    public static void setBulkPatterns(Map<BlockPos, PatternData> patternMap, Level level) {
        Map<ChunkPos, Map<BlockPos, PatternData>> chunkPatterns = new HashMap<>();
        for (Map.Entry<BlockPos, PatternData> blockPosPatternDataEntry : patternMap.entrySet()) {
            BlockPos pos = blockPosPatternDataEntry.getKey();
            chunkPatterns.computeIfAbsent(ChunkPos.containing(pos), k -> new HashMap<>()).put(pos, blockPosPatternDataEntry.getValue());
        }

        for (Map.Entry<ChunkPos, Map<BlockPos, PatternData>> chunkPosMapEntry : chunkPatterns.entrySet()) {
            LevelChunk chunk = level.getChunkAt(chunkPosMapEntry.getKey().getWorldPosition());
            operation(chunk, patterns -> patterns.putAll(chunkPosMapEntry.getValue()));
        }

        patternMap.keySet().stream().map(SectionPos::of).distinct().forEach(sectionPos -> {
            if (level.isClientSide()){
                MSFClientUtils.rebuildChunkSection(sectionPos);
            } else if (level instanceof ServerLevel serverLevel){
                PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(sectionPos.center()), new RebuildChunkSectionPacket(sectionPos.x(), sectionPos.y(), sectionPos.z()));
            }
        });
    }


    public static PatternData getPattern(BlockPos pos, Level level){
        LevelChunk chunk = level.getChunkAt(pos);
        return chunk.getData(MSFDataAttachments.BLOCK_PATTERNS).patterns.get(pos);
    }


    public static boolean hasPattern(BlockPos pos, Level level){
        BlockPatternCapability capability = getBlockPatterns(level, pos);
        return capability.patterns.containsKey(pos);
    }

    public static void removePattern(BlockPos pos, Level level) {
        operation(level, pos, patterns -> patterns.remove(pos));
    }

    public static void recolor(Level level, BlockPos pos, int color) {
        operation(level, pos,
                patterns -> patterns.computeIfPresent(pos, (k, data) -> new PatternData(data.patternId, color, data.direction, data.isGlowing)));
    }

    public static void enableGlowing(Level level, BlockPos pos) {
        operation(level, pos,
                patterns -> patterns.computeIfPresent(pos, (k, data) -> new PatternData(data.patternId, data.color, data.direction, true)));
    }

    // pattern=pat color=6, direction=dir, glowing=glow
    public record PatternData(int patternId, int color, Direction direction, boolean isGlowing) {
        public static final Codec<PatternData> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.INT.fieldOf("id").forGetter(PatternData::patternId),
                        Codec.INT.fieldOf("col").forGetter(PatternData::color),
                        Direction.CODEC.fieldOf("dir").forGetter(PatternData::direction),
                        Codec.BOOL.fieldOf("glw").forGetter(PatternData::isGlowing)
                ).apply(instance, PatternData::new));

        public static final StreamCodec<? super ByteBuf, PatternData> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.INT, PatternData::patternId,
                        ByteBufCodecs.INT, PatternData::color,
                        Direction.STREAM_CODEC, PatternData::direction,
                        ByteBufCodecs.BOOL, PatternData::isGlowing,
                        PatternData::new
                );

    }
}
