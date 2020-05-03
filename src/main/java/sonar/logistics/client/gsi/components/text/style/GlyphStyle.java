package sonar.logistics.client.gsi.components.text.style;

import sonar.logistics.client.gsi.properties.ColourProperty;

public class GlyphStyle {

	public float fontHeight = 0.0625F;

	public ColourProperty textColour = new ColourProperty(255, 255, 255);
	public ColourProperty backgroundColour = new ColourProperty(0, 0, 0);

	public byte action_id = -1;

	public boolean bold = false;
	public boolean italic = false;
	public boolean underlined = false;
	public boolean strikethrough = false;
	public boolean obfuscated = false;
	public boolean shadow = false;
	public boolean seeThroughType = false;

	public GlyphStyle() {}

	public GlyphStyle copy() {
		GlyphStyle style = new GlyphStyle();
		style.fontHeight = fontHeight;
		style.textColour = textColour;
		style.backgroundColour = backgroundColour;
		style.bold = bold;
		style.italic = italic;
		style.underlined = underlined;
		style.strikethrough = strikethrough;
		style.obfuscated = obfuscated;
		style.shadow = shadow;
		style.action_id = action_id;
		return style;
	}
/*
	public void setFontColour(byte[] textColour) {
		this.textColour = textColour;
	}

	public byte[] getFontColour() {
		return textColour;
	}

	public void setBackgroundColour(byte[] backgroundColour) {
		this.backgroundColour = backgroundColour;
	}

	public byte[] getBackgroundColour() {
		return backgroundColour;
	}

	public void setActionID(byte actionID) {
		action_id = actionID;
	}

	public byte getActionID() {
		return action_id;
	}

	public boolean needsSave() {
		return textColour != null || backgroundColour != null || bold || italic || underlined || strikethrough || obfuscated || shadow || action_id != -1;
	}

	public boolean matching(GlyphStyle ss) {
		byte[] format = this.getByteFormatting();
		byte[] compareFormat = ss.getByteFormatting();
		for (int f = 0; f < format.length; f++) {
			if (format[f] != compareFormat[f]) {
				return false;
			}
		}
		return fontHeight == ss.fontHeight;
	}
	TODO
	@Override
	public void readData(NBTTagCompound nbt, SyncType type) {
		if (!nbt.getBoolean("def")) {
			fromIntFormatting(nbt.getIntArray("c"));
			fromByteFormatting(nbt.getByteArray("f"));
		}
	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound nbt, SyncType type) {
		boolean save = needsSave();
		nbt.setBoolean("def", !save);
		if (save) {
			nbt.setIntArray("c", getIntFormatting());
			nbt.setByteArray("f", getByteFormatting());
		}
		return nbt;
	}
	*/
	/*
	public byte[] getByteFormatting() {
		byte[] format = new byte[16];
		int byteId = 0;
		format[0] = (byte) (bold ? 1 : 0);
		format[1] = (byte) (italic ? 1 : 0);
		format[2] = (byte) (underlined ? 1 : 0);
		format[3] = (byte) (strikethrough ? 1 : 0);
		format[4] = (byte) (obfuscated ? 1 : 0);
		format[5] = (byte) (shadow ? 1 : 0);
		format[6] = textColour == null ? 0: textColour[0];
		format[7] = textColour == null ? 0: textColour[1];
		format[8] = textColour == null ? 0: textColour[2];
		format[9] = textColour == null ? 0: textColour[3];
		format[10] = backgroundColour == null ? 0: backgroundColour[0];
		format[11] = backgroundColour == null ? 0: backgroundColour[1];
		format[12] = backgroundColour == null ? 0: backgroundColour[2];
		format[13] = backgroundColour == null ? 0: backgroundColour[3];
		format[14] = action_id;
		format[15] = (byte) (seeThroughType ? 1 : 0);
		return format;
	}

	public void fromByteFormatting(byte[] format) {
		if (format.length != 16) {
			return;
		}
		bold = format[0] == 1;
		italic = format[1] == 1;
		underlined = format[2] == 1;
		strikethrough = format[3] == 1;
		obfuscated = format[4] == 1;
		shadow = format[5] == 1;
		textColour = new byte[4];
		textColour[0] = format[6];
		textColour[1] = format[7];
		textColour[2] = format[8];
		textColour[3] = format[9];
		backgroundColour = new byte[4];
		backgroundColour[0] = format[10];
		backgroundColour[1] = format[11];
		backgroundColour[2] = format[12];
		backgroundColour[3] = format[13];
		action_id = format[14];
		seeThroughType = format[15] == 1;
	}
	*/
}
