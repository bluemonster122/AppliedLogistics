package tech.flatstone.appliedlogistics.common.fluids;

import tech.flatstone.appliedlogistics.common.blocks.fluids.BlockFluidSteel;

public class FluidSteel extends FluidBase {
    public static FluidSteel INSTANCE;

    public FluidSteel() {
        super("steel", BlockFluidSteel.class, true);
        INSTANCE = this;
        this.getFluid().setLuminosity(15);
        this.getFluid().setViscosity(5000);
    }
}