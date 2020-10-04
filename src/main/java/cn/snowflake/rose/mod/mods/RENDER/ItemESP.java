package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;
import java.awt.*;

public class ItemESP extends Module {
    public ItemESP() {
        super("ItemESP",Category.RENDER);
    }
    public Value<Boolean> nametag = new Value<Boolean>("ItemESP_NameRender", false);


    @EventTarget
    public void onRender2D(EventRender2D event) {
        ScaledResolution scaledRes = new ScaledResolution(this.mc,this.mc.displayWidth,this.mc.displayHeight);
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        this.mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity instanceof EntityItem) {
                EntityItem ent = (EntityItem)entity;
                double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)event.getPartialTicks();
                double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)event.getPartialTicks();
                double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)event.getPartialTicks();
                AxisAlignedBB bb = ((EntityItem) entity).getBoundingBox().expand(0.1, 0.1, 0.1);
                Vector3d[] corners = new Vector3d[]{new Vector3d(posX + bb.minX - bb.maxX + (double)(ent.width / 2.0f), posY, posZ + bb.minZ - bb.maxZ + (double)(ent.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(ent.width / 2.0f), posY, posZ + bb.minZ - bb.maxZ + (double)(ent.width / 2.0f)), new Vector3d(posX + bb.minX - bb.maxX + (double)(ent.width / 2.0f), posY, posZ + bb.maxZ - bb.minZ - (double)(ent.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(ent.width / 2.0f), posY, posZ + bb.maxZ - bb.minZ - (double)(ent.width / 2.0f)), new Vector3d(posX + bb.minX - bb.maxX + (double)(ent.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + (double)(ent.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(ent.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + (double)(ent.width / 2.0f)), new Vector3d(posX + bb.minX - bb.maxX + (double)(ent.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - (double)(ent.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(ent.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - (double)(ent.width / 2.0f))};
                GLUProjection.Projection result = null;
                Vector4f transformed = new Vector4f((float)scaledRes.getScaledWidth() * 2.0f, (float)scaledRes.getScaledHeight() * 2.0f, -1.0f, -1.0f);
                for (Vector3d vec : corners) {
                    result = GLUProjection.getInstance().project(vec.x - RenderManager.instance.viewerPosX, vec.y - RenderManager.instance.viewerPosY, vec.z - RenderManager.instance.viewerPosZ, GLUProjection.ClampMode.NONE, true);
                    transformed.setX((float)Math.min((double)transformed.getX(), result.getX()));
                    transformed.setY((float)Math.min((double)transformed.getY(), result.getY()));
                    transformed.setW((float)Math.max((double)transformed.getW(), result.getX()));
                    transformed.setZ((float)Math.max((double)transformed.getZ(), result.getY()));
                }

                if (RenderUtil.isInViewFrustrum(ent)) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    float x = transformed.x * 2.0f;
                    float x2 = transformed.w * 2.0f;
                    float y = transformed.y * 2.0f;
                    float y2 = transformed.z * 2.0f;

                    RenderUtil.drawHollowBox(x, y, x2, y2, 3.0f, Color.BLACK.getRGB());
                    RenderUtil.drawHollowBox(x + 1.0f, y + 1.0f, x2 - 1.0f, y2 + 1.0f, 1.0f, new Color(255, 255, 255).getRGB());
                    if (ent.getEntityItem().getMaxDamage() > 0) {
                        double offset = y2 - y;
                        double percentoffset = offset / (double)ent.getEntityItem().getMaxDamage();
                        double finalnumber = percentoffset * (double)(ent.getEntityItem().getMaxDamage() - ent.getEntityItem().getItemDamage());
                        RenderUtil.drawRect(x - 4.0f, y, x - 1.0f, y2 + 3.0f, -16777216);
                        RenderUtil.drawRect((double)(x - 3.0f), (double)y2 - finalnumber + 1.0, (double)(x - 2.0f), (double)(y2 + 2.0f), Colors.RED.c);
                    }
                    if (this.nametag.getValueState().booleanValue()) {
                        String nametext = StringUtils.stripControlCodes(ent.getEntityItem().getItem().getItemStackDisplayName(ent.getEntityItem()));
                        this.mc.fontRenderer.drawStringWithShadow(nametext, (int)(x + (x2 - x) / 2.0f - (float)(this.mc.fontRenderer.getStringWidth(nametext) / 2)), (int)(y - (float)this.mc.fontRenderer.FONT_HEIGHT - 2.0f), -1);
                    }
                    GlStateManager.popMatrix();
                }
            }

        });
    }

}