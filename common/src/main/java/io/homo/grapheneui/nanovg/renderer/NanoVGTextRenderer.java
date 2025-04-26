package io.homo.grapheneui.nanovg.renderer;

import io.homo.grapheneui.impl.Vec2;
import io.homo.grapheneui.nanovg.NanoVGContext;
import io.homo.grapheneui.nanovg.NanoVGFont;
import io.homo.grapheneui.nanovg.NanoVGRendererBase;
import io.homo.grapheneui.utils.Color;
import org.lwjgl.nanovg.NanoVG;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.nanovg.NanoVG.*;

public class NanoVGTextRenderer extends NanoVGRendererBase {
    public static NanoVGTextRenderer INSTANCE;

    public NanoVGTextRenderer(NanoVGContext context) {
        INSTANCE = this;
    }

    private List<String> splitText(String text, float maxWidth) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty() || maxWidth <= 0) {
            return lines;
        }

        int lineStart = 0;
        int textLength = text.length();
        while (lineStart < textLength) {
            int lineEnd = findLineEnd(text, lineStart, maxWidth);
            lines.add(text.substring(lineStart, lineEnd));
            lineStart = lineEnd;
        }

        return lines;
    }

    private int findLineEnd(String text, int start, float maxWidth) {
        int end = start;
        float width = 0;
        char[] characters = text.toCharArray();

        while (end < characters.length) {
            width += measureTextWidth(String.valueOf(characters[end]));
            if (width > maxWidth) {
                break;
            }
            end++;
        }

        return end;
    }

    public float measureTextWidth(String text) {
        float[] bounds = new float[4];
        NanoVG.nvgTextBounds(contextPtr, 0, 0, text, bounds);
        return bounds[2] - bounds[0];
    }

    public float measureTextHeight(String text) {
        float[] bounds = new float[4];
        NanoVG.nvgTextBounds(contextPtr, 0, 0, text, bounds);
        return bounds[3] - bounds[1];
    }

    public Vec2 measureText(String text) {
        float[] bounds = new float[4];
        NanoVG.nvgTextBounds(contextPtr, 0, 0, text, bounds);
        return new Vec2(
                bounds[2] - bounds[0],
                bounds[3] - bounds[1]
        );
    }

    public TextMetrics drawText(NanoVGFont font, float fontSize, String text,
                                float startX, float startY, float maxWidth,
                                float lineHeight, Color color) {
        return drawAlignedText(
                font.name,
                fontSize,
                text,
                startX,
                startY,
                maxWidth,
                lineHeight,
                color,
                TextAlign.of(TextAlign.ALIGN_LEFT, TextAlign.ALIGN_TOP),
                false
        );
    }

    public TextMetrics drawWrappedText(NanoVGFont font, float fontSize, String text,
                                       float startX, float startY, float maxWidth,
                                       float lineHeight, Color color) {
        return drawAlignedText(
                font.name,
                fontSize,
                text,
                startX,
                startY,
                maxWidth,
                lineHeight,
                color,
                TextAlign.of(TextAlign.ALIGN_LEFT, TextAlign.ALIGN_TOP),
                true
        );
    }

    public TextMetrics drawWrappedAlignedText(NanoVGFont font, float fontSize, String text,
                                              float startX, float startY, float maxWidth,
                                              float lineHeight, Color color, TextAlign align) {
        return drawAlignedText(
                font.name,
                fontSize,
                text,
                startX,
                startY,
                maxWidth,
                lineHeight,
                color,
                align,
                true
        );
    }

    public TextMetrics drawAlignedText(
            NanoVGFont fontName,
            float fontSize,
            String text,
            float startX,
            float startY,
            float maxWidth,
            float lineHeight,
            Color color,
            TextAlign align,
            boolean wrap) {
        return drawAlignedText(
                fontName.name,
                fontSize,
                text,
                startX,
                startY,
                maxWidth,
                lineHeight,
                color,
                align,
                wrap
        );
    }

    public TextMetrics drawAlignedText(
            String fontName,
            float fontSize,
            String text,
            float startX,
            float startY,
            float maxWidth,
            float lineHeight,
            Color color,
            TextAlign align,
            boolean wrap) {
        color = color.copy().alpha((int) (nvg.globalAlpha() * color.alpha()));
        nvgSave(contextPtr);
        TextMetrics metrics = calculateTextMetrics(fontName, fontSize, text, maxWidth, lineHeight, wrap);
        nvgTextAlign(contextPtr, align.horizontal() | align.vertical());
        nvgFontSize(contextPtr, fontSize);
        nvgFontFace(contextPtr, fontName);
        nvgFillColor(contextPtr, color.nvg());
        float yPos = startY;
        for (String line : metrics.lines) {
            nvgText(contextPtr, (float) Math.ceil(startX), (float) Math.ceil(yPos), line);
            yPos += lineHeight;
        }
        nvgRestore(contextPtr);

        return metrics;
    }

    private float calculateHorizontalPosition(String line, float maxWidth, TextAlign align) {
        float lineWidth = measureTextWidth(line);
        return switch (align.horizontal()) {
            case TextAlign.ALIGN_CENTER -> (maxWidth - lineWidth) / 2;
            case TextAlign.ALIGN_RIGHT -> maxWidth - lineWidth;
            default -> 0;
        };
    }

    public TextMetrics calculateTextMetrics(NanoVGFont fontName, float fontSize,
                                            String text, float maxWidth,
                                            float lineHeight, boolean wrap) {
        return calculateTextMetrics(
                fontName.name,
                fontSize,
                text,
                maxWidth,
                lineHeight,
                wrap
        );
    }

    public TextMetrics calculateTextMetrics(String fontName, float fontSize,
                                            String text, float maxWidth,
                                            float lineHeight, boolean wrap) {
        nvgSave(contextPtr);
        nvgFontSize(contextPtr, fontSize);
        nvgFontFace(contextPtr, fontName);

        List<String> lines = wrap ? splitText(text, maxWidth) : List.of(text.split("\n"));
        float maxLineWidth = 0;
        for (String line : lines) {
            float width = measureTextWidth(line);
            if (width > maxLineWidth) maxLineWidth = width;
        }
        nvgRestore(contextPtr);
        return new TextMetrics(lines, lineHeight, maxLineWidth);
    }

    private float calculateVerticalOffset(int align, TextMetrics metrics) {
        return switch (align) {
            case TextAlign.ALIGN_BOTTOM -> -metrics.totalHeight;
            case TextAlign.ALIGN_TOP -> 0;
            case TextAlign.ALIGN_MIDDLE -> -metrics.totalHeight * 0.5f;
            default -> 0;
        };
    }

    public static class TextMetrics {
        public final List<String> lines;
        public final float totalHeight;
        public final float maxLineWidth;

        public TextMetrics(List<String> lines, float lineHeight, float maxLineWidth) {
            this.lines = lines;
            this.totalHeight = Math.max(lines.size() * lineHeight - 4, 0);
            this.maxLineWidth = maxLineWidth;
        }
    }
}