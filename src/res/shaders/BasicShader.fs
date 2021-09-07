#version 330 core

in vec2 texCoords;
uniform sampler2D texture;

out vec4 FragColor;

void main() {

    vec4 value = texture(texture, texCoords);
    FragColor = value;
}
