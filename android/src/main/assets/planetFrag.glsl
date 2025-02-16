precision highp float;

uniform float iTime;
uniform int planetID;

varying vec2 v_texCoord;

vec4 mod289(vec4 x)
{
    return x - floor(x * (1.0 / 289.0)) * 289.0;
}

vec4 permute(vec4 x)
{
    return mod289(((x*34.0)+10.0)*x);
}

vec4 taylorInvSqrt(vec4 r)
{
    return 1.79284291400159 - 0.85373472095314 * r;
}

vec2 fade(vec2 t) {
    return t*t*t*(t*(t*6.0-15.0)+10.0);
}

float cnoise(vec2 P)
{
    vec4 Pi = floor(P.xyxy) + vec4(0.0, 0.0, 1.0, 1.0);
    vec4 Pf = fract(P.xyxy) - vec4(0.0, 0.0, 1.0, 1.0);
    Pi = mod289(Pi); // To avoid truncation effects in permutation
    vec4 ix = Pi.xzxz;
    vec4 iy = Pi.yyww;
    vec4 fx = Pf.xzxz;
    vec4 fy = Pf.yyww;

    vec4 i = permute(permute(ix) + iy);

    vec4 gx = fract(i * (1.0 / 41.0)) * 2.0 - 1.0 ;
    vec4 gy = abs(gx) - 0.5 ;
    vec4 tx = floor(gx + 0.5);
    gx = gx - tx;

    vec2 g00 = vec2(gx.x,gy.x);
    vec2 g10 = vec2(gx.y,gy.y);
    vec2 g01 = vec2(gx.z,gy.z);
    vec2 g11 = vec2(gx.w,gy.w);

    vec4 norm = taylorInvSqrt(vec4(dot(g00, g00), dot(g01, g01), dot(g10, g10), dot(g11, g11)));

    float n00 = norm.x * dot(g00, vec2(fx.x, fy.x));
    float n10 = norm.y * dot(g10, vec2(fx.y, fy.y));
    float n01 = norm.z * dot(g01, vec2(fx.z, fy.z));
    float n11 = norm.w * dot(g11, vec2(fx.w, fy.w));

    vec2 fade_xy = fade(Pf.xy);
    vec2 n_x = mix(vec2(n00, n01), vec2(n10, n11), fade_xy.x);
    float n_xy = mix(n_x.x, n_x.y, fade_xy.y);
    return 2.3 * n_xy;
}


float getCircleHeight(float x, float r) {
    return sqrt(max(0.0, r * r - x * x));
}


