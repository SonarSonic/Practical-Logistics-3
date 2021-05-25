package sonar.logistics.server.data.methods;

import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import sonar.logistics.server.data.types.sources.SourceData;

public class VanillaMethods {

    public static Method FURNACE_BURN_TIME;
    public static Method FURNACE_CURRENT_ITEM_BURN_TIME;
    public static Method FURNACE_COOK_TIME;
    public static Method FURNACE_TOTAL_COOK_TIME;

    public static Method BLOCK_POS_X;
    public static Method BLOCK_POS_Y;
    public static Method BLOCK_POS_Z;
    public static Method BLOCK_FACE;
    public static Method BLOCK_TRANSLATION_KEY;
    public static Method BLOCK_METADATA_FROM_STATE;
    public static Method BLOCK_HARDNESS;
    public static Method BLOCK_HARVEST_LEVEL;
    public static Method BLOCK_IS_FOLIAGE;
    public static Method BLOCK_IS_WOOD;
    public static Method BLOCK_CAN_SUSTAIN_LEAVES;
    public static Method BLOCK_WEAK_POWER;
    public static Method BLOCK_STRONG_POWER;
    public static Method BLOCK_INDIRECTLY_POWERED;
    public static Method BLOCK_IS_SIDE_SOLID;


    public static Method BLOCK_IS_MAX_AGE;

    public static Method BLOCK_FLUID_LUMINOSITY;
    public static Method BLOCK_FLUID_DENSITY;
    public static Method BLOCK_FLUID_TEMPERATURE;
    public static Method BLOCK_FLUID_VISCOSITY;
    public static Method BLOCK_FLUID_IS_GASEOUS;
    public static Method BLOCK_FLUID_RARITY;

    public static Method WORLD_IS_RAINING;
    public static Method WORLD_IS_THUNDERING;
    public static Method WORLD_NAME;
    public static Method WORLD_DIMENSION;
    public static Method WORLD_DIMENSION_NAME;

    public static Method ENTITY_POS_X;
    public static Method ENTITY_POS_Y;
    public static Method ENTITY_POS_Z;
    public static Method ENTITY_NAME;
    public static Method ENTITY_HEALTH;
    public static Method ENTITY_MAX_HEALTH;
    public static Method ENTITY_TOTAL_ARMOR;

    public static Method PLAYER_IS_CREATIVE;
    public static Method PLAYER_IS_SPECTATOR;
    public static Method PLAYER_FOOD_LEVEL;
    public static Method PLAYER_SATURATION_LEVEL;

    public static Method ADDRESS_DATA;
    public static Method ITEM_CAPABILITY;
    public static Method ENERGY_CAPABILITY;
    public static Method FLUID_CAPABILITY;

