package sonar.logistics.client.gsi.style;

public enum ComponentAlignment {

	LEFT, CENTERED, RIGHT;

	public float align(float scale, float max) {
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
