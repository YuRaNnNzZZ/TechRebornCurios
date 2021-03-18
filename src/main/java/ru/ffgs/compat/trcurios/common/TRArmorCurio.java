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

package ru.ffgs.compat.trcurios.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.api.items.ArmorTickable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.component.ICurio;
import top.theillusivec4.curios.api.type.component.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.util.Optional;

public class TRArmorCurio implements ICurio {
    public TRArmorCurio() {
        super();
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity)) return;

        ItemStack stack = getStackInSlot(identifier, index, livingEntity);
        if (stack.isEmpty()) return;

        Item item = stack.getItem();
        if (item instanceof ArmorTickable) {
            ((ArmorTickable) item).tickArmor(stack, (PlayerEntity) livingEntity);
        }
    }

    @Override
    public void onUnequip(String identifier, int index, LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity)) return;

        ItemStack stack = getStackInSlot(identifier, index, livingEntity);
        if (stack.isEmpty()) return;

        Item item = stack.getItem();
        if (item instanceof ArmorRemoveHandler) {
            ((ArmorRemoveHandler) item).onRemoved((PlayerEntity) livingEntity);
        }
    }

    public static ItemStack getStackInSlot(String identifier, int index, LivingEntity livingEntity) {
        ICuriosHelper helper = CuriosApi.getCuriosHelper();
        Optional<ICuriosItemHandler> handler = helper.getCuriosHandler(livingEntity);
        if (!handler.isPresent()) return ItemStack.EMPTY;

        Optional<ICurioStacksHandler> stacksHandlerOptional = handler.get().getStacksHandler(identifier);
        if (!stacksHandlerOptional.isPresent()) return ItemStack.EMPTY;

        IDynamicStackHandler stacksHandler = stacksHandlerOptional.get().getStacks();

        return stacksHandler.getStack(index);
    }
}
