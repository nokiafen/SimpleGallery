package mrc.heli.dot.utils;

import ohos.media.image.PixelMap;

public interface Transformation {

    PixelMap transform(PixelMap pixelMap,float zoomValue);
}
