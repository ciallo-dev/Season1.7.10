package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;

public class VClipUP extends Module {

    public static Value<Double> block = new Value<Double>("VClipUP_Block", 4.5D, 1.0D, 6.0D,0.1D);


    public VClipUP() {
        super("VClipUP", "VClipUP Down", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + block.getValueState(), mc.thePlayer.posZ);
        set(false);
        super.onEnable();
    }
}