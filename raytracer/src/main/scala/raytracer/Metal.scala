package raytracer

/** @author
  *   nick
  * @since 2021/09/24
  */
class Metal(val albedo: Color, fuzzy: Double) extends Material {

  override def scatter(rayIn: Ray, hitRecord: HitRecord): Option[Scattered] = {
    val fuzzy = if (this.fuzzy < 1.0) this.fuzzy else 1.0
    val reflected = rayIn.direction.unitVector.reflect(hitRecord.normal)
    val scattered = Scattered(Ray(hitRecord.p, reflected + fuzzy * Vec3.randomInUnitSphere), albedo)
    if (scattered.ray.direction.dot(hitRecord.normal) > 0.0) {
      Some(scattered)
    } else {
      None
    }
  }
}
