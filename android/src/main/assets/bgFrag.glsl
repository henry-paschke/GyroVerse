precision highp float;

uniform float iTime;
varying vec2 v_texCoords;

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

// Classic Perlin noise
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


void main()
{
    // Normalized pixel coordinates (from 0 to 1)
    //vec2 uv = fragCoord/iResolution.xy;
    //float aspectRatio = iResolution.x / iResolution.y;
    vec2 u_fragCoord = v_texCoords;//vec2(uv.x * aspectRatio, uv.y);


    float planetScale = 0.3;

    float noise1 = cnoise((u_fragCoord + iTime * vec2(0.06, 0.03))  * 3.0 * planetScale) * 0.24;
    float noise2 = cnoise((u_fragCoord + iTime * vec2(0.03, 0.01)) * 2.0 * vec2(0.1, 0.1) * 30.0 * planetScale) * 0.23;
    float noise3 = cnoise((u_fragCoord + iTime * vec2(0.01, 0.04)) * vec2(0.1, 0.3) * 50.0 * planetScale) * 0.32;
    float noise4 = cnoise((u_fragCoord + iTime * vec2(0.06, 0.01))  * 5.0 * planetScale) * 0.4;


    float bgNoise = noise1 + noise2 + noise3 + noise4;

    // Generate multiple layers of stars with varying parameters
    float starNoise1 = cnoise(u_fragCoord * 500.0 + iTime * vec2(0.6, 0.5)) > 0.94 ? 1.0 : 0.0;
    float starNoise2 = cnoise(u_fragCoord * 400.0 + iTime * vec2(-0.3, 0.2)) > 0.93 ? 1.0 : 0.0;
    float starNoise3 = cnoise(u_fragCoord * 700.0 + iTime * vec2(0.4, -0.1)) > 0.92 ? 1.0 : 0.0;
    float starNoise4 = cnoise(u_fragCoord * 300.0 + iTime * vec2(-0.7, 0.3)) > 0.95 ? 1.0 : 0.0;
    float starNoise5 = cnoise(u_fragCoord * 60.0 + iTime * vec2(0.2, 0.6)) > 0.94 ? 1.0 : 0.0;
    float starNoise6 = cnoise(u_fragCoord * 45.0 + iTime * vec2(-0.5, 0.4)) > 0.95 ? 1.0 : 0.0;
    float starNoise7 = cnoise(u_fragCoord * 35.0 + iTime * vec2(0.1, -0.2)) > 0.93 ? 1.0 : 0.0;
    float starNoise8 = cnoise(u_fragCoord * 80.0 + iTime * vec2(-0.4, 0.7)) > 0.96 ? 1.0 : 0.0;
    float starNoise9 = cnoise(u_fragCoord * 55.0 + iTime * vec2(0.3, -0.5)) > 0.93 ? 1.0 : 0.0;
    float starNoise10 = cnoise(u_fragCoord * 25.0 + iTime * vec2(-0.8, 0.1)) > 0.94 ? 1.0 : 0.0;

    // Star color blending
    vec3 bgCol1 = vec3(0.041, 0.08, 0.22);
    vec3 bgCol2 = vec3(0.0, 0.0, 0.0);
    vec3 col = mix(bgCol1, bgCol2, bgNoise);

    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise1);
    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise2);
    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise3);
    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise4);
    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise5);
    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise6);
    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise7);
    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise8);
    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise9);
    col = mix(col, vec3(1.0, 1.0, 1.0), starNoise10);

    vec2 lightPos = vec2(0,0);
    float lightAmount = length(lightPos - u_fragCoord);
    col = mix(col, vec3(1.0, 1.0, 0.0),  0.14 * lightAmount);


    // Output to screen
    gl_FragColor = vec4(col,1.0);
}