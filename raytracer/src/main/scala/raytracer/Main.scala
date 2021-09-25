package raytracer

import java.io.{ File, FileOutputStream, FileWriter, PrintStream, PrintWriter }
import scala.annotation.tailrec
import scala.util.Random

/**
 * @author nick
 * @since 2021/09/18
 */
object Main extends App:

  import raytracer.*

  def rayColor(r: Ray) = {
    val t = hitSphere(new Point3(0, 0, -1), 0.5, r)
    if (t > 0.0) {
      val N = (r.at(t) - new Vec3(0, 0, -1)).unitVector
      0.5 * new Color(N.x + 1, N.y + 1, N.z + 1)
    } else {
      val unitDirection = r.direction.unitVector
      val t = 0.5 * (unitDirection.y + 1.0)
      (1.0 - t) * new Color(1.0, 1.0, 1.0) + t * new Color(0.5, 0.7, 1.0)
    }
  }

  def rayColor(ray: Ray, world: Hittable, depth: Int): Color = {
    if (depth <= 0) {
      new Color(0, 0, 0)
    } else {
      world.hit(ray, 0.001, Double.PositiveInfinity).map { rec =>
        rec.material.scatter(ray, rec).map { scattered =>
          scattered.attentuation * rayColor(scattered.ray, world, depth - 1)
        }.getOrElse(new Color(0, 0, 0))
      }.getOrElse {
        val unitDirection: Point3 = ray.direction.unitVector
        val t: Double = 0.5 * (unitDirection.y + 1.0)
        (1.0 - t) * new Color(1.0, 1.0, 1.0) + t * new Color(0.5, 0.7, 1.0)
      }
    }
  }


  // Image
  val aspectRatio = 16.0 / 9.0
  val imageWidth = 400
  val imageHeight = (imageWidth / aspectRatio).toInt
  val samplesPerPixel = 100
  val maxDepth = 50

  // World
  val materialGround = new Lambertian(new Color(0.8, 0.8, 0.0))
  val materialCenter = new Lambertian(new Color(0.1, 0.2, 0.5))
  val materialLeft = new Dieletric(1.5)
  val materialRight = new Metal(new Color(0.8, 0.6, 0.2), 0.0)
  val world = new HittableList(Vector(
    new Sphere(new Point3(0.0, -100.5, -1.0), 100.0, materialGround),
    new Sphere(new Point3(0.0, 0.0, -1.0), 0.5, materialCenter),
    new Sphere(new Point3(-1.0, 0.0, -1.0), 0.5, materialLeft),
    new Sphere(new Point3(-1.0, 0.0, -1.0), -0.4, materialLeft),
    new Sphere(new Point3(1.0, 0.0, -1.0), 0.5, materialRight),
  ))

  // Camera
  val cam = new Camera()

  val rand = new Random()
  val outfile = "hollow-glass-sphere.ppm"
  println(s"Generating $outfile")
  val file = new PrintWriter(new FileWriter(new File(outfile)))
  file.println(s"P3\n$imageWidth $imageHeight\n255")

  for j <- imageHeight - 1 to 0 by -1 do
    print(s"Scanline = $j...\r")
    for i <- 0 to imageWidth - 1 do
      val pixelColor: Color = Range(0, samplesPerPixel).foldLeft(new Color(0, 0, 0)) { case (color, _) =>
        val u = (i + rand.nextDouble()) / (imageWidth - 1)
        val v = (j + rand.nextDouble()) / (imageHeight - 1)
        val ray = cam.getRay(u, v)

        rayColor(ray, world, maxDepth) + color
      }
      file.writeColor(pixelColor, samplesPerPixel)

  file.close()

  println("\nDone")