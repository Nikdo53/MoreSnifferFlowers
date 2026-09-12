package net.abraxator.moresnifferflowers.client.gui.screen.cookbook;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.MSFDataMaps;
import net.abraxator.moresnifferflowers.components.nutrition.Nutrition;
import net.abraxator.moresnifferflowers.components.nutrition.NutritionType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class CookbookScreen extends Screen {
    private static final Identifier TEXTURE = MoreSnifferFlowers.loc("textures/gui/cookbook.png");
    public static final Identifier RENDERABLES = MoreSnifferFlowers.loc("textures/gui/cookbook_renderables.png");
    public static final Identifier GUIDE_0 = MoreSnifferFlowers.loc("textures/gui/cookbook_guide1.png");
    public static final Identifier GUIDE_1 = MoreSnifferFlowers.loc("textures/gui/cookbook_guide2.png");
    public static final Identifier ERROR = MoreSnifferFlowers.loc("textures/gui/cookbook_error.png");

    private final int ROWS = 8;
    private final int COLUMNS = 5;
    private final int PAGE_SIZE = ROWS * COLUMNS;
    private final int SCROLLBAR_HEIGHT = 142;
    private final int SCROLLER_HEIGHT = 15;
    public Page page = Page.CONTENTS;
    public int guide_page = 0;
    public NutritionType type;
    private List<Nutrition.NutritionPair> nutritions = new ArrayList<>();
    private float scrollOffs;
    private int startIndex;
    private boolean isScrolling;
    public int yOffset = 0;
    public int yTotal = 0;
    public static int MAX_Y = 140;


    public CookbookScreen() {
        super(Component.empty());
    }


    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int x = (this.width - 272) / 2;
        int y = (this.height - 180) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, 272, 180, 512, 256);
        if(page == Page.ITEMS) {
            this.renderItems(graphics, mouseX, mouseY, x, y);
        }

        if (page == Page.GUIDE){
            this.renderGuide(graphics, mouseX, mouseY, x, y);
        }

        if (page == Page.ERROR)
            graphics.blit(RenderPipelines.GUI_TEXTURED, ERROR, x, y,0, 0, 166, 256, 256, 256);

        this.renderContents(graphics, mouseX, mouseY, x, y);
    }

    public void renderNutritionInfo(GuiGraphicsExtractor guiGraphics, Nutrition.NutritionPair nutrition) {
        int x = (this.width - 272) / 2;
        int y = (this.height - 180) / 2;
        int xPos = x + 150;
        int yPos = y + 20;
        ItemStack item = nutrition.item().getDefaultInstance();
        String string = item.getDisplayName().getString();
        int nameLength = string.length();

        guiGraphics.textWithWordWrap(font, FormattedText.of(string, Style.EMPTY.withBold(true).withUnderlined(true)), xPos, yPos, 108, ChatFormatting.DARK_GRAY.getColor());
        if (nameLength > 16) yPos += 9;
        for (Nutrition.NutritionEntry nutritionEntry : nutrition.nutrition().entryList()) {
            yPos += 10;
            MutableComponent nutritionName = Component.translatable("gui.moresnifferflowers.cookbook." + nutritionEntry.nutrition().name).withStyle(ChatFormatting.BOLD);
            guiGraphics.text(font, nutritionName.append(" : ").append(String.valueOf(nutritionEntry.weight())), xPos, yPos, nutritionEntry.nutrition().color);
        }
    }

    public void renderEffectInfo(GuiGraphicsExtractor guiGraphics, MobEffect effect, boolean isPositive) {
        int x = (this.width - 272) / 2;
        int y = (this.height - 180) / 2;
        int xPos = x + 150;
        int yPos = y + 20;
        String string = effect.getDisplayName().getString();
        int nameLength = string.length();
        int color = isPositive ? 0x67911c : 0x8d2a22;

        guiGraphics.textWithWordWrap(font, FormattedText.of(string, Style.EMPTY.withBold(true).withUnderlined(true)), xPos, yPos, 100, color);

        yPos += 15;
        MutableComponent effectDescription = Component.translatable(effect.getDescriptionId() + ".description").withStyle(ChatFormatting.DARK_GRAY);
        guiGraphics.textWithWordWrap(font, effectDescription, xPos, yPos, 108, ChatFormatting.DARK_GRAY.getColor());
    }

    private void renderGuide(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        Identifier texture = guide_page == 0 ? GUIDE_0 : GUIDE_1;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,texture, x  + 10, y, 0, 0, 256, 176, 255, 255);
    }


    private void renderItems(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        int xPos = 18;
        int yPos = 16;
        int yStarting = yPos;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,RENDERABLES, x + 17, y + 15, 25, 0, 111, 144, 255, 255);

        for (int i = startIndex * COLUMNS + 1; i < startIndex * COLUMNS  + 1 + PAGE_SIZE && i < this.nutritions.size() + 1; i++) {
            Nutrition.NutritionPair nutrition = nutritions.get(i - 1);
            boolean unlocked = minecraft.player.getData(MSFDataAttachments.NUTRITION).unlockedItems().contains(nutrition.item());

            nutrition = unlocked ? nutrition : new Nutrition.NutritionPair(nutrition.item(), Nutrition.EMPTY);
            if (yPos >= 16) addRenderableWidget(new ItemWidget(x + xPos, y + yPos, Component.empty(), nutrition, this));

            if (i % COLUMNS != 0) {
                xPos += 18;
            } else {
                yPos += 18;
                xPos = 18;
            }
        }

        if (xPos > 18) yPos += 18;
        xPos = 18;
        yPos += 4;
      //  yPos += startIndex * 18;

        if (canRender(yPos)) addRenderableWidget(new EffectWidget(x + xPos, y + yPos, Component.empty(), type, this, false));
        yPos += 25;
        if (canRender(yPos))  addRenderableWidget(new EffectWidget(x + xPos, y + yPos, Component.empty(), type, this, true));

        boolean scrollable = yTotal > MAX_Y;
        int scrollAmount = (int) ((SCROLLBAR_HEIGHT - SCROLLER_HEIGHT) * this.scrollOffs);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RENDERABLES, x + 115, y + 16 + scrollAmount, scrollable ? 0 : 12, 0, 12, 15, 256, 256);

        this.yTotal = yPos - yStarting + startIndex * 18 + 10;
    }

    public boolean canRender(int yPos){
        return yPos >= 16 && yPos <= MAX_Y + 10;
    }

    private void renderContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        for (int i = 0; i < NutritionType.values().length; i++) {
            NutritionType type = NutritionType.byId(i);
            this.addRenderableWidget(new TypeWidget(x + 271, y + 24*i + 13 + i*2, type, 24, 24, Component.literal(type.name), this));
        }
        int i = 5; // the guide tab
        this.addRenderableWidget(new TypeWidget(x + 271, y + 24*i + 13 + i*2, null, 24, 24, Component.literal("Guide"), this));
    }
    
    public void pageToItems(NutritionType type) {
        Map<ResourceKey<Item>, Nutrition> nutritionTypeSet = BuiltInRegistries.ITEM.getDataMap(MSFDataMaps.NUTRITION);

        if (nutritionTypeSet.isEmpty()) {
            this.turnPage(Page.ERROR);
            return;
        }

        List<Nutrition.NutritionPair> list = new ArrayList<>(nutritionTypeSet.entrySet().stream()
                .filter(entry -> entry.getValue().hasType(type))
                .map(e -> new Nutrition.NutritionPair(BuiltInRegistries.ITEM.getValue(e.getKey()), e.getValue())).toList());

        list.sort(Comparator.comparing( pair -> {
            float weight = 0;
            for (Nutrition.NutritionEntry entry : pair.nutrition().entryList()){
                    if (entry.nutrition().equals(type)){
                        weight += entry.weight();
                    } else {
                        weight += 0.001f* entry.weight();
                    }
            }
            return -weight; // .reversed doesnt work for some reason???
        }));
        this.nutritions = list;
        this.turnPage(Page.ITEMS);
    }
    
    public void turnPage(Page page) {
        startIndex = 0;
        yTotal = 0;
        guide_page = 0;
        this.page = page;
        this.clearWidgets();
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        int x = (this.width - 272) / 2;
        int y = (this.height - 180) / 2;

        if (page == Page.GUIDE){
            if (guide_page == 0) {
                guide_page = 1;
            } else if (guide_page == 1) {
                guide_page = 0;
            }
        }

        this.isScrolling = isMouseOver(event.x(), event.y(), x + 115, y + 15, 15, MAX_Y) && this.page == Page.ITEMS;

        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if(this.page == Page.ITEMS && this.isScrolling) {
            mouseScrolled(event.x(), event.y(), 0, - dy / 40);
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int totalItems = this.nutritions.size();
        int maxRows = Math.max(1, (int) Math.ceil((double) totalItems / COLUMNS));
        float value = (yTotal - MAX_Y) / 18f;
        int scrollableRows = Math.max(0, Mth.ceil(value));

        if (scrollableRows > 0) {
            float f = (float) scrollY / (float) scrollableRows;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            
            this.startIndex = (int) Math.max((this.scrollOffs * scrollableRows), 0);

            if (startIndex > scrollableRows) startIndex = scrollableRows;
            clearWidgets();
        }

        return true;
    }

    private int getTotalRowCount() {
        return Mth.positiveCeilDiv(this.nutritions.size(), 5);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }
    
    public enum Page {
        CONTENTS,
        ITEMS,
        GUIDE,
        ERROR
    }
}
