package raytracer

import java.io.{File, FileOutputStream, FileWriter, PrintStream, PrintWriter}
import java.time.{Duration, LocalDateTime}
import java.util.Date
import scala.annotation.tailrec
import scala.util.Random

object BookCover extends App:

  import raytracer.*

  def randomScene: HittableList = {

    // Generate random spheres to litter the scene with.
    val randomSpheres = Range(-11, 11)
      .map { a =>
        Range(-11, 11)
          .map { b =>
            val chooseMat = rand.nextDouble()
            val center = new Point3(a + 0.9 * rand.nextDouble(), 0.2, b + 0.9 * rand.nextDouble())

            if ((center - new Point3(4, 0.2, 0)).length > 0.9) {
              if (chooseMat < 0.8) {
                // diffuse
                val albedo: Color = Vec3.random * Vec3.random
                val sphereMaterial = new Lambertian(albedo)
                Some(new Sphere(center, 0.2, sphereMaterial))
              } else if (chooseMat < 0.95) {
                // metal
                val albedo: Color = Vec3.random(0.5, 1)
                val fuzz = rand.randomDouble(0, 0.5)
                val sphereMaterial = new Metal(albedo, fuzz)
                Some(new Sphere(center, 0.2, sphereMaterial))
              } else {
                // glass
                val sphereMaterial = new Dieletric(1.5)
                Some(new Sphere(center, 0.2, sphereMaterial))
              }
            } else {
              None
            }
          }
          .toVector
          .flatten
      }
      .toVector
      .flatten

    // Fixed spheres - These are always present in the scene.
    val fixedSpheres = Vector(
      new Sphere(new Point3(0, -1000, 0), 1000, new Lambertian(new Color(0.5, 0.5, 0.5))),
      new Sphere(new Point3(0, 1, 0), 1.0, new Dieletric(1.5)),
      new Sphere(new Point3(-4, 1, 0), 1.0, new Lambertian(new Color(0.4, 0.2, 0.1))),
      new Sphere(new Point3(4, 1, 0), 1.0, new Metal(new Color(0.7, 0.6, 0.5), 0.0))
    )

    // Combine all into a list of hittable objects
    new HittableList(randomSpheres.appendedAll(fixedSpheres))
  }

  // Image
  val aspectRatio = 3.0 / 2.0
  val imageWidth = 1200
  val imageHeight = (imageWidth / aspectRatio).toInt
  val samplesPerPixel = 500

  // World
  val world = randomScene

  // Camera
  val lookFrom = new Point3(13, 2, 3)
  val lookAt = new Point3(0, 0, 0)
  val vup = new Vec3(0, 1, 0)
  val distToFocus = 10.0
  val aperture = 0.1

  val cam = new Camera(
    lookFrom,
    lookAt,
    vup,
    verticalFieldOfView = 20.0,
    aperture,
    distToFocus,
    aspectRatio
  )

  val outfile = "book-cover.ppm"
  println(s"Generating $outfile")
  val start = LocalDateTime.now()
  val file = new PrintWriter(new FileWriter(new File(outfile)))
  file.println(s"P3\n$imageWidth $imageHeight\n255")

  for j <- imageHeight - 1 to 0 by -1 do
    print(s"Scanline = ${j + 1} of $imageHeight...\r")
    for i <- 0 to imageWidth - 1 do
      val pixelColor: Color = Range(0, samplesPerPixel).foldLeft(new Color(0, 0, 0)) {
        case (color, _) =>
          val u = (i + rand.nextDouble()) / (imageWidth - 1)
          val v = (j + rand.nextDouble()) / (imageHeight - 1)
          val ray = cam.getRay(u, v)

          rayColor(ray, world) + color
      }
      file.writeColor(pixelColor, samplesPerPixel)

  file.close()

  val elapsedTime = Math.abs(Duration.between(start, LocalDateTime.now()).toMinutes)

  println(s"\nDone in $elapsedTime minutes")