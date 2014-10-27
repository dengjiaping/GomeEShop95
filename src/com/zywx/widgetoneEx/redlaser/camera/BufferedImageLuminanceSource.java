package com.zywx.widgetoneEx.redlaser.camera;

import android.graphics.Bitmap;

import com.google.zxing.LuminanceSource;

public class BufferedImageLuminanceSource extends LuminanceSource {
    private final Bitmap image;
    private final int left;
    private final int top;
    private int[] rgbData;

    public BufferedImageLuminanceSource(Bitmap image) {
        this(image, 0, 0, image.getWidth(), image.getHeight());
    }

    public BufferedImageLuminanceSource(Bitmap image, int left, int top, int width, int height) {
        super(width, height);

        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();
        if (left + width > sourceWidth || top + height > sourceHeight) {
            throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
        }

        this.image = image;
        this.left = left;
        this.top = top;
    }

    // These methods use an integer calculation for luminance derived from:
    // <code>Y = 0.299R + 0.587G + 0.114B</code>
    @Override
    public byte[] getRow(int y, byte[] row) {
        if (y < 0 || y >= getHeight()) {
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        }
        int width = getWidth();
        if (row == null || row.length < width) {
            row = new byte[width];
        }

        if (rgbData == null || rgbData.length < width) {
            rgbData = new int[width];
        }

        image.getPixels(rgbData, 0, width, left, top + y, width, 1);

        for (int x = 0; x < width; x++) {

            int pixel = rgbData[x];
            int luminance = (306 * ((pixel >> 16) & 0xFF) + 601 * ((pixel >> 8) & 0xFF) + 117 * (pixel & 0xFF)) >> 10;
            row[x] = (byte) luminance;
        }
        return row;
    }

    @Override
    public byte[] getMatrix() {
        int width = getWidth();
        int height = getHeight();
        int area = width * height;
        byte[] matrix = new byte[area];

        int[] rgb = new int[area];
        image.getPixels(rgb, 0, width, left, top, width, height);
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                int pixel = rgb[offset + x];
                int luminance = (306 * ((pixel >> 16) & 0xFF) + 601 * ((pixel >> 8) & 0xFF) + 117 * (pixel & 0xFF)) >> 10;
                matrix[offset + x] = (byte) luminance;
            }
        }
        return matrix;
    }

    @Override
    public boolean isCropSupported() {
        return true;
    }

    @Override
    public LuminanceSource crop(int left, int top, int width, int height) {
        return new BufferedImageLuminanceSource(image, this.left + left, this.top + top, width, height);
    }

}