#version 330 core
precision lowp float;
in vec2 vTexCoord;
uniform sampler2D uTexture;

uniform vec2 screenSize;
uniform vec2 direction;    // 单轴模糊方向（如(1,0)或(0,1)）
uniform float radius;       // 模糊半径（像素单位）
uniform float sigma;        // 高斯分布参数
out vec4 fragColor;

const float PI = 3.1415927;

void main() {
    int iRadius = int(radius);
    float sigmaSq = sigma * sigma;
    float sum = 0.0;
    vec4 colorSum = vec4(0.0);
    for (int i = -iRadius; i <= iRadius; ++i) {
        float xOffset = float(i) * direction.x;
        float yOffset = float(i) * direction.y;
        float exponent = -(xOffset * xOffset + yOffset * yOffset) / (2.0 * sigmaSq);
        float weight = exp(exponent) / (sigma * sqrt(2.0 * PI));
        vec2 sampleCoord = vTexCoord + vec2(xOffset, yOffset) / screenSize;
        vec4 sampleColor = texture(uTexture, sampleCoord);
        sum += weight;
        colorSum += sampleColor * weight;
    }
    fragColor = colorSum / sum;
}