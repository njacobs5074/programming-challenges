package raytracer

/** @author
  *   nick
  * @since 2021/09/22
  */
class Camera(
  lookFrom: Point3,
  lookAt: Point3,
  vup: Vec3,
  verticalFieldOfView: Double,
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
  val horizontal = viewportWidth * u
  val vertical = viewportHeight * v
  val lowerLeftCorner = origin - horizontal / 2.0 - vertical / 2.0 - w

  def getRay(s: Double, t: Double): Ray =
    new Ray(origin, lowerLeftCorner + s * horizontal + t * vertical - origin)

}
