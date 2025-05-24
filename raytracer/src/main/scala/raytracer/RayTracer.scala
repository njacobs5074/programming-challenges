package raytracer

import raytracer.io.PPMFileWriter

import java.io.File
import java.time.{ Duration, LocalDateTime }

class RayTracer(
  world: Hittable,
  outfile: String,
  imageWidth: Int,
  imageHeight: Int,
  samplesPerPixel: Int,
  camera: Camera
):

  def render(): Unit = {
    println(s"Generating $outfile")

    val start = LocalDateTime.now()
    val file = new PPMFileWriter(imageWidth, imageHeight, samplesPerPixel, File(outfile))

    for j <- imageHeight - 1 to 0 by -1 do
      print(s"Scanline: ${j + 1} of $imageHeight...\r")

      for i <- 0 until imageWidth do
        val pixelColor: Color = Range(0, samplesPerPixel).foldLeft(new Color(0, 0, 0)) {
          case (color, _) =>
            val u = (i + rand.nextDouble()) / (imageWidth - 1)
            val v = (j + rand.nextDouble()) / (imageHeight - 1)
            val ray = camera.getRay(u, v)

            rayColor(ray, world) + color
        }
        file.writeColor(pixelColor)

    file.close()

    val elapsedTime = Math.abs(Duration.between(start, LocalDateTime.now()).getSeconds)

    println(s"\nDone in $elapsedTime seconds")
  }
