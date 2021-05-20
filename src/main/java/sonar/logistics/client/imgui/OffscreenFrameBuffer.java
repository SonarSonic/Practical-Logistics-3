package sonar.logistics.client.imgui;

import org.lwjgl.opengl.GL40;

//SRC: http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-14-render-to-texture/#render-to-texture
public class OffscreenFrameBuffer {

    public final int width;
    public final int height;

    public int frameBufferID;
    public int textureID;
    public int depthRenderBufferID;
    public boolean error;

    public OffscreenFrameBuffer(int width, int height){
        this.width = width;
        this.height = height;
        init(width, height);
    }

    public void init(int width, int height){
        // The framebuffer, which regroups 0, 1, or more textures, and 0 or 1 depth buffer.
        frameBufferID = GL40.glGenFramebuffers();
        GL40.glBindFramebuffer(GL40.GL_FRAMEBUFFER, frameBufferID);

        // The texture we're going to render to
        textureID = GL40.glGenTextures();

        // "Bind" the newly created texture : all future texture functions will modify this texture
        GL40.glBindTexture(GL40.GL_TEXTURE_2D, textureID);

        // Give an empty image to OpenGL ( the last "0" )
        GL40.glTexImage2D(GL40.GL_TEXTURE_2D, 0, GL40.GL_RGBA, width, height, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, 0);

        // Poor filtering. Needed !
        GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MAG_FILTER, GL40.GL_NEAREST);
        GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MIN_FILTER, GL40.GL_NEAREST);


        // The depth buffer
        depthRenderBufferID = GL40.glGenRenderbuffers();
        GL40.glBindRenderbuffer(GL40.GL_RENDERBUFFER, depthRenderBufferID);
        GL40.glRenderbufferStorage(GL40.GL_RENDERBUFFER, GL40.GL_DEPTH_COMPONENT, width, height);
        GL40.glFramebufferRenderbuffer(GL40.GL_FRAMEBUFFER, GL40.GL_DEPTH_ATTACHMENT, GL40.GL_RENDERBUFFER, depthRenderBufferID);

        // Set "renderedTexture" as our colour attachement #0
        GL40.glFramebufferTexture(GL40.GL_FRAMEBUFFER, GL40.GL_COLOR_ATTACHMENT0, textureID, 0);

        // Set the list of draw buffers.
        GL40.glDrawBuffers(new int[GL40.GL_COLOR_ATTACHMENT0]); // "1" is the size of DrawBuffers

        // Always check that our framebuffer is ok
        if(GL40.glCheckFramebufferStatus(GL40.GL_FRAMEBUFFER) != GL40.GL_FRAMEBUFFER_COMPLETE){
            error = true;
        }
    }

}
