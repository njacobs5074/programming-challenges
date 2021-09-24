package raytracer

class Sphere(val center: Point3, val radius: Double, val material: Material) extends Hittable:
  override def hit(ray: Ray, tMin: Double, tMax: Double): Option[HitRecord] = {
    import raytracer.Vec3

    // Find the nearest root that lies in the acceptable range or None if there isn't one.
    def nearestRoot(halfB: Double, sqrtd: Double, a: Double): Option[Double] =
      (-halfB - sqrtd) / a match {
        case root1 if root1 < tMin || tMax < root1 =>
          val root2 = (-halfB + sqrtd) / a
          if (root2 < tMin || tMax < root2) None else Some(root2)
        case root1 =>
          Some(root1)
      }

    val oc: Point3 = ray.origin - center
    val a: Double = ray.direction.lengthSquared
    val halfB: Double = oc.dot(ray.direction)
    val c: Double = oc.lengthSquared - radius * radius

    val discrimant: Double = halfB * halfB - a * c
    if (discrimant < 0) {
      None
    } else {
      nearestRoot(halfB, Math.sqrt(discrimant), a).map { root =>
        val p: Point3 = ray.at(root)
        val normal: Vec3 = (p - center) / radius
        HitRecord(p, root, ray, normal, material)
      }
    }
  }