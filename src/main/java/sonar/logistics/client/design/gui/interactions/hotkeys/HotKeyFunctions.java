package sonar.logistics.client.design.gui.interactions.hotkeys;

import net.minecraft.client.gui.screen.Screen;
import sonar.logistics.client.design.gui.GSIDesignSettings;
import sonar.logistics.client.design.gui.interactions.DefaultTextInteraction;
import sonar.logistics.client.gsi.components.text.glyph.BulletPointGlyph;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;

public enum HotKeyFunctions {

	//cursor move
	HOME((key, scanCode, mod) -> key == 268 && !Screen.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorToStart(interaction.cursor);}),//
	END((key, scanCode, mod) -> key == 269 && !Screen.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorToEnd(interaction.cursor);}),//
	UP((key, scanCode, mod) -> key == 265 && !Screen.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorUp(interaction.cursor);}),//
	DOWN((key, scanCode, mod) -> key == 264 && !Screen.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorDown(interaction.cursor);}),//
	LEFT((key, scanCode, mod) -> key == 263 && !Screen.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorLeft(interaction.cursor);}),//
	RIGHT((key, scanCode, mod) -> key == 262 && !Screen.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorRight(interaction.cursor);}),//

	///TODO CONFIRM THESE SHIFT MOVES WORK.
	HOME_SHIFT((key, scanCode, mod) -> key == 268 && Screen.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorToStart(interaction.selectionEnd);}),//
	END_SHIFT((key, scanCode, mod) -> key == 269 && Screen.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorToEnd(interaction.selectionEnd);}),//
	UP_SHIFT((key, scanCode, mod) -> key == 265 && Screen.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorUp(interaction.selectionEnd);}),//
	DOWN_SHIFT((key, scanCode, mod) -> key == 264 && Screen.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorDown(interaction.selectionEnd);}),//
	LEFT_SHIFT((key, scanCode, mod) -> key == 263 && Screen.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorLeft(interaction.selectionEnd);}),//
	RIGHT_SHIFT((key, scanCode, mod) -> key == 262 && Screen.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorRight(interaction.selectionEnd);}),//


	//selection move

	//format changes

	BOLD((key, scanCode, mod) -> key == 78 && Screen.hasControlDown() && !Screen.hasShiftDown() && !Screen.hasAltDown(), interaction -> GSIDesignSettings.toggleBoldStyling()),//
	ITALIC((key, scanCode, mod) -> key == 73 && Screen.hasControlDown() && !Screen.hasShiftDown() && !Screen.hasAltDown(), interaction -> GSIDesignSettings.toggleItalicStyling()),//
	UNDERLINE((key, scanCode, mod) -> key == 85 && Screen.hasControlDown() && !Screen.hasShiftDown() && !Screen.hasAltDown(), interaction -> GSIDesignSettings.toggleUnderlineStyling()),//
	OBFUSCATED((key, scanCode, mod) -> key == 79 && Screen.hasControlDown() && !Screen.hasShiftDown() && !Screen.hasAltDown(), interaction -> GSIDesignSettings.toggleObfuscatedStyling()),//


	ENTER((key, scanCode, mod) -> key == 257 && !Screen.hasShiftDown(), i -> i.addGlyph(GSIDesignSettings.getLineBreakGlyph(false, GSIDesignSettings.lineStyle.copy()), GSIDesignSettings.glyphStyle)),//
	COPY((key, scanCode, mod) -> Screen.isCopy(key), DefaultTextInteraction::copy),//
	PASTE((key, scanCode, mod) -> Screen.isPaste(key), DefaultTextInteraction::paste),//
	CUT((key, scanCode, mod) -> Screen.isCut(key), DefaultTextInteraction::cut),//
	BACKSPACE((key, scanCode, mod) -> key == 259, i -> i.deleteGlyph(true)),//
	DEL((key, scanCode, mod) -> key == 261, i -> i.deleteGlyph(false)),//
	
	//no line required
	//SAVE((key, scanCode, mod) -> key == Keyboard.KEY_S && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown(), (gui, chr, key) -> gui.save()),//
	SELECT_ALL((key, scanCode, mod) -> Screen.isSelectAll(key), DefaultTextInteraction::selectAll); //
	//DESELECT_ALL((key, scanCode, mod) -> c == 4, (gui, chr, key) -> GuiActions.DESELECT_ALL.trigger(gui)); //


	public IKeyMatch key;
	public ITextFunction textFunction;

	HotKeyFunctions(IKeyMatch key, ITextFunction textFunction) {
		this.key = key;
		this.textFunction = textFunction;
	}

	public static boolean triggerHotKey(DefaultTextInteraction textInteraction, int key, int scanCode, int modifiers) {
		for (HotKeyFunctions func : HotKeyFunctions.values()) {
			if (func.key.canTrigger(key, scanCode, modifiers)) {
				func.textFunction.trigger(textInteraction);
				return true;
			}
		}
		return false;
	}
}