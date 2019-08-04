package com.leocth.tryingout.misc;

/**
 * This is a Java file created by LeoC200 on 2019/8/3 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */

import javax.annotation.Nullable;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public final class FontUtils {

	private FontUtils() {/* this is a utility class!!! what do you expect??? STATIC METHODS ONLY GUYYYYS!!!*/}
	
	public static ITextComponent makeWeblink(ITextComponent txt) {
		return makeWeblink(txt, null);
	}
	
	public static ITextComponent makeWeblink(ITextComponent txt, @Nullable Style style) {
		// TODO test is txt a valid URL
		if (style == null) {
			style = new Style().setColor(TextFormatting.WHITE)
					.setUnderlined(true)
					.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, txt.getString()));
		}
		ITextComponent t = txt.deepCopy().setStyle(style);
		return t;
	}
	public static ITextComponent makeWeblink(ITextComponent txt, String str, @Nullable Style style) {
		if (style == null) {
			style = new Style().setColor(TextFormatting.WHITE)
					.setUnderlined(true)
					.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, str));
		}
		ITextComponent t = txt.deepCopy().setStyle(style);
		return t;
	}

}
