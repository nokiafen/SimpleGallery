package mrc.heli.dot.utils;

import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.media.image.common.ImageInfo;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

/**
 * RenderScript图片高斯模糊
 * Created by Tamsiree on 9/2/16.
 */
public class TBlurTool {
    /**
     * 建议模糊度(在0.0到25.0之间)
     */
    private static final float BLUR_RADIUS = 20f;
    private static final int SCALED_WIDTH = 100;
    private static final int SCALED_HEIGHT = 100;
    private static final int iterations = 3;


    public static void blur(Image imageView, PixelMap bitmap) {
        imageView.setPixelMap(getBlurBitmap(imageView.getContext(), bitmap, BLUR_RADIUS));
    }

    public static void blur(Image imageView, PixelMap bitmap, Float radius) {
        imageView.setPixelMap(getBlurBitmap(imageView.getContext(), bitmap, radius));
    }


    public static PixelMap getBlurBitmap(Context context, PixelMap bitmap) {
        return getBlurBitmap(context, bitmap, BLUR_RADIUS);
    }

    /**
     * 得到模糊后的bitmap
     *
     * @param context
     * @param bitmap
     * @param radius
     * @return
     */
    public static PixelMap getBlurBitmap(Context context, PixelMap bitmap, Float radius ) {
        ImageInfo imageInfo = bitmap.getImageInfo();
        if (imageInfo == null) {
            return null;
        }
        Size size = imageInfo.size;
        int width = size.width;
        int height = size.height;
        Rect region = new Rect(0, 0, width, height);
        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];
        bitmap.readPixels(inPixels, 0, width, region);
        for (int i = 0; i < iterations; i++) {
            setblur(inPixels, outPixels, width, height, radius);
            setblur(outPixels, inPixels, height, width, radius);
        }
        blurFractional(inPixels, outPixels, width, height, radius);
        blurFractional(outPixels, inPixels, height, width, radius);
        PixelMap.InitializationOptions options = new PixelMap.InitializationOptions();
        options.size = size;
        options.editable = true;
        PixelMap outImage = PixelMap.create(inPixels, options);
        return outImage;
    }

//    /**
//     * 快速模糊
//     *
//     * 先缩小原图，对小图进行模糊，再放大回原先尺寸
//     *
//     * @param src    源图片
//     * @param scale  缩小倍数(0...1)
//     * @param radius 模糊半径
//     * @return 模糊后的图片
//     */
//    public static PixelMap fastBlur(PixelMap src, Float scale, float radius, boolean recycle){
//        if (RxImageTool.isEmptyBitmap(src)) {
//            return null;
//        }
//        ImageInfo imageInfo = src.getImageInfo();
//        if (imageInfo == null) {
//            return null;
//        }
//
//        Size size = imageInfo.size;
//        int scaleWidth = (int) (size.width*scale);
//        int scaleHeight = (int) (size.height*scale);
//        ImagePacker packer=ImagePacker.create();
//        byte[] bytes=new byte[size.width*size.height];
//        packer.initializePacking(bytes,null);
//        packer.addImage(src);
//        packer.finalizePacking();
//        ImageSource imageSource=ImageSource.create(bytes,null);
//        ImageSource.DecodingOptions options=new ImageSource.DecodingOptions();
//        options.desiredSize=new Size(scaleWidth,scaleHeight);
//        options.editable=true;
//        PixelMap scaleImage=imageSource.createPixelmap(options);
//        Rect region = new Rect(0, 0, scaleWidth, scaleHeight);
//        int[] inPixels = new int[scaleWidth * scaleHeight];
//        int[] outPixels = new int[scaleWidth * scaleHeight];
//        scaleImage.readPixels(inPixels, 0, scaleWidth, region);
//        for (int i = 0; i < iterations; i++) {
//            setblur(inPixels, outPixels, scaleWidth, scaleHeight, radius);
//            setblur(outPixels, inPixels, scaleWidth, scaleHeight, radius);
//        }
//        blurFractional(inPixels, outPixels, scaleWidth, scaleHeight, radius);
//        blurFractional(outPixels, inPixels, scaleWidth, scaleHeight, radius);
//        PixelMap.InitializationOptions InitializationOptions = new PixelMap.InitializationOptions();
//        InitializationOptions.size = new Size(scaleWidth,scaleHeight);
//        options.editable = true;
//        PixelMap outImage = PixelMap.create(inPixels, InitializationOptions);
//        if (recycle){
//            src.release();
//        }
//        scaleImage.release();
//        return outImage;
//    }


    private static void setblur(int[] in, int[] out, int width, int height, float radius) {
        int widthMinus1 = width - 1;
        int r = (int) radius;
        int tableSize = 2 * r + 1;
        int[] divide = new int[256 * tableSize];

        for (int i = 0; i < 256 * tableSize; i++) {
            divide[i] = i / tableSize;
        }


        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for (int i = -r; i <= r; i++) {
                int rgb = in[inIndex + clamp(i, 0, width - 1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int x = 0; x < width; x++) {
                out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) | divide[tb];

                int i1 = x + r + 1;
                if (i1 > widthMinus1)
                    i1 = widthMinus1;
                int i2 = x - r;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }

    private static void blurFractional(int[] in, int[] out, int width, int height, float radius) {
        radius -= (int) radius;
        float f = 1.0f / (1 + 2 * radius);
        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;

            out[outIndex] = in[0];
            outIndex += height;
            for (int x = 1; x < width - 1; x++) {
                int i = inIndex + x;
                int rgb1 = in[i - 1];
                int rgb2 = in[i];
                int rgb3 = in[i + 1];

                int a1 = (rgb1 >> 24) & 0xff;
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;
                int a2 = (rgb2 >> 24) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;
                int a3 = (rgb3 >> 24) & 0xff;
                int r3 = (rgb3 >> 16) & 0xff;
                int g3 = (rgb3 >> 8) & 0xff;
                int b3 = rgb3 & 0xff;
                a1 = a2 + (int) ((a1 + a3) * radius);
                r1 = r2 + (int) ((r1 + r3) * radius);
                g1 = g2 + (int) ((g1 + g3) * radius);
                b1 = b2 + (int) ((b1 + b3) * radius);
                a1 *= f;
                r1 *= f;
                g1 *= f;
                b1 *= f;
                out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                outIndex += height;
            }
            out[outIndex] = in[width - 1];
            inIndex += width;
        }
    }

    private static int clamp(int x, int a, int b) {
        return (x < a) ? a : (x > b) ? b : x;
    }

}