void main()
{
    // Normalized pixel coordinates (from 0 to 1)
//    vec2 uv = fragCoord/iResolution.xy;
//    float aspectRatio = iResolution.x / iResolution.y;
    vec2 u_fragCoord = v_texCoord;//vec2(uv.x * aspectRatio, uv.y);


    // 0 = Earth
    // 1 = Mars
    // 2 = Mercury


    vec2 circlePos = vec2(0.5, 0.5);
    float circleRadius = 0.495;
    float planetScale = 0.23;

    vec4 bgCol = vec4(1.0, 1.0, 1.0, 0.0);
    vec3 ocean = vec3(0.0, 0.2, 0.8);
    vec3 beach = vec3(0.9, 0.8, 0.6);
    vec3 grass = vec3(0.3, 0.6, 0.1);
    vec3 dirt = vec3(0.4, 0.17, 0.17);
    vec3 cloudColor1 = vec3(1.0, 1.0, 1.0);
    vec3 cloudColor2 = vec3(0.9, 1.0, 0.9);
    vec3 cloudColor3 = vec3(0.7, 0.8, 0.8);
    vec3 lightCol = vec3(0.9, 0.8, 0.5);
    vec3 brightLightCol = vec3(0.8, 0.8, 0.7);
    lightCol = vec3(1.0, 1.0, 1.0);
    brightLightCol = vec3(1.0, 1.0, 1.0);

    vec3 darkCol = vec3(0.0, 0.0, 0.0);

    vec2 lightPos = vec2(0.3, 0.3);


    //circlePos = vec2(1.0 + sin(iTime) * 0.5, 0.5 + cos(iTime + 0.1) * 0.1);
    //lightPos = -circlePos * 0.5;

    float seaLevel = 0.0; //where ocean goes to from -1 to 1
    float beachLevel = 0.1;
    float grassLevel = 0.3;
    float speed = 0.1; // Rotation speed
    float edgeScale = 0.1; // How much the edges of the panet are distorted, range 0 to 1
    float cloudAlpha = 0.8;

    float cloudLevel1 = -0.5;
    float cloudLevel2 =  -0.4;
    float cloudLevel3 =  -0.1;

    vec2 noiseScale = vec2(1.0, 1.0);


    if (planetID == 3) {

        ocean = vec3(0.759, 0.341, 0.274);
        beach = vec3(0.5549019607843137, 0.04, 0.07);
        grass = vec3(0.76, 0.45, 0.12);
        dirt = ocean;

        cloudLevel1 = -0.45;
        cloudLevel2 =  -0.3;
        cloudLevel3 =  -0.2;
        cloudAlpha = 0.4;

    }  // MARS
    else if (planetID == 0) {


        planetScale = 0.6;

        ocean = vec3(0.839, 0.839, 0.829);
        beach = vec3(0.039, 0.039, 0.49);
        grass = vec3(0.427, 0.427, 0.427);
        dirt = vec3(0.0, 0.8, 1);

        seaLevel = 0.0; //where ocean goes to from -1 to 1
        beachLevel = 0.1;
        grassLevel = 0.1;

        cloudLevel1 = -1.0;
        cloudLevel2 =  -1.0;
        cloudLevel3 =  -1.0;
    } // MERCURY
    else if (planetID == 1) {

        planetScale = 0.6;

        ocean = vec3(0.804, 0.278, 0.169);
        beach = vec3(0.5549019607843137, 0.04, 0.07);
        grass = vec3(0.098, 0.031, 0.035);
        grass = vec3(1, 0.651, 0);
        dirt = ocean;

        lightCol = vec3(1.0, 0.9, 0.5);
        brightLightCol = vec3(1.0, 0.8, 0.7);
        darkCol = vec3(0.0, 0.0, 0.0);

        seaLevel = 0.0; //where ocean goes to from -1 to 1
        beachLevel = 0.2;

        cloudAlpha = 0.0;

    } // VENUS
    else if (planetID == 5) {

        planetScale = 0.14;

        ocean = vec3(0.686, 0.651, 0.439);
        beach = vec3(0.447, 0.518, 0.486);
        grass = vec3(0.51, 0.435, 0.227);
        dirt = vec3(0.647, 0.569, 0.361);


        lightPos = vec2(0.2, 0);

        seaLevel = -0.3; //where ocean goes to from -1 to 1
        beachLevel = 0.1;
        grassLevel = 0.3;
        speed = 0.1; // Rotation speed
        edgeScale = 0.1; // How much the edges of the panet are distorted, range 0 to 1
        cloudAlpha = 0.0;

        noiseScale = vec2(0.3, 7.0);
    } // SATURN
    else if (planetID == 4) {

        planetScale = 0.34;

        ocean = vec3(0.961, 0.455, 0.071);
        beach = vec3(0.812, 0.537, 0.325);
        grass = vec3(0.839, 0.647, 0.502);
        dirt = vec3(0.961, 0.455, 0.071);


        lightPos = vec2(0.2, 0);

        seaLevel = -0.3; //where ocean goes to from -1 to 1
        beachLevel = 0.3;
        grassLevel = 0.3;
        speed = 0.1; // Rotation speed
        edgeScale = 0.1; // How much the edges of the panet are distorted, range 0 to 1
        cloudAlpha = 0.7;

        cloudColor1 = vec3(0.961, 0.455, 0.071);
        cloudColor2 = vec3(0.812, 0.537, 0.325);
        cloudColor3 = vec3(0.839, 0.647, 0.502);

        noiseScale = vec2(0.3, 7.0);
    } // JUPITER
    else if (planetID == 6) {
        planetScale = 0.17;

        ocean = vec3(0.035, 0.918, 1);
        beach = vec3(0.024, 0.435, 0.643);
        grass = vec3(0.035, 0.918, 1);
        dirt = vec3(0.024, 0.435, 0.643);


        lightPos = vec2(0.2, 0);

        seaLevel = -0.3; //where ocean goes to from -1 to 1
        beachLevel = 0.1;
        grassLevel = 0.3;
        speed = 0.1; // Rotation speed
        edgeScale = 0.1; // How much the edges of the panet are distorted, range 0 to 1
        cloudAlpha = 0.0;

        noiseScale = vec2(0.3, 5.0);
    } // URANUS
    else if (planetID == 7) {
        planetScale = 0.17;

        ocean = vec3(0.035, 0.918, 1);
        beach = vec3(0.024, 0.435, 0.643);
        grass = vec3(0.035, 0.918, 1);
        dirt = vec3(0.008, 0.298, 0.545);


        lightPos = vec2(0.2, 0);

        seaLevel = -0.3; //where ocean goes to from -1 to 1
        beachLevel = 0.1;
        grassLevel = 0.4;
        speed = 0.1; // Rotation speed
        edgeScale = 0.1; // How much the edges of the panet are distorted, range 0 to 1
        cloudAlpha = 1.0;

        cloudColor1 = vec3(0.035, 0.918, 1);
        cloudColor2 = vec3(0.008, 0.298, 0.545);
        cloudColor3 = vec3(0.035, 0.918, 1);

        noiseScale = vec2(0.3, 5.0);
    } // NEPTUNE


    vec2 circleCoords = u_fragCoord - circlePos;

    float offset = iTime * speed + float(planetID);
    float planetHeight = getCircleHeight(circleCoords.x, circleRadius);
    vec2 cUV = vec2(circleCoords.x + offset, circleCoords.y / planetHeight + edgeScale);
    vec2 cFrag = vec2(circleCoords.x, circleCoords.y / planetHeight + edgeScale);
    vec2 cUVcloud1 = vec2(circleCoords.x + offset * 1.5, circleCoords.y / planetHeight + edgeScale);
    vec2 cUVcloud2 = vec2(circleCoords.x + offset * -1.3, circleCoords.y / planetHeight + edgeScale);
    float onSphere = step(abs(circleCoords.y), planetHeight);

    float noise1 = cnoise(cUV * noiseScale * 10.0 * planetScale) * 0.24;
    float noise2 = cnoise(cUV * noiseScale * 30.0 * planetScale) * 0.23;
    float noise3 = cnoise(cUV * noiseScale * 50.0 * planetScale) * 0.12;
    float noise4 = cnoise(cUV * noiseScale * 5.0 * planetScale) * 0.4;
    float noise5 = cnoise(cUV * noiseScale * 10.0 * planetScale);

    float cloudNoise1 = cnoise(cUVcloud1 * 10.0 * planetScale) * 0.5;
    float cloudNoise2 = cnoise(cUVcloud2 * 30.0 * planetScale) * 0.5;


    float surfaceNoise = noise1 + noise2 + noise3 + noise4;


    vec3 planetCol;
    if (surfaceNoise < seaLevel) planetCol = ocean;
    else if (surfaceNoise < seaLevel + beachLevel) planetCol = beach;
    else if (surfaceNoise < seaLevel + beachLevel + grassLevel) planetCol = grass;
    else if (noise5 > 0.0) planetCol = dirt;


    // Add clouds
    float cloudNoise = cloudNoise1 + cloudNoise2;

    vec3 cloudCol;
    if (cloudNoise < cloudLevel1) cloudCol = cloudColor1;
    else if (cloudNoise < cloudLevel2) cloudCol = cloudColor2;
    else if (cloudNoise < cloudLevel3) cloudCol = cloudColor3;
    else {
        cloudCol = vec3(1.0, 1.0, 1.0);
        cloudAlpha = 0.0;
    }

    planetCol = mix(planetCol, cloudCol, cloudAlpha);


    // Add shadow
    planetCol = mix(planetCol, darkCol,  length(circleCoords - lightPos));
    planetCol = mix(planetCol, lightCol, 0.1 * (1.0 - 1.3 * length(circleCoords - lightPos)));
    planetCol = mix(planetCol, lightCol, 0.01 * (1.0 - 0.1 * length(circleCoords - lightPos)));

    float outlineSize = 0.01;
    vec3 outlineCol = vec3(0.0, 0.0, 0.0);
    float outline = step(circleRadius - outlineSize, length(circleCoords));


    //bgCol = getBG(u_fragCoord);

    vec4 col = mix(bgCol, vec4(mix(planetCol, outlineCol, outline), 1.0), onSphere);

    // Output to screen
    gl_FragColor = col;
}