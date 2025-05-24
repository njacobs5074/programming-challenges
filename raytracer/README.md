# raytracer

Scala versions of Peter Shirley's series on ray tracing.

- [_Ray Tracing in One Weekend_](https://raytracing.github.io/books/RayTracingInOneWeekend.html)
- [_Ray Tracing: The Next Week_](https://raytracing.github.io/books/RayTracingTheNextWeek.html)


NB: All errors & omissions in this repo are the fault of this author.

## Building

1. Make sure you have Java 1.8+ in your path. I have confirmed that it runs with Java 1.8 and Java 11.
The requisite version of Scala will be downloaded as part of the build process.
1. Make sure that you have `sbt` in your path.

> Note: I use `sdkman.io` for managing my tool dependencies. `#YMMV`

## Running

This project uses `sbt` and so the easiest way to run this code is:

```shell
$ sbt run
```

This will present you with 2 different images to generate, a bookcover and just a simple image of 2 spheres.