package github.aqumpusaxy.pnud;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Pnud.MODID)
public class OwnableEntityHurtHandler {
    private static final Set<ResourceKey<DamageType>> DAMAGE_TYPES = Set.of(
            DamageTypes.CACTUS,
            DamageTypes.IN_FIRE,
            DamageTypes.FREEZE,
            DamageTypes.HOT_FLOOR,
            DamageTypes.SWEET_BERRY_BUSH
    );

    @SubscribeEvent
    public static void onPetHurt(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }

        if (!(event.getEntity() instanceof OwnableEntity ownable && ownable.getOwner() != null)) {
            return;
        }

        ResourceKey<DamageType> damageType = event.getSource().typeHolder().unwrapKey().orElse(null);
        if (damageType != null && DAMAGE_TYPES.contains(damageType)) {
            event.setCanceled(true);
        }
    }
}
