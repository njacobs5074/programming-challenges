package raytracer

import java.io.{ File, PrintWriter }

class PPMFileWriter(val file: File, imageWidth: Int, imageHeight: Int):
  def this(filename: String, imageWidth: Int, imageHeight: Int) =
    this(new File(filename), imageWidth, imageHeight)

  private val pw: PrintWriter = new PrintWriter(file)

  pw.println(s"P3\n$imageWidth $imageHeight\n255")

  def writeColor(pixel: Color, samplesPerPixel: Int): Unit = {
    def clamp(x: Double, min: Double, max: Double) =
      if (x < min) min
      else if (x > max) max
      else x

    val scale: Double = 1.0 / samplesPerPixel
    val r: Double = Math.sqrt(pixel.x * scale)
    val g: Double = Math.sqrt(pixel.y * scale)
    val b: Double = Math.sqrt(pixel.z * scale)

    pw.println(s"""
                   |${(256 * clamp(r, 0.0, 0.999)).toInt}
                   |${(256 * clamp(g, 0.0, 0.999)).toInt}
                   |${(256 * clamp(b, 0.0, 0.999)).toInt}
                   |""".stripMargin.replace("\n", " "))

  }

  def close(): Unit = pw.close()
