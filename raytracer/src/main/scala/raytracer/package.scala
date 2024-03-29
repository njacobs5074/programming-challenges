import scala.annotation.{ targetName, unused }
import scala.util.Random

package object raytracer:

  type Point3 = raytracer.Vec3
  type Color = raytracer.Vec3

  val rand = new Random()

  implicit class RandomExt(rand: Random):
    def randomDouble(min: Double, max: Double): Double =
      min + (max - min) * rand.nextDouble()

  implicit class DoubleExt(d: Double):
    def *(v: Vec3): Vec3 = v * d

    def squared: Double = d * d

  @unused
  def hitSphere(center: Point3, radius: Double, r: Ray): Double = {
    import Vec3.*

    val oc: Vec3 = r.origin - center
    val a = r.direction.lengthSquared
    val halfB = oc.dot(r.direction)
    val c = oc.lengthSquared - radius * radius
    val discrimant = halfB * halfB - a * c
    if (discrimant < 0) {
      -1.0
    } else {
      (-halfB - Math.sqrt(discrimant)) / a
    }
  }

  def rayColor(ray: Ray, world: Hittable, maxDepth: Int = 50): Color = {
    def loop(ray: Ray, depth: Int): Color = {
      if (depth <= 0) { // Check on depth of recursion - light is just absorbed
        new Color(0, 0, 0)
      } else {
        world
          .hit(ray, 0.001, Double.PositiveInfinity)
          .map(rec =>
            rec.material
              .scatter(ray, rec)
              .map(scattered => scattered.attenuation * loop(scattered.ray, depth - 1))
              .getOrElse(new Color(0, 0, 0))
          )
          .getOrElse {
            val unitDirection: Point3 = ray.direction.unitVector
            val t: Double = 0.5 * (unitDirection.y + 1.0)
            (1.0 - t) * new Color(1.0, 1.0, 1.0) + t * new Color(0.5, 0.7, 1.0)
          }
      }
    }

    loop(ray, maxDepth)
  }

  // Find the nearest root that lies in the acceptable range or None if there isn't one.
  def nearestRoot(
    halfB: Double,
    sqrtd: Double,
    a: Double,
    tMin: Double,
    tMax: Double
  ): Option[Double] =
    (-halfB - sqrtd) / a match {
      case root1 if root1 < tMin || tMax < root1 =>
        val root2 = (-halfB + sqrtd) / a
        if (root2 < tMin || tMax < root2) None else Some(root2)
      case root1 =>
        Some(root1)
    }
