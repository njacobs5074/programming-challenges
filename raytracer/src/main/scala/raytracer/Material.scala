package raytracer

case class Scattered(ray: Ray, attenuation: Color)

trait Material:
  def scatter(rayIn: Ray, hitRecord: HitRecord): Option[Scattered]
