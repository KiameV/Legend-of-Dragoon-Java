package legend.game.submap;

import legend.game.unpacker.FileData;

public class SomethingStructSub0c_2 {
  public short x_00;
  public short z_02;
  public int _04;
  public int _08;

  public SomethingStructSub0c_2(final FileData data) {
    this.x_00 = data.readShort(0x00);
    this.z_02 = data.readShort(0x02);
    this._04 = data.readInt(0x04);
    this._08 = data.readInt(0x08);
  }
}
