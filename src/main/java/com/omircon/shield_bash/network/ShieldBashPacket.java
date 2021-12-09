package com.omircon.shield_bash.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ShieldBashPacket {


    public ShieldBashPacket(FriendlyByteBuf packetBuffer) {
    }

    public ShieldBashPacket() {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx)
    {
        //System.out.println("tgagfa");
        ctx.get().enqueueWork(() -> {
            //System.out.println("tset");
            ServerPlayer player = ctx.get().getSender();
            if(player.getUseItem().getItem() == Items.SHIELD && player.isUsingItem()) {
                EntityHitResult entityRayTraceResult = getEntityHitResult(player);
                if (entityRayTraceResult != null) {
                    Entity entity = entityRayTraceResult.getEntity();
                    if (entity != null) {
                        //System.out.println("check");
                        player.attack(entity);
                        player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F + player.getCommandSenderWorld().random.nextFloat() * 0.4F);
                    }
                }
            }
        });
        return true;
    }

    public static EntityHitResult getEntityHitResult(ServerPlayer player)
    {
        Vec3 vector3d = player.getEyePosition(0.0F);
        double d0 = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
        double d1 = d0;
        Vec3 vector3d1 = player.getCamera().getViewVector(1.0F);

        Vec3 vector3d2 = vector3d.add(vector3d1.x * d0, vector3d1.y * d0, vector3d1.z * d0);
        float f = 1.0F;
        AABB axisalignedbb = player.getCamera().getBoundingBox().expandTowards(vector3d1.scale(d0)).inflate(1.0D, 1.0D, 1.0D);

        return getEntityHitResult(player.getCamera(), vector3d, vector3d2, axisalignedbb, (p_215312_0_) -> {
            return !p_215312_0_.isSpectator() && p_215312_0_.isPickable();
        }, d1);
    }

    public static EntityHitResult getEntityHitResult(Entity p_221273_0_, Vec3 p_221273_1_, Vec3 p_221273_2_, AABB p_221273_3_, Predicate<Entity> p_221273_4_, double p_221273_5_) {
        Level world = p_221273_0_.level;
        double d0 = p_221273_5_;
        Entity entity = null;
        Vec3 vector3d = null;

        for(Entity entity1 : world.getEntities(p_221273_0_, p_221273_3_, p_221273_4_)) {
            AABB axisalignedbb = entity1.getBoundingBox().inflate((double)entity1.getPickRadius());
            Optional<Vec3> optional = axisalignedbb.clip(p_221273_1_, p_221273_2_);
            if (axisalignedbb.contains(p_221273_1_)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    vector3d = optional.orElse(p_221273_1_);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vector3d1 = optional.get();
                double d1 = p_221273_1_.distanceToSqr(vector3d1);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == p_221273_0_.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            vector3d = vector3d1;
                        }
                    } else {
                        entity = entity1;
                        vector3d = vector3d1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, vector3d);
    }
    /*

    public static Entity rayTrace(World world, PlayerEntity player, double range) {
        Vector3d pos = player.getEyePosition(0f);
        Vector3d cam1 = player.getLookAngle();
        Vector3d cam2 = cam1.add(cam1.x * range, cam1.y * range, cam1.z * range);
        AxisAlignedBB aabb = player.getBoundingBox().expandTowards(cam1.scale(range)).inflate(1.0F, 1.0F, 1.0F);
        RayTraceResult ray = findEntity(world, player, pos, cam2, aabb, null, range);

        if(ray != null) {
            if(ray.getType() == RayTraceResult.Type.ENTITY && ray instanceof EntityRayTraceResult) {
                EntityRayTraceResult ray2 = (EntityRayTraceResult) ray;
                Entity entity = ray2.getEntity();
                return entity;
            }
        }
        return null;
    }

    private static EntityRayTraceResult findEntity(World world, PlayerEntity player, Vector3d pos, Vector3d look, AxisAlignedBB aabb, Predicate<Entity> filter, double range) {
        for(Entity entity1 : world.getEntities(player, aabb, filter)) {
            AxisAlignedBB mob = entity1.getBoundingBox().inflate((double)1.0F);
            if(intersect(pos, look, mob, range)) {
                return new EntityRayTraceResult(entity1);
            }
        }
        return null;
    }

    private static boolean intersect(Vector3d pos, Vector3d look, AxisAlignedBB mob, double range) {
        Vector3d invDir = new Vector3d(1f / look.x, 1f / look.y, 1f / look.z);

        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;

        Vector3d max = new Vector3d(mob.maxX, mob.maxY, mob.maxZ);
        Vector3d min = new Vector3d(mob.minX, mob.minY, mob.minZ);

        Vector3d bbox = signDirX ? max : min;
        double tmin = (bbox.x - pos.x) * invDir.x;
        bbox = signDirX ? min : max;
        double tmax = (bbox.x - pos.x) * invDir.x;
        bbox = signDirY ? max : min;
        double tymin = (bbox.y - pos.y) * invDir.y;
        bbox = signDirY ? min : max;
        double tymax = (bbox.y - pos.y) * invDir.y;

        if ((tmin > tymax) || (tymin > tmax)) {
            return false;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }

        bbox = signDirZ ? max : min;
        double tzmin = (bbox.z - pos.z) * invDir.z;
        bbox = signDirZ ? min : max;
        double tzmax = (bbox.z - pos.z) * invDir.z;

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return false;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if ((tmin < range) && (tmax > 0)) {
            return true;
        }
        return false;
    }

     */

}
