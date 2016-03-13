package tech.flatstone.appliedlogistics.common.blocks.builder;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tech.flatstone.appliedlogistics.AppliedLogistics;
import tech.flatstone.appliedlogistics.api.features.TechLevel;
import tech.flatstone.appliedlogistics.common.blocks.BlockBase;
import tech.flatstone.appliedlogistics.common.tileentities.builder.TileEntityBuilder;
import tech.flatstone.appliedlogistics.common.util.IProvideRecipe;

import java.util.List;

public class BlockBuilder extends BlockBase implements IProvideRecipe {
    public static final PropertyEnum TECHLEVEL = PropertyEnum.create("tech", TechLevel.class);

    public BlockBuilder() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TECHLEVEL, TechLevel.STONE_AGE));
        this.setTileEntity(TileEntityBuilder.class);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return true;

        playerIn.openGui(AppliedLogistics.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TECHLEVEL, TechLevel.byMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        TechLevel tier = (TechLevel) state.getValue(TECHLEVEL);
        return (tier.getMeta());
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{TECHLEVEL});
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < TechLevel.values().length; i++) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

    @Override
    public void RegisterRecipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 1, 0),
                "cwc",
                "wgw",
                "cxc",
                'c', "craftingTableWood",
                'w', "logWood",
                'g', "gearStone",
                'x', "chestWood"
        ));
    }
}