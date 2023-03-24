package raytracer

class Sphere(val center: Point3, val radius: Double, val material: Material) extends Hittable:
  override def hit(ray: Ray, tMin: Double, tMax: Double): Option[HitRecord] = {
    import raytracer.Vec3

    val oc: Point3 = ray.origin - center
    val a: Double = ray.direction.lengthSquared
    val halfB: Double = oc.dot(ray.direction)
    val c: Double = oc.lengthSquared - radius * radius

    val discriminant: Double = halfB * halfB - a * c
    if (discriminant < 0) {
      None
    } else {
      nearestRoot(halfB, Math.sqrt(discriminant), a, tMin, tMax).map { root =>
        val p: Point3 = ray.at(root)
        val normal: Vec3 = (p - center) / radius
        HitRecord(p, root, ray, normal, material)
      }
    }
  }
