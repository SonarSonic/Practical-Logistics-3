package sonar.logistics.client.gsi.interactions.hotkeys;

import net.minecraft.client.gui.screen.Screen;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.client.gsi.interactions.EditStandardTextInteraction;

public enum HotKeyFunctions {

	//cursor move
	HOME((handler, key, scanCode, mod) -> key == 268 && !handler.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorToStart(interaction.cursor);}),//
	END((handler, key, scanCode, mod) -> key == 269 && !handler.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorToEnd(interaction.cursor);}),//
	UP((handler, key, scanCode, mod) -> key == 265 && !handler.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorUp(interaction.cursor);}),//
	DOWN((handler, key, scanCode, mod) -> key == 264 && !handler.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorDown(interaction.cursor);}),//
	LEFT((handler, key, scanCode, mod) -> key == 263 && !handler.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorLeft(interaction.cursor);}),//
	RIGHT((handler, key, scanCode, mod) -> key == 262 && !handler.hasShiftDown(), interaction -> {interaction.clearSelection(); interaction.moveCursorRight(interaction.cursor);}),//

	HOME_SHIFT((handler, key, scanCode, mod) -> key == 268 && handler.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorToStart(interaction.selectionEnd);}),//
	END_SHIFT((handler, key, scanCode, mod) -> key == 269 && handler.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorToEnd(interaction.selectionEnd);}),//
	UP_SHIFT((handler, key, scanCode, mod) -> key == 265 && handler.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorUp(interaction.selectionEnd);}),//
	DOWN_SHIFT((handler, key, scanCode, mod) -> key == 264 && handler.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorDown(interaction.selectionEnd);}),//
	LEFT_SHIFT((handler, key, scanCode, mod) -> key == 263 && handler.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorLeft(interaction.selectionEnd);}),//
	RIGHT_SHIFT((handler, key, scanCode, mod) -> key == 262 && handler.hasShiftDown(), interaction -> {interaction.checkSelection(); interaction.moveCursorRight(interaction.selectionEnd);}),//


	//selection move

	//format changes

	BOLD((handler, key, scanCode, mod) -> key == 78 && handler.hasControlDown() && !handler.hasShiftDown() && !handler.hasAltDown(), interaction -> GSIDesignSettings.toggleBoldStyling()),//
	ITALIC((handler, key, scanCode, mod) -> key == 73 && handler.hasControlDown() && !handler.hasShiftDown() && !handler.hasAltDown(), interaction -> GSIDesignSettings.toggleItalicStyling()),//
	UNDERLINE((handler, key, scanCode, mod) -> key == 85 && handler.hasControlDown() && !handler.hasShiftDown() && !handler.hasAltDown(), interaction -> GSIDesignSettings.toggleUnderlineStyling()),//
	OBFUSCATED((handler, key, scanCode, mod) -> key == 79 && handler.hasControlDown() && !handler.hasShiftDown() && !handler.hasAltDown(), interaction -> GSIDesignSettings.toggleObfuscatedStyling()),//


	ENTER((handler, key, scanCode, mod) -> key == 257 && !handler.hasShiftDown(), EditStandardTextInteraction::enter),//
	COPY((handler, key, scanCode, mod) -> Screen.isCopy(key), EditStandardTextInteraction::copy),//
	PASTE((handler, key, scanCode, mod) -> Screen.isPaste(key), EditStandardTextInteraction::paste),//
	CUT((handler, key, scanCode, mod) -> Screen.isCut(key), EditStandardTextInteraction::cut),//
	BACKSPACE((handler, key, scanCode, mod) -> key == 259, i -> i.deleteGlyph(true)),//
	DEL((handler, key, scanCode, mod) -> key == 261, i -> i.deleteGlyph(false)),//
	
	//no line required
	//SAVE((key, scanCode, mod) -> key == Keyboard.KEY_S && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown(), (gui, chr, key) -> gui.save()),//
	SELECT_ALL((handler, key, scanCode, mod) -> Screen.isSelectAll(key), EditStandardTextInteraction::selectAll); //
	//DESELECT_ALL((key, scanCode, mod) -> c == 4, (gui, chr, key) -> GuiActions.DESELECT_ALL.trigger(gui)); //


	public IKeyMatch key;
	public ITextFunction textFunction;

	HotKeyFunctions(IKeyMatch key, ITextFunction textFunction) {
		this.key = key;
		this.textFunction = textFunction;
	}

	public static boolean triggerHotKey(EditStandardTextInteraction textInteraction, GSIInteractionHandler handler, int key, int scanCode, int modifiers) {
		for (HotKeyFunctions func : HotKeyFunctions.values()) {
			if (func.key.canTrigger(handler, key, scanCode, modifiers)) {
				func.textFunction.trigger(textInteraction);
				return true;
			}
		}
		return false;
	}
}