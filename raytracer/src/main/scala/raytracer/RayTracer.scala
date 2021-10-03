package raytracer

import zio.*

import java.io.{ File, FileWriter, PrintWriter }
import java.time.{ Duration, LocalDateTime }

class RayTracer(
  world: Hittable,
  outfile: String,
  imageWidth: Int,
  imageHeight: Int,
  samplesPerPixel: Int,
  camera: Camera,
  numFibers: Int = java.lang.Runtime.getRuntime.availableProcessors
):

  def render(): Unit = {
    println(s"Generating $outfile")

    val start = LocalDateTime.now()
    val file = new PPMFileWriter(outfile, imageWidth, imageHeight)

    for j <- imageHeight - 1 to 0 by -1 do
      print(s"Scanline: ${j + 1} of $imageHeight...\r")

      for i <- 0 to imageWidth - 1 do
        val pixelColor: Color = Range(0, samplesPerPixel).foldLeft(new Color(0, 0, 0)) {
          case (color, _) =>
            val u = (i + rand.nextDouble()) / (imageWidth - 1)
            val v = (j + rand.nextDouble()) / (imageHeight - 1)
            val ray = camera.getRay(u, v)

            rayColor(ray, world) + color
        }
        file.writeColor(pixelColor, samplesPerPixel)

    file.close()

    val elapsedTime = Math.abs(Duration.between(start, LocalDateTime.now()).toSeconds)

    println(s"\nDone in $elapsedTime seconds")
  }

  def generatePixel(row: Int, col: Int): UIO[(Int, Color)] =
    ZIO.succeed(
      (
        col,
        Range(0, samplesPerPixel).foldLeft(new Color(0, 0, 0)) { case (color, _) =>
          val u = (col + rand.nextDouble()) / (imageWidth - 1)
          val v = (row + rand.nextDouble()) / (imageHeight - 1)
          val ray = camera.getRay(u, v)

          rayColor(ray, world) + color
        }
      )
    )

  def generatePixels(row: Int, cols: Vector[Int]): UIO[Vector[(Int, Color)]] = {
    ZIO.foreach(cols)(generatePixel(row, _))
  }

  def generateScanline(row: Int): UIO[Vector[Color]] = {
    val work = Range(0, imageWidth).toVector.map(generatePixel(row, _)).toVector
    for {
      results <- ZIO.collectAllPar(work)
    } yield results.sortBy(_._1).map(_._2)
  }

  def printScanlines(ppmWriter: PPMFileWriter) = {
    ZIO.foreach(Range(imageHeight, 0, -1)) { row =>
      for {
        _ <- ZIO.succeed(print(s"Scanline: $row of $imageHeight...\r"))
        pixels <- generateScanline(row)
        _ <- ZIO.foreach(pixels)(pixel => ZIO.succeed(ppmWriter.writeColor(pixel, samplesPerPixel)))
      } yield ()
    }
  }

  def renderZIO(): UIO[Unit] = {
    for {
      _ <- ZIO.succeed(println(s"Generating $outfile with $numFibers fibers..."))
      start <- ZIO.succeed(LocalDateTime.now())
      ppmWriter <- ZIO.succeed(new PPMFileWriter(outfile, imageWidth, imageHeight))
      _ <- printScanlines(ppmWriter)
      _ <- ZIO.succeed(ppmWriter.close())
      elapsedTime <- ZIO.succeed(Math.abs(Duration.between(start, LocalDateTime.now()).toSeconds))
      _ <- ZIO.succeed(println(s"\nDone in $elapsedTime seconds"))
    } yield ()
  }
