package tech.flatstone.appliedlogistics.common.items.ore;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import tech.flatstone.appliedlogistics.ModInfo;
import tech.flatstone.appliedlogistics.common.items.ItemBase;
import tech.flatstone.appliedlogistics.common.util.EnumOres;
import tech.flatstone.appliedlogistics.common.util.IItemRenderer;

import java.util.List;

public class ItemOreIngot extends ItemBase implements IItemRenderer {
    public ItemOreIngot() {
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i = 0; i < EnumOres.values().length; i++) {
            if (!EnumOres.byMeta(i).isVanillaGen()) {
                subItems.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String name = super.getUnlocalizedName();
        String oreName = EnumOres.byMeta(stack.getItemDamage()).getUnlocalisedName();
        return name + "." + oreName;
    }

    @Override
    public void registerItemRenderer() {
        for (int i = 0; i < EnumOres.values().length; i++) {
            if (!EnumOres.byMeta(i).isVanillaGen()) {
                ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(ModInfo.MOD_ID + ":" + "ingot_" + EnumOres.byMeta(i).getUnlocalisedName(), "inventory"));
            } else {
                ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(EnumOres.byMeta(i).getUnlocalisedName() + "_ingot", "inventory"));
            }
        }
    }
}
