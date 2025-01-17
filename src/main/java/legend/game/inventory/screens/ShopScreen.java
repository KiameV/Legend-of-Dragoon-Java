package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.input.InputAction;
import legend.game.inventory.EquipItemResult;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.inventory.ShopEquipmentEvent;
import legend.game.modding.events.inventory.ShopItemEvent;
import legend.game.modding.events.inventory.ShopSellPriceEvent;
import legend.game.types.ActiveStatsa0;
import legend.game.types.EquipmentSlot;
import legend.game.types.LodString;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.lodmod.LodMod;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.SItem.Buy_8011c6a4;
import static legend.game.SItem.Cannot_be_armed_with_8011c6d4;
import static legend.game.SItem.Carried_8011c6b8;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.Leave_8011c6c8;
import static legend.game.SItem.Not_enough_money_8011c468;
import static legend.game.SItem.Number_kept_8011c7f4;
import static legend.game.SItem.Sell_8011c6ac;
import static legend.game.SItem.Which_item_do_you_want_to_sell_8011c4e4;
import static legend.game.SItem.Which_weapon_do_you_want_to_sell_8011c524;
import static legend.game.SItem.allocateOneFrameGlyph;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.canEquip;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.equipItem;
import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.glyphs_80114510;
import static legend.game.SItem.initGlyph;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderCentredText;
import static legend.game.SItem.renderEightDigitNumber;
import static legend.game.SItem.renderFiveDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderItemIcon;
import static legend.game.SItem.renderString;
import static legend.game.SItem.renderText;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.SItem.renderThreeDigitNumberComparison;
import static legend.game.SItem.renderTwoDigitNumber;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.addGold;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.intToStr;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.takeEquipment;
import static legend.game.Scus94491BpeSegment_8002.takeItem;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8007.shopId_8007a3b4;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.submap.SMap.shops_800f4930;

public class ShopScreen extends MenuScreen {
  private MenuState menuState = MenuState.INIT_0;
  private MenuState confirmDest;

  private int equipCharIndex;
  private int menuIndex_8011e0dc;
  private int menuIndex_8011e0e0;
  private int menuScroll_8011e0e4;
  private Renderable58 renderable_8011e0f0;
  private Renderable58 renderable_8011e0f4;
  private Renderable58 selectedMenuOptionRenderablePtr_800bdbe0;
  private Renderable58 selectedMenuOptionRenderablePtr_800bdbe4;
  private Renderable58 charHighlight;

  private final List<ShopEntry<? extends InventoryEntry>> inv = new ArrayList<>();

  /**
   * <ul>
   *   <li>0 - Item Shop</li>
   *   <li>1 - Weapon Shop</li>
   * </ul>
   */
  private int shopType;
  /**
   * <ul>
   *   <li>0 - Item Shop</li>
   *   <li>1 - Weapon Shop</li>
   * </ul>
   */
  private int shopType2;

  private final Renderable58[] charRenderables = new Renderable58[9];

  private double scrollAccumulator;
  private int mouseX;
  private int mouseY;

  @Override
  protected void render() {
    switch(this.menuState) {
      case INIT_0 -> {
        this.inv.clear();
        loadCharacterStats();
        this.menuIndex_8011e0dc = 0;
        this.menuIndex_8011e0e0 = 0;
        this.menuScroll_8011e0e4 = 0;
        this.menuState = MenuState.LOAD_ITEMS_1;
      }

      case LOAD_ITEMS_1 -> {
        if(uiFile_800bdc3c == null) {
          return;
        }

        startFadeEffect(2, 10);

        this.shopType = shops_800f4930.get(shopId_8007a3b4.get()).shopType_00.get() & 1;

        if(this.shopType == 0) {
          final List<ShopEntry<Equipment>> shopEntries = new ArrayList<>();

          for(int i = 0; i < 16; i++) {
            final int id = shops_800f4930.get(shopId_8007a3b4.get()).item_00.get(i).id_01.get();

            if(id != 0xff) {
              final RegistryId registryId = LodMod.equipmentIdMap.get(id);
              final Equipment equipment = REGISTRIES.equipment.getEntry(registryId).get();
              shopEntries.add(new ShopEntry<>(equipment, equipment.getPrice() * 2));
            }
          }

          final ShopEquipmentEvent event = EVENTS.postEvent(new ShopEquipmentEvent(shopId_8007a3b4.get(), shopEntries));
          this.inv.addAll(event.equipment);
        } else {
          final List<ShopEntry<Item>> shopEntries = new ArrayList<>();

          for(int i = 0; i < 16; i++) {
            final int id = shops_800f4930.get(shopId_8007a3b4.get()).item_00.get(i).id_01.get();

            if(id != 0xff) {
              final RegistryId registryId = LodMod.itemIdMap.get(id - 192);
              final Item item = REGISTRIES.items.getEntry(registryId).get();
              shopEntries.add(new ShopEntry<>(item, item.getPrice() * 2));
            }
          }

          final ShopItemEvent event = EVENTS.postEvent(new ShopItemEvent(shopId_8007a3b4.get(), shopEntries));
          this.inv.addAll(event.items);
        }

        cacheCharacterSlots();

        this.menuState = MenuState.INIT_2;
      }

      case INIT_2 -> {
        deallocateRenderables(0xff);
        renderGlyphs(glyphs_80114510, 0, 0);
        this.selectedMenuOptionRenderablePtr_800bdbe0 = allocateUiElement(0x7a, 0x7a, 49, this.getShopMenuYOffset(this.menuIndex_8011e0dc));
        FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe0);

        for(int charSlot = 0; charSlot < characterCount_8011d7c4.get(); charSlot++) {
          this.charRenderables[charSlot] = this.allocateCharRenderable(this.FUN_8010a818(charSlot), 174, characterIndices_800bdbb8.get(charSlot).get());
        }

        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
        this.menuState = MenuState.RENDER_3;
      }

      case RENDER_3 -> this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);

