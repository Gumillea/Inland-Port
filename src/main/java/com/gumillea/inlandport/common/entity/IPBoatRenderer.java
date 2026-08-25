package com.gumillea.inlandport.common.entity;

import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.client.model.ListModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IPBoatRenderer extends BoatRenderer {
    private final Pair<ResourceLocation, ListModel<Boat>> model;
    private final boolean hasChest;

    public IPBoatRenderer(EntityRendererProvider.Context context, boolean hasChest) {
        super(context, hasChest);
        this.hasChest = hasChest;
        ListModel<Boat> model = hasChest ? new ChestBoatModel(context.bakeLayer(ModelLayers.createChestBoatModelName(Boat.Type.OAK))) : new BoatModel(context.bakeLayer(ModelLayers.createBoatModelName(Boat.Type.OAK)));
        this.model = Pair.of(null, model);
    }

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if (boat instanceof IPBoat ipBoat) {
            ResourceLocation type = ipBoat.getCustomType();
            String hasChest = this.hasChest ? "chest_boat" : "boat";
            String path = "textures/entity/" + hasChest + "/";
            ResourceLocation texture = IPUtil.loc(type.getNamespace(), path + type.getPath().replace("_" + hasChest, "") + ".png");

            return Pair.of(texture, this.model.getSecond());
        }
        return super.getModelWithLocation(boat);
    }
}