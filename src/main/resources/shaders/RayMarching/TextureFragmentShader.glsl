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



// Дополнительные функций
// --------------------------------------------------------------------------------------------------------------------

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


// --------------------------------------------------------------------------------------------------------------------


// Получение растояний до объектов
// --------------------------------------------------------------------------------------------------------------------

// SDF
float sdfSphere(int i, vec3 p){
    return length(p) - 1;
}

float sdfCube(int i, vec3 p){
    vec3 q = abs(p)-1;
    return length(max(vec3(0), q)) + min(max(q.x, max(q.y, q.z)), 0);
}

float sdfPlane(int i, vec3 p){
    vec3 n = vec3(0, 1, 0);
    return dot(p, n);
}


vec2 map(vec3 p){
    vec2 object = vec2(-1, MAX_DIST + 1);
    float dist;

    vec3 sp;
    float minsp;
    for (int i = 0; i < u_count_object; i++){
        sp = vec3(u_scales[3*i], u_scales[3*i+1], u_scales[3*i+2]);
        minsp = min(u_scales[3*i], min(u_scales[3*i+1], u_scales[3*i+2]));

        vec3 point = (rotate(i) * vec4(p - vec3(u_positions[3*i], u_positions[3*i+1], u_positions[3*i+2]), 1)).xyz;
        switch (int(u_tags[i])){
            case 0:// Тег сферы
            dist = sdfSphere(i, point/sp);
            break;
            case 1:
            dist = sdfCube(i, point/sp);
            break;
            case 2:
            dist = sdfPlane(i, point/sp);
            break;
            default :
            dist = MAX_DIST + 1;
            break;
        }
        dist = dist*minsp;

        if (object.y > dist){
            object = vec2(i, dist);
        }
    }

    return object;
}


// Алгоритм получения точки пересечения
vec2 rayMarch(vec3 ro, vec3 rd){
    // Объявление точки марша
    vec3 p;
    // Переменная пулучающая информаций о ближайшем на итераций объекте(тег) и растояние до него
    vec2 hit = vec2(0);
    // Пременная хранящая информаций о ближайшем на итераций объекте(тег) и растояние от начала луча до точки марша
    vec2 object = vec2(-1, 0);
    for(int i=0; i< MAX_STEPS; i++){
        // Перемещение точки марша вдоль направления луча
        p = ro + object.y * rd;
        // Получение расстояния до ближайшего к точке марша объекта и растояние до него
        hit = map(p);
        // Изменение тега объекта
        object.x = hit.x;
        // Получение расстояние от точки марша до начала уча
        object.y += hit.y;
        // Условие остановки цикла
        if(abs(hit.y) < EPSILON || object.y > MAX_DIST) break;
    }
    return object;
}

// --------------------------------------------------------------------------------------------------------------------


// Рендер
// --------------------------------------------------------------------------------------------------------------------

vec3 getNormal(vec3 p){
    vec2 e = vec2(EPSILON, 0.0);
    vec3 n = vec3(map(p).y) - vec3(map(p - e.xyy).y, map(p - e.yxy).y, map(p - e.yyx).y);
    return normalize(n);
}

// Освещение по Фонгу
vec3 getLightFong(vec3 p, vec3 rd, vec3 color){
    // Получеие необходимых в расчетах пересенных
    vec3 L = normalize(u_light_pos - p);
    vec3 N = getNormal(p);
    vec3 V = -rd;
    vec3 R = reflect(-L, N);

    // Коэффицент интенсивности светового потока
    vec3 specColor = vec3(0.5);
    // Надбавка освещенности от зеркального освещения
    vec3 specular = specColor * pow(clamp(dot(R, V), 0.0, 1.0), 10.0);
    // Надбавка освещенности от рассеяного освещения
    vec3 diffuse = color * clamp(dot(L, N), 0.0, 1.0);
    // Надбавка освещенности от фонового освещения
    vec3 ambient = color * 0.05;

    // Получение теней
    float d = rayMarch(p + N * 0.02, normalize(u_light_pos)).y;
    if (d < length(u_light_pos - p)) return ambient;

    // Цвет пикселя с моделью освещения по Фонгу
    return diffuse + ambient + specular;
}

vec3 getMaterial(int i){
    return vec3(u_colors[3*i], u_colors[3*i+1], u_colors[3*i+2]);
}

mat3 getCam(vec3 ro, vec3 lookAt) {
    vec3 camF = normalize(vec3(lookAt - ro));
    vec3 camR = normalize(cross(vec3(0, 1, 0), camF));
    vec3 camU = cross(camF, camR);
    return mat3(camR, camU, camF);
}

vec3 getSky(vec3 rd){
    return u_background * pow(max(0, dot(rd, normalize(u_light_pos))), 256);
}
// Получение цвета пикселя
void render(inout vec3 color, in vec2 uv){
    vec3 ro = u_camera_pos;
    vec3 lookAt = ro + normalize(u_camera_dir);
    vec3 rd = getCam(ro, lookAt)*normalize(vec3(uv, FOV));

    vec2 object = rayMarch(ro, rd);

    if (object.y < MAX_DIST){
        vec3 p = ro + object.y * rd;
        vec3 material = getMaterial(int(object.x));
        color += getLightFong(p, rd, material);
//        color += material;
        color = mix(color, u_background, 1.0 - exp(-0.00008 * object.y * object.y));
    } else {
        color += u_background;
    }
}

// --------------------------------------------------------------------------------------------------------------------


void main(){
    vec2 uv = (2.0 * gl_FragCoord.xy - u_resolution)/u_resolution.y;

    vec3 color = vec3(0);
    vec3 textureColor = texture(textureSampler, pass_textureCoords).xyz;

    render(color, uv);

    color = pow(color, vec3(0.4545));
    fragColor = vec4(color, 1);
}