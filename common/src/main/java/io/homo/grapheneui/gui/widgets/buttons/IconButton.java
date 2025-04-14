package io.homo.grapheneui.gui.widgets.buttons;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.core.icon.Icon;
import io.homo.grapheneui.core.renderer.IconRenderer;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.impl.Vec2;

import java.util.Optional;
import java.util.function.Supplier;

public class IconButton extends FlatButton<IconButton> {
    private final IconRenderer iconRenderer = new IconRenderer();
    private Supplier<Optional<Icon>> iconSupplier;
    private float iconScale = 0.65f;

    public IconButton(Rectangle rectangle, Supplier<Optional<Icon>> iconSupplier) {
        super(rectangle);
        this.iconSupplier = iconSupplier;
    }

    public void setIconSupplier(Supplier<Optional<Icon>> iconSupplier) {
        this.iconSupplier = iconSupplier;
    }


    public void setIcon(Icon icon) {
        this.iconSupplier = icon == null ? Optional::empty : () -> Optional.of(icon);
    }

    public void setIconScale(float iconScale) {
        this.iconScale = iconScale;
    }

    @Override
    public boolean isZoom() {
        return false;
    }

    @Override
    protected void renderText(float mouseX, float mouseY, float delta) {
        Optional<Icon> icon = iconSupplier.get();
        if (icon.isPresent()) {
            float iconSize = Math.min(rectangle.width, rectangle.height) * this.iconScale;
            float iconScale = iconSize / GrapheneUI.CONST.ICON_SIZE;
            nvg.save();
            nvg.transform(
                    Transform.identity()
                            .position(
                                    rectangle.x + rectangle.width / 2f,
                                    rectangle.y + rectangle.height / 2f
                            )
                            .scale(new Vec2(iconScale))
                            .pivot(new Vec2(
                                    GrapheneUI.CONST.ICON_SIZE * 0.5f,
                                    GrapheneUI.CONST.ICON_SIZE * 0.5f
                            ))
            );
            nvg.fillColor(GrapheneUI.THEME.SVG_NORMAL);
            iconRenderer.renderIcon(icon.get());
            nvg.restore();
        }
    }
}
