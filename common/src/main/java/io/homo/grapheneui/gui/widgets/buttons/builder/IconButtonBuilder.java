package io.homo.grapheneui.gui.widgets.buttons.builder;

import io.homo.grapheneui.core.icon.Icon;
import io.homo.grapheneui.gui.widgets.buttons.IconButton;

import java.util.Optional;

public class IconButtonBuilder extends AbstractButtonBuilder<IconButton, IconButtonBuilder> {
    private Icon icon;
    private float iconScale = 0.65f;

    public IconButtonBuilder withIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public IconButtonBuilder withIconScale(float scale) {
        this.iconScale = scale;
        return this;
    }

    @Override
    protected IconButton createButton() {
        IconButton button = new IconButton(rectangle, () -> Optional.ofNullable(icon));
        button.setIconScale(iconScale);
        return button;
    }

    @Override
    protected void validate() {
        super.validate();
        if (icon == null) {
            throw new IllegalArgumentException("Icon must be specified");
        }
    }
}
