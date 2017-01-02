/*
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the FlatstoneTech Team ("FlatstoneTech"). By downloading or purchasing the software material, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 * FlatstoneTech SOFTWARE LICENSE
 * 1. Grant of License. FlatstoneTech grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 *  * Rent;
 *  * Sell;
 *  * Lease;
 *  * Offer on a pay-per-play basis;
 *  * Distribute for money or any other consideration; or
 *  * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 *  * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 *  4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by FlatstoneTech and is protected by United States copyright laws and international treaty provisions. FlatstoneTech shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted material. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 *  5. NO WARRANTIES. FLATSTONETECH DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. FlatstoneTech DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF FlatstoneTech ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF FlatstoneTech.
 *  Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against FlatstoneTech, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, FlatstoneTech SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF FLATSTONETECH OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package tech.flatstone.appliedlogistics.client.gui.misc;

import com.fireball1725.firelib.guimaker.GuiMakerGuiContainer;
import com.fireball1725.firelib.guimaker.objects.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.SaveInspectionHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.time.DurationFormatUtils;
import tech.flatstone.appliedlogistics.ModInfo;
import tech.flatstone.appliedlogistics.common.blocks.Blocks;
import tech.flatstone.appliedlogistics.common.network.PacketHandler;
import tech.flatstone.appliedlogistics.common.network.messages.PacketPatternStamperUpdateCheckBox;
import tech.flatstone.appliedlogistics.common.network.messages.PacketPatternStamperUpdateSelectedPlan;
import tech.flatstone.appliedlogistics.common.tileentities.misc.TileEntityPatternStamper;
import tech.flatstone.appliedlogistics.common.util.LanguageHelper;
import tech.flatstone.appliedlogistics.common.util.LogHelper;
import tech.flatstone.appliedlogistics.common.util.TileHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiPatternStamper extends GuiMakerGuiContainer {
    public static final ResourceLocation RESOURCE_BUILD_TIME = new ResourceLocation(ModInfo.MOD_ID, "textures/icons/stopwatch.png");
    public static final ResourceLocation RESOURCE_XP_COST = new ResourceLocation(ModInfo.MOD_ID, "textures/icons/xp_orb.png");
    public GuiLabel labelSlotDetails;
    public GuiButton buttonPrevious;
    public GuiButton buttonNext;
    public GuiButton buttonStamp;
    public GuiScrollBox scrollBoxOptions;
    public GuiScrollBox scrollBoxMaterials;
    public GuiCenteredLabel labelMachineName;
    public GuiDrawSimpleImage imageBuildTime;
    public GuiDrawSimpleImage imageXPCost;
    public GuiLabel labelCreativeMode;
    private TileEntityPatternStamper tileEntity;

    private List<GuiObject> guiPlanOptions = new ArrayList<>();

    public GuiPatternStamper(int id, EntityPlayer player, World world, BlockPos pos) {
        super(id, player, world, pos);

        this.xSize = 194;
        this.ySize = 232;
        
        tileEntity = TileHelper.getTileEntity(world, pos, TileEntityPatternStamper.class);

        labelSlotDetails = new GuiLabel(36, 7, 0xd5d5d5, "");
        buttonPrevious = new GuiButton(1, 4, 34, 16, "<");
        buttonNext = new GuiButton(2, 120, 34, 16, ">");
        buttonStamp = new GuiButton(3, 138, 34, 52, "Stamp");
        scrollBoxOptions = new GuiScrollBox(this, 4, 52, 186, 75);
        scrollBoxMaterials = new GuiScrollBox(this, 4, 24, 186, 103);
        labelMachineName = new GuiCenteredLabel(22, 38, 96, 0xd5d5d5, "");
        imageBuildTime = new GuiDrawSimpleImage(RESOURCE_BUILD_TIME, 6, 130);
        imageXPCost = new GuiDrawSimpleImage(RESOURCE_XP_COST, 90, 130);
        labelCreativeMode = new GuiLabel(150, 132, 0xffffff, 0.5f, "");
        imageXPCost.setScale(0.5f);
        imageBuildTime.setScale(0.5f);

        GuiTab tabMachine = new GuiTab(this, "Pattern Stamper", Blocks.BLOCK_PATTERN_STAMPER.getStack());
        GuiTab tabExport = new GuiTab(this, "Total Materials", new ItemStack(Items.BOOK));
        GuiTab tabAbout = new GuiTab(this, "About", 1);

        tabMachine.addGuiObject(new GuiSlot(Slot.class, 0, 4, 4, 28, 28));
        tabMachine.addGuiObject(labelSlotDetails);

        tabMachine.addGuiObject(new GuiWindow(34, 18, 156, 14)); // Progress Bar (for weight)...

        tabMachine.addGuiObject(buttonNext);
        tabMachine.addGuiObject(buttonPrevious);
        tabMachine.addGuiObject(buttonStamp);

        tabMachine.addGuiObject(labelMachineName);

        tabMachine.addGuiObject(new GuiLine(164, 14, 1, 18, 0xffa1a1a1));
        tabMachine.addGuiObject(new GuiLabel(158, 14, 0xa1a1a1, 0.5f, "Ok"));
        tabMachine.addGuiObject(new GuiLabel(166, 14, 0xa1a1a1, 0.5f, "Error"));

        tabMachine.addGuiObject(scrollBoxOptions);
        tabExport.addGuiObject(scrollBoxMaterials);

        // Bottom info bar
        tabMachine.addGuiObject(imageBuildTime);
        tabMachine.addGuiObject(imageXPCost);
        tabMachine.addGuiObject(labelCreativeMode);
        tabExport.addGuiObject(imageBuildTime);
        tabExport.addGuiObject(imageXPCost);
        tabExport.addGuiObject(labelCreativeMode);

        // Player's Inventory
        tabMachine.addGuiObject(new GuiLabel(8, 142, 0xd5d5d5, "Inventory"));
        tabMachine.addGuiObject(new GuiInventorySlots(4, 152));
        tabExport.addGuiObject(new GuiLabel(8, 142, 0xd5d5d5, "Inventory"));
        tabExport.addGuiObject(new GuiInventorySlots(4, 152));

        // Book and Quill Slot
        tabExport.addGuiObject(new GuiSlot(Slot.class, 1, 4, 4));

        tabExport.addGuiObject(new GuiButton(4, 140, 4, 50, 18, "Save"));

        tabExport.addGuiObject(new GuiLabel(24, 10, 0xffffff, "Export Materials List"));

        this.addGuiTab(tabMachine);
        this.addGuiTab(tabExport);
        this.addGuiTab(tabAbout);

        updateOptions();
    }

    @Override
    public void actionPerformed(int buttonID) {
        super.actionPerformed(buttonID);

        switch (buttonID) {
            case 1:
                PacketHandler.INSTANCE.sendToServer(new PacketPatternStamperUpdateSelectedPlan(tileEntity.getPos(), tileEntity.selectPrevPlan()));
                updateOptions();
                break;
            case 2:
                PacketHandler.INSTANCE.sendToServer(new PacketPatternStamperUpdateSelectedPlan(tileEntity.getPos(), tileEntity.selectNextPlan()));
                updateOptions();
                break;
            default:
                if (buttonID >= 100)
                    handleCheckBox(buttonID);

                break;
        }

    }

//    public void updatePartsList() {
//        LogHelper.info(">>> " + tileEntity.getPlanGuiObjectsList().size());
//        this.scrollBoxOptions.clearObjects();
//
//        for (GuiObject guiObject : tileEntity.getPlanGuiObjectsList())
//            this.scrollBoxOptions.addGuiObject(guiObject);
//
//        this.scrollBoxOptions.setMaxScrollY(tileEntity.getPlanGuiObjectsScrollYMax());
//        this.scrollBoxOptions.initGui();
//    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        buttonNext.setDisabled(true);
        buttonPrevious.setDisabled(true);
        buttonStamp.setDisabled(true);

        // Set TechLevel Label
        labelSlotDetails.setText(tileEntity.getPlanTechLevel() != null ?
                LanguageHelper.ITEM.translateMessage(String.format("plan_blank.%s.name", tileEntity.getPlanTechLevel().getName())) :
                LanguageHelper.MESSAGE.translateMessage("plan.insert")
        );

        if (tileEntity.isPlanValid()) {
            buttonNext.setDisabled(false);
            buttonPrevious.setDisabled(false);
            buttonStamp.setDisabled(false);
        }


//        imageBuildTime.setLabelText(String.format("Total Build Time: %s",
//                DurationFormatUtils.formatDuration(tileEntity.getRecipeTimeToBuild() * 1000, "HH:mm:ss")));
//        imageXPCost.setLabelText(String.format("Total XP Cost: %s%dL%s",
//                TextFormatting.GREEN, tileEntity.getRecipeXPRequired(), TextFormatting.RESET));
//
//        labelCreativeMode.setText("");
//        if (tileEntity.isCreativeMode())
//            labelCreativeMode.setText(String.format("%sCreative Mode%s", TextFormatting.DARK_PURPLE, TextFormatting.RESET));

        labelMachineName.setText(tileEntity.getSelectedMachine() != null ? tileEntity.getSelectedMachine().getName() : "");

        //
//        scrollBoxOptions.clearObjects();
//        for (GuiObject guiObject : tileEntity.getGuiPlanOptions())
//            scrollBoxOptions.addGuiObject(guiObject);

//        scrollBoxOptions.setMaxScrollY(tileEntity.getGuiPlanOptionY());

//        if (tileEntity.isPlanValid()) {
//            if (guiPlanOptions == null) {
//                this.guiPlanOptions = tileEntity.updateCheckBoxes();
//
//                scrollBoxOptions.clearObjects();
//                for (GuiObject guiObject : this.guiPlanOptions)
//                    scrollBoxOptions.addGuiObject(guiObject);
//
//                scrollBoxOptions.initGui();
//
//                if (this.guiPlanOptions.size() == 0)
//                    this.guiPlanOptions = null;
//            }
//        } else {
//            this.guiPlanOptions = null;
//            scrollBoxOptions.clearObjects();
//        }

    }

    public void updateOptions() {
        scrollBoxOptions.clearObjects();
        for (GuiObject guiObject : tileEntity.getGuiPlanOptions()) {
            scrollBoxOptions.addGuiObject(guiObject);
            LogHelper.info(">>> Adding: " + guiObject.toString());
        }
        scrollBoxOptions.setMaxScrollY(tileEntity.getGuiPlanOptionY());
        scrollBoxOptions.initGui();

        this.guiPlanOptions = tileEntity.getGuiPlanOptions();
    }

    @Override
    public void initGui() {
        super.initGui();

        tileEntity.initPlanItem(false);
        tileEntity.updateCheckBoxes();
        updateOptions();
    }

    public void handleCheckBox(int buttonID) {
        LogHelper.info(">>> Button ID: " + buttonID);

        GuiObject guiObject = guiPlanOptions.get(buttonID - 100);
        if (!(guiObject instanceof GuiCheckBox))
            return;

        GuiCheckBox guiCheckBox = (GuiCheckBox)guiObject;

        guiCheckBox.setSelected(!guiCheckBox.isSelected());

        PacketHandler.INSTANCE.sendToServer(new PacketPatternStamperUpdateCheckBox(tileEntity.getPos(), buttonID - 100, guiCheckBox.isSelected()));
    }
}
