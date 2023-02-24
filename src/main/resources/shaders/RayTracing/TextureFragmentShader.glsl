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


uvec4 R_STATE;

// Случайные числа
// --------------------------------------------------------------------------------------------------------------------

uint TausStep(uint z, int S1, int S2, int S3, uint M)
{
    uint b = (((z << S1) ^ z) >> S2);
    return (((z & M) << S3) ^ b);
}

uint LCGStep(uint z, uint A, uint C)
{
    return (A * z + C);
}

vec2 hash22(vec2 p)
{
    p += u_seed1.x;
    vec3 p3 = fract(vec3(p.xyx) * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yzx+33.33);
    return fract((p3.xx+p3.yz)*p3.zy);
}

float random()
{
    R_STATE.x = TausStep(R_STATE.x, 13, 19, 12, uint(4294967294));
    R_STATE.y = TausStep(R_STATE.y, 2, 25, 4, uint(4294967288));
    R_STATE.z = TausStep(R_STATE.z, 3, 11, 17, uint(4294967280));
    R_STATE.w = LCGStep(R_STATE.w, uint(1664525), uint(1013904223));
    return 2.3283064365387e-10 * float((R_STATE.x ^ R_STATE.y ^ R_STATE.z ^ R_STATE.w));
}

vec3 randomOnSphere() {
    vec3 rand = vec3(random(), random(), random());
    float theta = rand.x * 2.0 * 3.14159265;
    float v = rand.y;
    float phi = acos(2.0 * v - 1.0);
    float r = pow(rand.z, 1.0 / 3.0);
    float x = r * sin(phi) * cos(theta);
    float y = r * sin(phi) * sin(theta);
    float z = r * cos(phi);
    return vec3(x, y, z);
}

// --------------------------------------------------------------------------------------------------------------------


// Получение объекта с которым пересекся луч
// --------------------------------------------------------------------------------------------------------------------

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

    vec3 ocn = ((rotate(i)*vec4(ro, 1)).xyz - vec3(u_positions[3*i], u_positions[3*i+1], u_positions[3*i+2]))/ra;
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

vec2 rayCast(vec3 ro, vec3 rd, out vec3 n){
    vec4 dn = vec4(MAX_DIST + 1, 0, 0, 0);
    vec2 object = vec2(-1, dn.x);
    for (int i = 0; i < u_count_object; i++){
        switch (int(u_tags[i])){
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


// Получение цвета пикселя через rayTracing
// --------------------------------------------------------------------------------------------------------------------

// Получение цвета объекта
vec3 getMaterial(int i){
    if (i == -1) return vec3(1);
    return vec3(u_colors[3*i], u_colors[3*i+1], u_colors[3*i+2]);
}

// Получение цвета неба
vec3 getSky(vec3 rd){
    return u_background + u_background * pow(max(0, dot(rd, normalize(u_light_pos))), 64);
}

// Метод трассировки лучей
vec3 rayTrace(vec3 ro, vec3 rd){
    // Определения начального цвета пикселя как белого
    vec3 color = vec3(1);

    // Итерационные переменные начала и направления луча
    vec3 po = ro;
    vec3 pd = rd;

    // Инициализация переменной хранящей информацию о объекте с которым произошло пересечения
    vec2 object = vec2(-1, MAX_DIST + 1);
    // Инициализация вектора нормали
    vec3 n = vec3(0);
    // Инициализация максимльного числа переотражений луча
    int countReflect = 16;
    for(int i = 0; i < countReflect; i++){
        // Нахождение тега объект и рсстояния до точки пересечения методом Ray-casting
        object = rayCast(po, pd, n);

        // Если луч ушел в бесконечность, то возвращаем черный цвет
        if(object.x == -1){
            return vec3(0);
        }

        // Если объект в который попал луч имеет свойство светиться, то возвращаем в кчестве цвета
        // произведение цвета луча и цвета света источника
        if(u_glowing[int(object.x)] == 1){
            return color * getMaterial(int(object.x))*1.5;
        }

        // С каждым переотражением корректируем цвет умножением на цвет объекта с котрым пересекся луч
        color *= getMaterial(int(object.x));
        // Нахождение точки перечечения и установка её в качестве нового начала луча
        po = po + object.y * pd;

        // Инициализируем генерацию случайного единичного вектора и относительно него строим новое напрвление луча
        // путем взятия промежудочной стадий между вектором посностью слкчайным и идеально отраженным
        vec3 r = randomOnSphere();
        vec3 diffuse = normalize(r*dot(r, n));
        vec3 reflected = reflect(pd, n);
        // u_roughness определяет на сколько объект шероховат и как следствие на сколько близко нужно брыть к тому или иному варианту
        pd = mix(reflected, diffuse, u_roughness[int(object.x)]);
    }
    // Если количество переотражений не хватило для того чтобы попасть в объект, возвращаем черный цвет
    return vec3(0);
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

// Метод получения цвета
void render(inout vec3 color, in vec2 uv){
    // Устанвка позиций камеры полученной из основной программы
    vec3 ro = u_camera_pos;
    // Определение направления "взгляда" камеры
    vec3 lookAt = ro + normalize(u_camera_dir);
    // Создание луча и преобразование его в соответствий с направлением "взгляда" камеры
    vec3 rd = getCam(ro, lookAt) * normalize(vec3(uv, FOV));

    // Усреднение цвета пикслеля
    int countRay = 4;
    for (int i = 0; i < countRay; i++){
        color += rayTrace(ro, rd);
    }
    color /=countRay;
}

// --------------------------------------------------------------------------------------------------------------------


// Основной метод
// --------------------------------------------------------------------------------------------------------------------
void main(){
    // Преобразование относительных кординат окна OpenGl в "пиксельные" коррдинаты
    vec2 uv = (2.0 * gl_FragCoord.xy - u_resolution)/u_resolution.y;

    // Установка кофициэнтов для метода псевдослучайных чисел
    vec2 uvRes = hash22(uv + 1.0) * u_resolution + u_resolution;
    R_STATE.x = uint(u_seed1.x + uvRes.x);
    R_STATE.y = uint(u_seed1.y + uvRes.x);
    R_STATE.z = uint(u_seed2.x + uvRes.y);
    R_STATE.w = uint(u_seed2.y + uvRes.y);

    // Установка исходного цвета пикселя на белый
    vec3 color = vec3(0);
    // Получение цвета пикселя на текстуре предыдущего кадра
    vec3 textureColor = texture(textureSampler, pass_textureCoords).xyz;

    // Запуск метода рендеринга
    render(color, uv);

    // Кореекция гаммы для высветления изображения
    float white = 2.0;
    color *= white * 16.0;
    color = (color * (1.0 + color / white / white)) / (1.0 + color);

    // Смешение цветов с текущего и прошлого кадров
    color = mix(textureColor, color, u_sample_part);
    fragColor = vec4(color, 1);
}