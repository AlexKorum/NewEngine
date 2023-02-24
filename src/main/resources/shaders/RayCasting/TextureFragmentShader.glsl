#version 330 core

layout(location = 0) out vec4 fragColor;

in vec2 pass_textureCoords;

uniform sampler2D textureSampler;

// Константы для камеры
const float FOV = 1.0;

// Константы для луча
const int MAX_STEPS = 256;
const float MAX_DIST = 500;
const float EPSILON = 0.0001;

// Математические константы
const float PI = 3.14159265;

// Данные о размере экрана
uniform vec2 u_resolution;

// Цвет заднего фона
uniform vec3 u_background;

// Данные о объектах
const int MAX_OBJECTS = 256;
uniform float u_count_object;
uniform float[MAX_OBJECTS] u_tags;
uniform float[MAX_OBJECTS*3] u_positions;
uniform float[MAX_OBJECTS*3] u_rotations;
uniform float[MAX_OBJECTS*3] u_scales;
uniform float[MAX_OBJECTS*3] u_colors;
uniform float[MAX_OBJECTS] u_roughness;
uniform float[MAX_OBJECTS] u_glowing;

// Позиция и направление камеры
uniform vec3 u_camera_pos;
uniform vec3 u_camera_dir;
uniform float u_sample_part;

// Данные о свете и источниках света
uniform vec3 u_light_pos;

// Время с момента запуска программы
uniform float u_time;

// Случайные вектора
uniform vec2 u_seed1;
uniform vec2 u_seed2;


// Получение растяоний
// --------------------------------------------------------------------------------------------------------------------

// Матрица поворота объектов
// Матрица поворота объектов
mat4 rotate(int i){
    float cx = cos(-u_rotations[3*i]);
    float sx = sin(-u_rotations[3*i]);

    float cy = cos(-u_rotations[3*i+1]);
    float sy = sin(-u_rotations[3*i+1]);

    float cz = cos(-u_rotations[3*i+2]);
    float sz = sin(-u_rotations[3*i+2]);

    return mat4(
    vec4(1, 0, 0, 0),
    vec4(0, cx, -sx, 0),
    vec4(0, sx, cx, 0),
    vec4(0, 0, 0, 1)
    )
    *
    mat4(
    vec4(cy, 0, sy, 0),
    vec4(0, 1, 0, 0),
    vec4(-sy, 0, cy, 0),
    vec4(0, 0, 0, 1)
    )
    *
    mat4(
    vec4(cz, -sz, 0, 0),
    vec4(sz, cz, 0, 0),
    vec4(0, 0, 1, 0),
    vec4(0, 0, 0, 1)
    );
}

// Функций получения рстояния до объекта и нормали в точке пересечения
vec4 elipseIntersect(int i, vec3 ro, vec3 rd){
    vec3 ra = vec3(u_scales[3*i], u_scales[3*i+1], u_scales[3*i+2]);

    vec3 ocn = ((rotate(i)*vec4(ro  - vec3(u_positions[3*i], u_positions[3*i+1], u_positions[3*i+2]), 1)).xyz)/ra;
    vec3 rdn = (rotate(i)*vec4(rd, 1)).xyz/ra;
    float a = dot(rdn, rdn);
    float b = dot(ocn, rdn);
    float c = dot(ocn, ocn);
    float h = b*b - a*(c-1.0);
    if (h<0.0) return vec4(MAX_DIST+1, 0, 0, 0);
    h = sqrt(h);

    float dist = (-b-h)/a;
    vec3 p = ro - vec3(u_positions[3*i], u_positions[3*i+1], u_positions[3*i+2]) + dist*rd;
    vec3 n = normalize(p/(ra*ra));
    return vec4(dist, n);
}

vec4 cubeIntersect(int i, vec3 ro, vec3 rd){
    vec3 m = 1.0/(rotate(i)*vec4(rd, 1)).xyz;
    vec3 n = m*((rotate(i)*vec4(ro- vec3(u_positions[3*i], u_positions[3*i+1], u_positions[3*i+2]), 1)).xyz);
    vec3 k = abs(m)*vec3(u_scales[3*i], u_scales[3*i+1], u_scales[3*i+2]);
    vec3 t1 = -n - k;
    vec3 t2 = -n + k;
    float tN = max(max(t1.x, t1.y), t1.z);
    float tF = min(min(t2.x, t2.y), t2.z);
    if (tN > tF || tF < 0.0) return vec4(MAX_DIST+1, 0, 0, 0);

    vec3 oN = -sign((rotate(i)*vec4(rd, 1)).xyz) * step(t1.yzx, t1.xyz) * step(t1.zxy, t1.xyz);
    oN = (inverse(rotate(i)) * vec4(oN, 1)).xyz;
    return vec4(tN, oN);
}

