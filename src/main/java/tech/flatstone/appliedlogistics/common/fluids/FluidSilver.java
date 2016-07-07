package tech.flatstone.appliedlogistics.common.fluids;

import tech.flatstone.appliedlogistics.common.blocks.fluids.BlockFluidSilver;

public class FluidSilver extends FluidBase {
    public static FluidSilver INSTANCE;

    public FluidSilver() {
        super("silver", BlockFluidSilver.class, true);
        INSTANCE = this;
    }
}
