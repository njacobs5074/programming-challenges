package raytracer

import java.io.PrintWriter

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
    Vec3(e(0) / t, e(1) / t, e(2) / t)

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
    Vec3(this(1) * v(2) - this(2), v(2) * this(0) - v(0), v(0) * this(1) - v(1))

  def lengthSquared: Double =
    this(0) * this(0) + this(1) * this(1) + this(2) * this(2)

  def length: Double =
    Math.sqrt(lengthSquared)

object Vec3:
  def dot(u: Vec3, v: Vec3): Double =
    u(0) * v(0) + u(1) * v(1) + u(2) * v(2)