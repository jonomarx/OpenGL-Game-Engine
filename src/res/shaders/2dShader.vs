#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal0;
layout (location = 2) in vec2 texCoords0;

out vec3 normal;
out vec2 texCoords;

void main() {
    gl_Position = vec4(position.x,position.y,position.z,1.0);
    normal = normal0;
    texCoords = texCoords0;
}