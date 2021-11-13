package raytracer

object BookCover extends App:

  import raytracer.*

  def randomScene: HittableList = {

    // Generate random spheres to litter the scene with.
    val randomSpheres = Range(-11, 11).flatMap { a =>
      Range(-11, 11).flatMap { b =>
        val chooseMat = rand.nextDouble()
        val center = new Point3(a + 0.9 * rand.nextDouble(), 0.2, b + 0.9 * rand.nextDouble())

        if ((center - new Point3(4, 0.2, 0)).length > 0.9) {
          if (chooseMat < 0.8) {
            // diffuse
            val albedo: Color = Vec3.random * Vec3.random
            val sphereMaterial = new Lambertian(albedo)
            val center2 = center + new Vec3(0.0, rand.randomDouble(0.0, 0.5), 0.0)
            Some(new MovingSphere(center, center2, 0.0, 1.0, 0.2, sphereMaterial))
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
      }.toVector
    }.toVector

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
  val aspectRatio = 16.0 / 9.0
  val imageWidth = 400
  val imageHeight = (imageWidth / aspectRatio).toInt
  val samplesPerPixel = 100

  // World
  val world = randomScene

  // Camera
  val lookFrom = new Point3(13, 2, 3)
  val lookAt = new Point3(0, 0, 0)
  val viewUpVector = new Vec3(0, 1, 0)
  val distToFocus = 10.0
  val aperture = 0.1

  val camera = new Camera(
    lookFrom,
    lookAt,
    viewUpVector,
    verticalFieldOfView = 20.0,
    aperture,
    distToFocus,
    time0 = 0.0,
    time1 = 1.0,
    aspectRatio
  )

  new RayTracer(world, "bouncing-spheres.ppm", imageWidth, imageHeight, samplesPerPixel, camera)
    .render()
