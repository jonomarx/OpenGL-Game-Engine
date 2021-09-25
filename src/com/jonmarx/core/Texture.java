/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL33C.*;

/**
 *
 * @author Jon
 */
public class Texture {
    protected int width;
    protected int height;
    protected int id;
    
    private static BufferedImage getImage(String tex) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Texture.class.getResourceAsStream(tex));
        } catch (Exception ex) {
            Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("looked at: " + tex);
        }
        if(image == null) {
            try {
                image = ImageIO.read(new FileInputStream(tex));
            } catch(IOException ex) {
                Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return image;
    }
    
    public Texture(String tex) {
        this(getImage(tex));
    }
    
    public Texture(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        BufferedImage imagei = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = imagei.getGraphics();
        g.drawImage(image, 0, 0, null);
        ByteBuffer decodedImage = convertImageData(imagei);
        
        // Create a new OpenGL texture 
        id = glGenTextures();
        Cleanup.addTexture(id);
        
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, id);
            

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_BGR, GL_UNSIGNED_BYTE, decodedImage);
        
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);
    }
    
    private ByteBuffer convertImageData(BufferedImage bufferedImage) {
        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;

        ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace
                .getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 },
                true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

        raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                bufferedImage.getWidth(), bufferedImage.getHeight(), 4, null);
        texImage = new BufferedImage(glAlphaColorModel, raster, true,
                new Hashtable());

        // copy the source image into the produced image
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0, 0, 256, 256);
        g.drawImage(bufferedImage, 0, 0, null);
        texImage = bufferedImage;

        // build a byte buffer from the temporary image
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
                .getData();
        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        ((Buffer)imageBuffer).flip();

        return imageBuffer;
    }
}