    public static void init(){
        /* FIXME
        FURNACE_BURN_TIME = MethodRegistry.registerMethodTileEntity(MethodCategory.MACHINES,"furnace.burnTime", Integer.class, AbstractFurnaceTileEntity.class, (E, T) -> T.getField(0));
        FURNACE_CURRENT_ITEM_BURN_TIME = MethodRegistry.registerMethodTileEntity(MethodCategory.MACHINES,"furnace.itemBurnTime", Integer.class, TileEntityFurnace.class, (E, T) -> T.getField(1));
        FURNACE_COOK_TIME = MethodRegistry.registerMethodTileEntity( MethodCategory.MACHINES,"furnace.cookTime", Integer.class, TileEntityFurnace.class, (E, T) -> T.getField(2));
        FURNACE_TOTAL_COOK_TIME = MethodRegistry.registerMethodTileEntity(MethodCategory.MACHINES,"furnace.totalCookTime", Integer.class, TileEntityFurnace.class, (E, T) -> T.getField(3));
        */
        BLOCK_TRANSLATION_KEY = MethodRegistry.registerBlockMethod(MethodCategory.BLOCKS,"getTranslationKey", String.class, Block.class, (E, B) -> B.getTranslationKey());
        //BLOCK_METADATA_FROM_STATE = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"getMetaFromState", Integer.class, Block.class, (E, B) -> B.getMetaFromState(E.state()));
        BLOCK_POS_X = MethodRegistry.registerBlockMethod(MethodCategory.BLOCKS,"getX", Integer.class, Block.class, (E, B) -> E.pos().getX());
        BLOCK_POS_Y = MethodRegistry.registerBlockMethod(MethodCategory.BLOCKS,"getY", Integer.class, Block.class, (E, B) -> E.pos().getY());
        BLOCK_POS_Z = MethodRegistry.registerBlockMethod(MethodCategory.BLOCKS,"getZ", Integer.class, Block.class, (E, B) -> E.pos().getZ());
        BLOCK_FACE = MethodRegistry.registerBlockMethod(MethodCategory.BLOCKS,"getFace", String.class, Block.class, (E, B) -> E.face().name());
        BLOCK_HARDNESS = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"getBlockHardness", Float.class, Block.class, (E, B) -> E.state().getBlockHardness(E.world(), E.pos()));
        BLOCK_HARVEST_LEVEL = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"getHarvestLevel", Integer.class, Block.class, (E, B) -> B.getHarvestLevel(E.state()));
        BLOCK_IS_FOLIAGE = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"isFoliage", Boolean.class, Block.class, (E, B) -> B.isFoliage(E.state(), E.world(), E.pos()));
        //BLOCK_IS_WOOD = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"isWood", Boolean.class, Block.class, (E, B) -> B.isWood(E.state(), E.world(), E.pos()));
        //BLOCK_CAN_SUSTAIN_LEAVES = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"canSustainLeaves", Boolean.class, Block.class, (E, B) -> B.canSustainLeaves(E.state(), E.world(), E.pos()));
        BLOCK_WEAK_POWER = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"getWeakPower", Integer.class, Block.class, (E, B) -> E.state().getWeakPower(E.world(), E.pos(), E.face()));
        BLOCK_STRONG_POWER = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"getStrongPower", Integer.class, Block.class, (E, B) -> E.state().getStrongPower(E.world(), E.pos(), E.face()));
        //BLOCK_INDIRECTLY_POWERED = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"isBlockIndirectlyGettingPowered", Integer.class, Block.class, (E, B) -> E.world().isBlockIndirectlyGettingPowered(E.pos()));
        //BLOCK_IS_SIDE_SOLID = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"isSideSolid", Boolean.class, Block.class, (E, B) -> B.isSideSolid(E.state(), E.world(), E.pos(), E.face()));

        BLOCK_IS_MAX_AGE = MethodRegistry.registerBlockMethod( MethodCategory.BLOCKS,"isMaxAge", Boolean.class, CropsBlock.class, (E, B) -> B.isMaxAge(E.state()));

        /*
        BLOCK_FLUID_LUMINOSITY = MethodRegistry.registerBlockMethod(MethodCategory.FLUIDS,"getLuminosity", Integer.class, IBucketPickupHandler.class, (E, B) -> B.getFluid().getLuminosity());
        BLOCK_FLUID_DENSITY = MethodRegistry.registerBlockMethod( MethodCategory.FLUIDS,"getDensity", Integer.class, IBucketPickupHandler.class, (E, B) -> B.getFluid().getDensity());
        BLOCK_FLUID_TEMPERATURE = MethodRegistry.registerBlockMethod( MethodCategory.FLUIDS,"getTemperature", Integer.class, IBucketPickupHandler.class, (E, B) -> B.getFluid().getTemperature());
        BLOCK_FLUID_VISCOSITY = MethodRegistry.registerBlockMethod( MethodCategory.FLUIDS,"getViscosity", Integer.class, IBucketPickupHandler.class, (E, B) -> B.getFluid().getViscosity());
        BLOCK_FLUID_IS_GASEOUS = MethodRegistry.registerBlockMethod( MethodCategory.FLUIDS,"isGaseous", Boolean.class, IBucketPickupHandler.class, (E, B) -> B.getFluid().isGaseous());
        BLOCK_FLUID_RARITY = MethodRegistry.registerBlockMethod( MethodCategory.FLUIDS,"getRarity", String.class, IBucketPickupHandler.class, (E, B) -> B.getFluid().getRarity().rarityName);
         */

        WORLD_IS_RAINING = MethodRegistry.registerWorldMethod( MethodCategory.WORLD,"isRaining", Boolean.class, W -> W.getWorldInfo().isRaining());
        WORLD_IS_THUNDERING = MethodRegistry.registerWorldMethod( MethodCategory.WORLD,"isThundering", Boolean.class, W -> W.getWorldInfo().isThundering());
        WORLD_NAME = MethodRegistry.registerWorldMethod( MethodCategory.WORLD,"getWorldName", String.class, W -> W.getWorldInfo().getWorldName());
        WORLD_DIMENSION = MethodRegistry.registerWorldMethod(MethodCategory.WORLD,"getDimension", Integer.class, W -> W.getDimension().getType().getId());
        WORLD_DIMENSION_NAME = MethodRegistry.registerWorldMethod(MethodCategory.WORLD,"getName", String.class, W ->  W.getDimension().getType().getRegistryName().getPath());

        ENTITY_POS_X = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"getX", Integer.class, Entity.class, (E, B) -> B.getPosition().getX());
        ENTITY_POS_Y = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"getY", Integer.class, Entity.class, (E, B) -> B.getPosition().getY());
        ENTITY_POS_Z = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"getZ", Integer.class, Entity.class, (E, B) -> B.getPosition().getZ());
        ENTITY_NAME = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"getName", String.class, Entity.class, (E, B) -> B.getName().getString());
        ENTITY_HEALTH = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"getHealth", Float.class, LivingEntity.class, (E, B) -> B.getHealth());
        ENTITY_MAX_HEALTH = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"getMaxHealth", Float.class, LivingEntity.class, (E, B) -> B.getMaxHealth());
        ENTITY_TOTAL_ARMOR = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"getTotalArmorValue", Integer.class, LivingEntity.class, (E, B) -> B.getTotalArmorValue());

        PLAYER_IS_CREATIVE = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"isCreative", Boolean.class, ServerPlayerEntity.class, (E, P) -> P.isCreative());
        PLAYER_IS_SPECTATOR = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"isSpectator", Boolean.class, ServerPlayerEntity.class, (E, P) -> P.isSpectator());
        PLAYER_FOOD_LEVEL = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"getFoodStats", Integer.class, ServerPlayerEntity.class, (E, P) -> P.getFoodStats().getFoodLevel());
        PLAYER_SATURATION_LEVEL = MethodRegistry.registerEntityMethod(MethodCategory.ENTITIES,"getFoodStats", Float.class, ServerPlayerEntity.class, (E, P) -> P.getFoodStats().getSaturationLevel());

        ADDRESS_DATA = MethodRegistry.registerMethod(MethodCategory.NETWORK, "getSourceAddress", SourceData.class, (E) -> true, SourceData::fromEnvironment);



        /* FIXME
        ITEM_CAPABILITY = MethodRegistry.registerMethodTileEntity(MethodCategory.INVENTORIES,"items", IItemHandler.class, TileEntity.class, (E, T) -> T.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, E.face()));
        ENERGY_CAPABILITY = MethodRegistry.registerMethodTileEntity(MethodCategory.ENERGY,"storage", IEnergyStorage.class, TileEntity.class, (E, T) -> T.getCapability(CapabilityEnergy.ENERGY, E.face()));
        FLUID_CAPABILITY = MethodRegistry.registerMethodTileEntity(MethodCategory.FLUIDS,"tanks", IFluidHandler.class, TileEntity.class, (E, T) -> T.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, E.face()));
        */
    }
}
