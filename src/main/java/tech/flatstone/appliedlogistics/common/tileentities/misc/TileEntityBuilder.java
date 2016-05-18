/*
 *
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the FlatstoneTech Team ("FlatstoneTech"). By downloading or purchasing the software material, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 * FlatstoneTech SOFTWARE LICENSE
 * 1. Grant of License. FlatstoneTech grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 * Rent;
 * Sell;
 * Lease;
 * Offer on a pay-per-play basis;
 * Distribute for money or any other consideration; or
 * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 * 4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by FlatstoneTech and is protected by United States copyright laws and international treaty provisions. FlatstoneTech shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted material. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 * 5. NO WARRANTIES. FLATSTONETECH DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. FlatstoneTech DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF FlatstoneTech ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF FlatstoneTech.
 * Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against FlatstoneTech, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, FlatstoneTech SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF FLATSTONETECH OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package tech.flatstone.appliedlogistics.common.tileentities.misc;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import org.apache.commons.lang3.time.DurationFormatUtils;
import tech.flatstone.appliedlogistics.api.features.IMachinePlan;
import tech.flatstone.appliedlogistics.api.features.TechLevel;
import tech.flatstone.appliedlogistics.api.features.plan.PlanSlot;
import tech.flatstone.appliedlogistics.api.features.plan.SlotTechLevelProperties;
import tech.flatstone.appliedlogistics.api.registries.PlanRegistry;
import tech.flatstone.appliedlogistics.common.blocks.misc.BlockCrank;
import tech.flatstone.appliedlogistics.common.integrations.waila.IWailaBodyMessage;
import tech.flatstone.appliedlogistics.common.items.ItemPlanBase;
import tech.flatstone.appliedlogistics.common.tileentities.TileEntityMachineBase;
import tech.flatstone.appliedlogistics.common.tileentities.inventory.InternalInventory;
import tech.flatstone.appliedlogistics.common.tileentities.inventory.InventoryOperation;
import tech.flatstone.appliedlogistics.common.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TileEntityBuilder extends TileEntityMachineBase implements ITickable, INetworkButton, IWailaBodyMessage, ICrankable {
    private InternalInventory internalInventory = new InternalInventory(this, 56);
    private ItemStack planItem = null;
    private int currentTechLevel = -1;
    private int ticksRemaining = 0;
    private boolean machineWorking = false;
    private int ticksTotal = 0;
    private ItemStack outputItem = null;

    private List<BuilderSlotDetails> builderSlotDetailsList = new ArrayList<>();

    public List<BuilderSlotDetails> getBuilderSlotDetailsList() {
        return builderSlotDetailsList;
    }

    public boolean isMachineWorking() {
        return machineWorking;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }

    public int getCurrentTechLevel() {
        return currentTechLevel;
    }

    public int getTicksTotal() {
        return ticksTotal;
    }

    public ItemStack getPlanItem() {
        if (getPlanBase() == null)
            return null;

        return this.planItem;
    }

    public ItemPlanBase getPlanBase() {
        if (this.planItem == null)
            return null;

        if (!(this.planItem.hasTagCompound()))
            return null;

        if (!(this.planItem.getTagCompound().hasKey("PlanType")))
            return null;

        // Setup the Slot List
        String planName = this.planItem.getTagCompound().getString("PlanType");
        ItemPlanBase planBase = (ItemPlanBase) PlanRegistry.getPlanAsItem(planName);

        if (planBase == null)
            return null;

        if (!(planBase instanceof IMachinePlan))
            return null;

        return planBase;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        currentTechLevel = nbtTagCompound.getInteger("currentTechLevel");
        ticksRemaining = nbtTagCompound.getInteger("ticksRemaining");
        machineWorking = nbtTagCompound.getBoolean("machineWorking");
        ticksTotal = nbtTagCompound.getInteger("ticksTotal");

        if (nbtTagCompound.hasKey("outputItem")) {
            NBTTagCompound outputItemStack = nbtTagCompound.getCompoundTag("outputItem");
            outputItem = ItemStack.loadItemStackFromNBT(outputItemStack);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setInteger("currentTechLevel", currentTechLevel);
        nbtTagCompound.setInteger("ticksRemaining", ticksRemaining);
        nbtTagCompound.setBoolean("machineWorking", machineWorking);
        nbtTagCompound.setInteger("ticksTotal", ticksTotal);

        if (outputItem != null) {
            NBTTagCompound outputItemStack = new NBTTagCompound();
            outputItem.writeToNBT(outputItemStack);
            nbtTagCompound.setTag("outputItem", outputItemStack);
        }
    }

    @Override
    public boolean canBeRotated() {
        return true;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public List<String> getWailaBodyToolTip(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        List<String> newTooltip = currentTip;

        if (getPlanBase() == null || planItem == null)
            return newTooltip;

        newTooltip.add(String.format("%s: %s",
                LanguageHelper.LABEL.translateMessage("plan"),
                LanguageHelper.NONE.translateMessage(planItem.getUnlocalizedName() + ".name")
        ));

        if (ticksRemaining == 0)
            return newTooltip;

        float timePercent = (((float) this.ticksTotal - (float) this.ticksRemaining) / (float) this.ticksTotal) * 100;

        int secondsLeft = (ticksRemaining / 20) * 1000;

        newTooltip.add(String.format("%s: %s (%d%%)",
                LanguageHelper.LABEL.translateMessage("time_left"),
                DurationFormatUtils.formatDuration(secondsLeft, "mm:ss"),
                Math.round(timePercent)
        ));

        return newTooltip;
    }

    @Override
    public IInventory getInternalInventory() {
        return internalInventory;
    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InventoryOperation operation, ItemStack removed, ItemStack added) {
        if (operation == InventoryOperation.markDirty)
            return;

        if (this.worldObj == null)
            return;

        if (slot == 0) { // Change in the input slot...
            currentTechLevel = this.getBlockMetadata();
            this.markDirty();
            this.markForUpdate();

            this.worldObj.addBlockEvent(this.pos, this.blockType, EnumEventTypes.PLAN_SLOT_UPDATE.ordinal(), 0);

            TileHelper.DropItems(this, 1, 27);
        }
    }

    @Override
    public int[] getAccessibleSlotsBySide(EnumFacing side) {
        return new int[0];
    }

    @Override
    public void doCrank() {
        if (ticksRemaining > 0) {
            ticksRemaining = ticksRemaining - 20;

            this.markDirty();
            this.markForUpdate();

            if (ticksRemaining <= 0) {
                finishBuilding();
            }
        }
    }

    @Override
    public boolean canAttachCrank() {
        return getBlockMetadata() < 2;
    }

    @Override
    public boolean canCrank() {
        if (ticksRemaining > 0)
            return true;

        return false;
    }

    @Override
    public void actionPerformed(int buttonID, UUID playerUUID) {
        switch (buttonID) {
            case 0:
                initBuild();
                break;
            case 1:
                if (currentTechLevel == -1)
                    return;

                currentTechLevel++;
                if (currentTechLevel > this.getBlockMetadata())
                    currentTechLevel = 0;

                this.markDirty();
                this.markForUpdate();

                this.worldObj.addBlockEvent(this.pos, this.blockType, EnumEventTypes.PLAN_SLOT_UPDATE.ordinal(), 0);
                break;
            default:
                break;
        }
    }

    private void initPlanSlotChange() {
        this.planItem = this.internalInventory.getStackInSlot(0);
        builderSlotDetailsList.clear();

        if (this.planItem == null) {
            currentTechLevel = -1;

            this.markDirty();
            this.markForUpdate();

            return;
        }

        ItemPlanBase planBase = getPlanBase();

        if (planBase == null)
            return;

        List<PlanSlot> planSlots = ((IMachinePlan) planBase).getPlanSlots();

        for (PlanSlot slot : planSlots) {
            int minCount = -1;
            int maxCount = -1;
            SlotTechLevelProperties techLevelProperties = slot.getSlotProperties().get(TechLevel.byMeta(currentTechLevel));
            if (techLevelProperties != null) {
                minCount = techLevelProperties.getItemMinCount();
                maxCount = techLevelProperties.getItemMaxCount();
            }

            builderSlotDetailsList.add(new BuilderSlotDetails(slot.getSlotMaterial(), slot.getSlotDescription(), minCount, maxCount, slot.getSlotMaterialWeight(), slot.getSlotMaterialTimeToAdd()));
        }

        this.markDirty();
        this.markForUpdate();
    }

    public String getBuilderDetails() {
        //todo: replace "" with unlocalized stuff

        ItemPlanBase planBase = getPlanBase();
        if (planBase == null)
            return "";

        List<ItemStack> inventory = new ArrayList<>();
        for (int i = 1; i < 27; i++) {
            inventory.add(getInternalInventory().getStackInSlot(i));
        }

        return ((IMachinePlan) planBase).getPlanDetails(TechLevel.byMeta(this.currentTechLevel), inventory);
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        switch (EnumEventTypes.values()[id]) {
            case PLAN_SLOT_UPDATE:
                initPlanSlotChange();
                break;
            default:
                break;
        }

        return true;
    }

    public boolean isMeetingBuildRequirements() {
        if (getPlanBase() == null)
            return false;

        if (this.internalInventory.getStackInSlot(28) != null)
            return false;

        if (ticksRemaining > 0)
            return false;

        if (builderSlotDetailsList.size() == 0)
            return false;

        if (getBlockMetadata() == TechLevel.CREATIVE.getMeta())
            return true;

        int slotID = 0;
        for (BuilderSlotDetails slotDetails : builderSlotDetailsList) {
            slotID++;

            if (slotDetails.getSlotMaterialMaxCount() == -1)
                continue;

            ItemStack itemInSlot = this.internalInventory.getStackInSlot(slotID);
            if (itemInSlot == null && slotDetails.getSlotMaterialMinCount() > 0)
                return false;


            if (itemInSlot != null && itemInSlot.stackSize < slotDetails.getSlotMaterialMinCount())
                return false;
        }

        if (getTotalWeight() > ((IMachinePlan) getPlanBase()).getPlanMaxWeight(TechLevel.byMeta(currentTechLevel)))
            return false;

        return true;
    }

    public int getTotalWeight() {
        int slotID = 0;
        int totalWeight = 0;
        for (BuilderSlotDetails slotDetails : builderSlotDetailsList) {
            slotID++;

            if (slotDetails.getSlotMaterialMaxCount() == -1)
                continue;

            ItemStack itemInSlot = this.internalInventory.getStackInSlot(slotID);

            if (itemInSlot == null)
                continue;

            totalWeight += (slotDetails.getSlotMaterialWeight() * itemInSlot.stackSize);
        }

        return totalWeight;
    }

    public int getTotalTicks() {
        int slotID = 0;
        int totalTicks = 0;
        for (BuilderSlotDetails slotDetails : builderSlotDetailsList) {
            slotID++;

            if (slotDetails.getSlotMaterialMaxCount() == -1)
                continue;

            ItemStack itemInSlot = this.internalInventory.getStackInSlot(slotID);

            if (itemInSlot == null)
                continue;

            totalTicks += (slotDetails.getSlotMaterialTimeToAdd() * itemInSlot.stackSize);
        }

        return totalTicks;
    }

    public void initBuild() {
        int totalTicks = getTotalTicks();

        this.ticksRemaining = totalTicks;
        this.ticksTotal = totalTicks;

        int slotID = 0;
        for (BuilderSlotDetails slotDetails : builderSlotDetailsList) {
            slotID++;

            if (slotDetails.getSlotMaterialMaxCount() == -1)
                continue;

            ItemStack itemInSlot = this.internalInventory.getStackInSlot(slotID);

            this.internalInventory.setInventorySlotContents(slotID + 28, itemInSlot);
            this.internalInventory.setInventorySlotContents(slotID, null);
        }

        this.outputItem = ((IMachinePlan) (getPlanBase())).getPlanItem(TechLevel.byMeta(currentTechLevel));

        NBTTagCompound tagMachineItems = new NBTTagCompound();
        NBTTagCompound tagCompound = new NBTTagCompound();
        for (int i = 29; i < 56; i++) {
            NBTTagCompound item = new NBTTagCompound();
            ItemStack itemStack = this.getStackInSlot(i);
            if (itemStack != null) {
                itemStack.writeToNBT(item);
                tagCompound.setTag("item_" + (i - 29), item);
            }
        }
        tagMachineItems.setTag("MachineItemData", tagCompound);

        String planName = this.planItem.getTagCompound().getString("PlanType");
        tagMachineItems.setString("PlanType", planName);

        this.outputItem.setTagCompound(tagMachineItems);

        this.markDirty();
        this.markForUpdate();
    }

    private void finishBuilding() {
        this.internalInventory.setInventorySlotContents(28, this.outputItem);
        this.outputItem = null;
    }
}