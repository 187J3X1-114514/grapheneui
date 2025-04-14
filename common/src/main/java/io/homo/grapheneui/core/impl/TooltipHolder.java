package io.homo.grapheneui.core.impl;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class TooltipHolder {
    protected Supplier<Optional<String>> tooltipSupplier = Optional::empty;

    public static TooltipHolder none() {
        return new TooltipHolder() {
        };
    }

    public void setTooltipSupplier(Supplier<Optional<String>> supplier) {
        tooltipSupplier = supplier;
    }

    public Optional<String> getTooltip() {
        return tooltipSupplier.get();
    }

    public void setTooltip(String tooltip) {
        tooltipSupplier = () -> Optional.of(tooltip);
    }
}
