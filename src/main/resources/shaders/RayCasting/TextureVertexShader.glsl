// Версия ядра OpenGL
#version 330 core
// Координаты вершин модели
in vec3 position;
// Текстурные кооринаты
in vec2 textureCoords;

// Модифицированные текстурные кординаты
out vec2 pass_textureCoords;

void main(void){
    // Передача в ядро OpenGL координат вершин модели
    gl_Position = vec4(position, 1.0);
    // Модификация текстурных координат и  их передача в фрагментный шейдер
    pass_textureCoords = vec2(textureCoords.x, 1 - textureCoords.y);
}