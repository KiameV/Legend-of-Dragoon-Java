package legend.game.combat.environment;

import legend.core.gte.ModelPart10;
import legend.game.types.McqHeader;

import java.util.Arrays;

/** 0x18cb0 bytes */
public class BattlePreloadedEntities_18cb0 {
  public EncounterData38 encounterData_00;
  /** 3 slots for chars, 3 slots for dragoons */
  public final AdditionHits100[] additionHits_38 = new AdditionHits100[0x100];
  /** This reference is only valid while it's loading */
//  public MrgFile stageMrg_638;
//  public MrgFile stageTmdMrg_63c;

  public final BattleStage stage_963c = new BattleStage();
  public McqHeader stageMcq_9cb0;

  public final Rendering1298[] _9ce8 = new Rendering1298[3];

  public BattlePreloadedEntities_18cb0() {
    Arrays.setAll(this.additionHits_38, i -> new AdditionHits100());
    Arrays.setAll(this._9ce8, i -> new Rendering1298());
  }

  public static class AdditionHits100 {
    public final AdditionHitProperties20[] hits_00 = new AdditionHitProperties20[8];

    public AdditionHits100() {
      Arrays.setAll(this.hits_00, i -> new AdditionHitProperties20());
    }
  }

  public static class AdditionHitProperties20 {
    public int length;

    /** All hits except first hit of final addition (0xe0) seem to be 0xc0 */
    public short flags_00;
    public short totalFrames_02;
    public short overlayHitFrameOffset_04;
    public short totalSuccessFrames_06;
    public short damageMultiplier_08;
    public short spValue_0a;
    public short audioFile_0c;
    public short isFinalHit_0e;
    public short _10; // related to camera or voice? index into array?
    public short _12; // related to camera or voice? index into array?
    public short _14; // related to camera? index into array?
    public short hitDistanceFromTarget_16;
    public short framesToHitPosition_18;
    public short _1a; // always 32 (except for a few for Haschel), could be length of properties array
    public short framesPostFailure_1c;
    public short overlayStartingFrameOffset_1e;

    public AdditionHitProperties20() {
      this.length = 16;
    }

    public short get(final int propertyIndex) {
      return switch(propertyIndex) {
        case 0 -> this.flags_00;
        case 1 -> this.totalFrames_02;
        case 2 -> this.overlayHitFrameOffset_04;
        case 3 -> this.totalSuccessFrames_06;
        case 4 -> this.damageMultiplier_08;
        case 5 -> this.spValue_0a;
        case 6 -> this.audioFile_0c;
        case 7 -> this.isFinalHit_0e;
        case 8 -> this._10;
        case 9 -> this._12;
        case 10 -> this._14;
        case 11 -> this.hitDistanceFromTarget_16;
        case 12 -> this.framesToHitPosition_18;
        case 13 -> this._1a;
        case 14 -> this.framesPostFailure_1c;
        case 15 -> this.overlayStartingFrameOffset_1e;
        default -> throw new IllegalArgumentException(propertyIndex + " is an invalid addition hit property index.");
      };
    }

    public void set(final int propertyIndex, final short value) {
      switch(propertyIndex) {
        case 0 -> this.flags_00 = value;
        case 1 -> this.totalFrames_02 = value;
        case 2 -> this.overlayHitFrameOffset_04 = value;
        case 3 -> this.totalSuccessFrames_06 = value;
        case 4 -> this.damageMultiplier_08 = value;
        case 5 -> this.spValue_0a = value;
        case 6 -> this.audioFile_0c = value;
        case 7 -> this.isFinalHit_0e = value;
        case 8 -> this._10 = value;
        case 9 -> this._12 = value;
        case 10 -> this._14 = value;
        case 11 -> this.hitDistanceFromTarget_16 = value;
        case 12 -> this.framesToHitPosition_18 = value;
        case 13 -> this._1a = value;
        case 14 -> this.framesPostFailure_1c = value;
        case 15 -> this.overlayStartingFrameOffset_1e = value;
        default -> throw new IllegalArgumentException(propertyIndex + " is an invalid addition hit property index.");
      }
    }
  }

  public static class Rendering1298 {
    public ModelPart10[] dobj2s_00;
//    public final GsCOORDINATE2[] coord2s_230 = new GsCOORDINATE2[35]; // Use coord2 on dobj2
//    public final Transforms[] params_d20 = new Transforms[35]; // Use dobj2.coord2.transforms
  }
}
