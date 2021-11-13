package raytracer

class Lambertian(val albedo: Color) extends Material:
  override def scatter(rayIn: Ray, hitRecord: HitRecord): Option[Scattered] = {
    val scatterDirection = hitRecord.normal + Vec3.randomUnitVector

    Some(
      Scattered(
        Ray(
          hitRecord.p,
          if (scatterDirection.nearZero) hitRecord.normal else scatterDirection,
          rayIn.time
        ),
        albedo
      )
    )
  }
