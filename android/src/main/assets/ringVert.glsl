precision highp float;

attribute vec2 a_coord;
attribute vec2 a_texCoord;

uniform mat4 u_proj;
uniform mat4 u_model;

varying vec2 v_texCoord;

void main() {
    v_texCoord = a_texCoord;
    gl_Position = u_proj * u_model  * vec4(a_coord, 0.0, 1.0);
}