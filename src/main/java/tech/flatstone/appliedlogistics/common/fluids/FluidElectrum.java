package tech.flatstone.appliedlogistics.common.fluids;

import tech.flatstone.appliedlogistics.common.blocks.fluids.BlockFluidElectrum;

public class FluidElectrum extends FluidBase {
    public static FluidElectrum INSTANCE;

    public FluidElectrum() {
        super("electrum", BlockFluidElectrum.class, true);
        INSTANCE = this;
    }
}
