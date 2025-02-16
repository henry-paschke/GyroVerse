precision highp float;

attribute vec2 a_coord;
attribute vec2 a_texCoord;

uniform mat4 u_proj;

varying vec2 v_texCoord;

void main() {
        v_texCoord = a_texCoord;
        gl_Position = vec4(a_coord, 0.0, 1.0) * u_proj;
}