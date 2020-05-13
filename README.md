# Odisea en Kotlin Native
Introducción a Kotlin Native y ejemplo de Interoperabilidad con la librería magick wand, escrita en C, para hacer comparación de screenshots

Charla remota , para https://www.meetup.com/gdg-santiago-chile/events/270466964/


# La importancia de la interoperabilidad

* según [wikipedia](https://es.wikipedia.org/wiki/Interoperabilidad) : El Instituto de Ingenieros Eléctricos y Electrónicos (IEEE) define interoperabilidad como la habilidad de dos o más sistemas o componentes para intercambiar información y utilizar la información intercambiada

## La pega




Traspasar una tesis PostDoctoral , como plugin en Osirix, 

**Por que:**

los médicos($$$) usan esto bla bla (todos tienen mac)

weno se ve entretenido va ...  ( mm veo un riesgo, hay una manzana en la foto mmm , ya pero filo )

<img width=500 src="https://github.com/chinostroza/odisea-en-kotlin-native/raw/master/3.png" /> 



**Grave error la manzana era el problema fuck**

## Problemas :

 1. Osirix escrito en Objective C y por un médico
 2. Tesis PostDoctoral escrita en Python
 3. No sé Objective C :(
 4. Y no tengo un POSTDOC, en: 
    * Resonancia Magnética
    * Procesamiento avanzado de imágenes Volumétricas
    * Métodos de elementos finitos, para cuantificación de estructuras en 3 dimensiones.
    * Y muchossss algoritmos escritos en Python
    
    
    
    
 ## El proceso :
     
     1. Algoritmo , para extrar la aorta de una imagen 4D Flow, que sale de un resonador (40 min dentro del resonador, Yo)
        me pasaron un paper, lo implementé en -> Objective C (me demoré bastante)
     2. Generar un malla en 3D de la aorta -> C++ , me robé un generador de mallas de un proyecto open source (para médicos pobres y me salvo la vida)
     3. Suavizar la malla -> C++, me lo robé del mismo programa
     4. Aplicar algoritmos de métodos fínitos de la tesis -> C++
     5. Lectura de los datos generados -> C  (velocidad)
     6. Cálculo de los vectores de velocidad, del flujo sanguíneo -> C++
     
     * Funcionó , dentro de OsiriX , ahora todos los médicos lo pueden usar.... error eso va a pasar en 20 años más... :(
     
     
     * Por ahora necesitamos que le expliques a todo el equipo como reutilizar los componentes :(
     
     
     * Eso no va a pasar porque :
     
     * No van aprender Objective C, C++, C , saldría muy caro 
     
     
 ## Solución : 
     
     
     mmmm Eureka, todos saben Python
     
     PyObject :)
     
     Solución:
     
     Osirix  -> plugin Osirix -> Librería para 4D Flow  ->  PyObject (interoperabilidad con C y C++) -> Python (Científicos) 
     
     ahora todos los científicos pueden interactuar con Osirix, en Python :)
     
     ***Adios me largo :)***
     
# Kotlin native

* Kotlin/Native es una tecnología para compilar  
  código Kotlin a binarios nativos.  
  
  1. [LLVM](https://llvm.org/) backend para el compilador de Kotlin  
  2. Nativa implementación de la librería estandar de Kotlin
  
## 1. ¿ Que es LLVM ?

  Para entender LLVM, tenemos que entender la clásica implementación de un compilador
  
  <img width=640 src="https://github.com/chinostroza/odisea-en-kotlin-native/raw/master/1.png" /> 

### 1.1 3 principales componentes

  1. Frontend: Encargado de construir el Abstract Syntax Tree (AST),  
     para representar nuestro código fuente.
  2. Optimizer: Intenta mejorar el tiempo de ejecución del código.
  3. Backend o generador de código: Genera el código de maquina.
  
### 1.2 Ventajas de este diseño

  1. Cuando el compilador decide dar soporte a más de un lenguaje,   
  	es posible reutilizar el optimizador y arquitectura objetivo.
  
  2. Este diseño de 3 capas, permite más colaboración al ser desacoplado   
  	especialmente en el caso de que el compilador sea open source.
    
  3. Personas con diferentes habilidades pueden colaborar en el proyecto.
    
  <img width=500 src="https://github.com/chinostroza/odisea-en-kotlin-native/raw/master/2.png" />
  
### 1.3 Implementaciones exitosas 

    1. Java y .NET virtual machines
    2. Trasladar la entrada a C code
    3. GCC
    
    y los problemas :
    
    1. Mala performace cuando hay interoperabilidad con otros lenguajes que no tienen su modelo, como C
    2. Mala experiencia de debugging , compilación lenta, problemas con características no soportadas por C
    3. Gcc es Monolítico, no se pueden reutilizar piezas de éste, no es modular 
    
    
    
### 1.4 LLVM's Code Representation: LLVM IR

* El aspecto más importante del diseño de LLVM es ***Intermediate Representation (IR)***

* .ll file

```ll
define i32 @add1(i32 %a, i32 %b) {
entry:
  %tmp1 = add i32 %a, %b
  ret i32 %tmp1
}

define i32 @add2(i32 %a, i32 %b) {
entry:
  %tmp1 = icmp eq i32 %a, 0
  br i1 %tmp1, label %done, label %recurse

recurse:
  %tmp2 = sub i32 %a, 1
  %tmp3 = add i32 %b, 1
  %tmp4 = call i32 @add2(i32 %tmp2, i32 %tmp3)
  ret i32 %tmp4

done:
  ret i32 %b
}
```
* C code

```c
unsigned add1(unsigned a, unsigned b) {
  return a+b;
}

// Perhaps not the most efficient way to add two numbers.
unsigned add2(unsigned a, unsigned b) {
  if (a == 0) return b;
  return add2(a-1, b+1);
}
```

### 1.5 Usando Kotlin Native

1. Lo primero es crear un proyecto del tipo Kotlin Native en IntelliJ

2. Compilamos el código

3. ./build/bin/macos/releaseExecutable/odisea-kotlin-native.kexe

4. y deberiamos ver por consola *Hello, Kotlin/Native!*

5. la idea de la charla es poder ver como es la interoperabilidad con C

6. Para esto vamos a integrar de la librería MagickWand  
   del proyecto [https://imagemagick.org/](https://imagemagick.org/) 

7. Crear un archivo de definición, en la siguiente ruta ../nativeInterop/cinterop/Magickwand.def

8. Agregamos las siguientes opciones de compilación, obtenidas del siguiente comando:

```sh
pkg-config --cflags --libs MagickWand
```

```def
headers = MagickWand/MagickWand.h
headerFilter = MagickWand/*
compilerOpts.osx = -Xpreprocessor -fopenmp -DMAGICKCORE_HDRI_ENABLE=1 -DMAGICKCORE_QUANTUM_DEPTH=16 -Xpreprocessor -fopenmp -DMAGICKCORE_HDRI_ENABLE=1 -DMAGICKCORE_QUANTUM_DEPTH=16 -I/usr/local/Cellar/imagemagick/7.0.10-0/include/ImageMagick-7
linkerOpts.osx = -L/usr/local/Cellar/imagemagick/7.0.10-0/lib -lMagickWand-7.Q16HDRI -lMagickCore-7.Q16HDRI
```

9. Agregamos la configuración para el target objetivo , nuestro caso 'macos' en ../build.gradle

```groovy

...

kotlin {
    // For ARM, should be changed to iosArm32 or iosArm64
    // For Linux, should be changed to e.g. linuxX64
    // For MacOS, should be changed to e.g. macosX64
    // For Windows, should be changed to e.g. mingwX64
    macosX64("macos") {
        compilations.main {
            cinterops {
                Magickwand {
                    defFile project.file("src/nativeInterop/cinterop/Magickwand.def")
                    packageName 'sample'
                    compilerOpts '-I/path'
                    includeDirs.allHeaders("path")
                }
            }
        }
...
```

10. Procedemos a hacer el build del proyecto, para poder acceder los headers de MagickWand a través de kotlin

* Si no agrega las dependencias , procedemos a reimport el proyecto gradle

12. Procedemos a escribir nuestra aplicación, la idea es poder comparar 2 imágenes o ScreenShots </br>
    para esto creamos el siguiente archivo por ejemplo SampleMacos.kt`
    
* Acá tenemos un ejemplo en C

```c
#include <windows.h>
#include <wand/magick_wand.h>

void test_wand(void)
{
	MagickWand *img1 = NULL,*img2 = NULL,*m_wand = NULL;
	double distortion = 0.0;
	char info[128];

	MagickWandGenesis();
	img1 = NewMagickWand();
	img2 = NewMagickWand();
	MagickReadImage(img1, "rose.jpg");
	MagickReadImage(img2,"reconstruct.jpg");
	m_wand = MagickCompareImages(img1,img2,PeakSignalToNoiseRatioMetric,&distortion);

	sprintf(info,"%12.4f",distortion);
	MessageBox(NULL,info,"",MB_OK);

	/* Tidy up */
	m_wand = DestroyMagickWand(m_wand);
	img1 = DestroyMagickWand(img1);
	img2 = DestroyMagickWand(img2);
	MagickWandTerminus();
}
```

* Kotlin
    
```kotlin
package sample

import kotlinx.cinterop.*

fun hello(): String = "Hello, Kotlin/Native! and MagickWand :)"

fun main(args: Array<String>) {

    memScoped {

        //comenzando con el uso de magickwand
        MagickWandGenesis()

        val distortion = alloc<DoubleVar>()
        val alpha: CPointer<MagickWand>? = NewMagickWand()
        val beta: CPointer<MagickWand>? = NewMagickWand()

        MagickReadImage(alpha, args[0])
        MagickReadImage(beta, args[1])

        val result = MagickCompareImages(
            alpha,
            beta,
            MetricType.MeanSquaredErrorMetric,
            distortion.ptr
        )

        println(distortion.value.toDouble())
        MagickWriteImage(result, "result.jpg")
        DestroyMagickWand(result);
        DestroyMagickWand(alpha);
        DestroyMagickWand(beta);
        MagickWandTerminus();

        println(hello())

    }
}
```

13. Acá quiero detenerme un poco,

* C

```c
	MagickWand *img1 = NULL,*img2 = NULL,*m_wand = NULL;
	...
	m_wand = MagickCompareImages(img1,img2,PeakSignalToNoiseRatioMetric,&distortion);
	...
```
* Kotlin

```kotlin
	val distortion = alloc<DoubleVar>()
        val alpha: CPointer<MagickWand>? = NewMagickWand()
        val beta: CPointer<MagickWand>? = NewMagickWand()
	...
	val result = MagickCompareImages(
            alpha,
            beta,
            MetricType.MeanSquaredErrorMetric,
            distortion.ptr
        )
	...
```

Y eso PREGUNTAS ??
