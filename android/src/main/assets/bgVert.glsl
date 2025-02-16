attribute vec2 a_position;
varying vec2 v_texCoords; // UVs same as vertex position


void main() {
    v_texCoords = a_position;
    gl_Position = vec4(a_position, 0.0, 1.0);
}