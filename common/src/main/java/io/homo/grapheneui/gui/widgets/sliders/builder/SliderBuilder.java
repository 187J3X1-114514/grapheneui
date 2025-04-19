package io.homo.grapheneui.gui.widgets.sliders.builder;

import io.homo.grapheneui.gui.widgets.AbstractWidgetBuilder;
import io.homo.grapheneui.gui.widgets.sliders.Slider;
import io.homo.grapheneui.impl.Rectangle;

public class SliderBuilder extends AbstractWidgetBuilder<Slider, SliderBuilder> {
    private Rectangle rectangle;
    private float step = 1;
    private float max = 1;
    private float min = 1;
    private float value = 1;

    public SliderBuilder withRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
        return this;
    }

    public SliderBuilder withStep(float step) {
        this.step = step;
        return this;
    }

    public SliderBuilder withMax(float max) {
        this.max = max;
        return this;
    }

    public SliderBuilder withMin(float min) {
        this.min = min;
        return this;
    }

    public SliderBuilder withValue(float value) {
        this.value = value;
        return this;
    }

    public Slider build() {
        validate();
        Slider slider = new Slider(rectangle, step, max, min, value);
        configureWidget(slider);
        return slider;
    }

    private void validate() {
        if (rectangle == null) {
            throw new IllegalArgumentException("Rectangle must be specified");
        }
        if (min > max) {
            throw new IllegalArgumentException("Min value cannot be greater than Max value");
        }
        if (value < min || value > max) {
            throw new IllegalArgumentException("Value must be within the range of Min and Max");
        }
    }
}