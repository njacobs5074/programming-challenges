package raytracer

import java.io.{ File, FileOutputStream, FileWriter, PrintStream, PrintWriter }
import java.time.{ Duration, LocalDateTime }
import java.util.Date
import scala.annotation.tailrec
import scala.util.Random

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

  val world = new HittableList(
    Vector(
      new Sphere(new Point3(0.0, -100.5, -1.0), 100.0, materialGround),
      new Sphere(new Point3(0.0, 0.0, -1.0), 0.5, materialCenter),
      new Sphere(new Point3(-1.0, 0.0, -1.0), 0.5, materialLeft),
      new Sphere(new Point3(-1.0, 0.0, -1.0), -0.45, materialLeft),
      new Sphere(new Point3(1.0, 0.0, -1.0), 0.5, materialRight)
    )
  )

  // Camera
  val lookFrom = new Point3(3, 3, 2)
  val lookAt = new Point3(0, 0, -1)
  val vup = new Vec3(0, 1, 0)
  val distToFocus = (lookFrom - lookAt).length

  val camera = new Camera(lookFrom, lookAt, vup, 20, 2.0, distToFocus)

  val rayTracer = new RayTracer(
    world,
    "camera-with-depth-of-field.ppm",
    imageWidth,
    imageHeight,
    samplesPerPixel,
    camera
  )

  rayTracer.render()
