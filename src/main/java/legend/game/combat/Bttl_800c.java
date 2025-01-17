package legend.game.combat;

import legend.core.MathHelper;
import legend.core.Random;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdObjTable1c;
import legend.core.memory.Method;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.ComponentFunction;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuintConsumer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.EngineStateEnum;
import legend.game.Scus94491BpeSegment_8005;
import legend.game.characters.Element;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.BattleEntityStat;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.deff.BattleStruct24_2;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.effects.Attachment18;
import legend.game.combat.effects.ButtonPressHudMetrics06;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerData6cInner;
import legend.game.combat.effects.FullScreenOverlayEffect0e;
import legend.game.combat.effects.GuardEffectMetrics04;
import legend.game.combat.effects.ModelEffect13c;
import legend.game.combat.effects.RadialGradientEffect14;
import legend.game.combat.effects.SpriteMetrics08;
import legend.game.combat.effects.WeaponTrailEffect3c;
import legend.game.combat.effects.WeaponTrailEffectSegment2c;
import legend.game.combat.environment.ActiveStageData2c;
import legend.game.combat.environment.BattleCamera;
import legend.game.combat.environment.BattleHudBorderMetrics14;
import legend.game.combat.environment.BattleItemMenuArrowUvMetrics06;
import legend.game.combat.environment.BattleLightStruct64;
import legend.game.combat.environment.BattleMenuBackgroundDisplayMetrics0c;
import legend.game.combat.environment.BattleMenuBackgroundUvMetrics04;
import legend.game.combat.environment.BattleMenuHighlightMetrics12;
import legend.game.combat.environment.BattleMenuIconMetrics08;
import legend.game.combat.environment.BattleMenuTextMetrics08;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.combat.environment.BattleStage;
import legend.game.combat.environment.BattleStageDarkening1800;
import legend.game.combat.environment.BttlLightStruct84;
import legend.game.combat.environment.CameraOctParamCallback;
import legend.game.combat.environment.CameraQuadParamCallback;
import legend.game.combat.environment.CameraSeptParamCallback;
import legend.game.combat.environment.CombatPortraitBorderMetrics0c;
import legend.game.combat.environment.NameAndPortraitDisplayMetrics0c;
import legend.game.combat.environment.SpBarBorderMetrics04;
import legend.game.combat.types.BattleHudStatLabelMetrics0c;
import legend.game.combat.types.BattleObject;
import legend.game.combat.types.BattleStateEf4;
import legend.game.combat.types.BttlStruct08;
import legend.game.combat.types.CombatantAsset0c;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.DragoonSpells09;
import legend.game.combat.types.EnemyDrop;
import legend.game.combat.ui.BattleDisplayStats144;
import legend.game.combat.ui.BattleHudCharacterDisplay3c;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.combat.ui.CombatItem02;
import legend.game.combat.ui.CombatMenua4;
import legend.game.combat.ui.FloatingNumberC4;
import legend.game.fmv.Fmv;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.BattleEndedEvent;
import legend.game.modding.events.battle.BattleEntityTurnEvent;
import legend.game.modding.events.battle.BattleStartedEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.IntParam;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptEnum;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.tim.Tim;
import legend.game.types.CContainer;
import legend.game.types.CharacterData2c;
import legend.game.types.GsF_LIGHT;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.SpellStats0c;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.FUN_80013404;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.btldLoadEncounterSoundEffectsAndMusic;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.getCharacterName;
import static legend.game.Scus94491BpeSegment.loadDir;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.loadMcq;
import static legend.game.Scus94491BpeSegment.loadMusicPackage;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.renderMcq;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.setDepthResolution;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.startEncounterSounds;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment.stopAndResetSoundsAndSequences;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020308;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021520;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.scriptDeallocateAllTextboxes;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.MoveImage;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.previousEngineState_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.sssqFadeOut;
import static legend.game.Scus94491BpeSegment_8005.combatants_8005e398;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.clearRed_8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.battleFlags_800bc960;
import static legend.game.Scus94491BpeSegment_800b.battleLoaded_800bc94c;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.clearBlue_800babc0;
import static legend.game.Scus94491BpeSegment_800b.clearGreen_800bb104;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.equipmentOverflow;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
import static legend.game.Scus94491BpeSegment_800b.itemOverflow;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928;
import static legend.game.Scus94491BpeSegment_800b.livingCharCount_800bc97c;
import static legend.game.Scus94491BpeSegment_800b.livingCharIds_800bc968;
import static legend.game.Scus94491BpeSegment_800b.postBattleActionIndex_800bc974;
import static legend.game.Scus94491BpeSegment_800b.postCombatMainCallbackIndex_800bc91c;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;
import static legend.game.Scus94491BpeSegment_800b.unlockedUltimateAddition_800bc910;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Bttl_800d.calculateXAngleFromRefpointToViewpoint;
import static legend.game.combat.Bttl_800d.calculateYAngleFromRefpointToViewpoint;
import static legend.game.combat.Bttl_800d.resetCameraMovement;
import static legend.game.combat.Bttl_800e.FUN_800ee610;
import static legend.game.combat.Bttl_800e.FUN_800ef9e4;
import static legend.game.combat.Bttl_800e.allocateDeffManager;
import static legend.game.combat.Bttl_800e.allocateEffectManager;
import static legend.game.combat.Bttl_800e.allocateStageDarkeningStorage;
import static legend.game.combat.Bttl_800e.backupStageClut;
import static legend.game.combat.Bttl_800e.deallocateLightingControllerAndDeffManager;
import static legend.game.combat.Bttl_800e.deallocateStageDarkeningStorage;
import static legend.game.combat.Bttl_800e.drawUiElements;
import static legend.game.combat.Bttl_800e.loadBattleHudDeff;
import static legend.game.combat.Bttl_800e.loadStageTmd;
import static legend.game.combat.Bttl_800e.renderBattleStage;
import static legend.game.combat.Bttl_800e.renderBttlModel;
import static legend.game.combat.Bttl_800e.rotateBattleStage;
import static legend.game.combat.Bttl_800e.updateGameStateAndDeallocateMenu;
import static legend.game.combat.Bttl_800f.FUN_800f1a00;
import static legend.game.combat.Bttl_800f.FUN_800f417c;
import static legend.game.combat.Bttl_800f.FUN_800f60ac;
import static legend.game.combat.Bttl_800f.FUN_800f84c0;
import static legend.game.combat.Bttl_800f.FUN_800f863c;
import static legend.game.combat.Bttl_800f.addFloatingNumberForBent;
import static legend.game.combat.Bttl_800f.handleCombatMenu;
import static legend.game.combat.Bttl_800f.initializeCombatMenuIcons;
import static legend.game.combat.Bttl_800f.loadBattleHudTextures;
import static legend.game.combat.Bttl_800f.toggleBattleMenuSelectorRendering;
import static legend.game.combat.SEffe.scriptGetPositionScalerAttachmentVelocity;

public final class Bttl_800c {
  private Bttl_800c() { }

  public static final Vector3f ZERO = new Vector3f();

  public static final UnsignedShortRef currentPostCombatActionFrame_800c6690 = MEMORY.ref(2, 0x800c6690L, UnsignedShortRef::new);

  public static final IntRef aliveBentCount_800c669c = MEMORY.ref(4, 0x800c669cL, IntRef::new);
  /** The number of {@link Scus94491BpeSegment_8005#combatants_8005e398}s */
  public static final IntRef combatantCount_800c66a0 = MEMORY.ref(4, 0x800c66a0L, IntRef::new);
  public static final IntRef currentStage_800c66a4 = MEMORY.ref(4, 0x800c66a4L, IntRef::new);

  public static final IntRef _800c66a8 = MEMORY.ref(4, 0x800c66a8L, IntRef::new);
  public static final ShortRef _800c66ac = MEMORY.ref(2, 0x800c66acL, ShortRef::new);

  public static final IntRef currentCameraPositionIndicesIndex_800c66b0 = MEMORY.ref(4, 0x800c66b0L, IntRef::new);

  public static final IntRef _800c66b4 = MEMORY.ref(4, 0x800c66b4L, IntRef::new);
  public static final BoolRef stageHasModel_800c66b8 = MEMORY.ref(1, 0x800c66b8L, BoolRef::new);
  /** Character scripts deallocated? */
  public static final BoolRef combatDisabled_800c66b9 = MEMORY.ref(1, 0x800c66b9L, BoolRef::new);

  public static ScriptState<? extends BattleEntity27c> forcedTurnBent_800c66bc;
  /** These two bools are set but never used */
  public static final BoolRef unused_800c66c0 = MEMORY.ref(1, 0x800c66c0L, BoolRef::new);
  public static final BoolRef unused_800c66c1 = MEMORY.ref(1, 0x800c66c1L, BoolRef::new);

  public static final IntRef _800c66c4 = MEMORY.ref(4, 0x800c66c4L, IntRef::new);
  public static ScriptState<? extends BattleEntity27c> currentTurnBent_800c66c8;
  public static final IntRef mcqBaseOffsetX_800c66cc = MEMORY.ref(4, 0x800c66ccL, IntRef::new);
  public static final IntRef allBentCount_800c66d0 = MEMORY.ref(4, 0x800c66d0L, IntRef::new);
  public static final BoolRef shouldRenderMcq_800c66d4 = MEMORY.ref(1, 0x800c66d4L, BoolRef::new);

  public static ScriptFile script_800c66fc;

  public static final IntRef _800c6700 = MEMORY.ref(4, 0x800c6700L, IntRef::new);
  public static final IntRef _800c6704 = MEMORY.ref(4, 0x800c6704L, IntRef::new);

  public static final IntRef _800c6710 = MEMORY.ref(4, 0x800c6710L, IntRef::new);

  /** Struct for combat stage stuff */
  public static final ActiveStageData2c currentStageData_800c6718 = new ActiveStageData2c();
  public static final IntRef _800c6748 = MEMORY.ref(4, 0x800c6748L, IntRef::new);
  public static ScriptState<Void> scriptState_800c674c;

  public static final IntRef _800c6754 = MEMORY.ref(4, 0x800c6754L, IntRef::new);
  public static final IntRef aliveMonsterCount_800c6758 = MEMORY.ref(4, 0x800c6758L, IntRef::new);
  public static final IntRef currentDisplayableIconsBitset_800c675c = MEMORY.ref(4, 0x800c675cL, IntRef::new);
  public static final IntRef aliveCharCount_800c6760 = MEMORY.ref(4, 0x800c6760L, IntRef::new);
  public static final IntRef shouldRenderMcq_800c6764 = MEMORY.ref(4, 0x800c6764L, IntRef::new);
  public static final IntRef monsterCount_800c6768 = MEMORY.ref(4, 0x800c6768L, IntRef::new);
  public static final IntRef mcqStepX_800c676c = MEMORY.ref(4, 0x800c676cL, IntRef::new);
  public static final IntRef mcqStepY_800c6770 = MEMORY.ref(4, 0x800c6770L, IntRef::new);
  public static final IntRef mcqOffsetX_800c6774 = MEMORY.ref(4, 0x800c6774L, IntRef::new);
  public static final IntRef mcqOffsetY_800c6778 = MEMORY.ref(4, 0x800c6778L, IntRef::new);
  /** The number of player chars in combat (i.e. 1-3) */
  public static final IntRef charCount_800c677c = MEMORY.ref(4, 0x800c677cL, IntRef::new);
  /** This may be unused. Only referenced by the script engine, but seems like there may be no real uses */
  public static final IntRef currentCameraIndex_800c6780 = MEMORY.ref(4, 0x800c6780L, IntRef::new);

  public static final Pointer<CString> currentAddition_800c6790 = MEMORY.ref(4, 0x800c6790L, Pointer.deferred(1, CString.maxLength(30)));

  public static final MV cameraTransformMatrix_800c6798 = new MV();
  // public static final UnsignedIntRef flags_800c67b8 = MEMORY.ref(4, 0x800c67b8L, UnsignedIntRef::new);
  public static final IntRef screenOffsetX_800c67bc = MEMORY.ref(4, 0x800c67bcL, IntRef::new);
  public static final IntRef screenOffsetY_800c67c0 = MEMORY.ref(4, 0x800c67c0L, IntRef::new);
  public static final IntRef wobbleFramesRemaining_800c67c4 = MEMORY.ref(4, 0x800c67c4L, IntRef::new);
  public static final IntRef _800c67c8 = MEMORY.ref(4, 0x800c67c8L, IntRef::new);
  public static final IntRef _800c67cc = MEMORY.ref(4, 0x800c67ccL, IntRef::new);
  public static final IntRef _800c67d0 = MEMORY.ref(4, 0x800c67d0L, IntRef::new);

  public static final IntRef framesUntilWobble_800c67d4 = MEMORY.ref(4, 0x800c67d4L, IntRef::new);

  public static final IntRef cameraWobbleOffsetX_800c67e4 = MEMORY.ref(4, 0x800c67e4L, IntRef::new);
  public static final IntRef cameraWobbleOffsetY_800c67e8 = MEMORY.ref(4, 0x800c67e8L, IntRef::new);

  public static final BattleCamera camera_800c67f0 = new BattleCamera();

  public static ScriptState<? extends BattleEntity27c> scriptState_800c6914;
  public static final IntRef _800c6918 = MEMORY.ref(4, 0x800c6918L, IntRef::new);

  public static final IntRef lightTicks_800c6928 = MEMORY.ref(4, 0x800c6928L, IntRef::new);
  public static BttlLightStruct84[] lights_800c692c;
  public static BattleLightStruct64 _800c6930;

  public static BattleStruct24_2 _800c6938;
  public static DeffManager7cc deffManager_800c693c;
  /** Dunno what this is for, it's set to a pointer but unused. I removed the set for now. */
  public static final IntRef _800c6940 = MEMORY.ref(4, 0x800c6940L, IntRef::new);
  public static TmdObjTable1c[] tmds_800c6944;
  public static SpriteMetrics08[] spriteMetrics_800c6948;

  public static BattleStageDarkening1800 stageDarkening_800c6958;
  public static int stageDarkeningClutWidth_800c695c;

  public static final ArrayRef<DragoonSpells09> dragoonSpells_800c6960 = MEMORY.ref(1, 0x800c6960L, ArrayRef.of(DragoonSpells09.class, 3, 9, DragoonSpells09::new));

  /** These three are related to targeting */
  public static final ShortRef _800c697c = MEMORY.ref(2, 0x800c697cL, ShortRef::new);
  public static final ShortRef _800c697e = MEMORY.ref(2, 0x800c697eL, ShortRef::new);
  public static final ShortRef _800c6980 = MEMORY.ref(2, 0x800c6980L, ShortRef::new);

  public static final List<CombatItem02> combatItems_800c6988 = new ArrayList<>();
  public static final BoolRef itemTargetAll_800c69c8 = MEMORY.ref(4, 0x800c69c8L, BoolRef::new);

  public static final ArrayRef<LodString> currentEnemyNames_800c69d0 = MEMORY.ref(2, 0x800c69d0L, ArrayRef.of(LodString.class, 9, 0x2c, LodString::new));

  public static final FloatingNumberC4[] floatingNumbers_800c6b5c = new FloatingNumberC4[12];
  static {
    Arrays.setAll(floatingNumbers_800c6b5c, i -> new FloatingNumberC4());
  }
  public static CombatMenua4 combatMenu_800c6b60;
  public static Element dragoonSpaceElement_800c6b64;
  public static final IntRef itemTargetType_800c6b68 = MEMORY.ref(4, 0x800c6b68L, IntRef::new);

  public static final ArrayRef<IntRef> monsterBents_800c6b78 = MEMORY.ref(4, 0x800c6b78L, ArrayRef.of(IntRef.class, 9, 4, IntRef::new));
  public static final IntRef monsterCount_800c6b9c = MEMORY.ref(4, 0x800c6b9cL, IntRef::new);
  public static final ByteRef countCameraPositionIndicesIndices_800c6ba0 = MEMORY.ref(1, 0x800c6ba0L, ByteRef::new);
  public static final ByteRef currentCameraPositionIndicesIndicesIndex_800c6ba1 = MEMORY.ref(1, 0x800c6ba1L, ByteRef::new);

  /** Uhh, contains the monsters that Melbu summons during his fight...? */
  public static final ArrayRef<LodString> melbuMonsterNames_800c6ba8 = MEMORY.ref(2, 0x800c6ba8L, ArrayRef.of(LodString.class, 3, 0x2c, LodString::new));

  /**
   * One per character slot
   */
  public static final BattleDisplayStats144[] displayStats_800c6c2c = new BattleDisplayStats144[3];
  static {
    Arrays.setAll(displayStats_800c6c2c, i -> new BattleDisplayStats144());
  }

