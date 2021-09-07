package com.jonmarx.util;

//import com.jonmarx.game.Chunk;

public class StripGenerator {
    /*private static int stoneheight = 14;
    private static int grassheight = 2;
    private static int peak = 36;
    
    // simple terrain generation, not too complicated
    public static Chunk[] genStrip(int x, int y) {
        Chunk[] out = {new Chunk(x,0,y), new Chunk(x,1,y), new Chunk(x,2,y), new Chunk(x,3,y)};
        defaultGen(out);
        
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                double noise = ImprovedNoise.noise((x*16+i)/16f, 0, (y*16+j)/16f);
                if(noise >= 0.45) {
                    // snow mountain
                    int height = (int) (noise * peak/1.2f);
                    for(int s = stoneheight; s < stoneheight+grassheight + height; s++) {
                        getChunk(out, s).setBlock(i, s%16, j, (short)1);
                    }
                    int s = stoneheight+grassheight + height;
                    getChunk(out, s).setBlock(i, (s)%16, j, (short)5);
                    getChunk(out, s).setBlock(i, (s+1)%16, j, (short)5);
                } else if(noise >= 0.05) {
                    // stone mountain
                    int height = (int) (noise * peak);
                    for(int s = stoneheight; s < stoneheight+grassheight + height; s++) {
                        getChunk(out, s).setBlock(i, s%16, j, (short)1);
                    }
                } else if(noise >= -0.4) {
                    // grass hills
                    int height = (int) (noise * peak);
                    for(int s = stoneheight+grassheight + height; s > stoneheight; s--) {
                        if(noise > 0) getChunk(out, s).setBlock(i, s%16, j, (short)2);
                        else {
                            getChunk(out, s-1).setBlock(i, (s-1)%16, j, (short)2);
                            getChunk(out, s).setBlock(i, s%16, j, (short)0); 
                        }
                    }
                } else if(noise >= -0.5) {
                    // sand
                    getChunk(out, stoneheight+grassheight-1).setBlock(i, (stoneheight+grassheight-1)%16, j, (short)4);
                    getChunk(out, stoneheight+grassheight-2).setBlock(i, (stoneheight+grassheight-2)%16, j, (short)4);
                    if(noise >= -0.41) {
                        getChunk(out, stoneheight+grassheight).setBlock(i, (stoneheight+grassheight)%16, j, (short)4);
                    }
                } else {
                    // water
                    int height = (int) (noise * 4);
                    for(int w = stoneheight+grassheight; w > stoneheight+grassheight + height; w--) {
                        getChunk(out, w-1).setBlock(i, (w-1)%16, j, (short)3);
                    }
                    getChunk(out, stoneheight+grassheight + height-1).setBlock(i, (stoneheight+grassheight+height-1) % 16, j, (short) 4);
                }
            }
        }
        return out;
    }
    
    // generates stone and dirt
    private static void defaultGen(Chunk[] strip) {
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                for(int height = 0; height < stoneheight; height++) {
                    getChunk(strip, height).setBlock(i, height%16, j, (short)1);
                }
            }
        }
        
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                for(int height = stoneheight; height < stoneheight+grassheight; height++) {
                    getChunk(strip, height).setBlock(i, height%16, j, (short)2);
                }
            }
        }
    }
    
    // convient method to not write into a place like (17,16,16)
    private static Chunk getChunk(Chunk[] chunks, int height) {
        return chunks[height/16];
    }*/
}