      case BUY_4 -> {
        final ShopEntry<? extends InventoryEntry> entry = this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);

        if(this.shopType == 0) {
          this.renderEquipmentStatChange((Equipment)entry.item, characterIndices_800bdbb8.get(this.equipCharIndex).get());
        } else {
          this.renderNumberOfItems((Item)entry.item);
        }

        renderString(16, 122, entry.item.getDescription(), false);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.menuScroll_8011e0e4 > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.menuScroll_8011e0e4 - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.menuScroll_8011e0e4 < this.inv.size() - 6 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.menuScroll_8011e0e4 + 1);
          }
        }

        this.renderMenuEntries(this.inv, this.menuScroll_8011e0e4, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
      }

      case BUY_SELECT_CHAR_5 -> {
        final ShopEntry<? extends InventoryEntry> equipment = this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);
        this.renderEquipmentStatChange((Equipment)equipment.item, characterIndices_800bdbb8.get(this.equipCharIndex).get());
        renderString(16, 122, equipment.item.getDescription(), false);
        this.renderMenuEntries(this.inv, this.menuScroll_8011e0e4, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
      }

      case SELL_10 -> {
        final int count;
        if(this.shopType2 != 0) {
          renderText(Which_item_do_you_want_to_sell_8011c4e4, 16, 128, TextColour.BROWN);
          count = gameState_800babc8.items_2e9.size();

          if(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0 < count) {
            renderString(193, 122, gameState_800babc8.items_2e9.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).getDescription(), false);
          }
        } else {
          renderText(Which_weapon_do_you_want_to_sell_8011c524, 16, 128, TextColour.BROWN);
          count = gameState_800babc8.equipment_1e8.size();

          if(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0 < count) {
            renderString(193, 122, gameState_800babc8.equipment_1e8.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).description, false);
          }
        }

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.menuScroll_8011e0e4 > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            playSound(1);
            this.menuScroll_8011e0e4--;

            if(this.shopType2 == 0) {
              this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0));
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.menuIndex_8011e0e0);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.menuScroll_8011e0e4 < count - 6 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            playSound(1);
            this.menuScroll_8011e0e4++;

            if(this.shopType2 == 0) {
              this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0));
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.menuIndex_8011e0e0);
          }
        }

        this.renderItemList(this.menuScroll_8011e0e4, this.shopType2, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType2);
      }

      case _16, _17 -> {
        if(this.menuState == MenuState._16) {
          startFadeEffect(1, 10);
          this.menuState = MenuState._17;
        }

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          this.menuState = this.confirmDest;
        }

        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
      }

      case UNLOAD_19 -> {
        startFadeEffect(2, 10);
        deallocateRenderables(0xff);

        currentEngineState_8004dd04.menuClosed();

        whichMenu_800bdc38 = WhichMenu.UNLOAD_SHOP_MENU_10;
        textZ_800bdf00.set(13);
      }
    }
  }

  private void scroll(final int scroll) {
    this.menuScroll_8011e0e4 = scroll;
  }

  private int FUN_8010a864(@Nullable final Equipment equipment) {
    int s3 = -1;

    for(int i = 0; i < 7; i++) {
      if(characterIndices_800bdbb8.get(i).get() != -1) {
        this.charRenderables[i].y_44 = 174;

        if(equipment != null) {
          if(!canEquip(equipment, characterIndices_800bdbb8.get(i).get())) {
            this.charRenderables[i].y_44 = 250;
          } else if(s3 == -1) {
            s3 = i;
          }
        }
      }
    }

    if(s3 == -1) {
      s3 = 0;
    }

    return s3;
  }

  private void renderShopMenu(final int selectedMenuItem, final int isItemMenu) {
    renderCentredText(Buy_8011c6a4, 72, this.getShopMenuYOffset(0) + 2, selectedMenuItem != 0 ? TextColour.BROWN : TextColour.RED);
    renderCentredText(Sell_8011c6ac, 72, this.getShopMenuYOffset(1) + 2, selectedMenuItem != 1 ? TextColour.BROWN : TextColour.RED);
    renderCentredText(Carried_8011c6b8, 72, this.getShopMenuYOffset(2) + 2, selectedMenuItem != 2 ? TextColour.BROWN : TextColour.RED);
    renderCentredText(Leave_8011c6c8, 72, this.getShopMenuYOffset(3) + 2, selectedMenuItem != 3 ? TextColour.BROWN : TextColour.RED);

    if(isItemMenu != 0) {
      renderTwoDigitNumber(105, 36, gameState_800babc8.items_2e9.size(), 0x2);
      allocateOneFrameGlyph(94, 16, 16);
      renderTwoDigitNumber(123, 36, CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get()), 0x2);
    } else {
      renderThreeDigitNumber(93, 36, gameState_800babc8.equipment_1e8.size(), 0x2);
      allocateOneFrameGlyph(95, 16, 16);
      renderThreeDigitNumber(117, 36, 255, 0x2);
    }

    renderEightDigitNumber(87, 24, gameState_800babc8.gold_94, 0x2);
  }

  private void renderEquipmentStatChange(final Equipment equipment, final int charIndex) {
    if(charIndex != -1) {
      final ActiveStatsa0 oldStats = new ActiveStatsa0(stats_800be5f8[charIndex]);

      final Map<EquipmentSlot, Equipment> oldEquipment = new EnumMap<>(gameState_800babc8.charData_32c[charIndex].equipment_14);

      if(equipItem(equipment, charIndex).success) {
        allocateOneFrameGlyph(0x67, 210, 127);
        allocateOneFrameGlyph(0x68, 210, 137);
        allocateOneFrameGlyph(0x69, 210, 147);
        allocateOneFrameGlyph(0x6a, 210, 157);
        final ActiveStatsa0 newStats = stats_800be5f8[charIndex];
        renderThreeDigitNumber(246, 127, newStats.equipmentAttack_88, 0x2);
        renderThreeDigitNumber(246, 137, newStats.equipmentDefence_8c, 0x2);
        renderThreeDigitNumber(246, 147, newStats.equipmentMagicAttack_8a, 0x2);
        renderThreeDigitNumber(246, 157, newStats.equipmentMagicDefence_8e, 0x2);
        allocateOneFrameGlyph(0x6b, 274, 127);
        allocateOneFrameGlyph(0x6b, 274, 137);
        allocateOneFrameGlyph(0x6b, 274, 147);
        allocateOneFrameGlyph(0x6b, 274, 157);
        loadCharacterStats();
        renderThreeDigitNumberComparison(284, 127, oldStats.equipmentAttack_88, newStats.equipmentAttack_88);
        renderThreeDigitNumberComparison(284, 137, oldStats.equipmentDefence_8c, newStats.equipmentDefence_8c);
        renderThreeDigitNumberComparison(284, 147, oldStats.equipmentMagicAttack_8a, newStats.equipmentMagicAttack_8a);
        renderThreeDigitNumberComparison(284, 157, oldStats.equipmentMagicDefence_8e, newStats.equipmentMagicDefence_8e);
      } else {
        renderText(Cannot_be_armed_with_8011c6d4, 228, 137, TextColour.BROWN);
      }

      gameState_800babc8.charData_32c[charIndex].equipment_14.clear();
      gameState_800babc8.charData_32c[charIndex].equipment_14.putAll(oldEquipment);

      loadCharacterStats();
    }
  }

  private void renderNumberOfItems(final Item item) {
    int count = 0;
    for(int i = 0; i < gameState_800babc8.items_2e9.size(); i++) {
      if(gameState_800babc8.items_2e9.get(i) == item) {
        count++;
      }
    }

    final LodString num = new LodString(11);
    intToStr(count, num);
    renderText(Number_kept_8011c7f4, 228, 137, TextColour.BROWN);
    renderText(num, 274, 137, TextColour.BROWN);
  }

  private void renderItemList(final int firstItem, final int isItemMenu, final Renderable58 upArrow, final Renderable58 downArrow) {
    if(isItemMenu != 0) {
      int i;
      for(i = 0; firstItem + i < gameState_800babc8.items_2e9.size() && i < 6; i++) {
        final Item item = gameState_800babc8.items_2e9.get(firstItem + i);
        renderItemIcon(item.getIcon(), 151, this.menuEntryY(i), 0x8L);
        renderText(new LodString(item.getName()), 168, this.menuEntryY(i) + 2, TextColour.BROWN);

        final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(shopId_8007a3b4.get(), item, item.getPrice()));
        this.FUN_801069d0(324, this.menuEntryY(i) + 4, event.price);
      }

      downArrow.setVisible(firstItem + 6 <= gameState_800babc8.items_2e9.size() - 1);
    } else {
      int i;
      for(i = 0; firstItem + i < gameState_800babc8.equipment_1e8.size() && i < 6; i++) {
        final Equipment equipment = gameState_800babc8.equipment_1e8.get(firstItem + i);
        renderItemIcon(equipment.icon_0e, 151, this.menuEntryY(i), 0x8L);
        renderText(new LodString(equipment.name), 168, this.menuEntryY(i) + 2, equipment.canBeDiscarded() ? TextColour.BROWN : TextColour.MIDDLE_BROWN);

        if(equipment.canBeDiscarded()) {
          final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(shopId_8007a3b4.get(), equipment, equipment.getPrice()));
          renderFiveDigitNumber(322, this.menuEntryY(i) + 4, event.price);
        } else {
          renderItemIcon(58, 330, this.menuEntryY(i), 0x8L).clut_30 = 0x7eaa;
        }
      }

      downArrow.setVisible(firstItem + 6 <= gameState_800babc8.equipment_1e8.size() - 1);
    }

    upArrow.setVisible(firstItem != 0);
  }

  private void renderMenuEntries(final List<ShopEntry<? extends InventoryEntry>> list, final int startItemIndex, final Renderable58 upArrow, final Renderable58 downArrow) {
    int i;
    for(i = 0; i < Math.min(6, list.size() - startItemIndex); i++) {
      final ShopEntry<? extends InventoryEntry> item = list.get(startItemIndex + i);
      renderText(new LodString(item.item.getName()), 168, this.menuEntryY(i) + 2, TextColour.BROWN);
      renderFiveDigitNumber(324, this.menuEntryY(i) + 4, item.price);
      renderItemIcon(item.item.getIcon(), 151, this.menuEntryY(i), 0x8L);
    }

    upArrow.setVisible(startItemIndex != 0);
    downArrow.setVisible(i + startItemIndex < list.size());
  }

  private Renderable58 allocateCharRenderable(final int x, final int y, final int glyph) {
    if(glyph >= 9) {
      return null;
    }

    final Renderable58 s0 = allocateRenderable(uiFile_800bdc3c.portraits_cfac(), null);
    initGlyph(s0, glyph_801142d4);
    s0.tpage_2c++;
    s0.glyph_04 = glyph;
    s0.z_3c = 33;
    s0.x_40 = x;
    s0.y_44 = y;

    return s0;
  }

  private void FUN_801069d0(final int x, final int y, final int value) {
    // I didn't look at this method too closely, this may or may not be right
    this.renderNumber(x, y, value, 4);
  }

  private int FUN_8010a818(final int slot) {
    return slot * 50 + 17;
  }

  private int getShopMenuYOffset(final int slot) {
    return slot * 16 + 58;
  }

  private void FUN_8010a844(final MenuState nextMenuState) {
    this.menuState = MenuState._16;
    this.confirmDest = nextMenuState;
  }

  @Method(0x8010a808L)
  public int menuEntryY(final int index) {
    return index * 17 + 18;
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    this.mouseX = x;
    this.mouseY = y;

    if(this.menuState == MenuState.RENDER_3) {
      for(int i = 0; i < 4; i++) {
        if(this.menuIndex_8011e0dc != i && MathHelper.inBox(x, y, 41, this.getShopMenuYOffset(i), 59, 16)) {
          playSound(1);
          this.menuIndex_8011e0dc = i;

          this.menuScroll_8011e0e4 = 0;
          this.menuIndex_8011e0e0 = 0;
          this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_4) {
      for(int i = 0; i < Math.min(6, this.inv.size() - this.menuScroll_8011e0e4); i++) {
        if(this.menuIndex_8011e0e0 != i && MathHelper.inBox(this.mouseX, this.mouseY, 138, this.menuEntryY(i) - 2, 220, 17)) {
          playSound(1);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(i);

          if(this.shopType == 0) {
            this.equipCharIndex = this.FUN_8010a864((Equipment)this.inv.get(this.menuScroll_8011e0e4 + i).item);
          }

          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_SELECT_CHAR_5) {
      for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
        if(this.equipCharIndex != i && MathHelper.inBox(x, y, this.FUN_8010a818(i) - 9, 174, 50, 48)) {
          playSound(1);
          this.equipCharIndex = i;
          this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.SELL_10) {
      final int count = this.shopType2 != 0 ? gameState_800babc8.items_2e9.size() : gameState_800babc8.equipment_1e8.size();

      for(int i = 0; i < Math.min(count, 6); i++) {
        if(this.menuIndex_8011e0e0 != i && MathHelper.inBox(this.mouseX, this.mouseY, 138, this.menuEntryY(i), 220, 17)) {
          playSound(1);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(i);
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState == MenuState.RENDER_3) {
      for(int i = 0; i < 4; i++) {
        if(MathHelper.inBox(x, y, 41, this.getShopMenuYOffset(i), 59, 16)) {
          playSound(2);
          this.menuIndex_8011e0dc = i;

          this.menuScroll_8011e0e4 = 0;
          this.menuIndex_8011e0e0 = 0;
          this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(i);

          this.handleSelectedMenu(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_4) {
      for(int i = 0; i < Math.min(6, this.inv.size() - this.menuScroll_8011e0e4); i++) {
        if(MathHelper.inBox(this.mouseX, this.mouseY, 138, this.menuEntryY(i) - 2, 220, 17)) {
          playSound(2);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(i);

          if(this.shopType == 0) {
            this.equipCharIndex = this.FUN_8010a864((Equipment)this.inv.get(this.menuScroll_8011e0e4 + i).item);
          }

          playSound(2);

          final boolean hasSpace;
          if(this.shopType == 0) {
            hasSpace = gameState_800babc8.equipment_1e8.size() < 255;
          } else {
            hasSpace = gameState_800babc8.items_2e9.size() < CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get());
          }

          if(!hasSpace) {
            menuStack.pushScreen(new MessageBoxScreen(new LodString("Cannot carry any more"), 0, result -> { }));
          } else if(gameState_800babc8.gold_94 < this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).price) {
            menuStack.pushScreen(new MessageBoxScreen(Not_enough_money_8011c468, 0, result -> { }));
          } else {
            if(this.shopType != 0) {
              menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
                if(result == MessageBoxResult.YES) {
                  gameState_800babc8.gold_94 -= this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).price;
                  giveItem((Item)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
                }
              }));
            } else {
              this.charHighlight = allocateUiElement(0x83, 0x83, this.FUN_8010a818(this.equipCharIndex), 174);
              FUN_80104b60(this.charHighlight);
              this.menuState = MenuState.BUY_SELECT_CHAR_5;
            }
          }

          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_SELECT_CHAR_5) {
      for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
        if(MathHelper.inBox(x, y, this.FUN_8010a818(i) - 9, 174, 50, 48)) {
          playSound(2);
          this.equipCharIndex = i;
          this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);

          menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
            if(result == MessageBoxResult.YES) {
              gameState_800babc8.gold_94 -= this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).price;

              menuStack.pushScreen(new MessageBoxScreen(new LodString("Equip item?"), 2, result1 -> {
                if(result1 == MessageBoxResult.YES && canEquip((Equipment)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item, characterIndices_800bdbb8.get(this.equipCharIndex).get())) {
                  final EquipItemResult equipResult = equipItem((Equipment)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item, characterIndices_800bdbb8.get(this.equipCharIndex).get());

                  if(equipResult.previousEquipment != null) {
                    giveEquipment(equipResult.previousEquipment);
                  }
                } else {
                  giveEquipment((Equipment)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
                }

                this.menuState = MenuState.BUY_4;
                unloadRenderable(this.charHighlight);
                this.charHighlight = null;
              }));
            }
          }));

          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.SELL_10) {
      final int count = this.shopType2 != 0 ? gameState_800babc8.items_2e9.size() : gameState_800babc8.equipment_1e8.size();

      for(int i = 0; i < Math.min(count, 6); i++) {
        if(MathHelper.inBox(this.mouseX, this.mouseY, 138, this.menuEntryY(i), 220, 17)) {
          playSound(2);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(i);

          final int slot = this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0;
          if(this.shopType2 != 0 && slot >= gameState_800babc8.items_2e9.size() || this.shopType2 == 0 && (slot >= gameState_800babc8.equipment_1e8.size() || !gameState_800babc8.equipment_1e8.get(slot).canBeDiscarded())) {
            playSound(40);
          } else {
            playSound(2);

            menuStack.pushScreen(new MessageBoxScreen(new LodString("Sell item?"), 2, result -> {
              if(Objects.requireNonNull(result) == MessageBoxResult.YES) {
                final InventoryEntry inv;
                final int v0;
                if(this.shopType2 != 0) {
                  inv = gameState_800babc8.items_2e9.get(slot);
                  v0 = takeItem(slot);
                } else {
                  inv = gameState_800babc8.equipment_1e8.get(slot);
                  v0 = takeEquipment(slot);
                }

                if(v0 == 0) {
                  final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(shopId_8007a3b4.get(), inv, inv.getPrice()));
                  addGold(event.price);

                  if(this.menuScroll_8011e0e4 > 0 && this.menuScroll_8011e0e4 + 6 > count - 1) {
                    this.menuScroll_8011e0e4--;
                  }

                  if(this.menuIndex_8011e0e0 != 0 && this.menuIndex_8011e0e0 > count - 2) {
                    this.menuIndex_8011e0e0--;
                    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.menuIndex_8011e0e0);
                  }
                }
              }
            }));
          }

          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  protected void handleSelectedMenu(final int i) {
    switch(i) {
      case 0 -> { // Buy
        this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, this.menuEntryY(this.menuIndex_8011e0e0));
        FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);

        if(this.shopType == 0) {
          this.equipCharIndex = this.FUN_8010a864((Equipment)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
        }

        this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, this.menuEntryY(0));
        this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, this.menuEntryY(5));
        this.menuState = MenuState.BUY_4;
      }

      case 1 -> // Sell
        menuStack.pushScreen(new MessageBoxScreen(new LodString("What do you want to sell?"), new LodString("Armed"), new LodString("Items"), 2, result -> {
          switch(result) {
            case YES -> {
              this.menuIndex_8011e0e0 = 0;
              this.menuScroll_8011e0e4 = 0;
              this.shopType2 = 0;

              if(!gameState_800babc8.equipment_1e8.isEmpty()) {
                this.menuState = MenuState.SELL_10;
                this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, this.menuEntryY(0));
                this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, this.menuEntryY(0));
                this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, this.menuEntryY(5));
                FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);
                this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(0));
              } else {
                menuStack.pushScreen(new MessageBoxScreen(new LodString("You have no equipment\nto sell"), 0, result1 -> {}));
              }
            }

            case NO -> {
              this.shopType2 = 1;
              this.menuScroll_8011e0e4 = 0;
              this.menuIndex_8011e0e0 = 0;

              if(!gameState_800babc8.items_2e9.isEmpty()) {
                this.menuState = MenuState.SELL_10;
                this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, this.menuEntryY(0));
                this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, this.menuEntryY(5));
                this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, this.menuEntryY(0));
                FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);
              } else {
                menuStack.pushScreen(new MessageBoxScreen(new LodString("You have no items\nto sell"), 0, result1 -> {
                }));
              }
            }
          }
        }));

      case 2 -> // Carried
        menuStack.pushScreen(new ItemListScreen(() -> {
          menuStack.popScreen();
          startFadeEffect(2, 10);
          this.menuState = MenuState.INIT_2;
        }));

      case 3 -> // Leave
        this.FUN_8010a844(MenuState.UNLOAD_19);
    }
  }

  @Override
  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    if(super.mouseScrollHighRes(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState != MenuState.BUY_4 && this.menuState != MenuState.SELL_10) {
      return InputPropagation.PROPAGATE;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
    return InputPropagation.HANDLED;
  }

  private void menuMainShopRender3Escape() {
    playSound(3);
    this.FUN_8010a844(MenuState.UNLOAD_19);
  }

  private void menuMainShopRender3Select() {
    playSound(2);
    this.handleSelectedMenu(this.menuIndex_8011e0dc);
  }

  private void menuMainShopRender3NavigateUp() {
    playSound(1);

    if(this.menuIndex_8011e0dc > 0) {
      this.menuIndex_8011e0dc--;
    }

    this.menuScroll_8011e0e4 = 0;
    this.menuIndex_8011e0e0 = 0;
    this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(this.menuIndex_8011e0dc);
  }

  private void menuMainShopRender3NavigateDown() {
    playSound(1);

    if(this.menuIndex_8011e0dc < 3) {
      this.menuIndex_8011e0dc++;
    }

    this.menuScroll_8011e0e4 = 0;
    this.menuIndex_8011e0e0 = 0;
    this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(this.menuIndex_8011e0dc);
  }

  private void menuBuy4Escape() {
    playSound(3);
    this.menuState = MenuState.INIT_2;
  }

  private void menuBuy4Select() {
    playSound(2);

    final boolean hasSpace;
    if(this.shopType == 0) {
      hasSpace = gameState_800babc8.equipment_1e8.size() < 255;
    } else {
      hasSpace = gameState_800babc8.items_2e9.size() < CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get());
    }

    if(!hasSpace) {
      menuStack.pushScreen(new MessageBoxScreen(new LodString("Cannot carry anymore"), 0, result -> { }));
    } else if(gameState_800babc8.gold_94 < this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).price) {
      menuStack.pushScreen(new MessageBoxScreen(Not_enough_money_8011c468, 0, result -> { }));
    } else if(this.shopType != 0) {
      menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
        if(result == MessageBoxResult.YES) {
          gameState_800babc8.gold_94 -= this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).price;
          giveItem((Item)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
        }
      }));
    } else {
      this.charHighlight = allocateUiElement(0x83, 0x83, this.FUN_8010a818(this.equipCharIndex), 174);
      FUN_80104b60(this.charHighlight);
      this.menuState = MenuState.BUY_SELECT_CHAR_5;
    }
  }

  private void menuBuy4NavigateUp() {
    playSound(1);

    if(this.menuIndex_8011e0e0 > 0) {
      this.menuIndex_8011e0e0--;
    } else if(this.menuScroll_8011e0e4 > 0) {
      this.menuScroll_8011e0e4--;
    }

    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.menuIndex_8011e0e0);

    if(this.shopType == 0) {
      this.equipCharIndex = this.FUN_8010a864((Equipment)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
    }
  }

  private void menuBuy4NavigateDown() {
    playSound(1);

    if(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0 >= this.inv.size()) {
      playSound(40);
      return;
    }

    if(this.menuIndex_8011e0e0 < 5 && this.menuIndex_8011e0e0 < this.inv.size() - 1) {
      this.menuIndex_8011e0e0++;
    } else if((this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0) < this.inv.size() - 1) {
      this.menuScroll_8011e0e4++;
    }

    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.menuIndex_8011e0e0);

    if(this.shopType == 0) {
      this.equipCharIndex = this.FUN_8010a864((Equipment)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
    }
  }

  private void menuSelectChar5Escape() {
    playSound(3);
    this.menuState = MenuState.BUY_4;
    unloadRenderable(this.charHighlight);
    this.charHighlight = null;
  }

  private void menuSelectChar5Select() {
    menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
      if(result == MessageBoxResult.YES) {
        gameState_800babc8.gold_94 -= this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).price;

        menuStack.pushScreen(new MessageBoxScreen(new LodString("Equip item?"), 2, result1 -> {
          if(result1 == MessageBoxResult.YES && canEquip((Equipment)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item, characterIndices_800bdbb8.get(this.equipCharIndex).get())) {
            final EquipItemResult equipResult = equipItem((Equipment)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item, characterIndices_800bdbb8.get(this.equipCharIndex).get());

            if(equipResult.previousEquipment != null) {
              giveEquipment(equipResult.previousEquipment);
            }
          } else {
            giveEquipment((Equipment)this.inv.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
          }

          this.menuState = MenuState.BUY_4;
          unloadRenderable(this.charHighlight);
          this.charHighlight = null;
        }));
      }
    }));
  }

  private void menuSelectChar5NavigateLeft() {
    playSound(1);

    if(this.equipCharIndex > 0) {
      this.equipCharIndex--;
    }

    this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
  }

  private void menuSelectChar5NavigateRight() {
    playSound(1);

    if(this.equipCharIndex < characterCount_8011d7c4.get() - 1) {
      this.equipCharIndex++;
    }

    this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
  }

  private void menuSell10Escape() {
    playSound(3);
    unloadRenderable(this.selectedMenuOptionRenderablePtr_800bdbe4);
    this.menuState = MenuState.INIT_2;
  }

  private void menuSell10Select() {
    final int slot = this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0;
    if(this.shopType2 != 0 && slot >= gameState_800babc8.items_2e9.size() || this.shopType2 == 0 && (slot >= gameState_800babc8.equipment_1e8.size() || !gameState_800babc8.equipment_1e8.get(slot).canBeDiscarded())) {
      playSound(40);
    } else {
      playSound(2);

      menuStack.pushScreen(new MessageBoxScreen(new LodString("Sell item?"), 2, result -> {
        if(Objects.requireNonNull(result) == MessageBoxResult.YES) {
          final InventoryEntry inv;
          final int v0;
          final int count;
          if(this.shopType2 != 0) {
            inv = gameState_800babc8.items_2e9.get(slot);
            v0 = takeItem(slot);
            count = gameState_800babc8.items_2e9.size();
          } else {
            inv = gameState_800babc8.equipment_1e8.get(slot);
            v0 = takeEquipment(slot);
            count = gameState_800babc8.equipment_1e8.size();
          }

          if(v0 == 0) {
            final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(shopId_8007a3b4.get(), inv, inv.getPrice()));
            addGold(event.price);

            if(this.menuScroll_8011e0e4 > 0 && this.menuScroll_8011e0e4 + 6 > count) {
              this.menuScroll_8011e0e4--;
            }

            if(this.menuIndex_8011e0e0 != 0 && this.menuIndex_8011e0e0 > count - 1) {
              this.menuIndex_8011e0e0--;
              this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.menuIndex_8011e0e0);
            }
          }
        }
      }));
    }
  }

  private void menuSell10NavigateUp() {
    playSound(1);

    if(this.menuIndex_8011e0e0 > 0) {
      this.menuIndex_8011e0e0--;
    } else if(this.menuScroll_8011e0e4 > 0) {
      this.menuScroll_8011e0e4--;
    }

    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.menuIndex_8011e0e0);

  }

  private void menuSell10NavigateDown() {
    playSound(1);

    final int itemCount;
    if(this.shopType2 == 0) { // equipment
      itemCount = gameState_800babc8.equipment_1e8.size();
    } else { // items
      itemCount = gameState_800babc8.items_2e9.size();
    }

    if(this.menuIndex_8011e0e0 < 5) {
      this.menuIndex_8011e0e0++;
    } else if((this.menuIndex_8011e0e0 + this.menuScroll_8011e0e4) < itemCount - 1) {
      this.menuScroll_8011e0e4++;
    } else {
      playSound(40);
    }

    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.menuIndex_8011e0e0);
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    switch(this.menuState) {
      case RENDER_3 -> {
        if(inputAction == InputAction.BUTTON_EAST) {
          this.menuMainShopRender3Escape();
          return InputPropagation.HANDLED;
        }

        if(inputAction == InputAction.BUTTON_SOUTH) {
          this.menuMainShopRender3Select();
          return InputPropagation.HANDLED;
        }
      }

      case BUY_4 -> {
        if(inputAction == InputAction.BUTTON_EAST) {
          this.menuBuy4Escape();
          return InputPropagation.HANDLED;
        }

        if(inputAction == InputAction.BUTTON_SOUTH) {
          this.menuBuy4Select();
          return InputPropagation.HANDLED;
        }
      }

      case BUY_SELECT_CHAR_5 -> {
        if(inputAction == InputAction.DPAD_LEFT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_LEFT) {
          this.menuSelectChar5NavigateLeft();
          return InputPropagation.HANDLED;
        }

        if(inputAction == InputAction.DPAD_RIGHT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_RIGHT) {
          this.menuSelectChar5NavigateRight();
          return InputPropagation.HANDLED;
        }

        if(inputAction == InputAction.BUTTON_EAST) {
          this.menuSelectChar5Escape();
          return InputPropagation.HANDLED;
        }

        if(inputAction == InputAction.BUTTON_SOUTH) {
          this.menuSelectChar5Select();
          return InputPropagation.HANDLED;
        }
      }

      case SELL_10 -> {
        if(inputAction == InputAction.BUTTON_EAST) {
          this.menuSell10Escape();
          return InputPropagation.HANDLED;
        }

        if(inputAction == InputAction.BUTTON_SOUTH) {
          this.menuSell10Select();
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    switch(this.menuState) {
      case RENDER_3 -> {
        if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
          this.menuMainShopRender3NavigateUp();
          return InputPropagation.HANDLED;
        }

        if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
          this.menuMainShopRender3NavigateDown();
          return InputPropagation.HANDLED;
        }
      }

      case BUY_4 -> {
        if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
          this.menuBuy4NavigateUp();
          return InputPropagation.HANDLED;
        }

        if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
          this.menuBuy4NavigateDown();
          return InputPropagation.HANDLED;
        }
      }

      case SELL_10 -> {
        if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
          this.menuSell10NavigateUp();
          return InputPropagation.HANDLED;
        }

        if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
          this.menuSell10NavigateDown();
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  public static class ShopEntry<T extends InventoryEntry> {
    public final T item;
    public final int price;

    public ShopEntry(final T item, final int price) {
      this.item = item;
      this.price = price;
    }
  }

  public enum MenuState {
    INIT_0,
    LOAD_ITEMS_1,
    INIT_2,
    RENDER_3,
    BUY_4,
    BUY_SELECT_CHAR_5,
    SELL_10,
    _16,
    _17,
    UNLOAD_19,
  }
}
