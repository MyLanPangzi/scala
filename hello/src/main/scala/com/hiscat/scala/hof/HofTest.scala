package com.hiscat.scala.hof

object HofTest {
  def main(args: Array[String]): Unit = {
    Seq(20000, 70000, 40000)
      //      .map(x => x * 2)
      .map(_ * 2)
      .foreach(s => println(s))
    WeeklyWeatherForecast(Seq(18, 20, 30, 40)).forecastInFahrenheit.foreach(f => println(f))
    promotion(List(100000, 20000, 30000), _ * 2).foreach(s => println(s))
    println(urlBuilder(ssl = false, "www.hiscat.com")("/hello", "name=hello"))
  }

  def urlBuilder(ssl: Boolean, domainName: String): (String, String) => String = {
    val schema = if (ssl) "https://" else "http://"

    (endpoint: String, query: String) => s"$schema$domainName/$endpoint?$query"
  }

  private def promotion(salaries: List[Double], func: Double => Double): List[Double] = salaries.map(func)

  case class WeeklyWeatherForecast(temperatures: Seq[Double]) {
    private def conberCtoF(temp: Double) = temp * 1.8 + 32

    def forecastInFahrenheit: Seq[Double] = temperatures.map(conberCtoF)
  }

}
