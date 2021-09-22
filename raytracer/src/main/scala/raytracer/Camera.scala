package raytracer

/** @author
  *   nick
  * @since 2021/09/22
  */
class Camera(
  aspectRatio: Double = 16.0 / 9.0,
  viewportHeight: Double = 2.0,
  focalLength: Double = 1.0
) {
  private val viewportWidth = aspectRatio * viewportHeight
  private val origin = new Point3(0, 0, 0)
  private val horizontal = new Vec3(viewportWidth, 0, 0)
  private val vertical = new Vec3(0, viewportHeight, 0)
  private val lowerLeftCorner =
    origin - horizontal / 2.0 - vertical / 2.0 - new Vec3(0, 0, focalLength)

  def getRay(u: Double, v: Double) =
    new Ray(origin, lowerLeftCorner + u * horizontal + v * vertical - origin)
}
