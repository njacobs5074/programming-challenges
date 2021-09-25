import java.io.PrintWriter
import scala.util.Random

package object raytracer:

  type Point3 = raytracer.Vec3
  type Color = raytracer.Vec3

  val rand = new Random()

  def randomDouble(min: Double, max: Double): Double =
    min + (max - min) * rand.nextDouble()

  implicit class PrintWriterExt(out: PrintWriter):
    def writeColor(pixel: Color, samplesPerPixel: Int): Unit = {

      def clamp(x: Double, min: Double, max: Double) =
        if (x < min) min
        else if (x > max) max
        else x

      val scale: Double = 1.0 / samplesPerPixel
      val r: Double = Math.sqrt(pixel.x * scale)
      val g: Double = Math.sqrt(pixel.y * scale)
      val b: Double = Math.sqrt(pixel.z * scale)

      out.println(
        s"""
           |${(256 * clamp(r, 0.0, 0.999)).toInt}
           |${(256 * clamp(g, 0.0, 0.999)).toInt}
           |${(256 * clamp(b, 0.0, 0.999)).toInt}
           |""".stripMargin.replace("\n", " "))

    }

  implicit class DoubleExt(d: Double):
    def *(v: Vec3): Vec3 = v * d

    def squared: Double = d * d

  def hitSphere(center: Point3, radius: Double, r: Ray): Double = {
    import Vec3.*

    val oc: Vec3 = r.origin - center
    val a = r.direction.lengthSquared
    val halfB = oc.dot(r.direction)
    val c =  oc.lengthSquared - radius * radius
    val discrimant = halfB * halfB - a * c
    if (discrimant < 0) {
      -1.0
    } else {
      (-halfB - Math.sqrt(discrimant)) / a
    }
  }