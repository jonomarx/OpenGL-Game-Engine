#version 330 core

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 texPos0;

out vec2 texPos;

void main() {
    gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
    texPos = texPos0;
}
