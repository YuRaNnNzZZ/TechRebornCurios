/*
 * This file is part of TechReborn Curios, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 YuRaNnNzZZ
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.ffgs.compat.trcurios.client;

import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.ffgs.compat.trcurios.mixin.ArmorFeatureAccessorMixin;
import ru.ffgs.compat.trcurios.util.TechCurioUtils;
import top.theillusivec4.curios.api.type.component.IRenderableCurio;

import java.util.Map;

public class ArmorCurioRender implements IRenderableCurio {
    ArmorFeatureRenderer armorFeatureRenderer;
    EquipmentSlot slot;

    private static final Map<String, Identifier> ARMOR_TEXTURE_CACHE = Maps.newHashMap();
    BipedEntityModel bodyModel = new BipedEntityModel(1.0F);
    BipedEntityModel legsModel = new BipedEntityModel(0.5F);

    public ArmorCurioRender(EquipmentSlot slot) {
        this.slot = slot;
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int light,
                       LivingEntity livingEntity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        ItemStack itemStack = TechCurioUtils.getStackInSlot(identifier, index, livingEntity);
        if (itemStack.getItem() instanceof ArmorItem) {
            FeatureRendererContext rendererContext = (FeatureRendererContext) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);

            if (armorFeatureRenderer == null) {
                armorFeatureRenderer = new ArmorFeatureRenderer(rendererContext, legsModel, bodyModel);
            }

            ArmorFeatureAccessorMixin armorFeatureAccessor = (ArmorFeatureAccessorMixin) armorFeatureRenderer;

            BipedEntityModel model = slot == EquipmentSlot.LEGS ? legsModel : bodyModel;
            ((BipedEntityModel) rendererContext.getModel()).setAttributes(model);
            armorFeatureAccessor.invokeSetVisible(model, slot);

            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(getArmorTexture((ArmorItem) itemStack.getItem(), slot == EquipmentSlot.LEGS, null)), false, itemStack.hasGlint());
            model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    // Reimplementing vanilla function because Fabric API interferes with player's armor slot check
    private Identifier getArmorTexture(ArmorItem armorItem, boolean bl, @Nullable String string) {
        return ARMOR_TEXTURE_CACHE.computeIfAbsent("textures/models/armor/" + armorItem.getMaterial().getName() + "_layer_" + (bl ? 2 : 1) + (string == null ? "" : "_" + string) + ".png", Identifier::new);
    }
}
