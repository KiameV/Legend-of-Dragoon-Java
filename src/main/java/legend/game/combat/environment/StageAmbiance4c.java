package legend.game.combat.environment;

import legend.core.IoHelper;
import legend.game.scripting.Param;
import org.joml.Vector3f;

import java.nio.ByteBuffer;

public class StageAmbiance4c {
  public final Vector3f ambientColour_00 = new Vector3f();
  public final Vector3f _06 = new Vector3f();
  public int _0c;
  public int _0e;
  public final BattleStruct14[] _10 = {new BattleStruct14(), new BattleStruct14(), new BattleStruct14()};

  public StageAmbiance4c set(final ByteBuffer buffer) {
    IoHelper.readColour(buffer, this.ambientColour_00);
    IoHelper.readColour(buffer, this._06);
    this._0c = IoHelper.readShort(buffer);
    this._0e = IoHelper.readShort(buffer);

    for(final BattleStruct14 struct : this._10) {
      IoHelper.readSvec3_12(buffer, struct.lightDirection_00);
      struct.x_06 = IoHelper.readShort(buffer) / (float)0x1000;
      struct.y_08 = IoHelper.readShort(buffer) / (float)0x1000;
      struct.lightColour_0a.r.set(IoHelper.readUByte(buffer));
      struct.lightColour_0a.g.set(IoHelper.readUByte(buffer));
      struct.lightColour_0a.b.set(IoHelper.readUByte(buffer));
      struct._0d.r.set(IoHelper.readUByte(buffer));
      struct._0d.g.set(IoHelper.readUByte(buffer));
      struct._0d.b.set(IoHelper.readUByte(buffer));
      struct.x_10 = IoHelper.readShort(buffer) / (float)0x1000;
      struct.y_12 = IoHelper.readShort(buffer) / (float)0x1000;
    }

    return this;
  }

  public StageAmbiance4c set(final Param param) {
    final int[] vals = new int[0x4c / 4];
    for(int i = 0; i < vals.length; i++) {
      vals[i] = param.array(i).get();
    }

    this.ambientColour_00.x = (vals[0] & 0xffff) / 4096.0f;
    this.ambientColour_00.y = (vals[0] >>> 16 & 0xffff) / 4096.0f;
    this.ambientColour_00.z = (vals[1] & 0xffff) / 4096.0f;
    this._06.x = (vals[1] >>> 16 & 0xffff) / 4096.0f;
    this._06.y = (vals[2] & 0xffff) / 4096.0f;
    this._06.z = (vals[2] >>> 16 & 0xffff) / 4096.0f;
    this._0c = (short)vals[3];
    this._0e = (short)(vals[3] >> 16);

    int paramIndex = 4;
    for(final BattleStruct14 struct : this._10) {
      struct.lightDirection_00.x = (short)vals[paramIndex] / (float)0x1000;
      struct.lightDirection_00.y = (short)(vals[paramIndex] >> 16) / (float)0x1000;
      struct.lightDirection_00.z = (short)vals[paramIndex + 1] / (float)0x1000;
      struct.x_06 = (short)(vals[paramIndex + 1] >> 16) / (float)0x1000;
      struct.y_08 = (short)vals[paramIndex + 2] / (float)0x1000;
      struct.lightColour_0a.r.set(vals[paramIndex + 2] >>> 16 & 0xff);
      struct.lightColour_0a.g.set(vals[paramIndex + 2] >>> 24 & 0xff);
      struct.lightColour_0a.b.set(vals[paramIndex + 3] & 0xff);
      struct._0d.r.set(vals[paramIndex + 3] >>  8 & 0xff);
      struct._0d.g.set(vals[paramIndex + 3] >> 16 & 0xff);
      struct._0d.b.set(vals[paramIndex + 3] >> 24 & 0xff);
      struct.x_10 = (short)vals[paramIndex + 4] / (float)0x1000;
      struct.y_12 = (short)(vals[paramIndex + 4] >> 16) / (float)0x1000;
      paramIndex += 5;
    }

    return this;
  }
}
