package raytracer.io

import raytracer.Color

import java.io.{ File, FileWriter, PrintWriter }

class PPMFileWriter(
  imageWidth: Int,
  imageHeight: Int,
  samplesPerPixel: Int,
  printWriter: PrintWriter
) {
  def this(imageWidth: Int, imageHeight: Int, samplesPerPixel: Int, file: File) =
    this(imageWidth, imageHeight, samplesPerPixel, PrintWriter(FileWriter(file)))

  printWriter.println(s"P3\n$imageWidth $imageHeight\n255")

  def writeColor(pixel: Color): Unit = {
    def clamp(x: Double, min: Double, max: Double) =
      if (x < min) min
      else if (x > max) max
      else x

    val scale: Double = 1.0 / samplesPerPixel
    val r: Double = Math.sqrt(pixel.x * scale)
    val g: Double = Math.sqrt(pixel.y * scale)
    val b: Double = Math.sqrt(pixel.z * scale)

    printWriter.println(s"""
                   |${(256 * clamp(r, 0.0, 0.999)).toInt}
                   |${(256 * clamp(g, 0.0, 0.999)).toInt}
                   |${(256 * clamp(b, 0.0, 0.999)).toInt}
                   |""".stripMargin.replace("\n", " "))
  }

  def close() = printWriter.close()
}