vec4 planeIntersect(int i, vec3 ro, vec3 rd){
    vec4 n = rotate(i)*vec4(0, 1, 0, length(vec3(u_positions[3*i], u_positions[3*i+1], u_positions[3*i+2])));
    float dist = -(dot(ro, n.xyz)+n.w)/dot(rd, n.xyz);
    if (dist <= 0) return vec4(MAX_DIST+1, 0, 0, 0);
    return vec4(-(dot(ro, n.xyz)+n.w)/dot(rd, n.xyz), n.xyz);
}

// Получение первого пересечения луча
vec2 rayCast(vec3 ro, vec3 rd, out vec3 n){
    // Инициализация переменной ратояния и нормали
    vec4 dn = vec4(MAX_DIST+1, 0, 0, 0);
    // Инициализация возвращаемой переменной
    vec2 object = vec2(-1, dn.x);
    for (int i = 0; i < u_count_object; i++){
        switch (int(u_tags[i])){
            // Элипс
            case 0:
            dn = elipseIntersect(i, ro, rd);
            break;
            // Куб
            case 1:
            dn = cubeIntersect(i, ro, rd);
            break;
            // Плоскость
            case 2:
            dn = planeIntersect(i, ro, rd);
            break;
            // Тор
            default :
            dn = vec4(MAX_DIST + 1, 0, 0, 0);
            break;
        }
        if (object.y > dn.x && dn.x > 0){
            object.x = i;
            object.y = dn.x;
            n = dn.yzw;
        }
    }
    return object;
}

// --------------------------------------------------------------------------------------------------------------------


// Рендер
// --------------------------------------------------------------------------------------------------------------------

// Получение матрицы поворота камеры
mat3 getCam(vec3 ro, vec3 lookAt) {
    vec3 camF = normalize(vec3(lookAt - ro));
    vec3 camR = normalize(cross(vec3(0, 1, 0), camF));
    vec3 camU = cross(camF, camR);
    return mat3(camR, camU, camF);
}

// Получение цвета объекта
vec3 getMaterial(int i){
    return vec3(u_colors[3*i], u_colors[3*i+1], u_colors[3*i+2]);
}

// Получение цвета неба
vec3 getSun(vec3 rd){
    return vec3(1) * pow(max(0, dot(rd, normalize(u_light_pos))), 512);
}

// Получение цвет в точке
vec3 getLightFong(vec3 p, vec3 rd, vec3 n, vec3 color){
    vec3 L = normalize(u_light_pos - p);
    vec3 N = n;
    vec3 V = -rd;
    vec3 R = reflect(-L, N);

    vec3 specColor = vec3(0.5);
    vec3 specular = specColor * pow(clamp(dot(R, V), 0.0, 1.0), 10);
    vec3 diffuse = color * clamp(dot(L, N), 0.0, 1.0);
    vec3 ambient = color * 0.05;
    vec3 frenel = 0.25 * color * pow(1+dot(rd, N), 3);

    vec3 col = vec3(0);

    float d = rayCast(p + N*0.002, normalize(u_light_pos - p), n).y;
    if (0 < d && d < length(u_light_pos - p)) return ambient + frenel;
    return specular + diffuse + ambient + frenel;
}

// Получение цвета пикселя
void render(inout vec3 color, in vec2 uv){
    // Получение позиций камеры и направления "взгляда"
    vec3 ro = u_camera_pos;
    vec3 lookAt = ro + normalize(u_camera_dir);
    vec3 rd = getCam(ro, lookAt)*normalize(vec3(uv, FOV));

    // Инициализация вектора нормали
    vec3 n = vec3(0);
    // Получение тега объекта и растояния до точки пересечения
    vec2 object = rayCast(ro, rd, n);

    // Если точка пересечения находится по направлению просмотра и растояние до нее не больше максимальной дистанций
    if (object.y > 0 && object.y < MAX_DIST){
        // Получение точки пересечения
        vec3 p = ro + object.y * rd;
        // Инициализация цвета объекта (объект полностью ожного цвета)
        vec3 material = getMaterial(int(object.x));
        // Модификация цвета материала путем моделирования освещения и возврат результирующего цвета пикселя
        color += getLightFong(p, rd, n, material);
        // Создание эфекта тумана на краю дистанций отрисовки
        color = mix(color, u_background, 1.0 - exp(-0.00008 * object.y * object.y));
    } else {
        // Покраска пикселя в цвет заднего фона и добавление на небо Солнца
        color += u_background + getSun(rd);
    }
}

// --------------------------------------------------------------------------------------------------------------------

void main(){
    // Преобразование относительных кординат окна OpenGl в "пиксельные" коррдинаты
    vec2 uv = (2.0 * gl_FragCoord.xy - u_resolution)/u_resolution.y;

    // Установка исходного цвета пикселя на черный
    vec3 color = vec3(0);
    // Получение цвета пикселя на текстуре предыдущего кадра
    vec3 textureColor = texture(textureSampler, pass_textureCoords).xyz;

    // Метод рендеринга (для каждого вершинного метода свой)
    render(color, uv);

    // Гамма коррекция цвета
    color = pow(color, vec3(0.4545));
    // Установка цвета пикселя для текущего кадра
    fragColor = vec4(color, 1);
}