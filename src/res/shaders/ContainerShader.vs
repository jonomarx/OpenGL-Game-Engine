#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal0;
layout (location = 2) in vec2 texCoords0;
layout (location = 3) in vec4 jointWeights;
layout (location = 4) in ivec4 jointIndices;

out vec2 texCoords;
out vec3 fragPos;
out vec3 normal;
out vec4 fragPosLightSpace;

const int MAX_WEIGHTS = 4;
const int MAX_JOINTS = 100;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform mat4 lightSpaceMatrix;
uniform mat4 jointsMatrix[MAX_JOINTS];

out vec4 test;

void main() {
    vec4 totalPos = vec4(1.0);
    if(jointIndices != vec4(-1)) {
        for(int i = 0; i < MAX_WEIGHTS; i++) {
            if(jointIndices[i] == -1) continue;
            if(jointIndices[i] >= MAX_JOINTS) {
                totalPos = vec4(position, 1.0);
                break;
            }
            vec4 localPos = (jointsMatrix[jointIndices[i]]) * vec4(position, 1.0);
            totalPos += localPos * jointWeights[i];
        }
    } else {
        totalPos = vec4(position, 1.0);
    }
    mat4 viewModel = view * model;
    vec4 outt = projection * viewModel * totalPos;
    gl_Position = outt;
    test = vec4(outt.xyz / outt.w, 1.0);
    //gl_Position = test;
    
    fragPos = vec3(model * totalPos);
    texCoords = texCoords0;
    normal = normal0;
    fragPosLightSpace = lightSpaceMatrix * vec4(fragPos, 1.0);
} 



