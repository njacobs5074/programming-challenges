package raytracer

import raytracer.Vec3.randomInUnitSphere

import java.io.PrintWriter
import scala.annotation.tailrec

class Vec3(e0: Double, e1: Double, e2: Double):
  override def toString = s"[${e.mkString(",")}]"

  private val e: Array[Double] = Array[Double](e0, e1, e2)

  def this() = this(0, 0, 0)
  def this(e: Array[Double]) = this(e(0), e(1), e(2))

  def apply(i: Int): Double = e(i)

  def x = e(0)
  def y = e(1)
  def z = e(2)

  def unary_- = Vec3(e.map(_ * -1.0))

  def /(t: Double): Vec3 =
    Vec3(this(0) / t, this(1) / t, this(2) / t)

  def unitVector: Vec3 =
    this / this.length

  def +(v: Vec3): Vec3 =
    Vec3(this(0) + v(0), this(1) + v(1), this(2) + v(2))

  def -(v: Vec3): Vec3 =
    Vec3(this(0) - v(0), this(1) - v(1), this(2) - v(2))

  def *(v: Vec3): Vec3 =
    Vec3(this(0) * v(0), this(1) * v(1), this(2) * v(2))

  def *(t: Double): Vec3 =
    Vec3(this(0) * t, this(1) * t, this(2) * t)

  def dot(v: Vec3): Double =
    this(0) * v(0) + this(1) * v(1) + this(2) * v(2)

  def cross(v: Vec3): Vec3 =
    Vec3(
      this(1) * v(2) - this(2) * v(1),
      this(2) * v(0) - this(0) * v(2),
      this(0) * v(1) - this(1) * v(0)
    )

  def lengthSquared: Double =
    this(0).squared + this(1).squared + this(2).squared

  def length: Double =
    Math.sqrt(lengthSquared)

  def randomInHemisphere(normal: Vec3): Point3 = {
    val inUnitSphere = randomInUnitSphere
    if (inUnitSphere.dot(normal) > 0.0) {
      inUnitSphere
    } else {
      -inUnitSphere
    }
  }

  def nearZero: Boolean = {
    val s: Double = 1e-8
    (Math.abs(this(0)) < s) && (Math.abs(this(1)) < s) && (Math.abs(this(2)) < s)
  }

  def reflect(n: Vec3): Vec3 =
    this - 2 * dot(n) * n

  def refract(n: Vec3, etaIOverEtaT: Double): Vec3 = {
    val cosTheta: Double = Math.min(-this.dot(n), 1.0)
    val rOutPerpendicular: Vec3 = etaIOverEtaT * (this + cosTheta * n)
    val rOutParallel: Vec3 = -Math.sqrt(Math.abs(1.0 - rOutPerpendicular.lengthSquared)) * n
    rOutPerpendicular + rOutParallel
  }

object Vec3:
  def random: Vec3 = new Vec3(rand.nextDouble(), rand.nextDouble(), rand.nextDouble())

  def random(min: Double, max: Double): Vec3 =
    new Vec3(rand.randomDouble(min, max), rand.randomDouble(min, max), rand.randomDouble(min, max))

  def randomInUnitSphere = {
    @tailrec
    def loop(p: Vec3 = Vec3.random(-1, 1)): Vec3 =
      if (p.lengthSquared >= 1) loop() else p

    loop()
  }

  def randomInUnitDisk = {
    @tailrec
    def loop(p: Vec3 = Vec3(rand.randomDouble(-1, 1), rand.randomDouble(-1, 1), 0)): Vec3 =
      if (p.lengthSquared >= 1.0) loop() else p

    loop()
  }

  def randomUnitVector: Point3 = randomInUnitSphere.unitVector
