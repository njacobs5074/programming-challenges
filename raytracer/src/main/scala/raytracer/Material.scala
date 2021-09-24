package raytracer

case class Scattered(ray: Ray, attentuation: Color)

trait Material:
  def scatter(rayIn: Ray, hitRecord: HitRecord): Option[Scattered]
