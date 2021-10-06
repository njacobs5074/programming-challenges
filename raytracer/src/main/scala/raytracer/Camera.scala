package raytracer

class Camera(
  lookFrom: Point3,
  lookAt: Point3,
  vup: Vec3,
  verticalFieldOfView: Double,
  aperture: Double,
  focusDistance: Double,
  time0: Double = 0.0,
  time1: Double = 0.0,
  aspectRatio: Double = 16.0 / 9.0
) {
  val theta = Math.toRadians(verticalFieldOfView)
  val h = Math.tan(theta / 2.0)
  private val viewportHeight = 2.0 * h
  private val viewportWidth = aspectRatio * viewportHeight

  val w = (lookFrom - lookAt).unitVector
  val u = vup.cross(w).unitVector
  val v = w.cross(u)

  val origin = lookFrom
  val horizontal = focusDistance * viewportWidth * u
  val vertical = focusDistance * viewportHeight * v
  val lowerLeftCorner = origin - horizontal / 2.0 - vertical / 2.0 - focusDistance * w
  val lensRadius = aperture / 2.0

  def getRay(s: Double, t: Double): Ray = {
    val rd = lensRadius * Vec3.randomInUnitDisk
    val offset = u * rd.x + v * rd.y

    new Ray(
      origin + offset,
      lowerLeftCorner + s * horizontal + t * vertical - origin - offset,
      rand.randomDouble(time0, time1)
    )
  }

}
