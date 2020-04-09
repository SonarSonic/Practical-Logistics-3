package sonar.logistics.server.data.types.changes;

public enum EnumChangeable {

	INCREASED, // if the value has increased - if the value only changes and doesn't have an increased value this should still be used.

	DECREASED, // if the value has decreased

	EQUAL, // if no changes occurred to the value

	NEW_VALUE, // if the value hasn't been present before

	OLD_VALUE; // if the value is not longer present and should be removed

	public boolean shouldUpdate() {
		return this != EQUAL;
	}
	
	public boolean shouldDelete(){
		return this == OLD_VALUE;
	}

	public static EnumChangeable getChange(long count, long old){
		if(old == 0 && count != 0){
			return EnumChangeable.NEW_VALUE;
		}
		if (count == 0) {
			return EnumChangeable.OLD_VALUE;
		}
		if (count == old) {
			return EnumChangeable.EQUAL;
		}
		if (count > old) {
			return EnumChangeable.INCREASED;
		}
		return EnumChangeable.DECREASED;
	}
}
