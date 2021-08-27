#version 330 core

out vec4 FragColor;

in vec3 normal;
in vec3 fragPos;
in vec2 texCoords;
in vec4 fragPosLightSpace;

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

float calcShadow(vec4 shadow);
float calcShadow(vec4 shadow) {
    vec3 projCoords = shadow.xyz / shadow.w;
    projCoords = projCoords * 0.5 + 0.5;
    float closestDepth = texture(shadowTex, projCoords.xy).r;
    float currentDepth = projCoords.z;
    vec3 lightDir = normalize(lightPos - fragPos);
    float bias = max(0.05 * (1.0 - dot(normal, lightDir)), 0.005);
    return (currentDepth - bias > closestDepth ? 1.0 : 0.0);
}

vec3 calcDirLight(DirLight light, vec3 normal, vec3 viewDir);
vec3 calcDirLight(DirLight light, vec3 normal, vec3 viewDir) {
    vec3 color = texture(material.diffuse, texCoords).rgb;
    vec3 normall = normalize(normal);
    // ambient
    vec3 ambient = dirLight.ambient;
    // diffuse
    vec3 lightDir = normalize(lightPos - fragPos);
    float diff = max(dot(dirLight.dir, normall), 0.0);
    vec3 diffuse = diff * dirLight.diffuse;
    // specular
    vec3 viewDirr = normalize(viewPos - fragPos);
    float spec = 0.0;
    vec3 halfwayDir = normalize(lightDir + viewDirr);
    spec = pow(max(dot(normall, halfwayDir), 0.0), material.shininess);
    vec3 specular = spec * dirLight.specular;
    // calculate shadow
    float shadow = calcShadow(fragPosLightSpace);
    if (textureSize(shadowTex, 0).x == 1) {
        shadow = 0;
    }
    vec3 lighting = (ambient + ((1.0-shadow)) * (diffuse + specular)) * color;
    return lighting;
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

vec3 calcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir);
vec3 calcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.position - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    // attenuation
    float distance    = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));    
    // combine results
    vec3 ambient  = light.ambient  * texture(material.diffuse, texCoords).xyz;
    vec3 diffuse  = light.diffuse  * diff * texture(material.diffuse, texCoords).xyz;
    vec3 specular = light.specular * spec * texture(material.specular, texCoords).xyz;
    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    return (ambient + diffuse + specular);
}

void main() {
    vec3 norm = normalize(normal);
    vec3 viewDir = normalize(viewPos - fragPos);
    
    vec3 result = calcDirLight(dirLight, norm, viewDir);
    
    for(int i = 0; i < NR_POINT_LIGHTS; i++) {
        vec3 outt = calcPointLight(pointLights[i], norm, fragPos, viewDir);
        //if(pointLights[i].enabled != 0) result += outt;
    }
    FragColor = vec4(result, 1.0);
    if (textureSize(shadowTex, 0).x == 1) {
        FragColor.rgb = pow(FragColor.rgb, vec3(1.0/2.2));
    }
}