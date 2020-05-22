package sonar.logistics.client.gui.api;

import sonar.logistics.client.vectors.Quad2D;

public interface ISimpleWidget {

    void render(int mouseX, int mouseY, float partialTicks);

    Quad2D getQuad();
}
