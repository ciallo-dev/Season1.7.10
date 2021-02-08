package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import com.darkmagician6.eventapi.EventManager;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.EnumMap;

public class TransformSimpleNetworkWrapper implements Opcodes {

    public static void transformSimpleNetworkWrapper(ClassNode classNode, MethodNode methodNode) {
        if (methodNode.name.equalsIgnoreCase("sendToServer")) {

            InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(ALOAD,1));

            insnList.add(new VarInsnNode(ALOAD,0));
            insnList.add(new FieldInsnNode(GETFIELD, "cpw/mods/fml/common/network/simpleimpl/SimpleNetworkWrapper", "channels", "Ljava/util/EnumMap;"));

            insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(TransformSimpleNetworkWrapper.class), "simpleNetworkWrapperHook", "(Ljava/lang/Object;Ljava/util/EnumMap;)Z", false));
            LabelNode labelNode = new LabelNode();
            insnList.add(new JumpInsnNode(IFEQ,labelNode));
            insnList.add(new InsnNode(RETURN));
            insnList.add(labelNode);
//			insnList.add(new FrameNode(F_APPEND, 1, new Object[]{"cn/snowflake/rose/events/impl/EventFMLChannels"}, 0, null));
            methodNode.instructions.insert(insnList);
            LogManager.getLogger().info("Successfully initialized FML hook !");

        }
    }


    public static boolean simpleNetworkWrapperHook(Object object, EnumMap enumMap){
        EventFMLChannels eventFMLChannels = new EventFMLChannels(object,enumMap);
        EventManager.call(eventFMLChannels);
        return eventFMLChannels.isCancelled();
    }

}