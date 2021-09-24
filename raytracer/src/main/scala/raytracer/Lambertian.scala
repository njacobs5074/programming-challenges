package raytracer

/** @author
  *   nick
  * @since 2021/09/24
  */
class Lambertian(val albedo: Color) extends Material:
  override def scatter(rayIn: Ray, hitRecord: HitRecord): Option[Scattered] = {
    val scatterDirection = hitRecord.normal + Vec3.randomUnitVector

    Some(Scattered(
      Ray(
        hitRecord.p,
        if (scatterDirection.nearZero) hitRecord.normal else scatterDirection
      ),
      albedo)
    )
  }
