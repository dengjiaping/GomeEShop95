package com.gome.ecmall.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class LineTextView extends TextView {

    public LineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        FontMetrics fm = getPaint().getFontMetrics();
        float baseline = fm.descent - fm.ascent;
        float x = 0;
        float y = baseline; // 由于系统基于字体的底部来绘制文本，所有需要加上字体的高度。
        // 文本自动换行
        String[] texts = autoSplit(getText().toString(), getPaint(), getWidth() - getWidth() / 10);
        for (String text : texts) {
            if (!TextUtils.isEmpty(text)) {
                canvas.drawText(text, x, y, getPaint()); // 坐标以控件左上角为原点
                y += baseline + fm.leading; // 添加字体行间距
            }
        }

    }

    /**
     * 自动分割文本
     * 
     * @param content
     *            需要分割的文本
     * @param p
     *            画笔，用来根据字体测量文本的宽度
     * @param width
     *            最大的可显示像素（一般为控件的宽度）
     * @return 一个字符串数组，保存每行的文本
     */
    private String[] autoSplit(String content, Paint p, float width) {
        /** content的size */
        int length = content.length();
        /** content长度 */
        float textWidth = p.measureText(content);
        if (textWidth <= width) {
            return new String[] { content };
        }
        int start = 0, end = 1, i = 0;
        /** 行数 */
        int lines = (int) Math.ceil(textWidth / width); // 计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {
            if (p.measureText(content, start, end) > width) { // 文本宽度超出控件宽度时
                if (i == getLineNumber() - 1) {
                    end -= 1;
                    lineTexts[i++] = (String) content.subSequence(start, end) + "...";
                    start = end;
                    break;
                } else {
                    lineTexts[i++] = (String) content.subSequence(start, end);
                }
                start = end;
            }
            if (end == length) { // 不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }
        return lineTexts;
    }

    /**
     * 设置最大显示行数
     * 
     * @return
     */
    protected int getLineNumber() {
        return 3;
    }
}
