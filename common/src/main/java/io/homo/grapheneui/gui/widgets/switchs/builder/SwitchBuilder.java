package io.homo.grapheneui.gui.widgets.switchs.builder;

import io.homo.grapheneui.gui.widgets.AbstractWidgetBuilder;
import io.homo.grapheneui.gui.widgets.switchs.Switch;
import io.homo.grapheneui.impl.Rectangle;

public class SwitchBuilder extends AbstractWidgetBuilder<Switch, SwitchBuilder> {
    private Rectangle rectangle;
    private boolean checked = false;
    private boolean zoom = true;

    public SwitchBuilder withRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
        return this;
    }

    public SwitchBuilder withChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public SwitchBuilder withZoom(boolean zoom) {
        this.zoom = zoom;
        return this;
    }

    public Switch build() {
        validate();
        Switch switchWidget = new Switch(rectangle);
        switchWidget.setChecked(checked);
        switchWidget.setZoom(zoom);
        configureWidget(switchWidget);
        return switchWidget;
    }

    private void validate() {
        if (rectangle == null) {
            throw new IllegalArgumentException("Rectangle must be specified");
        }
    }
}