# odisea-en-kotlin-native
Introducción a Kotlin Native y ejemplo de Interoperabilidad con la librería magick wand, escrita en C, para hacer comparación de screenshots

Charla remota , para https://www.meetup.com/gdg-santiago-chile/events/270466964/

# Kotlin native

* Kotlin/Native es una tecnología para compilar  
  código Kotlin a binarios nativos.  
  
  1. [LLVM](https://llvm.org/) backend para el compilador de Kotlin  
  2. Nativa implementación de la librería estandar de Kotlin
  
* ¿ Que es LLVM ?
  Para entender LLVM, tenemos que entender la clásica implementación de un compilador
  
  <img width=500 src="https://github.com/chinostroza/odisea-en-kotlin-native/raw/master/1.png" /> 

* 3 principales componentes  
  1. Frontend: Encargado de construir el Abstract Syntax Tree (AST),  
     para representar nuestro código fuente.
  2. Optimizer: Intenta mejorar el tiempo de ejecución del código.
  3. Backend o generador de código: Genera el código de maquina.
  
* Implicaciones de este diseño
