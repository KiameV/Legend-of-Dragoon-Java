package legend.game.types;

public class TextboxText84 {
  public static final int END = 0xa0;
  public static final int LINE = 0xa1;
  /** Waits until the X button is pressed */
  public static final int BUTTON = 0xa2;
  public static final int MUTLIBOX = 0xa3;
  public static final int SPEED = 0xa5;
  /** Pauses for N ticks */
  public static final int PAUSE = 0xa6;
  public static final int COLOUR = 0xa7;
  public static final int VAR = 0xa8;
  public static final int X_OFFSET = 0xad;
  public static final int Y_OFFSET = 0xae;
  public static final int SAUTO = 0xb0;
  public static final int ELEMENT = 0xb1;
  public static final int ARROW = 0xb2;

  public static final int SHOW_VAR = 0x10;
  public static final int HAS_NAME = 0x200;
  public static final int PROCESSED_NEW_LINE = 0x400;
  public static final int SELECTION = 0x800;
  public static final int SHOW_ARROW = 0x1000;

  public TextboxTextState state_00;
  /**
   * <ul>
   *   <li>4 - named textbox</li>
   * </ul>
   */
  public int type_04;

  /**
   * <ul>
   *   <li>0x1 - ?</li>
   *   <li>0x2 - ?</li>
   *   <li>0x4 - ?</li>
   *   <li>0x8 - ?</li>
   *   <li>0x10 - textbox is displaying a var</li>
   *   <li>0x20 - disables being able to advance the textbox, maybe part of automatic textboxes?</li>
   *   <li>0x40 - ?</li>
   *   <li>0x80 - ?</li>
   *   <li>0x100 - ?</li>
   *   <li>0x200 - textbox has a name (yellow text at the top)</li>
   *   <li>0x400 - processed a new line</li>
   *   <li>0x800 - textbox has selection</li>
   *   <li>0x1000 - show textbox arrow</li>
   * </ul>
   */
  public int flags_08;
  public int z_0c;

  public float x_14;
  public float y_16;
  public float _18;
  public float _1a;
  public int chars_1c;
  public int lines_1e;

  public LodString str_24;

  public int textColour_28;

  public int _2a;
  public int _2c;

  public int charIndex_30;

  public int charX_34;
  public int charY_36;

  public int _3a;
  public int _3c;
  public int _3e;
  public int _40;

  public int _44;
  public final int[] digits_46 = new int[10];
  public TextboxChar08[] chars_58;
  public TextboxTextState _5c;
  /** Which line of text is selected (i.e. inns) (unknown how this differs from {@link #selectionLine_68} */
  public int selectionLine_60;
  public int ticksUntilStateTransition_64;
  /** Which line of text is selected (i.e. inns) (unknown how this differs from {@link #selectionLine_60} */
  public int selectionLine_68;
  /** The selected line index, relative to the start of the list (i.e. if the list starts at line 1, and the selected line is 1, this value will be 0 */
  public int selectionIndex_6c;
  /** The absolute line index of the last selectable line */
  public int maxSelectionLine_70;
  /** The absolute line index of the first selectable line */
  public int minSelectionLine_72;

  /** The state to change to after {@link #ticksUntilStateTransition_64} countdown finishes */
  public TextboxTextState stateAfterTransition_78;
  public int element_7c;
  public int digitIndex_80;
}
