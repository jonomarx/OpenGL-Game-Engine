#version 330 core

in vec2 texPos;
uniform sampler2D tex;

out vec4 FragColor;

uniform float gamma;

void main() {
    FragColor = texture(tex, texPos);
    FragColor.rgb = pow(FragColor.rgb, vec3(1.0/gamma));
}
