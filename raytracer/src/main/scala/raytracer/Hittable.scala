package raytracer

case class HitRecord(
  val p: Point3,
  val t: Double,
  ray: Ray,
  outwardNormal: Vec3,
  material: Material
):
  val frontFace: Boolean = ray.direction.dot(outwardNormal) < 0
  val normal: Vec3 = if (frontFace) outwardNormal else -outwardNormal

trait Hittable:
  def hit(r: Ray, tMin: Double, tMax: Double): Option[HitRecord]

class HittableList(val objects: Vector[Hittable]) extends Hittable:
  /** Iterate over the collection of Hittable objects and figure out which one, if any, is struck by
    * the supplied ray in the interval [tMin, tMax).
    */
  override def hit(ray: Ray, tMin: Double, tMax: Double): Option[HitRecord] =
    // Note that we start with hitting "nothing", thus the 'None' and tMax. If one of the objects
    // is hit, it will return a HitRecord which we then use to set closestSoFar
    objects
      .foldLeft[(Option[HitRecord], Double)]((None, tMax)) { case ((hitObj, closestSoFar), obj) =>
        val newHitObj = obj.hit(ray, tMin, closestSoFar).orElse(hitObj)
        (newHitObj, newHitObj.map(_.t).getOrElse(closestSoFar))
      }
      ._1
