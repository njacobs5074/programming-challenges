package raytracer

/** @author
  *   nick
  * @since 2021/09/25
  */
class Dieletric(val indexOfRefraction: Double) extends Material {
  override def scatter(rayIn: Ray, hitRecord: HitRecord): Option[Scattered] = {
    val refractionRatio = if (hitRecord.frontFace) 1.0 / indexOfRefraction else indexOfRefraction

    val unitDirection = rayIn.direction.unitVector
    val cosTheta = Math.min(-unitDirection.dot(hitRecord.normal), 1.0)
    val sinTheta = Math.sqrt(1.0 - cosTheta.squared)

    val cannotRefract = refractionRatio * sinTheta > 1.0
    val direction: Vec3 =
      if (cannotRefract || reflectance(cosTheta, refractionRatio) > rand.nextDouble()) {
        unitDirection.reflect(hitRecord.normal)
      } else {
        unitDirection.refract(hitRecord.normal, refractionRatio)
      }

    Some(Scattered(new Ray(hitRecord.p, direction, rayIn.time), new Color(1.0, 1.0, 1.0)))
  }

  // Use Schlick's approximation for reflectance.
  private def reflectance(cosine: Double, refractionIndex: Double): Double = {
    val r0 = ((1 - refractionIndex) / (1 + refractionIndex)).squared
    r0 + (1 - r0) * Math.pow((1 - cosine), 5.0)
  }
}
