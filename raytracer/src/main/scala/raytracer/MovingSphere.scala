package raytracer

class MovingSphere(
  center0: Point3,
  center1: Point3,
  time0: Double,
  time1: Double,
  radius: Double,
  material: Material
) extends Hittable {

  override def hit(ray: Ray, tMin: Double, tMax: Double): Option[HitRecord] = {
    def center(time: Double): Point3 =
      center0 + ((time - time0) / (time1 - time0)) * (center1 - center0)

    val oc: Vec3 = ray.origin - center(ray.time)
    val a = ray.direction.lengthSquared
    val halfB = oc.dot(ray.direction)
    val c = oc.lengthSquared - radius.squared

    val discriminant = halfB.squared - a * c
    if (discriminant < 0.0) {
      None
    } else {
      nearestRoot(halfB, Math.sqrt(discriminant), a, tMin, tMax).map { root =>
        val p: Point3 = ray.at(root)
        val outwardNormal = (p - center(ray.time)) / radius
        HitRecord(p, root, ray, outwardNormal, material)
      }
    }
  }
}
