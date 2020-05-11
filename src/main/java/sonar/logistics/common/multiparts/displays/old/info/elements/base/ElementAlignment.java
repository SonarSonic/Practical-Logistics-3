package sonar.logistics.common.multiparts.displays.old.info.elements.base;

public enum ElementAlignment {

	LEFT, CENTERED, RIGHT;

	public double align(double scale, double max) {
		switch (this) {
			case CENTERED:
				return (max / 2) - (scale / 2);
			case LEFT:
				break;
			case RIGHT:
				return max - scale;
		}
		return 0;
	}

}
