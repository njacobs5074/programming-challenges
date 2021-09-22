package raytracer

class Ray(val origin: Point3, val direction: Vec3):
  override def toString = s"origin=$origin,direction=$direction"

  def at(t: Double): Point3 =
    origin + (direction * t)