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

package ru.ffgs.compat.trcurios.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import reborncore.common.util.ItemUtils;
import team.reborn.energy.Energy;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.component.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ItemUtils.class)
public class ItemUtilsMixin {
	// @Inject(at = @At("TAIL"), method = "Lreborncore/common/util/ItemUtils;distributePowerToInventory(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;ILjava/util/function/Predicate;)V", remap = false)
	@Inject(at = @At("TAIL"), method = "Lreborncore/common/util/ItemUtils;distributePowerToInventory(Lnet/minecraft/class_1657;Lnet/minecraft/class_1799;ILjava/util/function/Predicate;)V", remap = false)
	private static void distributePowerToInventory(PlayerEntity player, ItemStack itemStack, int maxOutput, Predicate<ItemStack> filter, CallbackInfo info) {
		ICuriosHelper helper = CuriosApi.getCuriosHelper();

		Optional<ICuriosItemHandler> handler = helper.getCuriosHandler(player);
		if (!handler.isPresent()) return;

		for (Map.Entry<String, ICurioStacksHandler> entry : handler.get().getCurios().entrySet()) {
			ICurioStacksHandler stacksHandler = entry.getValue();
			IDynamicStackHandler dynamicStackHandler = stacksHandler.getStacks();

			for (int i = 0; i < dynamicStackHandler.size(); i++) {
				ItemStack invStack = dynamicStackHandler.getStack(i);

				if (invStack.isEmpty() || !filter.test(invStack)) {
					continue;
				}

				if (Energy.valid(invStack)) {
					Energy.of(itemStack)
							.into(Energy.of(invStack))
							.move(maxOutput);
				}
			}
		}
	}
}
