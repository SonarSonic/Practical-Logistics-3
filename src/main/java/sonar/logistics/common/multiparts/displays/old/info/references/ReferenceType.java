/*
package sonar.logistics.common.multiparts.displays.old.info.references;

import sonar.logistics.api.core.tiles.displays.info.IInfo;
import sonar.logistics.api.core.tiles.displays.info.INameableInfo;
import sonar.logistics.api.core.tiles.displays.info.ISuffixable;

public enum ReferenceType {
	INFO_TYPE, OBJECT_TYPE, IDENTIFIER, CLIENT_INFO, RAW_INFO, SUFFIX, PREFIX;

	public String getRefString(IInfo info) {
		
		if (info instanceof INameableInfo) {
			INameableInfo name = (INameableInfo) info;
			switch (this) {
			case IDENTIFIER:
				return name.getClientIdentifier();
			case INFO_TYPE:
				return name.getClientType();
			case OBJECT_TYPE:
				return "NONE";
			case CLIENT_INFO:
				return name.getClientObject();
			default:
				break;
			}
		}
		if (info instanceof ISuffixable) {
			ISuffixable suffix = (ISuffixable) info;
			switch (this) {
			case PREFIX:
				return suffix.getPrefix();
			case RAW_INFO:
				return suffix.getRawData();
			case SUFFIX:
				return suffix.getSuffix();
			default:
				break;
			}
		}

		return "";
	}
}*/
