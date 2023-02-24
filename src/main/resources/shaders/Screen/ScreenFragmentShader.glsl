#version 330 core

layout(location = 0) out vec4 fragColor;

in vec2 pass_textureCoords;

uniform sampler2D textureSampler;

void main(void){
    fragColor = texture(textureSampler, pass_textureCoords);
}