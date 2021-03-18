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

import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import techreborn.init.TRContent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosComponent;
import top.theillusivec4.curios.api.SlotTypeInfo;
import top.theillusivec4.curios.api.SlotTypePreset;

public class TechRebornCuriosMod implements ModInitializer {
	@Override
	public void onInitialize() {
		CuriosApi.enqueueSlotType(SlotTypeInfo.BuildScheme.REGISTER, SlotTypePreset.BACK.getInfoBuilder().build());

		registerCurio(TRContent.LAPOTRONIC_ORBPACK);
		registerCurio(TRContent.LITHIUM_ION_BATPACK);
//		registerCurio(TRContent.CLOAKING_DEVICE);
	}

	private void registerCurio(Item targetItem) {
		ItemComponentCallbackV2.event(targetItem).register((item, itemStack, componentContainer) -> componentContainer.put(CuriosComponent.ITEM, new TRArmorCurio()));
	}
}
