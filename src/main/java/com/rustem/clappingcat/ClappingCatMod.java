package com.rustem.clappingcat;

import com.rustem.clappingcat.entity.ClappingCatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(ClappingCatMod.MODID)
public class ClappingCatMod {
    public static final String MODID = "clappingcat";

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<EntityType<ClappingCatEntity>> CLAPPING_CAT =
            ENTITIES.register("clapping_cat", () -> EntityType.Builder
                    .of(ClappingCatEntity::new, MobCategory.MONSTER)
                    .sized(1.0F, 1.8F)
                    .clientTrackingRange(10)
                    .build("clapping_cat"));

    // ForgeSpawnEggItem takes a Supplier -> safe regardless of registry order
    public static final RegistryObject<Item> CLAPPING_CAT_SPAWN_EGG =
            ITEMS.register("clapping_cat_spawn_egg", () ->
                    new ForgeSpawnEggItem(CLAPPING_CAT, 0x15151A, 0xE6C46B, new Item.Properties()));

    public ClappingCatMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(modBus);
        ITEMS.register(modBus);
        modBus.addListener(this::registerAttributes);
        modBus.addListener(this::addToCreativeTab);
    }

    private void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(CLAPPING_CAT_SPAWN_EGG);
        }
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CLAPPING_CAT.get(), ClappingCatEntity.createAttributes().build());
    }
}
