# Odisea en Kotlin Native
Introducción a Kotlin Native y ejemplo de Interoperabilidad con la librería magick wand, escrita en C, para hacer comparación de screenshots

Charla remota , para https://www.meetup.com/gdg-santiago-chile/events/270466964/

# Kotlin native

* Kotlin/Native es una tecnología para compilar  
  código Kotlin a binarios nativos.  
  
  1. [LLVM](https://llvm.org/) backend para el compilador de Kotlin  
  2. Nativa implementación de la librería estandar de Kotlin
  
## 1. ¿ Que es LLVM ?

  Para entender LLVM, tenemos que entender la clásica implementación de un compilador
  
  <img width=500 src="https://github.com/chinostroza/odisea-en-kotlin-native/raw/master/1.png" /> 

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

//TODO falta agregar los problemas de estas implementaciones
// y dar la entrada al por que de LLVM

	1. Java y .NET virtual machines
    2. Trasladar la entrada a C code
    3. GCC
    
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

8. Agregamos las siguientes definiciones

```def
headers = MagickWand/MagickWand.h
headerFilter = MagickWand/*
compilerOpts.osx = -Xpreprocessor -fopenmp -DMAGICKCORE_HDRI_ENABLE=1 -DMAGICKCORE_QUANTUM_DEPTH=16 -Xpreprocessor -fopenmp -DMAGICKCORE_HDRI_ENABLE=1 -DMAGICKCORE_QUANTUM_DEPTH=16 -I/usr/local/Cellar/imagemagick/7.0.10-0/include/ImageMagick-7
linkerOpts.osx = -L/usr/local/Cellar/imagemagick/7.0.10-0/lib -lMagickWand-7.Q16HDRI -lMagickCore-7.Q16HDRI
```
