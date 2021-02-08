package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import com.darkmagician6.eventapi.EventManager;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.EnumMap;

public class TransformFMLEventChannel implements Opcodes {
    public static void transformFMLEventChannel(ClassNode classNode, MethodNode methodNode) {
        if (methodNode.name.equalsIgnoreCase("sendToServer")) {

            InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(ALOAD,1));//FMLProxyPacket

            insnList.add(new VarInsnNode(ALOAD,0));
            insnList.add(new FieldInsnNode(GETFIELD, "cpw/mods/fml/common/network/FMLEventChannel", "channels", "Ljava/util/EnumMap;"));

            insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(TransformFMLEventChannel.class), "FMLEventChannelHook", "(Lcpw/mods/fml/common/network/internal/FMLProxyPacket;Ljava/util/EnumMap;)Z", false));
            LabelNode labelNode = new LabelNode();
            insnList.add(new JumpInsnNode(IFEQ,labelNode));
            insnList.add(new InsnNode(RETURN));
            insnList.add(labelNode);
            methodNode.instructions.insert(insnList);
            LogManager.getLogger().info("Successfully initialized FML hook !");

        }
    }

    public static boolean FMLEventChannelHook(FMLProxyPacket fmlProxyPacket, EnumMap enumMap){
        EventFMLChannels eventFMLChannels = new EventFMLChannels(fmlProxyPacket,enumMap);
        EventManager.call(eventFMLChannels);
        return eventFMLChannels.isCancelled();
    }

}