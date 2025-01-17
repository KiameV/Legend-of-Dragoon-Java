package legend.core.gpu;

public class GpuCommandCopyDrawBufferToVram extends GpuCommand {
  private final int sourceX;
  private final int sourceY;
  private final int destX;
  private final int destY;
  private final int width;
  private final int height;

  public GpuCommandCopyDrawBufferToVram(final int sourceX, final int sourceY, final int destX, final int destY, final int width, final int height) {
    if(sourceX < 0) {
      throw new IllegalArgumentException("Negative sourceX " + sourceX);
    }

    if(sourceY < 0) {
      throw new IllegalArgumentException("Negative sourceY " + sourceY);
    }

    if(destX < 0) {
      throw new IllegalArgumentException("Negative destX " + destX);
    }

    if(destY < 0) {
      throw new IllegalArgumentException("Negative destY " + destY);
    }

    this.sourceX = sourceX;
    this.sourceY = sourceY;
    this.destX = destX;
    this.destY = destY;
    this.width = width;
    this.height = height;
  }

  @Override
  public void render(final Gpu gpu) {
    final int scale = gpu.getScale();

    final int[] data = new int[this.width * scale * this.height * scale];
    gpu.getDrawBuffer().getRegion(new Rect4i(this.sourceX * scale, this.sourceY * scale, this.width * scale, this.height * scale), data);

    final int[] unscaledData = new int[this.width * this.height];
    for(int y = 0; y < this.height; y++) {
      for(int x = 0; x < this.width; x++) {
        unscaledData[y * this.width + x] = data[y * scale * this.width * scale + x * scale];
      }
    }

    gpu.uploadData(new Rect4i(this.destX, this.destY, this.width, this.height), unscaledData);
  }
}
