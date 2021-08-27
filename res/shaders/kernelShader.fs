#version 330 core

in vec2 texPos;
uniform sampler2D tex;

out vec4 FragColor;

const float offset = 1.0 / 300.0;

uniform float kernel[9];

void main() {
    vec2 offsets[9] = vec2[] (
        vec2(-offset, offset),
        vec2(0.0, offset),
        vec2(offset, offset),
        vec2(-offset, 0.0),
        vec2(0.0, 0.0),
        vec2(offset, 0.0),
        vec2(-offset, -offset),
        vec2(0.0, -offset),
        vec2(offset, -offset)
    );
    
    vec3 sampleTex[9];
    for(int i = 0; i < 9; i++) {
        sampleTex[i] = vec3(texture(tex, texPos + offsets[i]));
    }
    
    vec3 color = vec3(0.0);
    for(int i = 0; i < 9; i++) {
        color += sampleTex[i] * kernel[i];
    }
    
    FragColor = vec4(color, 1.0);
}