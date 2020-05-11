package sonar.logistics.client.design.gui.interactions.hotkeys;

import net.minecraft.client.gui.screen.Screen;
import sonar.logistics.client.design.gui.interactions.ViewportTextInteraction;

public enum HotKeyFunctions {

	//cursor move
	//HOME((key, scanCode, mod) -> key == InputMappings.Input.Keyboard.KEY_HOME && !Screen.isShiftKeyDown(), (gui, string, chr, key) -> gui.cursorPosition.setXToFirst()),//
	//END((key, scanCode, mod) -> key == Keyboard.KEY_END && !Screen.isShiftKeyDown(), (gui, string, chr, key) -> gui.cursorPosition.setXToLast(gui.text)),//
	UP((key, scanCode, mod) -> key == 265 && !Screen.hasShiftDown(), interaction -> interaction.moveCursorVertically(interaction.cursor, -1)),//
	DOWN((key, scanCode, mod) -> key == 264 && !Screen.hasShiftDown(), interaction -> interaction.moveCursorVertically(interaction.cursor, 1)),//
	LEFT((key, scanCode, mod) -> key == 263 && !Screen.hasShiftDown(), interaction -> interaction.moveCursorHorizontally(interaction.cursor, -1)),//
	RIGHT((key, scanCode, mod) -> key == 262 && !Screen.hasShiftDown(), interaction -> interaction.moveCursorHorizontally(interaction.cursor, 1));//

	/*
	//selection move
	HOME_SHIFT((key, scanCode, mod) -> key == Keyboard.KEY_HOME && Screen.isShiftKeyDown(), (gui, string, chr, key) -> {if(gui.checkAndCreateSelection()){gui.selectPosition.setXToFirst();}}),//
	END_SHIFT((key, scanCode, mod) -> key == Keyboard.KEY_END && Screen.isShiftKeyDown(), (gui, string, chr, key) -> {if(gui.checkAndCreateSelection()){gui.selectPosition.setXToLast(gui.text);}}),//
	UP_SHIFT((key, scanCode, mod) -> key == Keyboard.KEY_UP && Screen.isShiftKeyDown(), (gui, string, chr, key) -> {if(gui.checkAndCreateSelection()){gui.selectPosition.moveY(gui.text, -1);}}),//
	DOWN_SHIFT((key, scanCode, mod) -> key == Keyboard.KEY_DOWN && Screen.isShiftKeyDown(), (gui, string, chr, key) -> {if(gui.checkAndCreateSelection()){gui.selectPosition.moveY(gui.text, -1);}}),//
	LEFT_SHIFT((key, scanCode, mod) -> key == Keyboard.KEY_LEFT && Screen.isShiftKeyDown(), (gui, string, chr, key) -> {if(gui.checkAndCreateSelection()){gui.selectPosition.moveX(gui.text, -1);}}),//
	RIGHT_SHIFT((key, scanCode, mod) -> key == Keyboard.KEY_RIGHT && Screen.isShiftKeyDown(), (gui, string, chr, key) -> {if(gui.checkAndCreateSelection()){gui.selectPosition.moveX(gui.text, 1);}}),//
	
	//format changes
	BOLD((key, scanCode, mod) -> key == Keyboard.KEY_N && Screen.isCtrlKeyDown() && !Screen.isShiftKeyDown() && !Screen.isAltKeyDown(), (gui, string, chr, key) -> gui.toggleSpecialFormatting(TextFormatting.BOLD)),//
	ITALIC((key, scanCode, mod) -> key == Keyboard.KEY_I && Screen.isCtrlKeyDown() && !Screen.isShiftKeyDown() && !Screen.isAltKeyDown(), (gui, string, chr, key) -> gui.toggleSpecialFormatting(TextFormatting.ITALIC)),//
	UNDERLINE((key, scanCode, mod) -> key == Keyboard.KEY_U && Screen.isCtrlKeyDown() && !Screen.isShiftKeyDown() && !Screen.isAltKeyDown(), (gui, string, chr, key) -> gui.toggleSpecialFormatting(TextFormatting.UNDERLINE)),//

	ENTER((key, scanCode, mod) -> key == Keyboard.KEY_RETURN || key == Keyboard.KEY_NUMPADENTER, (gui, string, chr, key) -> gui.onCarriageReturn()),//
	COPY((key, scanCode, mod) -> Screen.isKeyComboCtrlC(i), (gui, string, chr, key) -> gui.copy()),//
	PASTE((key, scanCode, mod) -> Screen.isKeyComboCtrlV(i), (gui, string, chr, key) -> gui.paste()),//
	CUT((key, scanCode, mod) -> Screen.isKeyComboCtrlX(i), (gui, string, chr, key) -> gui.cut()),//
	BACKSPACE((key, scanCode, mod) -> key == Keyboard.KEY_BACK, (gui, string, chr, key) -> gui.removeText(key)),//
	DEL((key, scanCode, mod) -> key == Keyboard.KEY_DELETE, (gui, string, chr, key) -> gui.removeText(key)),//
	
	//no line required
	SAVE((key, scanCode, mod) -> key == Keyboard.KEY_S && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown(), (gui, chr, key) -> gui.save()),//
	SELECT_ALL((key, scanCode, mod) -> GuiScreen.isKeyComboCtrlA(i), (gui, chr, key) -> GuiActions.SELECT_ALL.trigger(gui)), //
	DESELECT_ALL((key, scanCode, mod) -> c == 4, (gui, chr, key) -> GuiActions.DESELECT_ALL.trigger(gui)); //

	*/

	public IKeyMatch key;
	public ITextFunction textFunction;

	HotKeyFunctions(IKeyMatch key, ITextFunction textFunction) {
		this.key = key;
		this.textFunction = textFunction;
	}

	public static boolean triggerHotKey(ViewportTextInteraction textInteraction, int key, int scanCode, int modifiers) {
		for (HotKeyFunctions func : HotKeyFunctions.values()) {
			if (func.key.canTrigger(key, scanCode, modifiers)) {
				func.textFunction.trigger(textInteraction);
				return true;
			}
		}
		return false;
	}
}