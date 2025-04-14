package io.homo.grapheneui.core.renderer;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.core.icon.FluentIconPack;
import io.homo.grapheneui.core.icon.Icon;
import io.homo.grapheneui.impl.Vec2;
import io.homo.grapheneui.nanovg.NanoVG;
import io.homo.grapheneui.nanovg.NanoVGRendererBase;

public class IconRenderer extends NanoVGRendererBase {
    public void renderIcon(Icon icon) {
        nvg.save();
        Vec2 textSize = NanoVG.RENDERER.TEXT.measureText(icon.codePoint);
        NanoVG.RENDERER.TEXT.drawText(
                FluentIconPack.ICON_FONT,
                GrapheneUI.CONST.ICON_SIZE,
                icon.codePoint,
                -textSize.x / 2,
                -textSize.y / 2,
                1000000,
                GrapheneUI.CONST.ICON_SIZE,
                nvg.fillColor().nvg()
        );
        nvg.restore();
    }
}
