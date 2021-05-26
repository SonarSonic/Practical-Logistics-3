package sonar.logistics.util.debug;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import sonar.logistics.PL3;
import sonar.logistics.server.data.types.DataType;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.util.registry.Registries;

import java.util.Collection;
import java.util.List;

public class DebugCommands {

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event){
        event.getCommandDispatcher().register(
                Commands.literal("pl3")
                .requires((src) -> src.hasPermissionLevel(2))
                .then(Commands.literal("testDataSync").executes(DebugCommands::testEmptyDataSynchronisation))
        );
    }

    private static int testEmptyDataSynchronisation(CommandContext<CommandSource> commandContext) {
        sendMessage(commandContext, TextFormatting.BLUE + "Starting Data Synchronisation Test");

        Collection<DataType> types = Registries.getDataTypeRegistry().registry.values();

        boolean success = true;

        try{
            for(DataType type : types){
                for(boolean emptyTest : new boolean[]{true, false}){
                    String testType = emptyTest ? "Null Test" : "Data Test";

                    //// NBT TESTS \\\\

                    IData writeTest = emptyTest ? type.factory.create() : type.factory.createTest();
                    CompoundNBT nbt = new CompoundNBT();
                    type.factory.save(writeTest, "test", nbt);

                    IData readTest = type.factory.create();
                    type.factory.read(readTest, "test", nbt);

                    CompoundNBT compareNBT = new CompoundNBT();
                    type.factory.save(readTest, "test", compareNBT);

                    if(!compareNBT.equals(nbt)){
                        sendMessage(commandContext, String.format("%s: Error: %s Failed NBT Sync Test", testType, type.dataType));
                        PL3.LOGGER.error("{}: Invalid NBT Synchronisation for Data Type: {}", testType, type.dataType);
                        PL3.LOGGER.error("{}: Original NBT: {}, Result NBT {}", testType, nbt, compareNBT);
                        success = false;
                    }else{
                        PL3.LOGGER.info("{}: Passed NBT Synchronisation for Data Type: {}", testType, type.dataType);
                    }

                    //// BYTE BUF TESTS \\\\

                    IData writeBufferTest = emptyTest ? type.factory.create() : type.factory.createTest();
                    PacketBuffer writeBuffer = new PacketBuffer(Unpooled.buffer());
                    writeBuffer.writeInt(100);
                    type.factory.saveUpdate(writeBufferTest, writeBuffer);

                    IData readBufferTest = type.factory.create();
                    writeBuffer.readInt();
                    type.factory.readUpdate(readTest, writeBuffer);

                    PacketBuffer compareBuffer = new PacketBuffer(Unpooled.buffer());
                    compareBuffer.writeInt(100);
                    type.factory.saveUpdate(readBufferTest, compareBuffer);

                    if(!ByteBufUtil.equals(writeBuffer.asReadOnly(), 0, compareBuffer.asReadOnly(), 0, writeBuffer.readableBytes())){
                        sendMessage(commandContext, String.format("%s: Error: %s Failed ByteBuf Sync Test", testType, type.dataType));
                        PL3.LOGGER.error("{}: Invalid ByteBuf Synchronisation for Data Type: {}", testType, type.dataType);
                        PL3.LOGGER.error("{}: Original ByteBuf: {}, Result ByteBuf {}", testType, writeBuffer, compareBuffer);
                        success = false;
                    }else{
                        PL3.LOGGER.info("{}: Passed ByteBuf Synchronisation for Data Type: {}", testType, type.dataType);
                    }
                }
            }
        }catch (Exception e){
            success = false;
            e.printStackTrace();
        }
        sendMessage(commandContext, (success ? TextFormatting.GREEN + "PASSED" : TextFormatting.RED + "FAILED") + " Tested " + types.size() + " Data Types");
        return Command.SINGLE_SUCCESS;
    }

    public static void sendMessage(CommandContext<CommandSource> commandContext, String string){
        if(commandContext.getSource().getEntity() instanceof PlayerEntity){
            PlayerEntity entity = (PlayerEntity) commandContext.getSource().getEntity();
            entity.sendMessage(new StringTextComponent(string));
        }
        PL3.LOGGER.info("Debug Command: " + string);
    }
}
