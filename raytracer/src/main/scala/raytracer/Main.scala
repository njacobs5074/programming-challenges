package raytracer

import java.io.{ File, FileOutputStream, FileWriter, PrintStream, PrintWriter }
import java.time.{ Duration, LocalDateTime }
import java.util.Date
import scala.annotation.tailrec
import scala.util.Random

/**
 * @author nick
 * @since 2021/09/18
 */
object Main extends App:

  import raytracer.*

  // Image
  val aspectRatio = 16.0 / 9.0
  val imageWidth = 400
  val imageHeight = (imageWidth / aspectRatio).toInt
  val samplesPerPixel = 100

  // World
  val materialGround = new Lambertian(new Color(0.8, 0.8, 0.0))
  val materialCenter = new Lambertian(new Color(0.1, 0.2, 0.5))
  val materialLeft = new Dieletric(1.5)
  val materialRight = new Metal(new Color(0.8, 0.6, 0.2), 0.0)

  val world = new HittableList(Vector(
    new Sphere(new Point3(0.0, -100.5, -1.0), 100.0, materialGround),
    new Sphere(new Point3(0.0, 0.0, -1.0), 0.5, materialCenter),
    new Sphere(new Point3(-1.0, 0.0, -1.0), 0.5, materialLeft),
    new Sphere(new Point3(-1.0, 0.0, -1.0), -0.45, materialLeft),
    new Sphere(new Point3(1.0, 0.0, -1.0), 0.5, materialRight)
  ))

  // Camera
  val lookFrom = new Point3(3, 3, 2)
  val lookAt = new Point3(0, 0, -1)
  val vup = new Vec3(0, 1, 0)
  val distToFocus = (lookFrom - lookAt).length

  val cam = new Camera(lookFrom, lookAt, vup, 20, 2.0, distToFocus)

  val outfile = "camera-with-depth-of-field.ppm"
  println(s"Generating $outfile")
  val start = LocalDateTime.now()
  val file = new PrintWriter(new FileWriter(new File(outfile)))
  file.println(s"P3\n$imageWidth $imageHeight\n255")

  for j <- imageHeight - 1 to 0 by -1 do
    print(s"Scanline = ${j + 1} of $imageHeight...\r")
    for i <- 0 to imageWidth - 1 do
      val pixelColor: Color = Range(0, samplesPerPixel).foldLeft(new Color(0, 0, 0)) { case (color, _) =>
        val u = (i + rand.nextDouble()) / (imageWidth - 1)
        val v = (j + rand.nextDouble()) / (imageHeight - 1)
        val ray = cam.getRay(u, v)

        rayColor(ray, world) + color
      }
      file.writeColor(pixelColor, samplesPerPixel)

  file.close()

  val elapsedTime = Math.abs(Duration.between(start, LocalDateTime.now()).toMinutes)

  println(s"\nDone in $elapsedTime minutes")