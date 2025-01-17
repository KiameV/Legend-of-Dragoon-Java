package legend.game.submap;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class ChapterStruct08 implements MemoryRef {
  private final Value ref;

  public final IntRef _00;
  public final IntRef _04;

  public ChapterStruct08(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
