precision highp float;

uniform int planetID;
uniform bool flip;

varying vec2 v_texCoord;


void main()
{
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = v_texCoord;

    vec2 distFromCenter = uv - vec2(0.5, 0.5);


    vec3 ringCol = vec3(1.0, 1.0, 1.0); // Ring color (white for simplicity)
    vec2 ringScale = vec2(0.2, 1.0);   // Adjust the scale of the rings

    // Compute the length of the distance from the center, adjusted by the ring scale
    float len = length(distFromCenter * ringScale);

    float ringSize = 0.03;         // Ring thickness
    float ringDistance = 0.07;     // Distance from the center to the ring

    vec3 ringCol1 = vec3(1.0,0.0, 0.0);
    vec3 ringCol2 = vec3(0.0,1.0, 0.0);
    float ringCount = 3.0;
    float ringThreshold = -0.3;

    if (planetID == 4) {
        ringCol1 = vec3(1.0,1.0, 1.0);
        ringCol2 = vec3(0.14,0.14, 0.14);
    } else if (planetID == 6) {
        ringCount = 1.0;
        ringCol1 = vec3(1,0.992, 0.62);
        ringCol2 = vec3(0.435, 0.816, 0.871);
    }



    if (sin(len * 200.0 * ringCount) > ringThreshold) ringCol = ringCol1;
    else ringCol = ringCol2;

    // Check if the point is on the ring
    float onRing = step(ringDistance, len) * step(len, ringDistance + ringSize);

    onRing *=  flip ? step(0.5, uv.y) : 1.0 - step(0.5, uv.y);
    // Time varying pixel color
    vec4 col = mix(vec4(0.0, 0.0, 0.0, 0.0), vec4(ringCol, 1.0), onRing);

    // Output to screen
    gl_FragColor = col;
}