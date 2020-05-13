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