  public static final ArrayRef<UnsignedByteRef> cameraPositionIndicesIndices_800c6c30 = MEMORY.ref(4, 0x800c6c30L, ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new));

  public static BattleMenuStruct58 battleMenu_800c6c34;
  /** Only ever set to 1. 0 will set it to the top of the screen. */
  public static final IntRef battleHudYOffsetIndex_800c6c38 = MEMORY.ref(4, 0x800c6c38L, IntRef::new);
  public static final List<Item> usedRepeatItems_800c6c3c = new ArrayList<>();

  public static final ArrayRef<BattleHudCharacterDisplay3c> activePartyBattleHudCharacterDisplays_800c6c40 = MEMORY.ref(2, 0x800c6c40L, ArrayRef.of(BattleHudCharacterDisplay3c.class, 3, 0x3c, BattleHudCharacterDisplay3c::new));

  public static final IntRef countCombatUiFilesLoaded_800c6cf4 = MEMORY.ref(4, 0x800c6cf4L, IntRef::new);

  public static final Vector3i[] completedAdditionStarburstTranslationMagnitudes_800c6d94 = {
    new Vector3i(360, 210, 210),
    new Vector3i(210,  60, 210),
    new Vector3i(360, 210, 210),
    new Vector3i( 60, 210, 210),
  };
  public static final Vector3f[] completedAdditionStarburstAngleModifiers_800c6dac = {
    new Vector3f(0, MathHelper.psxDegToRad(-16), 0),
    new Vector3f(MathHelper.psxDegToRad(-16), 0, 0),
    new Vector3f(0, MathHelper.psxDegToRad(16), 0),
    new Vector3f(0, MathHelper.psxDegToRad(16), 0),
  };

  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#renderAdditionHitStarburst}</li>
   *   <li>{@link Bttl_800d#renderAdditionCompletedStarburst}</li>
   *   <li>{@link Bttl_800d#renderAdditionCompletedStarburst}</li>
   * </ol>
   */
  public static final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>>, EffectManagerData6c<EffectManagerData6cInner.VoidType>>[] additionStarburstRenderers_800c6dc4 = new BiConsumer[3];
  static {
    additionStarburstRenderers_800c6dc4[0] = Bttl_800d::renderAdditionHitStarburst;
    additionStarburstRenderers_800c6dc4[1] = Bttl_800d::renderAdditionCompletedStarburst;
    additionStarburstRenderers_800c6dc4[2] = Bttl_800d::renderAdditionCompletedStarburst;
  }

  public static final GsF_LIGHT light_800c6ddc = new GsF_LIGHT(1.0f, 1.0f, 1.0f);

  public static final ArrayRef<UnsignedShortRef> repeatItemIds_800c6e34 = MEMORY.ref(2, 0x800c6e34L, ArrayRef.of(UnsignedShortRef.class, 9, 2, UnsignedShortRef::new));

  public static final Vector2i[] combatUiElementRectDimensions_800c6e48 = {
    new Vector2i(16, 16),
    new Vector2i(16, 16),
    new Vector2i(16, 16),
    new Vector2i(16, 16),
    new Vector2i(16, 16),
    new Vector2i(16, 16),
  };
  /** Note: retail overlay doesn't have the last two elements, but the method that uses this copies the array and adds new elements */
  public static final int[] battleHudTextureVramXOffsets_800c6e60 = {0, 0x10, 0x20, 0x30, 0, 0x10};
  public static final ArrayRef<UnsignedIntRef> characterDragoonIndices_800c6e68 = MEMORY.ref(4, 0x800c6e68L, ArrayRef.of(UnsignedIntRef.class, 10, 4, UnsignedIntRef::new));

  public static final ArrayRef<IntRef> melbuMonsterNameIndices = MEMORY.ref(4, 0x800c6e90L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));

  public static final ArrayRef<CombatPortraitBorderMetrics0c> combatPortraitBorderVertexCoords_800c6e9c = MEMORY.ref(1, 0x800c6e9cL, ArrayRef.of(CombatPortraitBorderMetrics0c.class, 4, 0xc, CombatPortraitBorderMetrics0c::new));
  public static final ArrayRef<BattleHudStatLabelMetrics0c> battleHudStatLabelMetrics_800c6ecc = MEMORY.ref(1, 0x800c6eccL, ArrayRef.of(BattleHudStatLabelMetrics0c.class, 3, 0xc, BattleHudStatLabelMetrics0c::new));

  public static final ArrayRef<ArrayRef<UnsignedByteRef>> spBarColours_800c6f04 = MEMORY.ref(1, 0x800c6f04L, ArrayRef.of(ArrayRef.classFor(UnsignedByteRef.class), 7, 6, ArrayRef.of(UnsignedByteRef.class, 6, 1, UnsignedByteRef::new)));

  public static final ArrayRef<IntRef> melbuStageToMonsterNameIndices_800c6f30 = MEMORY.ref(4, 0x800c6f30L, ArrayRef.of(IntRef.class, 7, 4, IntRef::new));
  public static final ArrayRef<BattleHudBorderMetrics14> battleHudBorderMetrics_800c6f4c = MEMORY.ref(2, 0x800c6f4cL, ArrayRef.of(BattleHudBorderMetrics14.class, 8, 20, BattleHudBorderMetrics14::new));
  public static final ArrayRef<ArrayRef<UnsignedByteRef>> textboxColours_800c6fec = MEMORY.ref(1, 0x800c6fecL, ArrayRef.of(ArrayRef.classFor(UnsignedByteRef.class), 9, 3, ArrayRef.of(UnsignedByteRef.class, 3, 1, UnsignedByteRef::new)));

  public static final ArrayRef<ShortRef> digitOffsetXy_800c7014 = MEMORY.ref(2, 0x800c7014L, ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));
  public static final ArrayRef<UnsignedShortRef> floatingTextType1DigitUs_800c7028 = MEMORY.ref(2, 0x800c7028L, ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));

  @SuppressWarnings("unchecked")
  public static final RegistryDelegate<Element>[] characterElements_800c706c = new RegistryDelegate[] {CoreMod.FIRE_ELEMENT, CoreMod.WIND_ELEMENT, CoreMod.LIGHT_ELEMENT, CoreMod.DARK_ELEMENT, CoreMod.THUNDER_ELEMENT, CoreMod.WIND_ELEMENT, CoreMod.WATER_ELEMENT, CoreMod.EARTH_ELEMENT, CoreMod.LIGHT_ELEMENT};

  public static final ArrayRef<ShortRef> floatingTextType3DigitUs_800c70e0 = MEMORY.ref(2, 0x800c70e0L, ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));

  public static final ArrayRef<ShortRef> floatingTextDigitClutOffsets_800c70f4 = MEMORY.ref(2, 0x800c70f4L, ArrayRef.of(ShortRef.class, 15, 2, ShortRef::new));

  public static final Vector2i[] battleUiElementClutVramXy_800c7114 = {
    new Vector2i(0x2c0, 0x1f0),
    new Vector2i(0x380, 0x130),
  };
  public static final ArrayRef<UnsignedShortRef> targetAllItemIds_800c7124 = MEMORY.ref(2, 0x800c7124L, ArrayRef.of(UnsignedShortRef.class, 17, 2, UnsignedShortRef::new));

  public static final BattleMenuBackgroundUvMetrics04 battleItemMenuScrollArrowUvMetrics_800c7190 = MEMORY.ref(1, 0x800c7190L, BattleMenuBackgroundUvMetrics04::new);
  public static final ArrayRef<ShortRef> iconFlags_800c7194 = MEMORY.ref(2, 0x800c7194L, ArrayRef.of(ShortRef.class, 8, 2, ShortRef::new));

  public static final BattleMenuHighlightMetrics12 battleMenuHighlightMetrics_800c71bc = MEMORY.ref(2, 0x800c71bcL, BattleMenuHighlightMetrics12::new);
  public static final ArrayRef<ShortRef> dragoonSpiritIconClutOffsets_800c71d0 = MEMORY.ref(2, 0x800c71d0L, ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> battleMenuIconStates_800c71e4 = MEMORY.ref(2, 0x800c71e4L, ArrayRef.of(ShortRef.class, 4, 2, ShortRef::new));
  public static final ArrayRef<ByteRef> uiTextureElementBrightness_800c71ec = MEMORY.ref(1, 0x800c71ecL, ArrayRef.of(ByteRef.class, 3, 1, ByteRef::new));

  /** Different sets of bents for different target types (chars, monsters, all) */
  public static ScriptState<BattleEntity27c>[][] targetBents_800c71f0;

  public static final ArrayRef<UnsignedShortRef> protectedItems_800c72cc = MEMORY.ref(2, 0x800c72ccL, ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));

  public static final SpellStats0c[] spellStats_800fa0b8 = new SpellStats0c[128];
  public static final ArrayRef<ShortRef> postCombatActionTotalFrames_800fa6b8 = MEMORY.ref(2, 0x800fa6b8L, ArrayRef.of(ShortRef.class, 6, 2, ShortRef::new));

  public static final ArrayRef<ShortRef> postBattleActions_800fa6c4 = MEMORY.ref(2, 0x800fa6c4L, ArrayRef.of(ShortRef.class, 6, 2, ShortRef::new));

  public static final ArrayRef<ShortRef> postCombatActionFrames_800fa6d0 = MEMORY.ref(2, 0x800fa6d0L, ArrayRef.of(ShortRef.class, 6, 2, ShortRef::new));

  public static final IntRef mcqColour_800fa6dc = MEMORY.ref(4, 0x800fa6dcL, IntRef::new);
  public static final UnboundedArrayRef<RECT> combatantTimRects_800fa6e0 = MEMORY.ref(2, 0x800fa6e0L, UnboundedArrayRef.of(0x8, RECT::new));

  public static final ArrayRef<ShortRef> colourMaps_800fa730 = MEMORY.ref(2, 0x800fa730L, ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));

  public static final ArrayRef<UnsignedShortRef> additionNextLevelXp_800fa744 = MEMORY.ref(2, 0x800fa744L, ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));

  public static final Random seed_800fa754 = new Random();
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#renderDiscGradientEffect}</li>
   *   <li>{@link Bttl_800d#FUN_800d1e80}</li>
   *   <li>{@link Bttl_800d#renderRingGradientEffect}</li>
   *   <li>{@link Bttl_800d#renderDiscGradientEffect}</li>
   *   <li>{@link Bttl_800d#renderRingGradientEffect}</li>
   * </ol>
   */
  public static final QuintConsumer<EffectManagerData6c<EffectManagerData6cInner.RadialGradientType>, Integer, Vector2f[], RadialGradientEffect14, Translucency>[] radialGradientEffectRenderers_800fa758 = new QuintConsumer[5];
  static {
    radialGradientEffectRenderers_800fa758[0] = Bttl_800d::renderDiscGradientEffect;
    radialGradientEffectRenderers_800fa758[1] = Bttl_800d::FUN_800d1e80; // Not implemented
    radialGradientEffectRenderers_800fa758[2] = Bttl_800d::renderRingGradientEffect;
    radialGradientEffectRenderers_800fa758[3] = Bttl_800d::renderDiscGradientEffect;
    radialGradientEffectRenderers_800fa758[4] = Bttl_800d::renderRingGradientEffect;
  }

  public static final ArrayRef<GuardEffectMetrics04> guardEffectMetrics_800fa76c = MEMORY.ref(4, 0x800fa76cL, ArrayRef.of(GuardEffectMetrics04.class, 7, 4, GuardEffectMetrics04::new));

  /** ASCII chars - [0-9][A-Z][a-z]'-& <null> */
  public static final ArrayRef<ByteRef> asciiTable_800fa788 = MEMORY.ref(1, 0x800fa788L, ArrayRef.of(ByteRef.class, 0x66, 1, ByteRef::new));
  public static final ArrayRef<IntRef> charWidthAdjustTable_800fa7cc = MEMORY.ref(4, 0x800fa7ccL, ArrayRef.of(IntRef.class, 0x66, 4, IntRef::new));

  public static final CString additionNames_800fa8d4 = MEMORY.ref(4, 0x800fa8d4L, CString.maxLength(0x1bb));

  /** Next 4 globals are related to SpTextEffect40 */
  public static final ShortRef _800faa90 = MEMORY.ref(2, 0x800faa90L, ShortRef::new);
  public static final ShortRef _800faa92 = MEMORY.ref(2, 0x800faa92L, ShortRef::new);
  public static final ByteRef _800faa94 = MEMORY.ref(1, 0x800faa94L, ByteRef::new);

  public static final IntRef _800faa98 = MEMORY.ref(4, 0x800faa98L, IntRef::new);
  /** Next 2 globals are related to AdditionNameTextEffect1c */
  public static final ByteRef _800faa9c = MEMORY.ref(1, 0x800faa9cL, ByteRef::new);
  public static final ByteRef _800faa9d = MEMORY.ref(1, 0x800faa9dL, ByteRef::new);

  public static final ArrayRef<ButtonPressHudMetrics06> buttonPressHudMetrics_800faaa0 = MEMORY.ref(4, 0x800faaa0L, ArrayRef.of(ButtonPressHudMetrics06.class, 41, 6, ButtonPressHudMetrics06::new));

  public static final Vector3f cameraRotationVector_800fab98 = new Vector3f();
  public static final Vector3f temp1_800faba0 = new Vector3f();
  public static final Vector3f temp2_800faba8 = new Vector3f();

  public static final BoolRef useCameraWobble_800fabb8 = MEMORY.ref(1, 0x800fabb8L, BoolRef::new);

  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#setViewpointFromScriptTranslation}</li>
   *   <li>{@link Bttl_800d#setViewpointFromScriptAngle}</li>
   *   <li>{@link Bttl_800d#setViewpointFromScriptTranslationNoOp}</li>
   *   <li>{@link Bttl_800d#setViewpointFromScriptAngleNoOp}</li>
   *   <li>{@link Bttl_800d#setViewpointFromScriptTranslationRelativeToRefpoint}</li>
   *   <li>{@link Bttl_800d#setViewpointFromScriptAngleRelativeToRefpoint}</li>
   *   <li>{@link Bttl_800d#setViewpointFromScriptTranslationRelativeToObject}</li>
   *   <li>{@link Bttl_800d#setViewpointFromScriptAngleRelativeToObject}</li>
   * </ol>
   */
  public static final CameraQuadParamCallback[] viewpointSetFromScriptMethods_800fabbc = new CameraQuadParamCallback[8];
  static {
    viewpointSetFromScriptMethods_800fabbc[0] = Bttl_800d::setViewpointFromScriptTranslation;
    viewpointSetFromScriptMethods_800fabbc[1] = Bttl_800d::setViewpointFromScriptAngle;
    viewpointSetFromScriptMethods_800fabbc[2] = Bttl_800d::setViewpointFromScriptTranslationNoOp;
    viewpointSetFromScriptMethods_800fabbc[3] = Bttl_800d::setViewpointFromScriptAngleNoOp;
    viewpointSetFromScriptMethods_800fabbc[4] = Bttl_800d::setViewpointFromScriptTranslationRelativeToRefpoint;
    viewpointSetFromScriptMethods_800fabbc[5] = Bttl_800d::setViewpointFromScriptAngleRelativeToRefpoint;
    viewpointSetFromScriptMethods_800fabbc[6] = Bttl_800d::setViewpointFromScriptTranslationRelativeToObject;
    viewpointSetFromScriptMethods_800fabbc[7] = Bttl_800d::setViewpointFromScriptAngleRelativeToObject;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#setRefpointFromScriptTranslation}</li>
   *   <li>{@link Bttl_800d#setRefpointFromScriptAngle}</li>
   *   <li>{@link Bttl_800d#setRefpointFromScriptTranslationRelativeToViewpoint}</li>
   *   <li>{@link Bttl_800d#setRefpointFromScriptAngleRelativeToViewpoint}</li>
   *   <li>{@link Bttl_800d#setRefpointFromScriptTranslationNoOp}</li>
   *   <li>{@link Bttl_800d#setRefpointFromScriptAngleNoOp}</li>
   *   <li>{@link Bttl_800d#setRefpointFromScriptTranslationRelativeToObject}</li>
   *   <li>{@link Bttl_800d#setRefpointFromScriptAngleRelativeToObject}</li>
   * </ol>
   */
  public static final CameraQuadParamCallback[] refpointSetFromScriptMethods_800fabdc = new CameraQuadParamCallback[8];
  static {
    refpointSetFromScriptMethods_800fabdc[0] = Bttl_800d::setRefpointFromScriptTranslation;
    refpointSetFromScriptMethods_800fabdc[1] = Bttl_800d::setRefpointFromScriptAngle;
    refpointSetFromScriptMethods_800fabdc[2] = Bttl_800d::setRefpointFromScriptTranslationRelativeToViewpoint;
    refpointSetFromScriptMethods_800fabdc[3] = Bttl_800d::setRefpointFromScriptAngleRelativeToViewpoint;
    refpointSetFromScriptMethods_800fabdc[4] = Bttl_800d::setRefpointFromScriptTranslationNoOp;
    refpointSetFromScriptMethods_800fabdc[5] = Bttl_800d::setRefpointFromScriptAngleNoOp;
    refpointSetFromScriptMethods_800fabdc[6] = Bttl_800d::setRefpointFromScriptTranslationRelativeToObject;
    refpointSetFromScriptMethods_800fabdc[7] = Bttl_800d::setRefpointFromScriptAngleRelativeToObject;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d47dc}</li>
   *   <li>{@link Bttl_800d#FUN_800d496c}</li>
   *   <li>{@link Bttl_800d#FUN_800db564}</li>
   *   <li>{@link Bttl_800d#FUN_800db56c}</li>
   *   <li>{@link Bttl_800d#FUN_800d4bac}</li>
   *   <li>{@link Bttl_800d#FUN_800d4d7c}</li>
   *   <li>{@link Bttl_800d#FUN_800d4fbc}</li>
   *   <li>{@link Bttl_800d#FUN_800d519c}</li>
   * </ol>
   */
  public static final CameraSeptParamCallback[] _800fabfc = new CameraSeptParamCallback[8];
  static {
    _800fabfc[0] = Bttl_800d::FUN_800d47dc;
    _800fabfc[1] = Bttl_800d::FUN_800d496c;
    _800fabfc[2] = Bttl_800d::FUN_800db564;
    _800fabfc[3] = Bttl_800d::FUN_800db56c;
    _800fabfc[4] = Bttl_800d::FUN_800d4bac;
    _800fabfc[5] = Bttl_800d::FUN_800d4d7c;
    _800fabfc[6] = Bttl_800d::FUN_800d4fbc;
    _800fabfc[7] = Bttl_800d::FUN_800d519c;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d53e4}</li>
   *   <li>{@link Bttl_800d#FUN_800d5574}</li>
   *   <li>{@link Bttl_800d#FUN_800db78c}</li>
   *   <li>{@link Bttl_800d#FUN_800db794}</li>
   *   <li>{@link Bttl_800d#FUN_800d5740}</li>
   *   <li>{@link Bttl_800d#FUN_800d5930}</li>
   *   <li>{@link Bttl_800d#FUN_800d5afc}</li>
   *   <li>{@link Bttl_800d#FUN_800d5cf4}</li>
   * </ol>
   */
  public static final CameraSeptParamCallback[] _800fac1c = new CameraSeptParamCallback[8];
  static {
    _800fac1c[0] = Bttl_800d::FUN_800d53e4;
    _800fac1c[1] = null;
    _800fac1c[2] = null;
    _800fac1c[3] = null;
    _800fac1c[4] = null;
    _800fac1c[5] = null;
    _800fac1c[6] = null;
    _800fac1c[7] = null;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d5ec8}</li>
   *   <li>{@link Bttl_800d#FUN_800d60b0}</li>
   *   <li>{@link Bttl_800d#FUN_800db9d0}</li>
   *   <li>{@link Bttl_800d#FUN_800db9d8}</li>
   *   <li>{@link Bttl_800d#FUN_800d62d8}</li>
   *   <li>{@link Bttl_800d#FUN_800d64e4}</li>
   *   <li>{@link Bttl_800d#FUN_800d670c}</li>
   *   <li>{@link Bttl_800d#FUN_800d6960}</li>
   * </ol>
   */
  public static final CameraOctParamCallback[] _800fac3c = new CameraOctParamCallback[8];
  static {
    _800fac3c[0] = Bttl_800d::FUN_800d5ec8;
    _800fac3c[1] = Bttl_800d::FUN_800d60b0;
    _800fac3c[2] = Bttl_800d::FUN_800db9d0;
    _800fac3c[3] = Bttl_800d::FUN_800db9d8;
    _800fac3c[4] = Bttl_800d::FUN_800d62d8;
    _800fac3c[5] = Bttl_800d::FUN_800d64e4;
    _800fac3c[6] = Bttl_800d::FUN_800d670c;
    _800fac3c[7] = Bttl_800d::FUN_800d6960;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d6b90}</li>
   *   <li>{@link Bttl_800d#FUN_800d6d18}</li>
   *   <li>{@link Bttl_800d#FUN_800d6f58}</li>
   *   <li>{@link Bttl_800d#FUN_800d7128}</li>
   *   <li>{@link Bttl_800d#FUN_800db678}</li>
   *   <li>{@link Bttl_800d#FUN_800db680}</li>
   *   <li>{@link Bttl_800d#FUN_800d7368}</li>
   *   <li>{@link Bttl_800d#FUN_800d7548}</li>
   * </ol>
   */
  public static final CameraSeptParamCallback[] _800fac5c = new CameraSeptParamCallback[8];
  static {
    _800fac5c[0] = Bttl_800d::FUN_800d6b90;
    _800fac5c[1] = Bttl_800d::FUN_800d6d18;
    _800fac5c[2] = Bttl_800d::FUN_800d6f58;
    _800fac5c[3] = Bttl_800d::FUN_800d7128;
    _800fac5c[4] = Bttl_800d::FUN_800db678;
    _800fac5c[5] = Bttl_800d::FUN_800db680;
    _800fac5c[6] = Bttl_800d::FUN_800d7368;
    _800fac5c[7] = Bttl_800d::FUN_800d7548;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d7790}</li>
   *   <li>{@link Bttl_800d#FUN_800d7920}</li>
   *   <li>{@link Bttl_800d#FUN_800d7aec}</li>
   *   <li>{@link Bttl_800d#FUN_800d7cdc}</li>
   *   <li>{@link Bttl_800d#FUN_800db8a0}</li>
   *   <li>{@link Bttl_800d#FUN_800db8a8}</li>
   *   <li>{@link Bttl_800d#FUN_800d7ea8}</li>
   *   <li>{@link Bttl_800d#FUN_800d80a0}</li>
   * </ol>
   */
  public static final CameraSeptParamCallback[] _800fac7c = new CameraSeptParamCallback[8];
  static {
    _800fac7c[0] = Bttl_800d::FUN_800d7790;
    _800fac7c[1] = Bttl_800d::FUN_800d7920;
    _800fac7c[2] = Bttl_800d::FUN_800d7aec;
    _800fac7c[3] = Bttl_800d::FUN_800d7cdc;
    _800fac7c[4] = null;
    _800fac7c[5] = null;
    _800fac7c[6] = null;
    _800fac7c[7] = null;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d8274}</li>
   *   <li>{@link Bttl_800d#FUN_800d8424}</li>
   *   <li>{@link Bttl_800d#FUN_800d8614}</li>
   *   <li>{@link Bttl_800d#FUN_800d8808}</li>
   *   <li>{@link Bttl_800d#FUN_800dbb00}</li>
   *   <li>{@link Bttl_800d#FUN_800dbb08}</li>
   *   <li>{@link Bttl_800d#FUN_800d89f8}</li>
   *   <li>{@link Bttl_800d#FUN_800d8bf4}</li>
   * </ol>
   */
  public static final CameraOctParamCallback[] _800fac9c = new CameraOctParamCallback[8];
  static {
    _800fac9c[0] = Bttl_800d::FUN_800d8274;
    _800fac9c[1] = Bttl_800d::FUN_800d8424;
    _800fac9c[2] = Bttl_800d::FUN_800d8614;
    _800fac9c[3] = Bttl_800d::FUN_800d8808;
    _800fac9c[4] = Bttl_800d::FUN_800dbb00;
    _800fac9c[5] = Bttl_800d::FUN_800dbb08;
    _800fac9c[6] = Bttl_800d::FUN_800d89f8;
    _800fac9c[7] = Bttl_800d::FUN_800d8bf4;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dbe40}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe60}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe80}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe8c}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe98}</li>
   *   <li>{@link Bttl_800d#FUN_800dbef0}</li>
   *   <li>{@link Bttl_800d#FUN_800dbf70}</li>
   *   <li>{@link Bttl_800d#FUN_800dbfd4}</li>
   *   <li>{@link Bttl_800d#FUN_800d90c8}</li>
   *   <li>{@link Bttl_800d#FUN_800d9154}</li>
   *   <li>{@link Bttl_800d#FUN_800dc070}</li>
   *   <li>{@link Bttl_800d#FUN_800dc078}</li>
   *   <li>{@link Bttl_800d#FUN_800d9220}</li>
   *   <li>{@link Bttl_800d#FUN_800d92bc}</li>
   *   <li>{@link Bttl_800d#FUN_800d9380}</li>
   *   <li>{@link Bttl_800d#FUN_800d9438}</li>
   *   <li>{@link Bttl_800d#FUN_800d9518}</li>
   *   <li>{@link Bttl_800d#FUN_800d9650}</li>
   *   <li>{@link Bttl_800d#FUN_800dc080}</li>
   *   <li>{@link Bttl_800d#FUN_800dc088}</li>
   *   <li>{@link Bttl_800d#FUN_800d9788}</li>
   *   <li>{@link Bttl_800d#FUN_800d98d0}</li>
   *   <li>{@link Bttl_800d#FUN_800d9a68}</li>
   *   <li>{@link Bttl_800d#FUN_800d9bd4}</li>
   * </ol>
   */
  public static final Runnable[] cameraViewpointMethods_800facbc = new Runnable[24];
  static {
    cameraViewpointMethods_800facbc[0] = Bttl_800d::FUN_800dbe40;
    cameraViewpointMethods_800facbc[1] = Bttl_800d::FUN_800dbe60;
    cameraViewpointMethods_800facbc[2] = Bttl_800d::FUN_800dbe80;
    cameraViewpointMethods_800facbc[3] = Bttl_800d::FUN_800dbe8c;
    cameraViewpointMethods_800facbc[4] = Bttl_800d::FUN_800dbe98;
    cameraViewpointMethods_800facbc[5] = Bttl_800d::FUN_800dbef0;
    cameraViewpointMethods_800facbc[6] = Bttl_800d::FUN_800dbf70;
    cameraViewpointMethods_800facbc[7] = Bttl_800d::FUN_800dbfd4;
    cameraViewpointMethods_800facbc[8] = Bttl_800d::FUN_800d90c8;
    cameraViewpointMethods_800facbc[9] = Bttl_800d::FUN_800d9154;
    cameraViewpointMethods_800facbc[10] = Bttl_800d::FUN_800dc070;
    cameraViewpointMethods_800facbc[11] = Bttl_800d::FUN_800dc078;
    cameraViewpointMethods_800facbc[12] = Bttl_800d::FUN_800d9220;
    cameraViewpointMethods_800facbc[13] = Bttl_800d::FUN_800d92bc;
    cameraViewpointMethods_800facbc[14] = Bttl_800d::FUN_800d9380;
    cameraViewpointMethods_800facbc[15] = Bttl_800d::FUN_800d9438;
    cameraViewpointMethods_800facbc[16] = Bttl_800d::FUN_800d9518;
    cameraViewpointMethods_800facbc[17] = Bttl_800d::FUN_800d9650;
    cameraViewpointMethods_800facbc[18] = Bttl_800d::FUN_800dc080;
    cameraViewpointMethods_800facbc[19] = Bttl_800d::FUN_800dc088;
    cameraViewpointMethods_800facbc[20] = Bttl_800d::FUN_800d9788;
    cameraViewpointMethods_800facbc[21] = Bttl_800d::FUN_800d98d0;
    cameraViewpointMethods_800facbc[22] = Bttl_800d::FUN_800d9a68;
    cameraViewpointMethods_800facbc[23] = Bttl_800d::FUN_800d9bd4;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dc090}</li>
   *   <li>{@link Bttl_800d#FUN_800dc0b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc0d0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc128}</li>
   *   <li>{@link Bttl_800d#FUN_800dc1a8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc1b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc1b8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc21c}</li>
   *   <li>{@link Bttl_800d#FUN_800d9da0}</li>
   *   <li>{@link Bttl_800d#FUN_800d9e2c}</li>
   *   <li>{@link Bttl_800d#FUN_800d9ef8}</li>
   *   <li>{@link Bttl_800d#FUN_800d9f94}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2b8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2c0}</li>
   *   <li>{@link Bttl_800d#FUN_800da058}</li>
   *   <li>{@link Bttl_800d#FUN_800da110}</li>
   *   <li>{@link Bttl_800d#FUN_800da1f0}</li>
   *   <li>{@link Bttl_800d#FUN_800da328}</li>
   *   <li>{@link Bttl_800d#FUN_800da460}</li>
   *   <li>{@link Bttl_800d#FUN_800da5b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2c8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2d0}</li>
   *   <li>{@link Bttl_800d#FUN_800da750}</li>
   *   <li>{@link Bttl_800d#FUN_800da8bc}</li>
   * </ol>
   */
  public static final Runnable[] cameraRefpointMethods_800fad1c = new Runnable[24];
  static {
    cameraRefpointMethods_800fad1c[0] = Bttl_800d::FUN_800dc090;
    cameraRefpointMethods_800fad1c[1] = Bttl_800d::FUN_800dc0b0;
    cameraRefpointMethods_800fad1c[2] = Bttl_800d::FUN_800dc0d0;
    cameraRefpointMethods_800fad1c[3] = Bttl_800d::FUN_800dc128;
    cameraRefpointMethods_800fad1c[4] = Bttl_800d::FUN_800dc1a8;
    cameraRefpointMethods_800fad1c[5] = Bttl_800d::FUN_800dc1b0;
    cameraRefpointMethods_800fad1c[6] = Bttl_800d::FUN_800dc1b8;
    cameraRefpointMethods_800fad1c[7] = Bttl_800d::FUN_800dc21c;
    cameraRefpointMethods_800fad1c[8] = Bttl_800d::FUN_800d9da0;
    cameraRefpointMethods_800fad1c[9] = Bttl_800d::FUN_800d9e2c;
    cameraRefpointMethods_800fad1c[10] = Bttl_800d::FUN_800d9ef8;
    cameraRefpointMethods_800fad1c[11] = Bttl_800d::FUN_800d9f94;
    cameraRefpointMethods_800fad1c[12] = Bttl_800d::FUN_800dc2b8;
    cameraRefpointMethods_800fad1c[13] = Bttl_800d::FUN_800dc2c0;
    cameraRefpointMethods_800fad1c[14] = Bttl_800d::FUN_800da058;
    cameraRefpointMethods_800fad1c[15] = Bttl_800d::FUN_800da110;
    cameraRefpointMethods_800fad1c[16] = Bttl_800d::FUN_800da1f0;
    cameraRefpointMethods_800fad1c[17] = Bttl_800d::FUN_800da328;
    cameraRefpointMethods_800fad1c[18] = Bttl_800d::FUN_800da460;
    cameraRefpointMethods_800fad1c[19] = Bttl_800d::FUN_800da5b0;
    cameraRefpointMethods_800fad1c[20] = Bttl_800d::FUN_800dc2c8;
    cameraRefpointMethods_800fad1c[21] = Bttl_800d::FUN_800dc2d0;
    cameraRefpointMethods_800fad1c[22] = Bttl_800d::FUN_800da750;
    cameraRefpointMethods_800fad1c[23] = Bttl_800d::FUN_800da8bc;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#refpointRawComponent}</li>
   *   <li>{@link Bttl_800d#refpointAngleFrom0ToComponent}</li>
   *   <li>{@link Bttl_800d#refpointNoop1}</li>
   *   <li>{@link Bttl_800d#refpointNoop2}</li>
   *   <li>{@link Bttl_800d#refpointDeltaFromRefpointToComponent}</li>
   *   <li>{@link Bttl_800d#refpointAngleFromRefpointToComponent}</li>
   *   <li>{@link Bttl_800d#refpointDeltaFromScriptedObjToComponent}</li>
   *   <li>{@link Bttl_800d#refpointAngleFromScriptedObjToComponent}</li>
   * </ol>
   */
  public static final ComponentFunction[] refpointComponentMethods_800fad7c = new ComponentFunction[8];
  static {
    refpointComponentMethods_800fad7c[0] = Bttl_800d::refpointRawComponent;
    refpointComponentMethods_800fad7c[1] = Bttl_800d::refpointAngleFrom0ToComponent;
    refpointComponentMethods_800fad7c[2] = Bttl_800d::refpointNoop1;
    refpointComponentMethods_800fad7c[3] = Bttl_800d::refpointNoop2;
    refpointComponentMethods_800fad7c[4] = Bttl_800d::refpointDeltaFromRefpointToComponent;
    refpointComponentMethods_800fad7c[5] = Bttl_800d::refpointAngleFromRefpointToComponent;
    refpointComponentMethods_800fad7c[6] = Bttl_800d::refpointDeltaFromScriptedObjToComponent;
    refpointComponentMethods_800fad7c[7] = Bttl_800d::refpointAngleFromScriptedObjToComponent;
  }
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#viewpointRawComponent}</li>
   *   <li>{@link Bttl_800d#viewpointAngleFrom0ToComponent}</li>
   *   <li>{@link Bttl_800d#viewpointDeltaFromViewpointToComponent}</li>
   *   <li>{@link Bttl_800d#viewpointAngleFromViewpointToComponent}</li>
   *   <li>{@link Bttl_800d#viewpointNoop1}</li>
   *   <li>{@link Bttl_800d#viewpointNoop2}</li>
   *   <li>{@link Bttl_800d#viewpointDeltaFromScriptedObjToComponent}</li>
   *   <li>{@link Bttl_800d#viewpointAngleFromScriptedObjToComponent}</li>
   * </ol>
   */
  public static final ComponentFunction[] viewpointComponentMethods_800fad9c = new ComponentFunction[8];
  static {
    viewpointComponentMethods_800fad9c[0] = Bttl_800d::viewpointRawComponent;
    viewpointComponentMethods_800fad9c[1] = Bttl_800d::viewpointAngleFrom0ToComponent;
    viewpointComponentMethods_800fad9c[2] = Bttl_800d::viewpointDeltaFromViewpointToComponent;
    viewpointComponentMethods_800fad9c[3] = Bttl_800d::viewpointAngleFromViewpointToComponent;
    viewpointComponentMethods_800fad9c[4] = Bttl_800d::viewpointNoop1;
    viewpointComponentMethods_800fad9c[5] = Bttl_800d::viewpointNoop2;
    viewpointComponentMethods_800fad9c[6] = Bttl_800d::viewpointDeltaFromScriptedObjToComponent;
    viewpointComponentMethods_800fad9c[7] = Bttl_800d::viewpointAngleFromScriptedObjToComponent;
  }

  public static final ArrayRef<ShortRef> enemyDeffFileIndices_800faec4 = MEMORY.ref(2, 0x800faec4L, ArrayRef.of(ShortRef.class, 146, 2, ShortRef::new));

  public static final IntRef deffLoadingStage_800fafe8 = MEMORY.ref(4, 0x800fafe8L, IntRef::new);
  public static final ArrayRef<ByteRef> dragoonDeffFlags_800fafec = MEMORY.ref(1, 0x800fafecL, ArrayRef.of(ByteRef.class, 84, 1, ByteRef::new));
  /**
   * <ol start="0">
   *   <li>0x4</li>
   *   <li>0x9</li>
   *   <li>0xa</li>
   *   <li>0xb</li>
   *   <li>0xb</li>
   *   <li>0xd</li>
   *   <li>0x14</li>
   *   <li>0x16</li>
   *   <li>0x1b</li>
   *   <li>0x1c</li>
   *   <li>0x1e</li>
   *   <li>0x24</li>
   *   <li>0x28</li>
   *   <li>0x2a</li>
   *   <li>0x2c</li>
   *   <li>0x2e</li>
   *   <li>0x41</li>
   *   <li>0x42</li>
   *   <li>0x46</li>
   *   <li>0x47</li>
   *   <li>0x49</li>
   *   <li>0x4b</li>
   *   <li>0x4e</li>
   *   <li>0x52</li>
   *   <li>0xff</li>
   *   <li>0x0</li>
   *   <li>0x0</li>
   *   <li>0x0</li>
   * </ol>
   */
  public static final ArrayRef<ByteRef> dragoonDeffsWithExtraTims_800fb040 = MEMORY.ref(1, 0x800fb040L, ArrayRef.of(ByteRef.class, 28, 1, ByteRef::new));
  /**
   * <ol start="0">
   *   <li>10</li>
   *   <li>67</li>
   *   <li>69</li>
   *   <li>70 Melbu "generation" paintings</li>
   *   <li>70 Melbu "generation" paintings</li>
   *   <li>70 Melbu "generation" paintings</li>
   *   <li>0xff (end)</li>
   *   <li>0</li>
   * </ol>
   */
  public static final ArrayRef<ByteRef> cutsceneDeffsWithExtraTims_800fb05c = MEMORY.ref(1, 0x800fb05cL, ArrayRef.of(ByteRef.class, 8, 1, ByteRef::new));
  /**
   * <ol start="0">
   *   <li>Melbu 2-1</li>
   *   <li>Melbu 3-1</li>
   *   <li>Melbu 3-2</li>
   *   <li>Melbu 3</li>
   *   <li>Melbu 4</li>
   * </ol>
   *
   * The rest are -1
   */
  public static final ArrayRef<ByteRef> melbuStageIndices_800fb064 = MEMORY.ref(1, 0x800fb064L, ArrayRef.of(ByteRef.class, 8, 1, ByteRef::new));
  public static final ArrayRef<IntRef> modelColourMaps_800fb06c = MEMORY.ref(4, 0x800fb06cL, ArrayRef.of(IntRef.class, 32, 4, IntRef::new));

  public static final ArrayRef<IntRef> colourMapUvs_800fb0ec = MEMORY.ref(4, 0x800fb0ecL, ArrayRef.of(IntRef.class, 39, 4, IntRef::new));

  public static final ArrayRef<ShortRef> targetArrowOffsetY_800fb188 = MEMORY.ref(2, 0x800fb188L, ArrayRef.of(ShortRef.class, 8, 2, ShortRef::new));

  public static final ArrayRef<ShortRef> battleHudYOffsets_800fb198 = MEMORY.ref(2, 0x800fb198L, ArrayRef.of(ShortRef.class, 4, 2, ShortRef::new));

  /** Targeting ("All allies", "All players", "All") */
  public static final ArrayRef<Pointer<LodString>> targeting_800fb36c = MEMORY.ref(4, 0x800fb36cL, ArrayRef.of(Pointer.classFor(LodString.class),  3, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> playerNames_800fb378 = MEMORY.ref(4, 0x800fb378L, ArrayRef.of(Pointer.classFor(LodString.class), 11, 4, Pointer.deferred(4, LodString::new)));
  /** Poisoned, Dispirited, Weapon blocked, Stunned, Fearful, Confused, Bewitched, Petrified */
  public static final ArrayRef<Pointer<LodString>> ailments_800fb3a0 = MEMORY.ref(4, 0x800fb3a0L, ArrayRef.of(Pointer.classFor(LodString.class),  8, 4, Pointer.deferred(4, LodString::new)));

  /** Player names, player names, item names, dragoon spells, item descriptions, spell descriptions */
  public static final ArrayRef<Pointer<UnboundedArrayRef<Pointer<LodString>>>> allText_800fb3c0 = MEMORY.ref(4, 0x800fb3c0L, ArrayRef.of(Pointer.classFor(UnboundedArrayRef.classFor(Pointer.classFor(LodString.class))),  6, 4, Pointer.deferred(4, UnboundedArrayRef.of(4, Pointer.deferred(4, LodString::new)))));

  public static final ArrayRef<Pointer<NameAndPortraitDisplayMetrics0c>> hudNameAndPortraitMetrics_800fb444 = MEMORY.ref(4, 0x800fb444L, ArrayRef.of(Pointer.classFor(NameAndPortraitDisplayMetrics0c.class), 10, 4, Pointer.deferred(4, NameAndPortraitDisplayMetrics0c::new)));
  public static final ArrayRef<SpBarBorderMetrics04> spBarBorderMetrics_800fb46c = MEMORY.ref(1, 0x800fb46cL, ArrayRef.of(SpBarBorderMetrics04.class, 4, 4, SpBarBorderMetrics04::new));
  public static final ArrayRef<SpBarBorderMetrics04> spBarFlashingBorderMetrics_800fb47c = MEMORY.ref(1, 0x800fb47cL, ArrayRef.of(SpBarBorderMetrics04.class, 4, 4, SpBarBorderMetrics04::new));

  public static final ArrayRef<BattleItemMenuArrowUvMetrics06> battleMenuBackgroundMetrics_800fb5dc = MEMORY.ref(4, 0x800fb5dcL, ArrayRef.of(BattleItemMenuArrowUvMetrics06.class, 9, 6, BattleItemMenuArrowUvMetrics06::new));

  public static final ArrayRef<BattleMenuBackgroundDisplayMetrics0c> battleMenuBackgroundDisplayMetrics_800fb614 = MEMORY.ref(4, 0x800fb614L, ArrayRef.of(BattleMenuBackgroundDisplayMetrics0c.class, 8, 12, BattleMenuBackgroundDisplayMetrics0c::new));

  public static final ArrayRef<BattleMenuIconMetrics08> battleMenuIconMetrics_800fb674 = MEMORY.ref(4, 0x800fb674L, ArrayRef.of(BattleMenuIconMetrics08.class, 12, 8, BattleMenuIconMetrics08::new));

  public static final ArrayRef<ArrayRef<ShortRef>> battleMenuIconHeights_800fb6bc = MEMORY.ref(2, 0x800fb6bcL, ArrayRef.of(ArrayRef.classFor(ShortRef.class), 9, 6,  ArrayRef.of(ShortRef.class, 3, 2, ShortRef::new)));

  public static final ArrayRef<ArrayRef<ShortRef>> battleMenuIconVOffsets_800fb6f4 = MEMORY.ref(2, 0x800fb6f4L, ArrayRef.of(ArrayRef.classFor(ShortRef.class), 9, 6, ArrayRef.of(ShortRef.class, 3, 2, ShortRef::new)));

  public static final ArrayRef<BattleMenuTextMetrics08> battleMenuTextMetrics_800fb72c = MEMORY.ref(4, 0x800fb72cL, ArrayRef.of(BattleMenuTextMetrics08.class, 12, 8, BattleMenuTextMetrics08::new));

  @Method(0x800c7304L)
  public static void cacheLivingBents() {
    int i;
    int count;
    //LAB_800c7330
    for(i = 0, count = 0; i < allBentCount_800c66d0.get(); i++) {
      final ScriptState<? extends BattleEntity27c> bentState = battleState_8006e398.allBents_e0c[i];
      if((bentState.storage_44[7] & 0x40) == 0) {
        battleState_8006e398.aliveBents_e78[count] = bentState;
        count++;
      }

      //LAB_800c736c
    }

    //LAB_800c737c
    aliveBentCount_800c669c.set(count);

    //LAB_800c73b0
    for(i = 0, count = 0; i < charCount_800c677c.get(); i++) {
      final ScriptState<PlayerBattleEntity> playerState = battleState_8006e398.charBents_e40[i];
      if((playerState.storage_44[7] & 0x40) == 0) {
        battleState_8006e398.aliveCharBents_eac[count] = playerState;
        count++;
      }

      //LAB_800c73ec
    }

    //LAB_800c73fc
    aliveCharCount_800c6760.set(count);

    //LAB_800c7430
    for(i = 0, count = 0; i < monsterCount_800c6768.get(); i++) {
      final ScriptState<MonsterBattleEntity> monsterState = battleState_8006e398.monsterBents_e50[i];
      if((monsterState.storage_44[7] & 0x40) == 0) {
        battleState_8006e398.aliveMonsterBents_ebc[count] = monsterState;
        count++;
      }

      //LAB_800c746c
    }

    //LAB_800c747c
    aliveMonsterCount_800c6758.set(count);
  }

  @Method(0x800c7488L)
  public static int getHitProperty(final int charSlot, final int hitNum, final int hitPropertyIndex) {
    if((battleState_8006e398.charBents_e40[charSlot].storage_44[7] & 0x2) != 0) { // Is dragoon
      return battlePreloadedEntities_1f8003f4.additionHits_38[charSlot + 3].hits_00[hitNum].get(hitPropertyIndex);
    }

    //LAB_800c74fc
    return battlePreloadedEntities_1f8003f4.additionHits_38[charSlot].hits_00[hitNum].get(hitPropertyIndex);
  }

  @Method(0x800c7524L)
  public static void initBattle() {
    FUN_800c8624();

    gameState_800babc8._b4++;
    Arrays.fill(unlockedUltimateAddition_800bc910, false);
    goldGainedFromCombat_800bc920.set(0);

    spGained_800bc950.get(0).set(0);
    spGained_800bc950.get(1).set(0);
    spGained_800bc950.get(2).set(0);

    totalXpFromCombat_800bc95c.set(0);
    battleFlags_800bc960.set(0);
    postBattleActionIndex_800bc974.set(0);
    itemsDroppedByEnemies_800bc928.clear();
    itemOverflow.clear();
    equipmentOverflow.clear();

    int charIndex = gameState_800babc8.charIds_88[1];
    if(charIndex < 0) {
      gameState_800babc8.charIds_88[1] = gameState_800babc8.charIds_88[2];
      gameState_800babc8.charIds_88[2] = charIndex;
    }

    //LAB_800c75c0
    charIndex = gameState_800babc8.charIds_88[0];
    if(charIndex < 0) {
      gameState_800babc8.charIds_88[0] = gameState_800babc8.charIds_88[1];
      gameState_800babc8.charIds_88[1] = charIndex;
    }

    //LAB_800c75e8
    charIndex = gameState_800babc8.charIds_88[1];
    if(charIndex < 0) {
      gameState_800babc8.charIds_88[1] = gameState_800babc8.charIds_88[2];
      gameState_800babc8.charIds_88[2] = charIndex;
    }

    //LAB_800c760c
    allocateStageDarkeningStorage();
    btldLoadEncounterSoundEffectsAndMusic();

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7648L)
  public static void loadStageAndControllerScripts() {
    loadStage(battleStage_800bb0f4.get());
    loadSupportOverlay(1, SBtld::loadStageDataAndControllerScripts);
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c76a0L)
  public static void initializeViewportAndCamera() {
    if((battleFlags_800bc960.get() & 0x3) == 0x3) {
      resizeDisplay(320, 240);
      setDepthResolution(12);
      vsyncMode_8007a3b8 = 3;
      battleFlags_800bc960.or(0x40);
      setProjectionPlaneDistance(320);
      resetCameraMovement();
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c7718
  }

  @Method(0x800c772cL)
  public static void battleInitiateAndPreload_800c772c() {
    FUN_800c8e48();

    battleLoaded_800bc94c.set(true);

    startFadeEffect(4, 30);

    battleFlags_800bc960.or(0x20);
    battleState_8006e398.stageProgression_eec = 0;

    FUN_800ca980();
    FUN_800c8ee4();
    FUN_800cae44();

    allBentCount_800c66d0.set(0);
    monsterCount_800c6768.set(0);
    charCount_800c677c.set(0);

    loadSupportOverlay(1, SBtld::battlePrepareSelectedAdditionHitProperties_80109250);

    //LAB_800c7830
    for(int i = 0; i < 12; i++) {
      battleState_8006e398.allBents_e0c[i] = null;
    }

    FUN_800ee610();
    FUN_800f84c0();
    FUN_800f60ac();
    allocateDeffManager();

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c788cL)
  public static void deferAllocateEnemyBattleEntities() {
    loadSupportOverlay(1, SBtld::allocateEnemyBattleEntities);
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c78d4L)
  public static void allocatePlayerBattleEntities() {
    //LAB_800fbdb8
    for(charCount_800c677c.set(0); charCount_800c677c.get() < 3; charCount_800c677c.incr()) {
      if(gameState_800babc8.charIds_88[charCount_800c677c.get()] < 0) {
        break;
      }
    }

    //LAB_800fbde8
    final int[] charIndices = new int[charCount_800c677c.get()];

    //LAB_800fbe18
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      charIndices[charSlot] = addCombatant(0x200 + gameState_800babc8.charIds_88[charSlot] * 2, charSlot);
    }

    //LAB_800fbe4c
    //LAB_800fbe70
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charIndex = gameState_800babc8.charIds_88[charSlot];
      final String name = "Char ID " + charIndex + " (bent + " + (charSlot + 6) + ')';
      final ScriptState<PlayerBattleEntity> state = SCRIPTS.allocateScriptState(charSlot + 6, name, 0, new PlayerBattleEntity(name, charSlot + 6));
      state.setTicker(Bttl_800c::bentTicker);
      state.setDestructor(Bttl_800c::bentDestructor);
      battleState_8006e398.allBents_e0c[allBentCount_800c66d0.get()] = state;
      battleState_8006e398.charBents_e40[charSlot] = state;
      final PlayerBattleEntity bent = state.innerStruct_00;
      bent.magic_00 = BattleObject.BOBJ;
      bent.element = characterElements_800c706c[charIndex].get();
      bent.combatant_144 = getCombatant((short)charIndices[charSlot]);
      bent.charId_272 = charIndex;
      bent.bentSlot_274 = allBentCount_800c66d0.get();
      bent.charSlot_276 = charSlot;
      bent.combatantIndex_26c = charIndices[charSlot];
      bent.model_148.coord2_14.coord.transfer.x = charCount_800c677c.get() > 2 && charSlot == 0 ? 0x900 : 0xa00;
      bent.model_148.coord2_14.coord.transfer.y = 0.0f;
      // Alternates placing characters to the right and left of the main character (offsets by -0x400 for even character counts)
      bent.model_148.coord2_14.coord.transfer.z = 0x800 * ((charSlot + 1) / 2) * (charSlot % 2 * 2 - 1) + (charCount_800c677c.get() % 2 - 1) * 0x400;
      bent.model_148.coord2_14.transforms.rotate.zero();
      allBentCount_800c66d0.incr();
    }

    //LAB_800fbf6c
    battleState_8006e398.allBents_e0c[allBentCount_800c66d0.get()] = null;
    battleState_8006e398.charBents_e40[charCount_800c677c.get()] = null;

    FUN_800f863c();

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c791cL)
  public static void loadEncounterAssets() {
    loadEnemyTextures(2625 + encounterId_800bb0f8.get());

    //LAB_800fc030
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      if(getCombatant(i).charSlot_19c < 0) { // I think this means it's not a player
        loadCombatantTmdAndAnims(i);
      }

      //LAB_800fc050
    }

    //LAB_800fc064
    //LAB_800fc09c
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      combatants_8005e398[battleState_8006e398.charBents_e40[i].innerStruct_00.combatantIndex_26c].flags_19e |= 0x2a;
    }

    //LAB_800fc104
    loadPartyTims();
    loadPartyTmdAndAnims();
    battleFlags_800bc960.or(0x400);

    pregameLoadingStage_800bb10c.incr();
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc210L)
  public static void loadCharTmdAndAnims(final List<FileData> files, final int charSlot) {
    //LAB_800fc260
    final BattleEntity27c data = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;

    //LAB_800fc298
    combatantTmdAndAnimLoadedCallback(files, data.combatantIndex_26c, false);

    //LAB_800fc34c
    battleFlags_800bc960.or(0x4);
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc3c0L)
  public static void loadEnemyTextures(final int fileIndex) {
    // Example file: 2856
    loadDrgnDir(0, fileIndex, Bttl_800c::enemyTexturesLoadedCallback);
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc404L)
  public static void enemyTexturesLoadedCallback(final List<FileData> files) {
    final BattlePreloadedEntities_18cb0 s2 = battlePreloadedEntities_1f8003f4;

    //LAB_800fc434
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 a0 = getCombatant(i);

      if(a0.charSlot_19c < 0) {
        final int enemyIndex = a0.charIndex_1a2 & 0x1ff;

        //LAB_800fc464
        for(int enemySlot = 0; enemySlot < 3; enemySlot++) {
          if((s2.encounterData_00.enemyIndices_00[enemySlot] & 0x1ff) == enemyIndex && files.get(enemySlot).hasVirtualSize()) {
            loadCombatantTim(i, files.get(enemySlot));
            break;
          }
        }
      }
    }
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc504L)
  public static void loadPartyTims() {
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charId = gameState_800babc8.charIds_88[charSlot];
      final String name = getCharacterName(charId).toLowerCase();
      final int finalCharSlot = charSlot;
      loadFile("characters/%s/textures/combat".formatted(name), files -> loadCharacterTim(files, finalCharSlot));
    }
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc548L)
  public static void loadCharacterTim(final FileData file, final int charSlot) {
    final BattleEntity27c bent = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;
    loadCombatantTim(bent.combatantIndex_26c, file);
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc654L)
  public static void loadPartyTmdAndAnims() {
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charId = gameState_800babc8.charIds_88[charSlot];
      final String name = getCharacterName(charId).toLowerCase();
      final int finalCharSlot = charSlot;
      loadDir("characters/%s/models/combat".formatted(name), files -> loadCharTmdAndAnims(files, finalCharSlot));
    }
  }

  @Method(0x800c7964L)
  public static void loadHudAndAttackAnimations() {
    battleFlags_800bc960.or(0xc);

    loadBattleHudTextures();
    loadBattleHudDeff();

    //LAB_800c79a8
    for(int combatantIndex = 0; combatantIndex < combatantCount_800c66a0.get(); combatantIndex++) {
      loadAttackAnimations(combatantIndex);
    }

    //LAB_800c79c8
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c79f0L)
  public static void FUN_800c79f0() {
    currentTurnBent_800c66c8 = battleState_8006e398.allBents_e0c[0];
    FUN_800f417c();

    EVENTS.postEvent(new BattleStartedEvent());

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7a30L)
  public static void loadSEffe() {
    loadSupportOverlay(3, () -> { });
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7a80L)
  public static void calculateInitialTurnValues() {
    if(_800c66a8.get() != 0) {
      battleFlags_800bc960.or(0x10);

      //LAB_800c7ae4
      for(int i = 0; i < allBentCount_800c66d0.get(); i++) {
        final ScriptState<? extends BattleEntity27c> bentState = battleState_8006e398.allBents_e0c[i];
        final BattleEntity27c bent = bentState.innerStruct_00;

        if((bentState.storage_44[7] & 0x4) != 0) {
          bent.turnValue_4c = simpleRand() * 0xd9 / 0x10000;
        } else {
          //LAB_800c7b3c
          bent.turnValue_4c = simpleRand() * 0xa7 / 0x10000 + 0x32;
        }

        //LAB_800c7b68
      }

      //LAB_800c7b80
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c7b9c
  }

  @Method(0x800c7bb8L)
  public static void battleTick() {
    FUN_800ef9e4();
    drawUiElements();

    if(postBattleActionIndex_800bc974.get() != 0) {
      pregameLoadingStage_800bb10c.incr();
      return;
    }

    if(Unpacker.getLoadingFileCount() == 0 && allBentCount_800c66d0.get() > 0 && !combatDisabled_800c66b9.get() && FUN_800c7da8()) {
      vsyncMode_8007a3b8 = 3;
      mcqColour_800fa6dc.set(0x80);
      currentTurnBent_800c66c8.storage_44[7] &= 0xffff_efff;

      if(aliveCharCount_800c6760.get() > 0) {
        //LAB_800c7c98
        final ScriptState<? extends BattleEntity27c> forcedTurnBent = getForcedTurnBent();
        forcedTurnBent_800c66bc = forcedTurnBent;

        if(forcedTurnBent != null) { // A bent has a forced turn
          forcedTurnBent.storage_44[7] = forcedTurnBent.storage_44[7] & 0xffff_ffdf | 0x1008;
          currentTurnBent_800c66c8 = forcedTurnBent;
          EVENTS.postEvent(new BattleEntityTurnEvent<>(forcedTurnBent));
        } else { // Take regular turns
          //LAB_800c7ce8
          if(aliveMonsterCount_800c6758.get() > 0) { // Monsters alive, calculate next bent turn
            //LAB_800c7d3c
            final ScriptState<? extends BattleEntity27c> currentTurn = getCurrentTurnBent();
            currentTurnBent_800c66c8 = currentTurn;
            currentTurn.storage_44[7] |= 0x1008;
            EVENTS.postEvent(new BattleEntityTurnEvent<>(currentTurn));

            //LAB_800c7d74
          } else { // Monsters dead
            FUN_80020308();

            if(encounterId_800bb0f8.get() != 443) { // Standard victory
              postBattleActionIndex_800bc974.set(1);
              startEncounterSounds();
            } else { // Melbu Victory
              //LAB_800c7d30
              postBattleActionIndex_800bc974.set(4);
            }
          }
        }
      } else { // Game over
        loadMusicPackage(19);
        postBattleActionIndex_800bc974.set(2);
      }
    }

    //LAB_800c7d78
    if(postBattleActionIndex_800bc974.get() != 0) {
      //LAB_800c7d88
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c7d98
  }

  @Method(0x800c7da8L)
  public static boolean FUN_800c7da8() {
    //LAB_800c7dd8
    for(int i = 0; i < allBentCount_800c66d0.get(); i++) {
      if((battleState_8006e398.allBents_e0c[i].storage_44[7] & 0x408) != 0) {
        return false;
      }

      //LAB_800c7e10
    }

    //LAB_800c7e1c
    return true;
  }

  @Method(0x800c7e24L)
  public static ScriptState<? extends BattleEntity27c> getForcedTurnBent() {
    //LAB_800c7e54
    for(int i = 0; i < aliveBentCount_800c669c.get(); i++) {
      final ScriptState<? extends BattleEntity27c> bentState = battleState_8006e398.aliveBents_e78[i];
      if(bentState != null && (bentState.storage_44[7] & 0x20) != 0) {
        return bentState;
      }

      //LAB_800c7e8c
    }

    //LAB_800c7e98
    return null;
  }

  @Method(0x800c7ea0L)
  public static ScriptState<? extends BattleEntity27c> getCurrentTurnBent() {
    //LAB_800c7ee4
    for(int s4 = 0; s4 < 32; s4++) {
      //LAB_800c7ef0
      int highestTurnValue = 0;
      int highestCombatantindex = 0;
      for(int combatantIndex = 0; combatantIndex < aliveBentCount_800c669c.get(); combatantIndex++) {
        final int turnValue = battleState_8006e398.aliveBents_e78[combatantIndex].innerStruct_00.turnValue_4c;

        if(highestTurnValue <= turnValue) {
          highestTurnValue = turnValue;
          highestCombatantindex = combatantIndex;
        }

        //LAB_800c7f30
      }

      //LAB_800c7f40
      if(highestTurnValue > 0xd9) {
        final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.aliveBents_e78[highestCombatantindex];
        state.innerStruct_00.turnValue_4c = highestTurnValue - 0xd9;

        if((state.storage_44[7] & 0x4) == 0) {
          gameState_800babc8._b8++;
        }

        //LAB_800c7f9c
        return state;
      }

      //LAB_800c7fa4
      //LAB_800c7fb0
      for(int combatantIndex = 0; combatantIndex < aliveBentCount_800c669c.get(); combatantIndex++) {
        final BattleEntity27c bent = battleState_8006e398.aliveBents_e78[combatantIndex].innerStruct_00;
        highestTurnValue = bent.stats.getStat(CoreMod.SPEED_STAT.get()).get() * (simpleRand() + 0x4_4925);
        final int v1 = (int)(highestTurnValue * 0x35c2_9183L >>> 32) >> 16; //TODO _pretty_ sure this is roughly /312,110 (seems oddly specific?)
        bent.turnValue_4c += v1;
      }

      //LAB_800c8028
    }

    //LAB_800c8040
    return battleState_8006e398.aliveCharBents_eac[0];
  }

  @Method(0x800c8068L)
  public static void performPostBattleAction() {
    EVENTS.postEvent(new BattleEndedEvent());

    final int postBattleActionIndex = postBattleActionIndex_800bc974.get();

    if(currentPostCombatActionFrame_800c6690.get() == 0) {
      final int postBattleAction = postBattleActions_800fa6c4.get(postBattleActionIndex).get();

      if(postBattleAction >= 0) {
        _800c6748.set(postBattleAction);
        scriptState_800c6914 = currentTurnBent_800c66c8;
      }

      //LAB_800c80c8
      final int aliveCharBents = aliveCharCount_800c6760.get();
      livingCharCount_800bc97c.set(aliveCharBents);

      //LAB_800c8104
      for(int i = 0; i < aliveCharBents; i++) {
        livingCharIds_800bc968.get(i).set(battleState_8006e398.aliveCharBents_eac[i].innerStruct_00.charId_272);
      }

      //LAB_800c8144
      if(postBattleActionIndex == 1) {
        //LAB_800c8180
        for(int i = 0; i < charCount_800c677c.get(); i++) {
          battleState_8006e398.charBents_e40[i].storage_44[7] |= 0x8;
        }
      }
    }

    //LAB_800c81bc
    //LAB_800c81c0
    currentPostCombatActionFrame_800c6690.incr();

    if(currentPostCombatActionFrame_800c6690.get() >= postCombatActionTotalFrames_800fa6b8.get(postBattleActionIndex).get() || (press_800bee94.get() & 0xff) != 0 && currentPostCombatActionFrame_800c6690.get() >= 25) {
      //LAB_800c8214
      deallocateLightingControllerAndDeffManager();
      loadSupportOverlay(2, () -> { });

      if(fullScreenEffect_800bb140.currentColour_28 == 0) {
        startFadeEffect(1, postCombatActionFrames_800fa6d0.get(postBattleActionIndex).get());
      }

      //LAB_800c8274
      if(postBattleActionIndex == 2) {
        sssqFadeOut((short)(postCombatActionFrames_800fa6d0.get(2).get() - 2));
      }

      //LAB_800c8290
      currentPostCombatActionFrame_800c6690.set(0);
      pregameLoadingStage_800bb10c.incr();
    }
    //LAB_800c82a8
  }

  @Method(0x800c82b8L)
  public static void deallocateCombat() {
    if(fullScreenEffect_800bb140.currentColour_28 == 0xff) {
      updateGameStateAndDeallocateMenu();
      setStageHasNoModel();

      script_800c66fc = null;

      //LAB_800c8314
      scriptDeallocateAllTextboxes(null);
      scriptState_800c674c.deallocateWithChildren();

      //LAB_800c8368
      //LAB_800c8394
      stopAndResetSoundsAndSequences();

      //LAB_800c83b8
      while(allBentCount_800c66d0.get() > 0) {
        battleState_8006e398.allBents_e0c[0].deallocateWithChildren();
      }

      //LAB_800c83d8
      //LAB_800c83f4
      for(int combatantIndex = 0; combatantIndex < combatantCount_800c66a0.get(); combatantIndex++) {
        final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

        if(combatant != null) {
          //LAB_800c8418
          //LAB_800c8434
          if(combatant.mrg_00 != null) {
            combatant.mrg_00 = null;
          }

          if(combatant.mrg_04 != null) {
            combatant.mrg_04 = null;
          }
        }

        //LAB_800c8454
      }

      //LAB_800c847c
      FUN_800c8f18();
      FUN_800ca9b4();
      deallocateStageDarkeningStorage();
      FUN_800c8748();

      EngineStateEnum postCombatMainCallbackIndex = previousEngineState_8004dd28;
      if(postCombatMainCallbackIndex == EngineStateEnum.FMV_09) {
        postCombatMainCallbackIndex = EngineStateEnum.SUBMAP_05;
      }

      //LAB_800c84b4
      switch(postBattleActionIndex_800bc974.get()) {
        case 2 -> {
          final int encounter = encounterId_800bb0f8.get();
          if(encounter == 391 || encounter >= 404 && encounter < 408) { // Arena fights in Lohan
            //LAB_800c8514
            gameState_800babc8.scriptFlags2_bc.set(29, 27, true); // Died in arena fight
          } else {
            //LAB_800c8534
            postCombatMainCallbackIndex = EngineStateEnum.GAME_OVER_07;
          }
        }

        case 4 -> Fmv.playCurrentFmv(16, EngineStateEnum.FINAL_FMV_11);
      }

      //LAB_800c8558
      postCombatMainCallbackIndex_800bc91c = postCombatMainCallbackIndex;

      final int postCombatSubmapStage = currentStageData_800c6718.postCombatSubmapStage_0c;
      if(postCombatSubmapStage != 0xff) {
        submapScene_80052c34.set(postCombatSubmapStage);
      }

      //LAB_800c8578
      final int postCombatSubmapCut = currentStageData_800c6718.postCombatSubmapCut_28;
      if(postCombatSubmapCut != 0xffff) {
        submapCut_80052c30.set(postCombatSubmapCut);
      }

      //LAB_800c8590
      setDepthResolution(14);
      battleLoaded_800bc94c.set(false);

      switch(postBattleActionIndex_800bc974.get()) {
        case 1, 3 -> whichMenu_800bdc38 = WhichMenu.INIT_POST_COMBAT_REPORT_26;
        case 2, 4, 5 -> whichMenu_800bdc38 = WhichMenu.NONE_0;
      }

      //LAB_800c85f0
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c8604
  }

  @Method(0x800c8624L)
  public static void FUN_800c8624() {
    battlePreloadedEntities_1f8003f4 = new BattlePreloadedEntities_18cb0();
    battleState_8006e398 = new BattleStateEf4();
    targetBents_800c71f0 = new ScriptState[][] {battleState_8006e398.charBents_e40, battleState_8006e398.aliveMonsterBents_ebc, battleState_8006e398.aliveBents_e78};
  }

  @Method(0x800c8748L)
  public static void FUN_800c8748() {
    battlePreloadedEntities_1f8003f4 = null;
    battleState_8006e398 = null;
    targetBents_800c71f0 = null;
  }

  @Method(0x800c8774L)
  public static void loadStageTmdAndAnim(final String modelName, final List<FileData> files) {
    setStageHasNoModel();

    if(files.get(0).size() > 0 && files.get(1).size() > 0 && files.get(2).size() > 0) {
      _800c6754.set(1);
      stageHasModel_800c66b8.set(true);

      final BattleStage stage = battlePreloadedEntities_1f8003f4.stage_963c;
      loadStageTmd(stage, new CContainer(modelName, files.get(0), 10), new TmdAnimationFile(files.get(1)));
      stage.coord2_558.coord.transfer.set(0, 0, 0);
      stage.param_5a8.rotate.set(0.0f, MathHelper.TWO_PI / 4.0f, 0.0f);
    }

    //LAB_800c8818
  }

  @Method(0x800c882cL)
  public static void renderSkybox() {
    if(shouldRenderMcq_800c6764.get() == 0 || !shouldRenderMcq_800c66d4.get() || (battleFlags_800bc960.get() & 0x80) == 0) {
      //LAB_800c8ad8
      //LAB_800c8adc
      clearBlue_800babc0.set(0);
      clearGreen_800bb104.set(0);
      clearRed_8007a3a8.set(0);
    } else {
      final McqHeader mcq = battlePreloadedEntities_1f8003f4.stageMcq_9cb0;

      mcqOffsetX_800c6774.add(mcqStepX_800c676c.get());
      mcqOffsetY_800c6778.add(mcqStepY_800c6770.get());
      final int x0 = (mcqBaseOffsetX_800c66cc.get() * MathHelper.radToPsxDeg(calculateXAngleFromRefpointToViewpoint()) / 0x1000 + mcqOffsetX_800c6774.get()) % mcq.screenWidth_14 - centreScreenX_1f8003dc.get();
      final int x1 = x0 - mcq.screenWidth_14;
      final int x2 = x0 + mcq.screenWidth_14;
      int y = mcqOffsetY_800c6778.get() - MathHelper.radToPsxDeg(MathHelper.floorMod(calculateYAngleFromRefpointToViewpoint() + MathHelper.PI, MathHelper.TWO_PI)) + 0x760 - centreScreenY_1f8003de.get();
      renderMcq(mcq, 320, 0, x0, y, orderingTableSize_1f8003c8.get() - 2, mcqColour_800fa6dc.get());
      renderMcq(mcq, 320, 0, x1, y, orderingTableSize_1f8003c8.get() - 2, mcqColour_800fa6dc.get());

      if(centreScreenX_1f8003dc.get() >= x2) {
        renderMcq(mcq, 320, 0, x2, y, orderingTableSize_1f8003c8.get() - 2, mcqColour_800fa6dc.get());
      }

      //LAB_800c89d4
      if(mcq.magic_00 != McqHeader.MAGIC_1) {
        //LAB_800c89f8
        y += mcq.screenOffsetY_2a;
      }

      //LAB_800c8a04
      final int colour = mcqColour_800fa6dc.get();
      if(y >= -centreScreenY_1f8003de.get()) {
        clearRed_8007a3a8.set(mcq.colour0_18.getR() * colour / 0x80);
        clearGreen_800bb104.set(mcq.colour0_18.getG() * colour / 0x80);
        clearBlue_800babc0.set(mcq.colour0_18.getB() * colour / 0x80);
      } else {
        //LAB_800c8a74
        clearRed_8007a3a8.set(mcq.colour1_20.getR() * colour / 0x80);
        clearGreen_800bb104.set(mcq.colour1_20.getG() * colour / 0x80);
        clearBlue_800babc0.set(mcq.colour1_20.getB() * colour / 0x80);
      }
    }

    //LAB_800c8af0
  }

  @Method(0x800c8b20L)
  public static void loadStage(final int stage) {
    loadDrgnDir(0, 2497 + stage, files -> {
      if(files.get(0).hasVirtualSize()) {
        loadStageMcq(new McqHeader(files.get(0)));
      }

      if(files.get(1).size() != 0) {
        loadStageTim(files.get(1));
      }
    });

    loadDrgnDir(0, (2497 + stage) + "/0", files -> loadStageTmdAndAnim("DRGN0/" + (2497 + stage) + "/0", files));

    currentStage_800c66a4.set(stage);
  }

  @Method(0x800c8c84L)
  public static void loadStageTim(final FileData data) {
    final Tim tim = new Tim(data);

    LoadImage(tim.getImageRect(), tim.getImageData());

    if(tim.hasClut()) {
      LoadImage(tim.getClutRect(), tim.getClutData());
    }

    //LAB_800c8ccc
    backupStageClut(data);
  }

  @Method(0x800c8ce4L)
  public static void setStageHasNoModel() {
    stageHasModel_800c66b8.set(false);
  }

  @Method(0x800c8cf0L)
  public static void rotateAndRenderBattleStage() {
    if(stageHasModel_800c66b8.get() && _800c6754.get() != 0 && (battleFlags_800bc960.get() & 0x20) != 0) {
      rotateBattleStage(battlePreloadedEntities_1f8003f4.stage_963c);
      renderBattleStage(battlePreloadedEntities_1f8003f4.stage_963c);
    }

    //LAB_800c8d50
  }

  @Method(0x800c8d64L)
  public static void loadStageMcq(final McqHeader mcq) {
    final long x;
    if((battleFlags_800bc960.get() & 0x80) != 0) {
      x = 320;
      shouldRenderMcq_800c6764.set(1);
    } else {
      //LAB_800c8d98
      x = 512;
    }

    //LAB_800c8d9c
    loadMcq(mcq, x, 0);

    //LAB_800c8dc0
    battlePreloadedEntities_1f8003f4.stageMcq_9cb0 = mcq;

    shouldRenderMcq_800c66d4.set(true);
    mcqBaseOffsetX_800c66cc.set((0x400 / mcq.screenWidth_14 + 1) * mcq.screenWidth_14);
  }

  @Method(0x800c8e48L)
  public static void FUN_800c8e48() {
    if(shouldRenderMcq_800c66d4.get() && (battleFlags_800bc960.get() & 0x80) == 0) {
      final RECT sp0x10 = new RECT((short)512, (short)0, (short)battlePreloadedEntities_1f8003f4.stageMcq_9cb0.vramWidth_08, (short)256);
      MoveImage(sp0x10, 320, 0);
      shouldRenderMcq_800c6764.set(1);
      battleFlags_800bc960.or(0x80);
    }
    //LAB_800c8ec8
  }

  @Method(0x800c8ed8L)
  public static void setDontRenderStageBackground() {
    shouldRenderMcq_800c66d4.set(false);
  }

  @Method(0x800c8ee4L)
  public static void FUN_800c8ee4() {
    //LAB_800c8ef4
    //NOTE: zeroes 0x50 bytes after this array of structs ends
    Arrays.fill(combatants_8005e398, null);

    unused_800c66c0.set(true);
  }

  @Method(0x800c8f18L)
  public static void FUN_800c8f18() {
    unused_800c66c0.set(false);
  }

  @Method(0x800c8f24L)
  public static CombatantStruct1a8 getCombatant(final int index) {
    return combatants_8005e398[index];
  }

  @Method(0x800c8f50L)
  public static int addCombatant(final int a0, final int charSlot) {
    //LAB_800c8f6c
    for(int combatantIndex = 0; combatantIndex < 10; combatantIndex++) {
      if(combatants_8005e398[combatantIndex] == null) {
        final CombatantStruct1a8 combatant = new CombatantStruct1a8();
        combatants_8005e398[combatantIndex] = combatant;

        if(charSlot < 0) {
          combatant.flags_19e = 1;
        } else {
          //LAB_800c8f90
          combatant.flags_19e = 5;
        }

        //LAB_800c8f94
        combatant.charSlot_19c = charSlot;
        combatant.colourMap_1a0 = 0;
        combatant.charIndex_1a2 = a0;
        combatant._1a4 = -1;
        combatant._1a6 = -1;
        combatantCount_800c66a0.add(1);
        return combatantIndex;
      }
    }

    return -1;
  }

  @Method(0x800c8fd4L)
  public static void removeCombatant(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    if(combatant.colourMap_1a0 != 0) {
      FUN_800ca918(combatant.colourMap_1a0);
    }

    //LAB_800c9020
    //LAB_800c902c
    combatants_8005e398[combatantIndex] = null;

    combatantCount_800c66a0.sub(1);
  }

  @Method(0x800c9060L)
  public static int getCombatantIndex(final int charIndex) {
    //LAB_800c906c
    for(int i = 0; i < 10; i++) {
      final CombatantStruct1a8 combatant = combatants_8005e398[i];

      if(combatant != null && combatant.charIndex_1a2 == charIndex) {
        //LAB_800c90a8
        return i;
      }

      //LAB_800c9090
    }

    return -1;
  }

  @Method(0x800c90b0L)
  public static boolean isCombatantModelLoaded(final int combatantIndex) {
    //LAB_800c9114
    //LAB_800c9128
    //LAB_800c912c
    return (combatants_8005e398[combatantIndex]._1a4 >= 0 || combatants_8005e398[combatantIndex].mrg_00 != null && combatants_8005e398[combatantIndex].mrg_00.get(32).hasVirtualSize()) && isCombatantAssetLoaded(combatantIndex, 0);
  }

  @Method(0x800c913cL)
  public static ScriptFile getCombatantScript(final int index) {
    return combatants_8005e398[index].scriptPtr_10;
  }

  @Method(0x800c9170L)
  public static void deallocateCombatant(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    //LAB_800c91bc
    if(combatant.mrg_00 != null) {
      combatant.mrg_00 = null;
    }

    if(combatant.mrg_04 != null) {
      //LAB_800c91e8
      combatant.mrg_04 = null;
    }

    if(combatant._1a4 >= 0) {
      FUN_800cad64(combatant._1a4);
    }

    //LAB_800c921c
    if(combatant._1a6 >= 0) {
      FUN_800cad64(combatant._1a6);
    }

    //LAB_800c9234
    //LAB_800c9238
    for(int i = 0; i < 32; i++) {
      if(combatant.assets_14[i] != null && combatant.assets_14[i]._09 != 0) {
        FUN_800c9c7c(combatantIndex, i);
      }

      //LAB_800c9254
    }

    combatant.flags_19e &= 0xffe7;
  }

  @Method(0x800c9290L)
  public static void loadCombatantTmdAndAnims(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    if(combatant.charIndex_1a2 >= 0) {
      if((combatant.flags_19e & 0x8) == 0) {
        if(combatant.mrg_00 == null) {
          combatant.flags_19e |= 0x28;

          if((combatant.flags_19e & 0x4) == 0) {
            // Enemy TMDs
            final int fileIndex = 3137 + combatant.charIndex_1a2;
            loadDrgnDir(0, fileIndex, files -> Bttl_800c.combatantTmdAndAnimLoadedCallback(files, combatantIndex, true));
          } else {
            // Player TMDs
            //LAB_800c9334
            int charIndex = gameState_800babc8.charIds_88[combatant.charSlot_19c];
            combatant.flags_19e |= 0x2;

            if((combatant.charIndex_1a2 & 0x1) != 0) {
              if(charIndex == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
                charIndex = 10; // Divine dragoon
              }

              final String charName = getCharacterName(charIndex).toLowerCase();
              loadDir("characters/%s/models/dragoon".formatted(charName), files -> Bttl_800c.combatantTmdAndAnimLoadedCallback(files, combatantIndex, false));
            } else {
              final String charName = getCharacterName(charIndex).toLowerCase();
              loadDir("characters/%s/models/combat".formatted(charName), files -> Bttl_800c.combatantTmdAndAnimLoadedCallback(files, combatantIndex, false));
            }
          }
        }
      }
    }

    //LAB_800c940c
  }

  @Method(0x800c941cL)
  public static void combatantTmdAndAnimLoadedCallback(final List<FileData> files, final int combatantIndex, final boolean isMonster) {
    final CombatantStruct1a8 combatant = getCombatant(combatantIndex);
    combatant.flags_19e &= 0xffdf;

    if(!isMonster) {
      battleFlags_800bc960.or(0x4);
    }

    //LAB_800c947c
    combatant.mrg_00 = files;

    // I don't think this is actually used?
    if(files.get(34).hasVirtualSize()) {
      combatant.scriptPtr_10 = new ScriptFile("%s %d file 34".formatted(isMonster ? "monster" : "char", combatant.charSlot_19c), files.get(34).getBytes());
    }

    //LAB_800c94a0
    //LAB_800c94a4
    for(int animIndex = 0; animIndex < 32; animIndex++) {
      if(files.get(animIndex).hasVirtualSize()) {
        FUN_800c9a80(files.get(animIndex), 1, 0, combatantIndex, animIndex);
      }

      //LAB_800c94cc
    }
  }

  @Method(0x800c94f8L)
  public static int FUN_800c94f8(final int combatantIndex, final short a1) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];
    final int oldVal = combatant._1a4;
    combatant._1a4 = a1;
    return oldVal;
  }

  @Method(0x800c952cL)
  public static void FUN_800c952c(final Model124 model, final int combatantIndex) {
    final CombatantStruct1a8 s0 = combatants_8005e398[combatantIndex];

    final CContainer tmd;
    if(s0._1a4 >= 0) {
      tmd = new CContainer(model.name, FUN_800cad34(s0._1a4));
    } else {
      //LAB_800c9590
      if(s0.mrg_00 != null && s0.mrg_00.get(32).hasVirtualSize()) {
        tmd = new CContainer(model.name, s0.mrg_00.get(32));
      } else {
        throw new RuntimeException("anim undefined");
      }
    }

    //LAB_800c95bc
    s0.tmd_08 = tmd;

    final TmdAnimationFile anim = FUN_800ca31c(combatantIndex, 0);
    if((s0.flags_19e & 0x4) != 0) {
      final BattlePreloadedEntities_18cb0.Rendering1298 a0_0 = battlePreloadedEntities_1f8003f4._9ce8[s0.charSlot_19c];

      a0_0.dobj2s_00 = new ModelPart10[tmd.tmdPtr_00.tmd.header.nobj];
      Arrays.setAll(a0_0.dobj2s_00, i -> new ModelPart10());

      model.modelParts_00 = a0_0.dobj2s_00;

      final long a3;
      if((s0.charIndex_1a2 & 0x1) != 0) {
        a3 = 0x9L;
      } else {
        a3 = s0.charIndex_1a2 - 0x200 >>> 1;
      }

      //LAB_800c9650
      FUN_80021520(model, tmd, anim, a3);
    } else {
      //LAB_800c9664
      initModel(model, tmd, anim);
    }

    //LAB_800c9680
    s0.assets_14[0]._09++;
  }

  @Method(0x800c9708L)
  public static void loadAttackAnimations(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    if(combatant.charIndex_1a2 >= 0 && combatant.mrg_04 == null) {
      combatant.flags_19e |= 0x10;

      final int fileIndex;
      if((combatant.flags_19e & 0x4) == 0) {
        // Enemy attack animations
        fileIndex = 3593 + combatant.charIndex_1a2;
        loadDrgnDir(0, fileIndex, files -> Bttl_800c.attackAnimationsLoaded(files, combatantIndex, true, -1));
      } else {
        //LAB_800c97a4
        final int isDragoon = combatant.charIndex_1a2 & 0x1;
        final int charId = gameState_800babc8.charIds_88[combatant.charSlot_19c];
        if(isDragoon == 0) {
          // Additions
          if(charId != 2 && charId != 8) {
            fileIndex = 4031 + gameState_800babc8.charData_32c[charId].selectedAddition_19 + charId * 8 - additionOffsets_8004f5ac.get(charId).get();
          } else {
            // Retail fix: Shana/??? have selectedAddition 255 which loads a random file... just load Dart's first addition here, it isn't used (see GH#357)
            fileIndex = 4031 + charId * 8;
          }
        } else if(charId != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
          // Dragoon addition
          fileIndex = 4103 + charId;
        } else { // Divine dragoon
          // Divine dragoon addition
          fileIndex = 4112;
        }

        loadDrgnDir(0, fileIndex, files -> Bttl_800c.attackAnimationsLoaded(files, combatantIndex, false, combatant.charSlot_19c));
      }
    }

    //LAB_800c9888
  }

  @Method(0x800c9898L)
  public static void attackAnimationsLoaded(final List<FileData> files, final int combatantIndex, final boolean isMonster, final int charSlot) {
    final CombatantStruct1a8 combatant = getCombatant(combatantIndex);

    if(combatant.mrg_04 == null) {
      //LAB_800c9910
      if(!isMonster && files.size() == 64) {
        //LAB_800c9940
        for(int animIndex = 0; animIndex < 32; animIndex++) {
          if(files.get(32 + animIndex).hasVirtualSize()) {
            if(combatant.assets_14[animIndex] != null && combatant.assets_14[animIndex]._09 != 0) {
              FUN_800c9c7c(combatantIndex, animIndex);
            }

            //LAB_800c9974
            // Type 6 - TIM file (except it's loading animation data into VRAM???) TODO
            FUN_800c9a80(files.get(32 + animIndex), 6, charSlot, combatantIndex, animIndex);
          }
        }
      }

      //LAB_800c99d8
      combatant.mrg_04 = files;

      //LAB_800c99e8
      for(int animIndex = 0; animIndex < 32; animIndex++) {
        if(files.get(animIndex).hasVirtualSize()) {
          if(combatant.assets_14[animIndex] != null && combatant.assets_14[animIndex]._09 != 0) {
            FUN_800c9c7c(combatantIndex, animIndex);
          }

          //LAB_800c9a18
          FUN_800c9a80(files.get(animIndex), 2, 1, combatantIndex, animIndex);
        }

        //LAB_800c9a34
      }
    }

    //LAB_800c9a48
  }

  /**
   * @param type <ol>
   *               <li>Animation</li>
   *               <li>Animation</li>
   *               <li>Index</li>
   *               <li value="6">TIM</li>
   *             </ol>
   */
  @Method(0x800c9a80L)
  public static void FUN_800c9a80(final FileData data, final int type, final int a3, final int combatantIndex, final int animIndex) {
    CombatantAsset0c s3 = combatants_8005e398[combatantIndex].assets_14[animIndex];

    if(s3 != null) {
      FUN_800c9c7c(combatantIndex, animIndex);
    }

    //LAB_800c9b28
    if(type == 1) {
      //LAB_800c9b68
      if(data.readInt(0x4) == 0x1a45_5042) { // BPE
        final CombatantAsset0c.BpeType bpe = new CombatantAsset0c.BpeType(data);
        bpe._08 = a3;
        bpe.type_0a = 4;
        bpe.isLoaded_0b = false;
        s3 = bpe;
      } else {
        final CombatantAsset0c.AnimType anim = new CombatantAsset0c.AnimType(new TmdAnimationFile(data));
        anim._08 = a3;
        anim.type_0a = 1;
        anim.isLoaded_0b = false;
        s3 = anim;
      }
    } else if(type == 2) {
      //LAB_800c9b80
      //LAB_800c9b98
      if(data.readInt(0x4) == 0x1a45_5042) { // BPE
        //LAB_800c9b88
        final CombatantAsset0c.BpeType bpe = new CombatantAsset0c.BpeType(data);
        bpe._08 = a3;
        bpe.type_0a = 5;
        bpe.isLoaded_0b = false;
        s3 = bpe;
      } else {
        final CombatantAsset0c.AnimType anim = new CombatantAsset0c.AnimType(new TmdAnimationFile(data));
        anim._08 = a3;
        anim.type_0a = 2;
        anim.isLoaded_0b = false;
        s3 = anim;
      }
      //LAB_800c9b4c
    } else if(type == 3) {
      //LAB_800c9bb0
      final CombatantAsset0c.IndexType index = new CombatantAsset0c.IndexType(a3);
      index._08 = -1;
      index.type_0a = 3;
      index.isLoaded_0b = true;
      s3 = index;
    } else if(type == 6) {
      //LAB_800c9bcc
      final CombatantAsset0c.TimType tim = new CombatantAsset0c.TimType(data);
      tim._08 = -1;
      tim.type_0a = 6;
      tim.isLoaded_0b = false;
      s3 = tim;
    } else {
      return;
    }

    //LAB_800c9c44
    s3.BttlStruct08_index_04 = -1;
    s3.BattleStructEf4Sub08_index_06 = -1;
    s3._09 = 0;

    combatants_8005e398[combatantIndex].assets_14[animIndex] = s3;

    //LAB_800c9c54
  }

  @Method(0x800c9c7cL)
  public static void FUN_800c9c7c(final int combatantIndex, final int animIndex) {
    final CombatantAsset0c s0 = combatants_8005e398[combatantIndex].assets_14[animIndex];

    if(s0 != null) {
      //LAB_800c9cec
      while(s0._09 > 0) {
        FUN_800ca194(combatantIndex, animIndex);
      }

      //LAB_800c9d04
      if(s0 instanceof final CombatantAsset0c.IndexType index) {
        FUN_800cad64(index.index_00);
      } else if(s0 instanceof final CombatantAsset0c.BpeType bpe) {
        if(bpe.isLoaded_0b) {
          final int a0 = bpe.BttlStruct08_index_04;
          if(a0 >= 0) {
            //LAB_800c9d78
            FUN_800cad64(a0);
          } else {
            battleState_8006e398._d8c[bpe.BattleStructEf4Sub08_index_06].used_04 = false;
          }
        }
      }

      //LAB_800c9d84
      combatants_8005e398[combatantIndex].assets_14[animIndex] = null;
    }

    //LAB_800c9da0
  }

  @Method(0x800c9db8L)
  public static void FUN_800c9db8(final int combatantIndex, final int animIndex, final int a2) {
    FUN_800c9c7c(combatantIndex, animIndex);
    FUN_800c9a80(null, 3, a2, combatantIndex, animIndex);
  }

  @Method(0x800c9e10L)
  public static boolean FUN_800c9e10(final int combatantIndex, final int animIndex) {
    final CombatantAsset0c s0 = combatants_8005e398[combatantIndex].assets_14[animIndex];

    if(s0 instanceof final CombatantAsset0c.AnimType animType) {
      return animType.anim_00 != null;
    }

    if(s0 instanceof final CombatantAsset0c.IndexType indexType) {
      return indexType.index_00 >= 0;
    }

    if(s0 instanceof final CombatantAsset0c.BpeType bpeType) {
      if(!bpeType.isLoaded_0b) {
        final int a3 = _800c66ac.get() + 1 & 0xffff_fff0;
        _800c66ac.set((short)a3);
        battleState_8006e398._d8c[a3]._00 = bpeType;
        battleState_8006e398._d8c[a3].used_04 = true;
        bpeType.isLoaded_0b = true;
        bpeType.BattleStructEf4Sub08_index_06 = a3;

        FUN_800c9fcc(new FileData(Unpacker.decompress(bpeType.bpe_00)), a3);
      }

      return true;
    }

    if(s0 instanceof final CombatantAsset0c.TimType timType) {
      if(!timType.isLoaded_0b) {
        s0.BttlStruct08_index_04 = FUN_800caae4(timType.data, 3);
        s0.isLoaded_0b = true;
      }

      //LAB_800c9fb4
      return true;
    }

    return false;
  }

  @Method(0x800c9fccL)
  public static void FUN_800c9fcc(final FileData data, final int param) {
    final CombatantAsset0c s0 = battleState_8006e398._d8c[param]._00;

    if(s0.isLoaded_0b && battleState_8006e398._d8c[param].used_04) {
      s0.BttlStruct08_index_04 = FUN_800caae4(data, 3);
      s0.BattleStructEf4Sub08_index_06 = -1;
      battleState_8006e398._d8c[param].used_04 = false;
    }

    //LAB_800ca040
  }

  @Method(0x800ca054L)
  public static boolean isCombatantAssetLoaded(final int combatantIndex, final int assetIndex) {
    final CombatantAsset0c asset = combatants_8005e398[combatantIndex].assets_14[assetIndex];

    if(asset instanceof CombatantAsset0c.AnimType || asset instanceof CombatantAsset0c.IndexType) {
      return true;
    }

    if(asset instanceof CombatantAsset0c.BpeType || asset instanceof CombatantAsset0c.TimType) {
      return asset.isLoaded_0b && asset.BttlStruct08_index_04 >= 0;
    }

    //LAB_800ca0f8
    return false;
  }

  @Method(0x800ca100L)
  public static void FUN_800ca100(final Model124 model, final int combatantIndex, final int animIndex) {
    loadModelStandardAnimation(model, FUN_800ca31c(combatantIndex, animIndex));
    combatants_8005e398[combatantIndex].assets_14[0]._09++;
  }

  @Method(0x800ca194L)
  public static boolean FUN_800ca194(final int combatantIndex, final int animIndex) {
    final CombatantAsset0c s0 = combatants_8005e398[combatantIndex].assets_14[animIndex];

    if(s0 != null) {
      if(s0._09 > 0) {
        s0._09--;
      }

      //LAB_800ca1f4
      final int type = s0.type_0a;

      if(type < 4) {
        return true;
      }

      if(s0._09 == 0) {
        if(s0.BttlStruct08_index_04 >= 0) {
          FUN_800cad64(s0.BttlStruct08_index_04);
        }

        //LAB_800ca240
        s0.BttlStruct08_index_04 = -1;
        s0.BattleStructEf4Sub08_index_06 = -1;
        s0.isLoaded_0b = false;
      }
    }

    //LAB_800ca258
    //LAB_800ca25c
    return true;
  }

  @Method(0x800ca26cL)
  public static boolean FUN_800ca26c(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    //LAB_800ca2bc
    boolean s3 = true;
    for(int i = 0; i < 32; i++) {
      if(combatant.assets_14[i] != null && combatant.assets_14[i]._09 == 0) {
        if(FUN_800ca194(combatantIndex, i)) {
          s3 = !s3;
        } else {
          s3 = false;
        }
      }

      //LAB_800ca2e8
    }

    return s3;
  }

  @Method(0x800ca31cL)
  public static TmdAnimationFile FUN_800ca31c(final int combatantIndex, final int animIndex) {
    final CombatantAsset0c a0_0 = combatants_8005e398[combatantIndex].assets_14[animIndex];

    if(a0_0 instanceof final CombatantAsset0c.AnimType animType) {
      return animType.anim_00;
    }

    if(a0_0 instanceof final CombatantAsset0c.IndexType indexType) {
      final int s0 = indexType.index_00;

      return new TmdAnimationFile(FUN_800cad34(s0));
    }

    if(a0_0 instanceof CombatantAsset0c.BpeType || a0_0 instanceof CombatantAsset0c.TimType) {
      if(a0_0.isLoaded_0b) {
        final int s0 = a0_0.BttlStruct08_index_04;

        if(s0 >= 0) {
          //LAB_800ca3f4
          return new TmdAnimationFile(FUN_800cad34(s0));
        }
      }
    }

    return null;
  }

  @Method(0x800ca418L)
  public static void FUN_800ca418(final int index) {
    final CombatantStruct1a8 combatant = combatants_8005e398[index];

    //LAB_800ca488
    //LAB_800ca494
    for(int i = 0; i < 32; i++) {
      if(combatant.assets_14[i] instanceof CombatantAsset0c.AnimType && combatant.assets_14[i].type_0a == 2 || combatant.assets_14[i] instanceof CombatantAsset0c.TimType) {
        //LAB_800ca4c0
        FUN_800c9c7c(index, i);
      }

      //LAB_800ca4cc
    }

    if(combatant.mrg_04 != null) {
      combatant.mrg_04 = null;
    }

    //LAB_800ca4f8
    combatant.flags_19e &= 0xffef;
  }

  @Method(0x800ca528L)
  public static int FUN_800ca528(final int combatantIndex, final int a1) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];
    final int oldVal = combatant._1a6;
    combatant._1a6 = a1;
    return oldVal;
  }

  @Method(0x800ca55cL)
  public static void loadCombatantTextures(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    if(combatant.charIndex_1a2 >= 0) {
      int fileIndex = gameState_800babc8.charIds_88[combatant.charSlot_19c];

      if((combatant.charIndex_1a2 & 0x1) != 0) {
        if(fileIndex == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
          fileIndex = 10;
        }

        final String charName = getCharacterName(fileIndex).toLowerCase();
        loadFile("characters/%s/textures/dragoon".formatted(charName), files -> Bttl_800c.loadCombatantTim(combatantIndex, files));
      } else {
        final String charName = getCharacterName(fileIndex).toLowerCase();
        loadFile("characters/%s/textures/combat".formatted(charName), files -> Bttl_800c.loadCombatantTim(combatantIndex, files));
      }
    }

    //LAB_800ca64c
  }

  @Method(0x800ca75cL)
  public static void loadCombatantTim(final int combatantIndex, final FileData timFile) {
    final int a0;

    if(combatantIndex >= 0) {
      //LAB_800ca77c
      final CombatantStruct1a8 s0 = getCombatant(combatantIndex);

      if(s0.colourMap_1a0 == 0) {
        final int charSlot = s0.charSlot_19c;

        if(charSlot < 0) {
          s0.colourMap_1a0 = FUN_800ca89c(s0.charIndex_1a2);
        } else {
          //LAB_800ca7c4
          s0.colourMap_1a0 = charSlot + 1;
        }
      }

      a0 = s0.colourMap_1a0;
    } else {
      a0 = 0;
    }

    //LAB_800ca7d0
    loadCombatantTim2(a0, timFile);
  }

  @Method(0x800ca7ecL)
  public static void loadCombatantTim2(final int a0, final FileData timFile) {
    final Tim tim = new Tim(timFile);

    if(a0 != 0) {
      //LAB_800ca83c
      final RECT combatantTimRect = combatantTimRects_800fa6e0.get(a0);
      LoadImage(combatantTimRect, tim.getImageData());

      if(tim.hasClut()) {
        final RECT clutRect = tim.getClutRect();
        clutRect.x.set(combatantTimRect.x.get());
        clutRect.y.set((short)(combatantTimRect.y.get() + 240));

        //LAB_800ca884
        LoadImage(clutRect, tim.getClutData());
      }
    } else {
      final RECT imageRect = tim.getImageRect();

      // This is a fix for a retail bug where they try to load a TMD as a TIM (it has a 0 w/h anyway so no data gets loaded) see GH#330b
      if(imageRect.x.get() == 0x41 && imageRect.y.get() == 0 && imageRect.w.get() == 0 && imageRect.h.get() == 0) {
        return;
      }

      tim.uploadToGpu();
    }

    //LAB_800ca88c
  }

  @Method(0x800ca89cL)
  public static int FUN_800ca89c(final int a0) {
    //LAB_800ca8ac
    //LAB_800ca8c4
    for(int i = a0 < 0x200 ? 4 : 1; i < 9; i++) {
      final int a0_0 = 0x1 << i;

      if((_800c66c4.get() & a0_0) == 0) {
        _800c66c4.or(a0_0);
        return i;
      }

      //LAB_800ca8e4
    }

    //LAB_800ca8f4
    return 0;
  }

  @Method(0x800ca8fcL)
  public static void FUN_800ca8fc(final int shift) {
    _800c66c4.or(0x1 << shift);
  }

  @Method(0x800ca918L)
  public static void FUN_800ca918(final int shift) {
    _800c66c4.and(~(0x1 << shift));
  }

  @Method(0x800ca938L)
  public static short getCombatantColourMap(final int combatantIndex) {
    return colourMaps_800fa730.get(combatants_8005e398[combatantIndex].colourMap_1a0).get();
  }

  @Method(0x800ca980L)
  public static void FUN_800ca980() {
    unused_800c66c1.set(true);
  }

  @Method(0x800ca9b4L)
  public static void FUN_800ca9b4() {
    unused_800c66c1.set(false);

    //LAB_800ca9d8
    for(int s1 = 0; s1 < 0x100; s1++) {
      final BttlStruct08 s0 = battleState_8006e398._580[s1];
      if(s0._04 >= 2) {
        s0._04 = 0;
      }
    }
  }

  @Method(0x800caa20L)
  public static int FUN_800caa20() {
    _800c66b4.add(1);
    if(_800c66b4.get() >= 0x100) {
      _800c66b4.set(0);
    }

    //LAB_800caa44
    //LAB_800caa64
    for(int i = _800c66b4.get(); i < 0x100; i++) {
      final BttlStruct08 a1 = battleState_8006e398._580[i];

      if(a1._04 == 0) {
        //LAB_800caacc
        _800c66b4.set(i);
        a1.data_00 = null;
        a1._04 = 1;
        return i;
      }
    }

    //LAB_800caa88
    //LAB_800caaa4
    for(int i = 0; i < _800c66b4.get(); i++) {
      final BttlStruct08 a1 = battleState_8006e398._580[i];

      if(a1._04 == 0) {
        //LAB_800caacc
        _800c66b4.set(i);
        a1.data_00 = null;
        a1._04 = 1;
        return i;
      }
    }

    //LAB_800caac4
    throw new RuntimeException("Failed to find free slot");
  }

  @Method(0x800caae4L)
  public static int FUN_800caae4(final FileData fileData, final int a1) {
    final int index = FUN_800caa20();

    final BttlStruct08 a0 = battleState_8006e398._580[index];
    a0.data_00 = fileData;
    a0._04 = a1;

    //LAB_800cab3c
    return index;
  }

  @Method(0x800cac38L)
  public static int FUN_800cac38(final int drgnIndex, final int fileIndex) {
    final int index = FUN_800caa20();

    loadDrgnFile(drgnIndex, fileIndex, data -> FUN_800cacb0(data, index));

    //LAB_800cac98
    return index;
  }

  @Method(0x800cacb0L)
  public static void FUN_800cacb0(final FileData data, final int index) {
    final BttlStruct08 a1 = battleState_8006e398._580[index];

    if(a1._04 == 1) {
      a1.data_00 = data;
      a1._04 = 2;
    }

    //LAB_800cad04
  }

  @Method(0x800cad34L)
  public static FileData FUN_800cad34(final int index) {
    return battleState_8006e398._580[index].data_00;
  }

  @Method(0x800cad50L)
  public static BttlStruct08 FUN_800cad50(final int index) {
    return battleState_8006e398._580[index];
  }

  @Method(0x800cad64L)
  public static void FUN_800cad64(final int index) {
    final BttlStruct08 s0 = battleState_8006e398._580[index];

    if(s0._04 != 1) {
      s0.data_00 = null;
    }

    //LAB_800cada8
    s0._04 = 0;
  }

  @Method(0x800cae44L)
  public static void FUN_800cae44() {
    currentDisplayableIconsBitset_800c675c.set(0);
  }

  @Method(0x800cae50L)
  public static void bentTicker(final ScriptState<? extends BattleEntity27c> state, final BattleEntity27c bent) {
    bent._278 = 0;

    final int v1;
    if((state.storage_44[7] & 0x4) != 0) {
      v1 = battleFlags_800bc960.get() & 0x110;
    } else {
      //LAB_800cae94
      v1 = battleFlags_800bc960.get() & 0x210;
    }

    //LAB_800cae98
    if(v1 != 0 && isCombatantModelLoaded(bent.combatantIndex_26c)) {
      bent.model_148.colourMap_9d = getCombatantColourMap(bent.combatantIndex_26c);
      bent.loadingAnimIndex_26e = 0;
      FUN_800c952c(bent.model_148, bent.combatantIndex_26c);
      bent._278 = 1;
      bent.currentAnimIndex_270 = -1;

      if((state.storage_44[7] & 0x800) == 0) {
        final ScriptFile script;
        if((state.storage_44[7] & 0x4) != 0) {
          script = getCombatantScript(bent.combatantIndex_26c);
        } else {
          //LAB_800caf18
          script = script_800c66fc;
        }

        //LAB_800caf20
        state.loadScriptFile(script);
      }

      //LAB_800caf2c
      state.setTicker(Bttl_800c::FUN_800caf50);
    }

    //LAB_800caf38
  }

  @Method(0x800caf2cL)
  public static void FUN_800caf50(final ScriptState<? extends BattleEntity27c> state, final BattleEntity27c bent) {
    state.setRenderer(Bttl_800c::FUN_800cb024);
    state.setTicker(Bttl_800c::FUN_800cafb4);
    FUN_800cafb4(state, bent);
  }

  @Method(0x800cafb4L)
  public static void FUN_800cafb4(final ScriptState<? extends BattleEntity27c> state, final BattleEntity27c bent) {
    if((state.storage_44[7] & 0x211) == 0) {
      applyModelRotationAndScale(bent.model_148);

      if((state.storage_44[7] & 0x80) == 0 || bent.model_148.remainingFrames_9e != 0) {
        //LAB_800cb004
        animateModel(bent.model_148);
      }
    }

    //LAB_800cb00c
  }

  @Method(0x800cb024L)
  public static void FUN_800cb024(final ScriptState<? extends BattleEntity27c> state, final BattleEntity27c bent) {
    if((state.storage_44[7] & 0x211) == 0) {
      renderBttlModel(bent.model_148);
    }

    //LAB_800cb048
  }

  @Method(0x800cb058L)
  public static void bentDestructor(final ScriptState<? extends BattleEntity27c> state, final BattleEntity27c bent) {
    //LAB_800cb088
    FUN_800ca194(bent.combatantIndex_26c, bent.loadingAnimIndex_26e);

    allBentCount_800c66d0.decr();

    //LAB_800cb0d4
    for(int i = bent.bentSlot_274; i < allBentCount_800c66d0.get(); i++) {
      battleState_8006e398.allBents_e0c[i] = battleState_8006e398.allBents_e0c[i + 1];
      battleState_8006e398.allBents_e0c[i].innerStruct_00.bentSlot_274 = i;
    }

    //LAB_800cb11c
    if((state.storage_44[7] & 0x4) != 0) {
      monsterCount_800c6768.decr();

      //LAB_800cb168
      for(int i = bent.charSlot_276; i < monsterCount_800c6768.get(); i++) {
        battleState_8006e398.monsterBents_e50[i] = battleState_8006e398.monsterBents_e50[i + 1];
        battleState_8006e398.monsterBents_e50[i].innerStruct_00.charSlot_276 = i;
      }
    } else {
      //LAB_800cb1b8
      charCount_800c677c.decr();

      //LAB_800cb1f4
      for(int i = bent.charSlot_276; i < charCount_800c677c.get(); i++) {
        battleState_8006e398.charBents_e40[i] = battleState_8006e398.charBents_e40[i + 1];
        battleState_8006e398.charBents_e40[i].innerStruct_00.charSlot_276 = i;
      }
    }

    //LAB_800cb23c
  }

  @Method(0x800cb250L)
  public static boolean FUN_800cb250(final ScriptState<BattleEntity27c> state, final BattleEntity27c data) {
    float x = state._e8.x;
    float y = state._e8.y;
    float z = state._e8.z;

    if(state.scriptState_c8 != null) {
      final BattleEntity27c data2 = state.scriptState_c8.innerStruct_00;

      x += data2.model_148.coord2_14.coord.transfer.x;
      y += data2.model_148.coord2_14.coord.transfer.y;
      z += data2.model_148.coord2_14.coord.transfer.z;
    }

    //LAB_800cb2ac
    state.ticks_cc--;
    if(state.ticks_cc > 0) {
      state._d0.sub(state._dc);
      data.model_148.coord2_14.coord.transfer.x = x - state._d0.x;
      data.model_148.coord2_14.coord.transfer.y = y - state._d0.y;
      data.model_148.coord2_14.coord.transfer.z = z - state._d0.z;
      state._dc.y += state._f4;
      return false;
    }

    //LAB_800cb338
    data.model_148.coord2_14.coord.transfer.set(x, y, z);
    return true;
  }

  @Method(0x800cb34cL)
  public static boolean FUN_800cb34c(final ScriptState<BattleEntity27c> state, final BattleEntity27c data) {
    final BattleEntity27c bent = state.scriptState_c8.innerStruct_00;
    final Vector3f vec = bent.model_148.coord2_14.coord.transfer;
    final float angle = MathHelper.atan2(vec.x - data.model_148.coord2_14.coord.transfer.x, vec.z - data.model_148.coord2_14.coord.transfer.z) + MathHelper.PI;

    state.ticks_cc--;
    if(state.ticks_cc > 0) {
      state._d0.x -= state._d0.y; // This is correct, sometimes this vec is used as (angle, step)
      data.model_148.coord2_14.transforms.rotate.y = angle + state._d0.x;
      return false;
    }

    //LAB_800cb3e0
    data.model_148.coord2_14.transforms.rotate.y = angle;

    //LAB_800cb3e8
    return true;
  }

  @ScriptDescription("Sets the position of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800cb3fcL)
  public static FlowControl scriptSetBentPos(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.coord2_14.coord.transfer.x = script.params_20[1].get();
    bent.model_148.coord2_14.coord.transfer.y = script.params_20[2].get();
    bent.model_148.coord2_14.coord.transfer.z = script.params_20[3].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the position of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800cb468L)
  public static FlowControl scriptGetBentPos(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(Math.round(bent.model_148.coord2_14.coord.transfer.x));
    script.params_20[2].set(Math.round(bent.model_148.coord2_14.coord.transfer.y));
    script.params_20[3].set(Math.round(bent.model_148.coord2_14.coord.transfer.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the rotation of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z rotation (PSX degrees)")
  @Method(0x800cb4c8L)
  public static FlowControl scriptSetBentRotation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.coord2_14.transforms.rotate.x = MathHelper.psxDegToRad(script.params_20[1].get());
    bent.model_148.coord2_14.transforms.rotate.y = MathHelper.psxDegToRad(script.params_20[2].get());
    bent.model_148.coord2_14.transforms.rotate.z = MathHelper.psxDegToRad(script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the Y rotation of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @Method(0x800cb534L)
  public static FlowControl scriptSetBentRotationY(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.coord2_14.transforms.rotate.y = MathHelper.psxDegToRad(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the rotation of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z rotation (PSX degrees)")
  @Method(0x800cb578L)
  public static FlowControl scriptGetBentRotation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(MathHelper.radToPsxDeg(bent.model_148.coord2_14.transforms.rotate.x));
    script.params_20[2].set(MathHelper.radToPsxDeg(bent.model_148.coord2_14.transforms.rotate.y));
    script.params_20[3].set(MathHelper.radToPsxDeg(bent.model_148.coord2_14.transforms.rotate.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the status resist flags of a monster battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "flags", description = "The status resist flags")
  @Method(0x800cb5d8L)
  public static FlowControl scriptGetMonsterStatusResistFlags(final RunningScript<?> script) {
    final MonsterBattleEntity monster = (MonsterBattleEntity)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(monster.monsterStatusResistFlag_76);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets bit 10 of battle entity script flags (possibly whether a battle entity renders and animates?)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to set the flag, false otherwise")
  @Method(0x800cb618L)
  public static FlowControl FUN_800cb618(final RunningScript<?> script) {
    final ScriptState<?> a1 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];

    //LAB_800cb668
    if(script.params_20[1].get() != 0) {
      a1.storage_44[7] &= 0xffff_ffef;
    } else {
      //LAB_800cb65c
      a1.storage_44[7] |= 0x10;
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets whether or not a battle entity's animation interpolation is enabled")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to enable interpolation, false otherwise")
  @Method(0x800cb674L)
  public static FlowControl scriptSetInterpolationEnabled(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.disableInterpolation_a2 = script.params_20[1].get() == 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to loading a battle entity's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "animIndex", description = "The animation index")
  @Method(0x800cb6bcL)
  public static FlowControl FUN_800cb6bc(final RunningScript<?> a0) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[a0.params_20[0].get()];
    if((state.storage_44[7] & 0x1) != 0) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;
    final int animIndex = a0.params_20[1].get();

    if(bent.currentAnimIndex_270 < 0) {
      FUN_800c9e10(bent.combatantIndex_26c, animIndex);
      bent.currentAnimIndex_270 = animIndex;
    } else if(bent.currentAnimIndex_270 != animIndex) {
      FUN_800ca194(bent.combatantIndex_26c, bent.currentAnimIndex_270);

      //LAB_800cb73c
      FUN_800c9e10(bent.combatantIndex_26c, animIndex);
      bent.currentAnimIndex_270 = animIndex;
    }

    //LAB_800cb750
    return FlowControl.PAUSE;
  }

  @ScriptDescription("No-op")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @Method(0x800cb764L)
  public static FlowControl FUN_800cb764(final RunningScript<?> a0) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related battle entity animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cb76cL)
  public static FlowControl FUN_800cb76c(final RunningScript<?> a0) {
    final ScriptState<?> s2 = scriptStatePtrArr_800bc1c0[a0.params_20[0].get()];
    final BattleEntity27c s0 = (BattleEntity27c)s2.innerStruct_00;
    if((s2.storage_44[7] & 0x1) == 0) {
      int animIndex = s0.currentAnimIndex_270;

      if(animIndex < 0) {
        animIndex = 0;
      }

      //LAB_800cb7d0
      if(isCombatantAssetLoaded(s0.combatantIndex_26c, animIndex)) {
        FUN_800ca194(s0.combatantIndex_26c, s0.loadingAnimIndex_26e);
        FUN_800ca100(s0.model_148, s0.combatantIndex_26c, animIndex);
        s2.storage_44[7] &= 0xffff_ff6f;
        s0.model_148.animationState_9c = 1;
        s0.loadingAnimIndex_26e = animIndex;
        s0.currentAnimIndex_270 = -1;
        return FlowControl.CONTINUE;
      }
    }

    //LAB_800cb830
    //LAB_800cb834
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Sets a battle entity's loading animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "animIndex", description = "The animation index")
  @Method(0x800cb84cL)
  public static FlowControl scriptSetLoadingBentAnimationIndex(final RunningScript<?> script) {
    final ScriptState<?> s2 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleEntity27c s0 = (BattleEntity27c)s2.innerStruct_00;

    if((s2.storage_44[7] & 0x1) == 0) {
      final int newAnim = script.params_20[1].get();
      final int currentAnim = s0.currentAnimIndex_270;

      if(currentAnim >= 0) {
        if(currentAnim != newAnim) {
          FUN_800ca194(s0.combatantIndex_26c, currentAnim);
        }

        //LAB_800cb8d0
        s0.currentAnimIndex_270 = -1;
      }

      //LAB_800cb8d4
      if(isCombatantAssetLoaded(s0.combatantIndex_26c, newAnim)) {
        FUN_800ca194(s0.combatantIndex_26c, s0.loadingAnimIndex_26e);
        FUN_800ca100(s0.model_148, s0.combatantIndex_26c, newAnim);
        s2.storage_44[7] &= 0xffff_ff6f;
        s0.model_148.animationState_9c = 1;
        s0.loadingAnimIndex_26e = newAnim;
        s0.currentAnimIndex_270 = -1;
        return FlowControl.CONTINUE;
      }

      //LAB_800cb934
      FUN_800c9e10(s0.combatantIndex_26c, newAnim);
    }

    //LAB_800cb944
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Something related battle entity animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cb95cL)
  public static FlowControl FUN_800cb95c(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    FUN_800ca26c(bent.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a battle entity's loading animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "animationIndex", description = "The animation index")
  @Method(0x800cb9b0L)
  public static FlowControl scriptGetLoadingBentAnimationIndex(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.loadingAnimIndex_26e);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Pauses a battle entity's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cb9f0L)
  public static FlowControl scriptPauseAnimation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.animationState_9c = 2;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Resumes a battle entity's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cba28L)
  public static FlowControl scriptResumeAnimation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.animationState_9c = 1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's loop state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "loop", description = "True to enable looping, false to disable")
  @Method(0x800cba60L)
  public static FlowControl scriptSetBentAnimationLoopState(final RunningScript<?> script) {
    //LAB_800cbab0
    if(script.params_20[1].get() != 0) {
      scriptStatePtrArr_800bc1c0[script.params_20[0].get()].storage_44[7] &= 0xffff_ff7f;
    } else {
      //LAB_800cbaa4
      scriptStatePtrArr_800bc1c0[script.params_20[0].get()].storage_44[7] |= 0x80;
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Check if a battle entity's animation has finished")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "finished", description = "True if finished, false otherwise")
  @Method(0x800cbabcL)
  public static FlowControl scriptAnimationHasFinished(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.model_148.remainingFrames_9e > 0 ? 0 : 1);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cbb00L)
  public static FlowControl FUN_800cbb00(final RunningScript<?> script) {
    final int scriptIndex = script.params_20[0].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex];
    BattleEntity27c v1 = state.innerStruct_00;

    float x = v1.model_148.coord2_14.coord.transfer.x;
    float y = v1.model_148.coord2_14.coord.transfer.y;
    float z = v1.model_148.coord2_14.coord.transfer.z;

    final int t0 = script.params_20[1].get();
    if(t0 >= 0) {
      state.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[t0];
      v1 = state.scriptState_c8.innerStruct_00;
      x -= v1.model_148.coord2_14.coord.transfer.x;
      y -= v1.model_148.coord2_14.coord.transfer.y;
      z -= v1.model_148.coord2_14.coord.transfer.z;
    } else {
      state.scriptState_c8 = null;
    }

    //LAB_800cbb98
    FUN_800cdc1c(state, x, y, z, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0, script.params_20[2].get());
    state.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cbc14L)
  public static FlowControl FUN_800cbc14(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = state.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);
    final int scriptIndex2 = script.params_20[1].get();

    if(scriptIndex2 >= 0) {
      state.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = state.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      state.scriptState_c8 = null;
    }

    //LAB_800cbcc4
    final float x = script.params_20[3].get() - vec.x;
    final float y = script.params_20[4].get() - vec.y;
    final float z = script.params_20[5].get() - vec.z;
    FUN_800cdc1c(state, vec.x, vec.y, vec.z, x, y, z, 0, Math.round(Math.sqrt(x * x + y * y + z * z) / script.params_20[2].get()));
    state.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cbde0L)
  public static FlowControl FUN_800cbde0(final RunningScript<?> script) {
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    BattleEntity27c bent = state.innerStruct_00;
    float x = bent.model_148.coord2_14.coord.transfer.x;
    float y = bent.model_148.coord2_14.coord.transfer.y;
    float z = bent.model_148.coord2_14.coord.transfer.z;

    if(script.params_20[1].get() >= 0) {
      state.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()];
      bent = state.scriptState_c8.innerStruct_00;
      x -= bent.model_148.coord2_14.coord.transfer.x;
      y -= bent.model_148.coord2_14.coord.transfer.y;
      z -= bent.model_148.coord2_14.coord.transfer.z;
    } else {
      state.scriptState_c8 = null;
    }

    //LAB_800cbe78
    FUN_800cdc1c(state, x, y, z, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0x20, script.params_20[2].get());
    state.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cbef8L)
  public static FlowControl FUN_800cbef8(final RunningScript<?> state) {
    final int scriptIndex1 = state.params_20[0].get();
    final int scriptIndex2 = state.params_20[1].get();
    final ScriptState<BattleEntity27c> s5 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = s5.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      s5.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = s5.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      s5.scriptState_c8 = null;
    }

    //LAB_800cbfa8
    final float x = state.params_20[3].get() - vec.x;
    final float y = state.params_20[4].get() - vec.y;
    final float z = state.params_20[5].get() - vec.z;
    FUN_800cdc1c(s5, vec.x, vec.y, vec.z, state.params_20[3].get(), state.params_20[4].get(), state.params_20[5].get(), 0x20, Math.round(Math.sqrt(x * x + y * y + z * z) / state.params_20[2].get()));
    s5.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc0c8L)
  public static FlowControl FUN_800cc0c8(final RunningScript<?> script) {
    final int s0 = script.params_20[0].get();
    final ScriptState<BattleEntity27c> a0 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[s0];
    final Vector3f a1 = new Vector3f(a0.innerStruct_00.model_148.coord2_14.coord.transfer);
    final int t0 = script.params_20[1].get();

    if(t0 >= 0) {
      a0.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[t0];
      a1.sub(a0.scriptState_c8.innerStruct_00.model_148.coord2_14.coord.transfer);
    } else {
      a0.scriptState_c8 = null;
    }

    //LAB_800cc160
    FUN_800cdc1c(a0, a1.x, a1.y, a1.z, script.params_20[3].get(), a1.y, script.params_20[4].get(), 0, script.params_20[2].get());
    a0.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc1ccL)
  public static FlowControl FUN_800cc1cc(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state1 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = state1.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      state1.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = state1.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      state1.scriptState_c8 = null;
    }

    //LAB_800cc27c
    final float x = script.params_20[3].get() - vec.x;
    final float z = script.params_20[4].get() - vec.z;
    FUN_800cdc1c(state1, vec.x, vec.y, vec.z, script.params_20[3].get(), vec.y, script.params_20[4].get(), 0, Math.round(Math.sqrt(x * x + z * z) / script.params_20[2].get()));
    state1.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc364L)
  public static FlowControl FUN_800cc364(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state1 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = state1.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      state1.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = state1.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      state1.scriptState_c8 = null;
    }

    //LAB_800cc3fc
    FUN_800cdc1c(state1, vec.x, vec.y, vec.z, script.params_20[3].get(), vec.y, script.params_20[4].get(), 0x20, script.params_20[2].get());
    state1.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc46cL)
  public static FlowControl FUN_800cc46c(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> s5 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = s5.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      s5.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = s5.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      s5.scriptState_c8 = null;
    }

    //LAB_800cc51c
    final float x = script.params_20[3].get() - vec.x;
    final float z = script.params_20[4].get() - vec.z;
    FUN_800cdc1c(s5, vec.x, vec.y, vec.z, script.params_20[3].get(), vec.y, script.params_20[4].get(), 0x20, Math.round(Math.sqrt(x * x + z * z) / script.params_20[2].get()));
    s5.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Turn a battle entity towards another")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndexToTurn", description = "A BattleEntity27c script index to turn")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndexTarget", description = "A BattleEntity27c script index to target")
  @Method(0x800cc608L)
  public static FlowControl scriptBentLookAtBent(final RunningScript<?> script) {
    final BattleEntity27c s0 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c v0 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    s0.model_148.coord2_14.transforms.rotate.y = MathHelper.atan2(v0.model_148.coord2_14.coord.transfer.x - s0.model_148.coord2_14.coord.transfer.x, v0.model_148.coord2_14.coord.transfer.z - s0.model_148.coord2_14.coord.transfer.z) + MathHelper.PI;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @Method(0x800cc698L)
  public static FlowControl FUN_800cc698(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state1 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final ScriptState<BattleEntity27c> state2 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
    final BattleEntity27c bent1 = state1.innerStruct_00;
    final BattleEntity27c bent2 = state2.innerStruct_00;
    final int ticks = script.params_20[2].get();
    final float v0 = MathHelper.floorMod(MathHelper.atan2(bent2.model_148.coord2_14.coord.transfer.x - bent1.model_148.coord2_14.coord.transfer.x, bent2.model_148.coord2_14.coord.transfer.z - bent1.model_148.coord2_14.coord.transfer.z) - bent1.model_148.coord2_14.transforms.rotate.y, MathHelper.TWO_PI) - MathHelper.PI;
    state1.scriptState_c8 = state2;
    state1.ticks_cc = ticks;
    state1._d0.x = v0;
    state1._d0.y = v0 / ticks;
    state1.setTempTicker(Bttl_800c::FUN_800cb34c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cc784L)
  public static FlowControl FUN_800cc784(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    FUN_800ca418(bent.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to battle entity asset loading")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cc7d8L)
  public static FlowControl FUN_800cc7d8(final RunningScript<?> script) {
    final ScriptState<?> v1 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final long s2 = v1.storage_44[7] & 0x4;
    final int s1 = ((BattleEntity27c)v1.innerStruct_00).combatantIndex_26c;

    //LAB_800cc83c
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      if(i != s1) {
        final CombatantStruct1a8 combatant = getCombatant(i);

        if((combatant.flags_19e & 0x1) != 0 && combatant.mrg_04 != null && combatant.charIndex_1a2 >= 0) {
          final int v0 = combatant.flags_19e >>> 2 ^ 1;

          if(s2 == 0) {
            //LAB_800cc8ac
            if((v0 & 1) == 0) {
              //LAB_800cc8b4
              FUN_800ca418(i);
            }
          } else {
            if((v0 & 1) != 0) {
              FUN_800ca418(i);
            }
          }
        }
      }

      //LAB_800cc8bc
    }

    //LAB_800cc8d8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads the attack animations for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cc8f4L)
  public static FlowControl scriptLoadAttackAnimations(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    loadAttackAnimations(bent.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads the attack animations for every combatant")
  @Method(0x800cc948L)
  public static FlowControl loadAllCharAttackAnimations(final RunningScript<?> script) {
    //LAB_800cc970
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 combatant = getCombatant(i);
      if((combatant.flags_19e & 0x1) != 0 && combatant.charIndex_1a2 >= 0) {
        loadAttackAnimations(i);
      }

      //LAB_800cc9a8
    }

    //LAB_800cc9c0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Enables or disables a battle entity's texture's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex", description = "The texture index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "enabled", description = "True to enable, false otherwise")
  @Method(0x800cc9d8L)
  public static FlowControl scriptEnableBentTextureAnimation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.animateTextures_ec[script.params_20[1].get()] = script.params_20[2].get() > 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets up battle menu, handles its input, and renders it")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "displayableIconsBitset", description = "A bitset of which icons are displayed")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "disabledIconsBitset", description = "A bitset of which icons are disabled")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "selectedAction", description = "The action the player has selected")
  @Method(0x800cca34L)
  public static FlowControl scriptSetUpAndHandleCombatMenu(final RunningScript<BattleEntity27c> script) {
    final int displayableIconsBitset = script.params_20[0].get();

    if(currentDisplayableIconsBitset_800c675c.get() != displayableIconsBitset || (script.scriptState_04.storage_44[7] & 0x1000) != 0) {
      //LAB_800cca7c
      final int disabledIconsBitset;

      if(script.paramCount_14 == 2) {
        disabledIconsBitset = 0;
      } else {
        //LAB_800ccaa0
        disabledIconsBitset = script.params_20[1].get();
      }

      //LAB_800ccab4
      initializeCombatMenuIcons(script.scriptState_04, displayableIconsBitset, disabledIconsBitset);

      script.scriptState_04.storage_44[7] &= 0xffff_efff;
      currentDisplayableIconsBitset_800c675c.set(displayableIconsBitset);
    }

    //LAB_800ccaec
    toggleBattleMenuSelectorRendering(true);

    final int selectedAction = handleCombatMenu();
    if(selectedAction == 0) {
      //LAB_800ccb24
      return FlowControl.PAUSE_AND_REWIND;
    }

    toggleBattleMenuSelectorRendering(false);
    script.params_20[2].set(selectedAction - 1);

    //LAB_800ccb28
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a damage number to a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @Method(0x800ccb3cL)
  public static FlowControl scriptRenderDamage(final RunningScript<?> script) {
    Bttl_800f.renderDamage(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a floating number above a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The number to show")
  @Method(0x800ccb70L)
  public static FlowControl scriptAddFloatingNumberForBent(final RunningScript<?> script) {
    addFloatingNumberForBent(script.params_20[0].get(), script.params_20[1].get(), 0xdL);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "isDragoon", description = "Whether or not the battle entity is a dragoon")
  @Method(0x800ccba4L)
  public static FlowControl FUN_800ccba4(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;
    final CombatantStruct1a8 combatant = bent.combatant_144;
    final int combatantIndex = bent.combatantIndex_26c;

    if((state.storage_44[7] & 0x1) == 0) {
      if(bent.currentAnimIndex_270 >= 0) {
        FUN_800ca194(combatantIndex, bent.currentAnimIndex_270);
      }

      //LAB_800ccc24
      deallocateCombatant(combatantIndex);

      if(script.params_20[1].get() != 0) {
        state.storage_44[7] |= 0x3;
        combatant.charIndex_1a2 |= 0x1;
      } else {
        //LAB_800ccc60
        state.storage_44[7] = state.storage_44[7] & 0xffff_fffd | 0x1;
        combatant.charIndex_1a2 &= 0xfffe;
      }

      //LAB_800ccc78
      loadCombatantTextures(combatantIndex);
      loadCombatantTmdAndAnims(combatantIndex);
      //LAB_800ccc94
    } else if((combatant.flags_19e & 0x20) == 0) {
      FUN_800c952c(bent.model_148, combatantIndex);
      bent.loadingAnimIndex_26e = 0;
      bent.currentAnimIndex_270 = -1;
      state.storage_44[7] &= 0xffff_fffe;
      return FlowControl.CONTINUE;
    }

    //LAB_800ccccc
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Gets the character or monster ID for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "id", description = "The character or monster ID")
  @Method(0x800cccf4L)
  public static FlowControl scriptGetCharOrMonsterId(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.charId_272);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a stat value of a battle entity (doesn't allow setting negative HP)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new stat value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @Method(0x800ccd34L)
  public static FlowControl scriptSetBentStat(final RunningScript<?> script) {
    int value = script.params_20[1].get();
    if(script.params_20[2].get() == 2 && value < 0) {
      value = 0;
    }

    //LAB_800ccd8c
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.setStat(BattleEntityStat.fromLegacy(script.params_20[2].get()), value);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a stat value of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new stat value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @Method(0x800ccda0L)
  public static FlowControl scriptSetBentRawStat(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.setStat(BattleEntityStat.fromLegacy(Math.max(0, script.params_20[2].get())), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a stat value of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The stat value")
  @Method(0x800cce04L)
  public static FlowControl scriptGetBentStat(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[2].set(bent.getStat(BattleEntityStat.fromLegacy(script.params_20[1].get())));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a stat value of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The stat value")
  @Method(0x800cce70L)
  public static FlowControl scriptGetBentStat2(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[2].set(bent.getStat(BattleEntityStat.fromLegacy(script.params_20[1].get())));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to HUD")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800ccec8L)
  public static FlowControl FUN_800ccec8(final RunningScript<?> script) {
    FUN_800f1a00(script.params_20[0].get() > 0 ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets post-battle action to 3")
  @Method(0x800ccef8L)
  public static FlowControl FUN_800ccef8(final RunningScript<?> script) {
    postBattleActionIndex_800bc974.set(3);
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Sets the post-battle action")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "action", description = "The post-battle action")
  @Method(0x800ccf0cL)
  public static FlowControl scriptSetPostBattleAction(final RunningScript<?> script) {
    postBattleActionIndex_800bc974.set(script.params_20[0].get());
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Sets a battle entity as dead (or not dead) and drops its loot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dead", description = "True for dead, false otherwise")
  @Method(0x800ccf2cL)
  public static FlowControl scriptSetBentDeadAndDropLoot(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleEntity27c data = (BattleEntity27c)state.innerStruct_00;

    int flags = state.storage_44[7];
    if(script.params_20[1].get() != 0) {
      if((flags & 0x40) == 0) { // Not dead
        flags = flags | 0x40; // Set dead

        if((flags & 0x4) != 0) { // Monster
          final CombatantStruct1a8 enemyCombatant = data.combatant_144;
          goldGainedFromCombat_800bc920.add(enemyCombatant.gold_196);
          totalXpFromCombat_800bc95c.add(enemyCombatant.xp_194);

          if((flags & 0x2000) == 0) { // Hasn't already dropped loot
            for(final CombatantStruct1a8.ItemDrop drop : enemyCombatant.drops) {
              if(simpleRand() * 100 >> 16 < drop.chance()) {
                if(drop.item() instanceof final Equipment equipment) {
                  itemsDroppedByEnemies_800bc928.add(new EnemyDrop(equipment.getIcon(), equipment.getName(), () -> giveEquipment(equipment), () -> equipmentOverflow.add(equipment)));
                } else if(drop.item() instanceof final Item item) {
                  itemsDroppedByEnemies_800bc928.add(new EnemyDrop(item.getIcon(), item.getName(), () -> giveItem(item), () -> itemOverflow.add(item)));
                }

                flags |= 0x2000;
              }
            }
          }
        }
      }
    } else {
      //LAB_800cd04c
      flags = flags & 0xffff_ffbf;
    }

    //LAB_800cd054
    state.storage_44[7] = flags;
    cacheLivingBents();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity as dead (or not), but doesn't drop loot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dead", description = "True for dead, false otherwise")
  @Method(0x800cd078L)
  public static FlowControl scriptSetBentDead(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];

    //LAB_800cd0d0
    if(script.params_20[1].get() != 0) {
      state.storage_44[7] |= 0x40;
    } else {
      //LAB_800cd0c4
      state.storage_44[7] &= 0xffff_ffbf;
    }

    cacheLivingBents();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the hit property of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "hitNum", description = "The hit number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "hitPropertyIndex", description = "The hit property index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The hit property value")
  @Method(0x800cd0ecL)
  public static FlowControl scriptGetHitProperty(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[3].set(getHitProperty(
      bent.charSlot_276,
      script.params_20[1].get(),
      script.params_20[2].get()
    ));

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Levels up a character's addition")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "doIt", description = "If false, this method does nothing")
  @Method(0x800cd160L)
  public static FlowControl scriptLevelUpAddition(final RunningScript<?> script) {
    if(script.params_20[1].get() != 0) {
      final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

      final int charIndex = bent.charId_272;
      final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];

      final int additionIndex = charData.selectedAddition_19 - additionOffsets_8004f5ac.get(charIndex).get();
      if(charIndex == 2 || charIndex == 8 || additionIndex < 0) {
        //LAB_800cd200
        return FlowControl.CONTINUE;
      }

      //LAB_800cd208
      final int additionXp = Math.min(99, charData.additionXp_22[additionIndex] + 1);

      //LAB_800cd240
      //LAB_800cd288
      while(charData.additionLevels_1a[additionIndex] < 5 && additionXp >= additionNextLevelXp_800fa744.get(charData.additionLevels_1a[additionIndex]).get()) {
        charData.additionLevels_1a[additionIndex]++;
      }

      //LAB_800cd2ac
      int nonMaxedAdditions = additionCounts_8004f5c0.get(charIndex).get();
      int lastNonMaxAdditionIndex = -1;

      // Find the first addition that isn't already maxed out
      //LAB_800cd2ec
      for(int additionIndex2 = 0; additionIndex2 < additionCounts_8004f5c0.get(charIndex).get(); additionIndex2++) {
        if(charData.additionLevels_1a[additionIndex2] == 5) {
          nonMaxedAdditions--;
        } else {
          //LAB_800cd308
          lastNonMaxAdditionIndex = additionIndex2;
        }

        //LAB_800cd30c
      }

      // If there's only one addition that isn't maxed (the ultimate addition), unlock it
      //LAB_800cd31c
      if(nonMaxedAdditions < 2 && (charData.partyFlags_04 & 0x40) == 0) {
        charData.partyFlags_04 |= 0x40;

        if(lastNonMaxAdditionIndex >= 0) {
          charData.additionLevels_1a[lastNonMaxAdditionIndex] = 1;
        }

        //LAB_800cd36c
        unlockedUltimateAddition_800bc910[bent.charSlot_276] = true;
      }

      //LAB_800cd390
      charData.additionXp_22[additionIndex] = additionXp;
    }

    //LAB_800cd3ac
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Changes the visibility of a battle entity's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "visible", description = "True for visible, false otherwise")
  @Method(0x800cd3b4L)
  public static FlowControl scriptSetModelPartVisibility(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    if(script.params_20[2].get() != 0) {
      bent.model_148.partInvisible_f4 &= ~(0x1L << script.params_20[1].get());
    } else {
      //LAB_800cd420
      bent.model_148.partInvisible_f4 |= 0x1L << script.params_20[1].get();
    }

    //LAB_800cd460
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, something to do with loading files")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "drgnIndex", description = "The DRGN#.BIN index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fileIndex", description = "The file index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "index", description = "The output battleState_8006e398._580 array index")
  @Method(0x800cd468L)
  public static FlowControl FUN_800cd468(final RunningScript<?> script) {
    script.params_20[2].set(FUN_800cac38(script.params_20[0].get(), script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, something to do with loading files, may wait until the file is loaded")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The battleState_8006e398._580 array index")
  @Method(0x800cd4b0L)
  public static FlowControl FUN_800cd4b0(final RunningScript<?> script) {
    final BttlStruct08 v0 = FUN_800cad50(script.params_20[0].get());
    return v0._04 == 1 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, something to do with loading files, may clear the file entry")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The battleState_8006e398._580 array index")
  @Method(0x800cd4f0L)
  public static FlowControl FUN_800cd4f0(final RunningScript<?> script) {
    FUN_800cad64(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a combatant to the battle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charIndexAndFlags", description = "Exact use unknown, seems to be flags, and possibly character ID in the higher bits?")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The new combatant's index")
  @Method(0x800cd52cL)
  public static FlowControl scriptAddCombatant(final RunningScript<?> script) {
    script.params_20[1].set(addCombatant(script.params_20[0].get(), -1));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates and removes a combatant from the battle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant's index")
  @Method(0x800cd570L)
  public static FlowControl scriptDeallocateAndRemoveCombatant(final RunningScript<?> script) {
    deallocateCombatant(script.params_20[0].get());
    removeCombatant(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocate a battle entity child of this script")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "entrypointIndex", description = "The entrypoint of this script for the new battle entity to enter")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant to attach to the new battle entity")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The allocated script index")
  @Method(0x800cd5b4L)
  public static FlowControl scriptAllocateBent(final RunningScript<?> script) {
    final String name = "Bent allocated by script " + script.scriptState_04.index;
    final ScriptState<MonsterBattleEntity> state = SCRIPTS.allocateScriptState(name, new MonsterBattleEntity(name));
    script.params_20[2].set(state.index);
    state.setTicker(Bttl_800c::bentTicker);
    state.setDestructor(Bttl_800c::bentDestructor);
    state.loadScriptFile(script.scriptState_04.scriptPtr_14, script.params_20[0].get());
    state.storage_44[7] |= 0x804;
    battleState_8006e398.allBents_e0c[allBentCount_800c66d0.get()] = state;
    battleState_8006e398.monsterBents_e50[monsterCount_800c6768.get()] = state;

    final BattleEntity27c bent = state.innerStruct_00;
    bent.magic_00 = BattleObject.BOBJ;
    final CombatantStruct1a8 combatant = getCombatant(script.params_20[1].get());
    bent.combatant_144 = combatant;
    bent.combatantIndex_26c = script.params_20[1].get();
    bent.charId_272 = combatant.charIndex_1a2;
    bent.bentSlot_274 = allBentCount_800c66d0.get();
    allBentCount_800c66d0.incr();
    bent.charSlot_276 = monsterCount_800c6768.get();
    monsterCount_800c6768.incr();
    bent.model_148.coord2_14.coord.transfer.set(0, 0, 0);
    bent.model_148.coord2_14.transforms.rotate.zero();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to combatants")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800cd740L)
  public static FlowControl FUN_800cd740(final RunningScript<?> script) {
    final BttlStruct08 v0 = FUN_800cad50(script.params_20[0].get());

    if(v0._04 == 1) {
      //LAB_800cd794
      return FlowControl.PAUSE_AND_REWIND;
    }

    FUN_800c94f8(script.params_20[0].get(), (short)script.params_20[1].get());

    //LAB_800cd798
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to combatants")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0", description = "Combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800cd7a8L)
  public static FlowControl FUN_800cd7a8(final RunningScript<?> script) {
    final BttlStruct08 v0 = FUN_800cad50(script.params_20[0].get());

    if(v0._04 == 1) {
      //LAB_800cd7fc
      return FlowControl.PAUSE_AND_REWIND;
    }

    FUN_800ca528(script.params_20[0].get(), (short)script.params_20[1].get());

    //LAB_800cd800
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to combatants")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @Method(0x800cd810L)
  public static FlowControl FUN_800cd810(final RunningScript<?> script) {
    final int s0 = script.params_20[2].get();

    if(s0 >= 0) {
      //LAB_800cd85c
      final BttlStruct08 v0 = FUN_800cad50(s0);

      if(v0._04 == 1) {
        return FlowControl.PAUSE_AND_REWIND;
      }

      FUN_800c9db8(script.params_20[0].get(), script.params_20[1].get(), s0);
    } else {
      FUN_800c9c7c(script.params_20[0].get(), script.params_20[1].get());
    }

    //LAB_800cd890
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to combatant textures")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800cd8a4L)
  public static FlowControl FUN_800cd8a4(final RunningScript<?> script) {
    final BttlStruct08 a1 = FUN_800cad50(script.params_20[1].get());

    if(a1._04 == 1) {
      //LAB_800cd8fc
      return FlowControl.PAUSE_AND_REWIND;
    }

    loadCombatantTim(script.params_20[0].get(), a1.data_00);

    //LAB_800cd900
    return FlowControl.PAUSE;
  }

  @ScriptDescription("Unknown, loads files")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "drgnIndex", description = "The DRGN#.BIN index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fileIndex", description = "The file index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "queueIndex", description = "The index into the file queue")
  @Method(0x800cd910L)
  public static FlowControl FUN_800cd910(final RunningScript<?> script) {
    script.params_20[2].set(FUN_800cac38(script.params_20[0].get(), script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the combatant index for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @Method(0x800cd958L)
  public static FlowControl scriptGetCombatantIndex(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the battle entity slot or char slot for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "charOrBentSlot", description = "The character or battle entity slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "mode", description = "If true, returns character slot, if false returns battle entity slot")
  @Method(0x800cd998L)
  public static FlowControl scriptGetBentSlot(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if(script.params_20[2].get() != 0) {
      script.params_20[1].set(bent.charSlot_276);
    } else {
      //LAB_800cd9e8
      script.params_20[1].set(bent.bentSlot_274);
    }

    //LAB_800cd9f4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the number of parts in a battle entity's model")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c's script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "count")
  @Method(0x800cd9fcL)
  public static FlowControl scriptGetBentNobj(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.model_148.modelParts_00.length);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates a battle entity's combatant")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cda3cL)
  public static FlowControl scriptDeallocateCombatant(final RunningScript<?> script) {
    deallocateCombatant(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to asset loading")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cda78L)
  public static FlowControl FUN_800cda78(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if(!isCombatantModelLoaded(bent.combatantIndex_26c)) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800cdacc
    bent.combatant_144.charIndex_1a2 = -1;

    //LAB_800cdaf4
    bent.loadingAnimIndex_26e = 0;
    FUN_800c952c(bent.model_148, bent.combatantIndex_26c);

    //LAB_800cdb08
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stops rendering the battle stage and skybox")
  @Method(0x800cdb18L)
  public static FlowControl scriptStopRenderingStage(final RunningScript<?> script) {
    setStageHasNoModel();
    setDontRenderStageBackground();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads a new battle stage")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stageIndex", description = "The stage index")
  @Method(0x800cdb44L)
  public static FlowControl scriptLoadStage(final RunningScript<?> script) {
    loadStage(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears combat scripts and sets the combat disabled flag")
  @Method(0x800cdb74L)
  public static FlowControl scriptDisableCombat(final RunningScript<?> script) {
    combatDisabled_800c66b9.set(true);

    //LAB_800cdbb8
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      battleState_8006e398.charBents_e40[i].loadScriptFile(null);
    }

    //LAB_800cdbe0
    script_800c66fc = null;

    //LAB_800cdc00
    return FlowControl.CONTINUE;
  }

  @Method(0x800cdc1cL)
  public static void FUN_800cdc1c(final ScriptState<BattleEntity27c> s1, final float x0, final float y0, final float z0, final float x1, final float y1, final float z1, final float a7, final int ticks) {
    final float dx = x1 - x0;
    final float dy = y1 - y0;
    final float dz = z1 - z0;

    s1.ticks_cc = ticks;
    s1._e8.set(x1, y1, z1);
    s1._d0.set(dx, dy, dz);

    // Fix for retail /0 bug
    if(ticks > 0) {
      s1._dc.set(dx / ticks, 0.0f, dz / ticks);
    } else {
      s1._dc.zero();
    }

    s1._dc.y = FUN_80013404(a7, dy, ticks);
    s1._f4 = a7;
  }

  @Method(0x800cdcecL)
  public static void getVertexMinMaxByComponent(final Model124 model, final int dobjIndex, final Vector3f smallestVertRef, final Vector3f largestVertRef, final EffectManagerData6c<EffectManagerData6cInner.WeaponTrailType> manager, final IntRef smallestIndexRef, final IntRef largestIndexRef) {
    float largest = -Float.MAX_VALUE;
    float smallest = Float.MAX_VALUE;
    int largestIndex = 0;
    int smallestIndex = 0;
    final TmdObjTable1c tmd = model.modelParts_00[dobjIndex].tmd_08;

    //LAB_800cdd24
    for(int i = 0; i < tmd.n_vert_04; i++) {
      final Vector3f vert = tmd.vert_top_00[i];
      final float val = vert.get(manager._10.vertexComponent_24);

      if(val >= largest) {
        largest = val;
        largestIndex = i;
        largestVertRef.set(vert);
        //LAB_800cdd7c
      } else if(val <= smallest) {
        smallest = val;
        smallestIndex = i;
        smallestVertRef.set(vert);
      }
      //LAB_800cddbc
    }

    //LAB_800cddcc
    largestIndexRef.set(largestIndex);
    smallestIndexRef.set(smallestIndex);
  }

  @Method(0x800cdde4L)
  public static WeaponTrailEffectSegment2c getRootSegment(final WeaponTrailEffect3c trail) {
    WeaponTrailEffectSegment2c segment = trail.currentSegment_38;

    //LAB_800cddfc
    while(segment.previousSegmentRef_24 != null) {
      segment = segment.previousSegmentRef_24;
    }

    //LAB_800cde14
    return segment;
  }

  @Method(0x800cde1cL)
  public static WeaponTrailEffectSegment2c FUN_800cde1c(final WeaponTrailEffect3c trail) {
    WeaponTrailEffectSegment2c segment = trail.segments_34[0];

    int segmentIndex = 0;
    //LAB_800cde3c
    while(segment._03) {
      segmentIndex++;
      segment = trail.segments_34[segmentIndex];
    }

    //LAB_800cde50
    if(segmentIndex == 64) {
      segment = getRootSegment(trail);
      segment._03 = false;

      if(segment.nextSegmentRef_28 != null) {
        segment.nextSegmentRef_28.previousSegmentRef_24 = null;
      }
    }

    //LAB_800cde80
    //LAB_800cde84
    return segment;
  }

  @Method(0x800cde94L)
  public static void renderWeaponTrailEffect(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.WeaponTrailType>> state, final EffectManagerData6c<EffectManagerData6cInner.WeaponTrailType> data) {
    // Prevent garbage trails from rendering across the screen
    final int renderCoordThreshold = 5000;
    boolean renderCoordThresholdExceeded;

    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)data.effect_44;

    if(trail.currentSegment_38 != null) {
      final Vector3i colour = new Vector3i(data._10.colour_1c).mul(0x100);
      final Vector3i colourStep = new Vector3i(colour).div(trail.segmentCount_0e);
      WeaponTrailEffectSegment2c segment = trail.currentSegment_38;

      final Vector2f v0 = new Vector2f();
      transformWorldspaceToScreenspace(segment.endpointCoords_04[0], v0);
      renderCoordThresholdExceeded = Math.abs(v0.x) > renderCoordThreshold || Math.abs(v0.y) > renderCoordThreshold;

      final Vector2f v2 = new Vector2f();
      final float z = transformWorldspaceToScreenspace(segment.endpointCoords_04[1], v2) / 4.0f;
      renderCoordThresholdExceeded = renderCoordThresholdExceeded || Math.abs(v2.x) > renderCoordThreshold || Math.abs(v2.y) > renderCoordThreshold;

      //LAB_800cdf94
      segment = segment.previousSegmentRef_24;
      for(int i = 0; i < trail.segmentCount_0e && segment != null; i++) {
        final Vector2f v1 = new Vector2f();
        transformWorldspaceToScreenspace(segment.endpointCoords_04[0], v1);
        renderCoordThresholdExceeded = renderCoordThresholdExceeded || Math.abs(v1.x) > renderCoordThreshold || Math.abs(v1.y) > renderCoordThreshold;
        final Vector2f v3 = new Vector2f();
        transformWorldspaceToScreenspace(segment.endpointCoords_04[1], v3);
        renderCoordThresholdExceeded = renderCoordThresholdExceeded || Math.abs(v3.x) > renderCoordThreshold || Math.abs(v3.y) > renderCoordThreshold;

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .translucent(Translucency.B_PLUS_F)
          .pos(0, v0.x, v0.y)
          .pos(1, v1.x, v1.y)
          .pos(2, v2.x, v2.y)
          .pos(3, v3.x, v3.y)
          .monochrome(0, 0)
          .monochrome(1, 0)
          .rgb(2, colour.x >>> 8, colour.y >>> 8, colour.z >>> 8);

        colour.sub(colourStep);

        cmd.rgb(3, colour.x >>> 8, colour.y >>> 8, colour.z >>> 8);

        float zFinal = z + data._10.z_22;
        if(zFinal >= 0xa0 && !renderCoordThresholdExceeded) {
          if(zFinal >= 0xffe) {
            zFinal = 0xffe;
          }

          //LAB_800ce138
          GPU.queueCommand(zFinal / 4.0f, cmd);
        }

        //LAB_800ce14c
        v0.x = v1.x;
        v0.y = v1.y;
        v2.x = v3.x;
        v2.y = v3.y;
        segment = segment.previousSegmentRef_24;
        renderCoordThresholdExceeded =
          Math.abs(v0.x) > renderCoordThreshold ||
          Math.abs(v0.y) > renderCoordThreshold ||
          Math.abs(v2.x) > renderCoordThreshold ||
          Math.abs(v2.y) > renderCoordThreshold;
      }

      //LAB_800ce1a0
      //LAB_800ce1a4
    }

    //LAB_800ce230
  }

  @Method(0x800ce254L)
  public static void tickWeaponTrailEffect(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.WeaponTrailType>> state, final EffectManagerData6c<EffectManagerData6cInner.WeaponTrailType> data) {
    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)data.effect_44;
    trail.currentSegmentIndex_00++;
    if(trail.currentSegmentIndex_00 == 0) {
      final IntRef smallestVertexIndex = new IntRef();
      final IntRef largestVertexIndex = new IntRef();
      getVertexMinMaxByComponent(trail.parentModel_30, trail.dobjIndex_08, trail.smallestVertex_20, trail.largestVertex_10, data, smallestVertexIndex, largestVertexIndex);
      trail.smallestVertexIndex_0c = smallestVertexIndex.get();
      trail.largestVertexIndex_0a = largestVertexIndex.get();
      return;
    }

    //LAB_800ce2c4
    WeaponTrailEffectSegment2c segment = FUN_800cde1c(trail);

    if(trail.currentSegment_38 != null) {
      trail.currentSegment_38.nextSegmentRef_28 = segment;
    }

    //LAB_800ce2e4
    segment.unused_00 = 0x6c;
    segment.unused_01 = 0x63;
    segment.unused_02 = 0x73;
    segment._03 = true;
    segment.nextSegmentRef_28 = null;
    segment.previousSegmentRef_24 = trail.currentSegment_38;
    trail.currentSegment_38 = segment;

    //LAB_800ce320
    for(int i = 0; i < 2; i++) {
      final MV perspectiveTransformMatrix = new MV();
      GsGetLw(trail.parentModel_30.modelParts_00[trail.dobjIndex_08].coord2_04, perspectiveTransformMatrix);
      (i == 0 ? trail.smallestVertex_20 : trail.largestVertex_10).mul(perspectiveTransformMatrix, segment.endpointCoords_04[i]); // Yes, I hate me for this syntax too
      segment.endpointCoords_04[i].add(perspectiveTransformMatrix.transfer);
    }

    //LAB_800ce3e0
    segment = trail.currentSegment_38;
    while(segment != null) {
      applyWeaponTrailScaling(segment.endpointCoords_04[1], segment.endpointCoords_04[0], 1.0f, 0.25f);
      segment = segment.previousSegmentRef_24;
    }

    //LAB_800ce404
    //LAB_800ce40c
    int s6;
    for(int i = 0; i < 2; i++) {
      segment = trail.currentSegment_38;
      s6 = 0;

      //LAB_800ce41c
      while(segment != null) {
        if(segment.nextSegmentRef_28 != null) {
          if(segment.previousSegmentRef_24 != null) {
            WeaponTrailEffectSegment2c previousSegment = segment.previousSegmentRef_24;

            //LAB_800ce444
            final WeaponTrailEffectSegment2c[] sp0x50 = new WeaponTrailEffectSegment2c[2];
            for(int j = 0; j < 2; j++) {
              final WeaponTrailEffectSegment2c v0 = FUN_800cde1c(trail);
              sp0x50[j] = v0;
              v0.unused_00 = 0x6c;
              v0.unused_01 = 0x63;
              v0.unused_02 = 0x73;
              v0._03 = true;
              v0.endpointCoords_04[0].set(previousSegment.endpointCoords_04[0]).sub(segment.endpointCoords_04[0]).div(3).add(segment.endpointCoords_04[0]);
              v0.endpointCoords_04[1].set(previousSegment.endpointCoords_04[1]).sub(segment.endpointCoords_04[1]).div(3).add(segment.endpointCoords_04[1]);
              previousSegment = segment.nextSegmentRef_28;
            }

            sp0x50[0].previousSegmentRef_24 = segment.previousSegmentRef_24;
            sp0x50[1].previousSegmentRef_24 = sp0x50[0];
            sp0x50[1].nextSegmentRef_28 = segment.nextSegmentRef_28;
            sp0x50[0].nextSegmentRef_28 = sp0x50[1];
            segment.nextSegmentRef_28.previousSegmentRef_24 = sp0x50[1];
            segment.previousSegmentRef_24.nextSegmentRef_28 = sp0x50[0];
            segment._03 = false;
            segment = segment.previousSegmentRef_24;
            s6++;
            if(s6 > i * 2 || segment == null) {
              break;
            }
          }
        }

        //LAB_800ce630
        segment = segment.previousSegmentRef_24;
      }

      //LAB_800ce640
    }

    //LAB_800ce650
  }

  @ScriptDescription("Allocates a weapon trail effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The battle object index to trail behind")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "dobjIndex", description = "The model index")
  @Method(0x800ce6a8L)
  public static FlowControl scriptAllocateWeaponTrailEffect(final RunningScript<? extends BattleObject> script) {
    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.WeaponTrailType>> state = allocateEffectManager(
      "Weapon trail",
      script.scriptState_04,
      Bttl_800c::tickWeaponTrailEffect,
      Bttl_800c::renderWeaponTrailEffect,
      null,
      new WeaponTrailEffect3c(),
      new EffectManagerData6cInner.WeaponTrailType()
    );

    final EffectManagerData6c<EffectManagerData6cInner.WeaponTrailType> manager = state.innerStruct_00;
    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)manager.effect_44;

    //LAB_800ce75c
    for(int i = 0; i < 65; i++) {
      final WeaponTrailEffectSegment2c segment = trail.segments_34[i];
      segment.unused_00 = 0x6c;
      segment.unused_01 = 0x63;
      segment.unused_02 = 0x73;
      segment._03 = false;
      segment.previousSegmentRef_24 = null;
      segment.nextSegmentRef_28 = null;
    }

    trail.currentSegment_38 = null;
    trail.currentSegmentIndex_00 = -1;
    trail.parentIndex_04 = script.params_20[1].get();
    trail.dobjIndex_08 = script.params_20[2].get();
    trail.segmentCount_0e = 20;
    manager._10.colour_1c.set(0xff, 0x80, 0x60);

    final BattleObject parent = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);
    if(BattleObject.EM__.equals(parent.magic_00)) {
      trail.parentModel_30 = ((ModelEffect13c)((EffectManagerData6c<?>)parent).effect_44).model_10;
    } else {
      //LAB_800ce7f8
      trail.parentModel_30 = ((BattleEntity27c)parent).model_148;
    }

    //LAB_800ce804
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ce83cL)
  public static void setWeaponTrailSegmentCount(final int scriptIndex, int segmentCount) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)manager.effect_44;

    segmentCount = segmentCount * 4;
    if((segmentCount & 0xff) > 0x40) {
      segmentCount = 0x40;
    }

    trail.segmentCount_0e = segmentCount;
  }

  @Method(0x800ce880L)
  public static void applyWeaponTrailScaling(final Vector3f largestVertex, final Vector3f smallestVertex, final float largestVertexDiffScale, final float smallestVertexDiffScale) {
    final Vector3f vertexDiff = new Vector3f();
    final Vector3f scaledVertexDiff = new Vector3f();
    vertexDiff.set(largestVertex).sub(smallestVertex);

    scaledVertexDiff.set(
      vertexDiff.x * largestVertexDiffScale,
      vertexDiff.y * largestVertexDiffScale,
      vertexDiff.z * largestVertexDiffScale
    );

    largestVertex.set(smallestVertex).add(scaledVertexDiff);

    scaledVertexDiff.set(
      vertexDiff.x * smallestVertexDiffScale,
      vertexDiff.y * smallestVertexDiffScale,
      vertexDiff.z * smallestVertexDiffScale
    );

    smallestVertex.add(scaledVertexDiff);
  }

  @ScriptDescription("Rescales the weapon trail effect as it progresses")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The weapon trail effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "smallScalingFactor", description = "The scaling factor for the trailing end")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "largeScalingFactor", description = "The scaling factor for the leading end")
  @Method(0x800ce9b0L)
  public static FlowControl scriptApplyWeaponTrailScaling(final RunningScript<?> script) {
    final EffectManagerData6c<EffectManagerData6cInner.WeaponTrailType> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerData6cInner.WeaponTrailType.class));
    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)manager.effect_44;
    applyWeaponTrailScaling(trail.largestVertex_10, trail.smallestVertex_20, script.params_20[2].get() / (float)0x1000, script.params_20[1].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cea1cL)
  public static void scriptGetScriptedObjectPos(final int scriptIndex, final Vector3f posOut) {
    final BattleObject bobj = (BattleObject)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    posOut.set(bobj.getPosition());
  }

  @Method(0x800cea9cL)
  public static void tickFullScreenOverlay(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager) {
    final FullScreenOverlayEffect0e effect = (FullScreenOverlayEffect0e)manager.effect_44;

    if(effect.ticksRemaining_0c != 0) {
      effect.r_00 += effect.stepR_06;
      effect.g_02 += effect.stepG_08;
      effect.b_04 += effect.stepB_0a;
      effect.ticksRemaining_0c--;
    }

    //LAB_800ceb20
  }

  @Method(0x800ceb28L)
  public static void renderFullScreenOverlay(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager) {
    final FullScreenOverlayEffect0e a0 = (FullScreenOverlayEffect0e)manager.effect_44;

    GPU.queueCommand(30, new GpuCommandQuad()
      .translucent(Translucency.of(manager._10.flags_00 >>> 28 & 0b11))
      .rgb(a0.r_00 >> 8, a0.g_02 >> 8, a0.b_04 >> 8)
      .pos(-160, -120, 320, 280)
    );
  }

  /** Used at the end of Rose transform, lots during Albert transform */
  @ScriptDescription("Allocates a full-screen overlay effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "startR", description = "The starting red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "startG", description = "The starting green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "startB", description = "The starting blue channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "endR", description = "The ending red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "endG", description = "The ending green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "endB", description = "The ending blue channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks before the colour transition finishes")
  @Method(0x800cec8cL)
  public static FlowControl scriptAllocateFullScreenOverlay(final RunningScript<? extends BattleObject> script) {
    final int r = script.params_20[1].get() << 8;
    final int g = script.params_20[2].get() << 8;
    final int b = script.params_20[3].get() << 8;
    final int fullR = script.params_20[4].get() << 8 & 0xffff; // Retail bug in violet dragon - overflow
    final int fullG = script.params_20[5].get() << 8 & 0xffff; //
    final int fullB = script.params_20[6].get() << 8 & 0xffff; //
    final int ticks = script.params_20[7].get() & 0xffff;

    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state = allocateEffectManager(
      "Full screen overlay rgb(%x, %x, %x) -> rgb(%x, %x, %x)".formatted(r, g, b, fullR, fullG, fullB),
      script.scriptState_04,
      Bttl_800c::tickFullScreenOverlay,
      Bttl_800c::renderFullScreenOverlay,
      null,
      new FullScreenOverlayEffect0e()
    );

    final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager = state.innerStruct_00;
    manager._10.flags_00 = 0x5000_0000;

    final FullScreenOverlayEffect0e effect = (FullScreenOverlayEffect0e)manager.effect_44;
    effect.r_00 = r;
    effect.g_02 = g;
    effect.b_04 = b;
    effect.stepR_06 = (short)((fullR - r) / ticks);
    effect.stepG_08 = (short)((fullG - g) / ticks);
    effect.stepB_0a = (short)((fullB - b) / ticks);
    effect.ticksRemaining_0c = ticks;

    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Generates a random number using the Mersenne Twister algorithm")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The random number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "min", description = "The minimum value (inclusive)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "max", description = "The maximum value (exclusive)")
  @Method(0x800cee50L)
  public static FlowControl scriptRand(final RunningScript<?> script) {
    final int min = script.params_20[1].get();
    final int max = script.params_20[2].get();
    script.params_20[0].set(seed_800fa754.nextInt(min, max));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a weapon trail effect's segment count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The segment count")
  @Method(0x800ceeccL)
  public static FlowControl scriptSetWeaponTrailSegmentCount(final RunningScript<?> script) {
    setWeaponTrailSegmentCount(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  /** Used in Flameshot */
  @ScriptDescription("Renders a full-screen, coloured quad")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "translucency", description = "The translucency mode")
  @Method(0x800cef00L)
  public static FlowControl scriptRenderColouredQuad(final RunningScript<?> script) {
    GPU.queueCommand(30, new GpuCommandQuad()
      .translucent(Translucency.of(script.params_20[3].get() + 1))
      .rgb(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get())
      .pos(-160, -120, 320, 280)
    );

    return FlowControl.CONTINUE;
  }

  @Method(0x800cf03cL)
  public static int FUN_800cf03c(final EffectManagerData6c<?> manager, final Attachment18 attachment) {
    manager._10.trans_04.x += attachment._0c.x * attachment.direction_14;
    manager._10.trans_04.y += attachment._0c.y * attachment.direction_14;
    manager._10.trans_04.z += attachment._0c.z * attachment.direction_14;
    attachment.direction_14 = (byte)-attachment.direction_14;
    return 1;
  }

  @ScriptDescription("Adds an unknown, unused attachment to an effect. Seems to oscillate an effect's position back and forth by a certain amount.")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p4")
  @Method(0x800cf0b4L)
  public static FlowControl FUN_800cf0b4(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final Attachment18 attachment = manager.addAttachment(0, 0, Bttl_800c::FUN_800cf03c, new Attachment18());
    attachment.direction_14 = 1;

    if(script.params_20[4].get() != 0) {
      script.params_20[1].set(0);

      scriptGetPositionScalerAttachmentVelocity(script);

      final int p1 = script.params_20[1].get();
      attachment._0c.x = p1 * script.params_20[2].get() >> 16;
      attachment._0c.y = p1 * script.params_20[3].get() >> 16;
      attachment._0c.z = p1 * script.params_20[4].get() >> 16;
    } else {
      //LAB_800cf1e0
      attachment._0c.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    }

    //LAB_800cf1f8
    if(manager.hasAttachment(1)) {
      manager.removeAttachment(1);
    }

    //LAB_800cf218
    return FlowControl.CONTINUE;
  }

  /** @return Z */
  @Method(0x800cf244L)
  public static float transformWorldspaceToScreenspace(final Vector3f pos, final Vector2f out) {
    final Vector3f sp0x10 = new Vector3f();
    pos.mul(worldToScreenMatrix_800c3548, sp0x10);
    sp0x10.add(worldToScreenMatrix_800c3548.transfer);
    out.x = MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.x, sp0x10.z);
    out.y = MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.y, sp0x10.z);
    return sp0x10.z;
  }

  @Method(0x800cf37cL)
  public static void rotateAndTranslateEffect(final EffectManagerData6c<?> manager, @Nullable final Vector3f extraRotation, final Vector3f vertex, final Vector3f out) {
    final Vector3f rotations = new Vector3f(manager._10.rot_10);

    if(extraRotation != null) {
      //LAB_800cf3c4
      rotations.add(extraRotation);
    }

    //LAB_800cf400
    final MV transforms = new MV();
    transforms.rotationXYZ(rotations);

    vertex.mul(transforms, out);
    out.add(transforms.transfer);
  }

  @Method(0x800cf4f4L)
  public static void getRelativeOffset(final EffectManagerData6c<?> manager, @Nullable final Vector3f extraRotation, final Vector3f in, final Vector3f out) {
    final MV sp0x28 = new MV();

    final Vector3f sp0x20 = new Vector3f(manager.getRotation());

    if(extraRotation != null) {
      //LAB_800cf53c
      sp0x20.add(extraRotation);
    }

    //LAB_800cf578
    sp0x28.rotationXYZ(sp0x20);
    sp0x28.transfer.set(manager.getPosition());

    in.mul(sp0x28, out);
    out.add(sp0x28.transfer);
  }

  @Method(0x800cf684L)
  public static void FUN_800cf684(final Vector3f rotation, final Vector3f translation, final Vector3f vector, final Vector3f out) {
    final MV transforms = new MV();
    transforms.rotationXYZ(rotation);
    transforms.transfer.set(translation);
    vector.mul(transforms, out);
    out.add(transforms.transfer);
  }

  /** @return Z */
  @Method(0x800cf7d4L)
  public static float FUN_800cf7d4(final Vector3f rotation, final Vector3f translation1, final Vector3f translation2, final Vector2f out) {
    final Vector3f sp0x10 = new Vector3f(translation1);
    sp0x10.mul(worldToScreenMatrix_800c3548);

    GTE.setTransforms(worldToScreenMatrix_800c3548);

    final MV oldTransforms = new MV();
    GTE.getTransforms(oldTransforms);

    final MV sp0x58 = new MV();
    sp0x58.rotationXYZ(rotation);
    oldTransforms.mul(sp0x58);
    oldTransforms.transfer.add(sp0x10);

    GTE.setTransforms(oldTransforms);
    GTE.perspectiveTransform(translation2);

    out.set(GTE.getScreenX(2), GTE.getScreenY(2));
    return GTE.getScreenZ(3);
  }

  /** @return Z */
  @Method(0x800cfb14L)
  public static float FUN_800cfb14(final EffectManagerData6c<?> manager, final Vector3f translation, final Vector2f out) {
    return FUN_800cf7d4(manager._10.rot_10, manager._10.trans_04, translation, out);
  }

  /** @return Z */
  @Method(0x800cfb94L)
  public static float FUN_800cfb94(final EffectManagerData6c<?> manager, final Vector3f rotation, final Vector3f translation, final Vector2f out) {
    final Vector3f tempRotation = new Vector3f(manager._10.rot_10).add(rotation);
    return FUN_800cf7d4(tempRotation, manager._10.trans_04, translation, out);
  }

  /** @return Z */
  @Method(0x800cfc20L)
  public static float FUN_800cfc20(final Vector3f managerRotation, final Vector3f managerTranslation, final Vector3f translation, final Vector2f out) {
    return FUN_800cf7d4(managerRotation, managerTranslation, translation, out);
  }

  @ScriptDescription("Gets a battle object's local world matrix translation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800cfcccL)
  public static FlowControl scriptGetBobjLocalWorldMatrixTranslation(final RunningScript<?> script) {
    final BattleObject bobj = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);

    final Model124 model;
    if(BattleObject.EM__.equals(bobj.magic_00)) {
      model = ((ModelEffect13c)((EffectManagerData6c<?>)bobj).effect_44).model_10;
    } else {
      //LAB_800cfd34
      model = ((BattleEntity27c)bobj).model_148;
    }

    //LAB_800cfd40
    final MV lw = new MV();
    GsGetLw(model.modelParts_00[script.params_20[1].get()].coord2_04, lw);
    // This was multiplying vector (0, 0, 0) so I removed it
    script.params_20[2].set(Math.round(lw.transfer.x));
    script.params_20[3].set(Math.round(lw.transfer.y));
    script.params_20[4].set(Math.round(lw.transfer.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a bounding box dimension for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "axis", description = "The axis index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "size", description = "The size")
  @Method(0x800cfdf8L)
  public static FlowControl scriptGetBentDimension(final RunningScript<?> script) {
    final BattleEntity27c bent = SCRIPTS.getObject(script.params_20[0].get(), BattleEntity27c.class);
    final int componentIndex = script.params_20[1].get();

    //LAB_800cfe54
    float largest = -Float.MAX_VALUE;
    float smallest = Float.MAX_VALUE;
    for(int animIndex = bent.model_148.partCount_98 - 1; animIndex >= 0; animIndex--) {
      final float component = bent.model_148.modelParts_00[animIndex].coord2_04.coord.transfer.get(componentIndex);

      if(largest < component) {
        largest = component;
        //LAB_800cfe84
      } else if(smallest > component) {
        smallest = component;
      }

      //LAB_800cfe90
    }

    //LAB_800cfe9c
    script.params_20[2].set(Math.round(largest - smallest));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800cfec8L)
  public static FlowControl FUN_800cfec8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800cfed0L)
  public static void setMtSeed(final long seed) {
    seed_800fa754.setSeed(seed ^ 0x75b_d924L);
  }

  @ScriptDescription("Sets the Mersenne Twister random number seed")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "seed", description = "The seed")
  @Method(0x800cff24L)
  public static FlowControl scriptSetMtSeed(final RunningScript<?> script) {
    setMtSeed(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  /**
   * Holy crap this method was complicated... the way the stack is set up, all params after a3 are part of the sp0x90 array. Count is the number of parameters.
   * It's basically a variadic method so I'm changing the signature to that.
   * <p>
   * This method allows you to call a script function from the main game engine. Variadic params get passed in as the param array.
   */
  @Method(0x800cff54L)
  public static void callScriptFunction(final Consumer<RunningScript<?>> func, final int... params) {
    final RunningScript<Void> script = new RunningScript<>(null);

    //LAB_800cff90
    for(int i = 0; i < params.length; i++) {
      script.params_20[i] = new IntParam(params[i]);
    }

    //LAB_800cffbc
    func.accept(script);
  }

  /** Sets translation vector to position of individual part of model associated with scriptIndex */
  @Method(0x800cffd8L)
  public static void getModelObjectTranslation(final int scriptIndex, final Vector3f translation, final int objIndex) {
    final MV transformMatrix = new MV();
    GsGetLw(((BattleEntity27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00).model_148.modelParts_00[objIndex].coord2_04, transformMatrix);
    // Does nothing? Changed line below to set //ApplyMatrixLV(transformMatrix, new VECTOR(), translation);
    translation.set(transformMatrix.transfer);
  }
}
