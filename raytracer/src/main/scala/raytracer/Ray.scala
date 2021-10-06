package raytracer

class Ray(val origin: Point3, val direction: Vec3, val time: Double = 0.0):

  def at(t: Double): Point3 =
    origin + (direction * t)

  override def toString = s"origin=$origin,direction=$direction"
