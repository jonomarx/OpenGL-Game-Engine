#version 330 core

out vec4 FragColor;

in vec3 normal;
in vec2 texCoords;

uniform vec3 viewPos;
uniform vec3 lightPos;

uniform sampler2D shadowTex;

struct Material {
    sampler2D diffuse;
    sampler2D specular;
    float shininess;
}; 

uniform Material material;

struct DirLight {
    vec3 dir;
    
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};
uniform DirLight dirLight;

vec3 calcDirLight(DirLight light);
vec3 calcDirLight(DirLight light) {
    vec3 color = texture(material.diffuse, texCoords).rgb;
    return color;
}

struct PointLight {    
    vec3 position;
    
    float constant;
    float linear;
    float quadratic;  
    
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    
    int enabled;
};  
#define NR_POINT_LIGHTS 4
uniform PointLight pointLights[NR_POINT_LIGHTS];

void main() {
    vec3 norm = normalize(normal);
    
    vec3 result = calcDirLight(dirLight);
    
    FragColor = vec4(result, 1.0);
    if (textureSize(shadowTex, 0).x == 1) {
        FragColor.rgb = pow(FragColor.rgb, vec3(1.0/2.2));
    }
}