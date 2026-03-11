package github.aqumpusaxy.pnud;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Pnud.MODID)
public class EntityTargetHandler {
    private static final float THRESHOLD_PERCENTAGE = 0.25F;

    @SubscribeEvent
    public static void onPetHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        if (!(entity instanceof OwnableEntity ownable && ownable.getOwner() != null)) {
            return;
        }

        //FIXME: 清除范围内(?)所有生物的仇恨,并且让他们选择新的目标
        if (entity.getHealth() - event.getAmount() <= entity.getMaxHealth() * THRESHOLD_PERCENTAGE) {
            if (event.getSource().getEntity() instanceof Mob sourceMob) {
                sourceMob.setTarget(null);
            }

            if (entity instanceof Mob pet) {
                pet.setTarget(null);
                pet.moveTo(ownable.getOwner().position());
            }
        }
    }

    @SubscribeEvent
    public static void onPetTargetChange(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        LivingEntity newTarget = event.getNewTarget();

        if (newTarget == null) return;

        if (isLowHealthPet(entity) || isLowHealthPet(newTarget)) {
            event.setNewTarget(null);
        }
    }

    private static boolean isLowHealthPet(LivingEntity entity) {
        return entity instanceof OwnableEntity pet
                && pet.getOwner() != null
                && entity.getHealth() <= entity.getMaxHealth() * THRESHOLD_PERCENTAGE;
    }
}
