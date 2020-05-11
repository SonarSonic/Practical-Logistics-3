package sonar.logistics.common.multiparts.displays.old.info.elements.base;

public interface IHyperlinkRequirement {
	
	String getHyperlink();
	
	void onGuiClosed(String hyperlink);	
}
