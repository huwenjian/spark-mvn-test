import com.kcm.SegmentWords
import org.apache.spark.{SparkConf, SparkContext}

object distinctName {


  def main(args: Array[String]): Unit = {
    allNameSort()
  }


  def allNameSort(): Unit = {
    val conf: SparkConf = newSparkConf
    val sc = new SparkContext(conf)
    val names = sc.textFile("C:\\Users\\崔傅成\\Desktop\\信用搜索处理\\Name\\1-导出来的数据\\t-company-Name.txt")
    names.map(name => (name.trim.replace(" ", ""), 1))
      .filter(name => name._1.length > 0)
      .reduceByKey((value1, value2) => value1 + value2)
      .sortBy(value => value._2)
      .keys
      .filter(name => !SegmentWords.isName(name))
      .repartition(5)
      .saveAsTextFile("C:\\Users\\崔傅成\\Desktop\\信用搜索处理\\Name\\2-处理完的数据\\t-company-Name-去重-notName")
    sc.stop()
  }

  def secondNameSort(): Unit = {
    val conf: SparkConf = newSparkConf
    val sc = new SparkContext(conf)
    val names = sc.textFile("C:\\Users\\崔傅成\\Desktop\\信用搜索处理\\t-company-Name.txt")
    names
      .filter(name => name.trim.length > 1 && name.trim.length < 6)
      .flatMap(name => name.trim.substring(1).split(""))
      .map(name => (name, 1))
      .reduceByKey((v1, v2) => v1 + v2)
      .sortBy(value => value._2)
      .saveAsTextFile("C:\\Users\\崔傅成\\Desktop\\信用搜索处理\\去掉姓后的name单个字")
    sc.stop()
  }

  def firstNameSort(): Unit = {
    val conf: SparkConf = newSparkConf
    val sc = new SparkContext(conf)
    val names = sc.textFile("C:\\Users\\崔傅成\\Desktop\\信用搜索处理\\t-company-Name.txt")
    val count = names.filter(name => name.trim.length > 1 && name.trim.length < 6 && name.trim.substring(0, 1) == "焦").count()
    println(count)
    //      .map(name => (name.trim.substring(0, 1), 1))
    //      .reduceByKey((v1, v2) => v1 + v2)
    //      .sortBy(value => value._2)
    //      .saveAsTextFile("C:\\Users\\崔傅成\\Desktop\\信用搜索处理\\姓分组")
    sc.stop()
  }

  private def newSparkConf = {
    val conf = new SparkConf().setMaster("local[3]")
      .setAppName("Spark Pi")
      .set("spark.ui.port", "9995")
      .set("spark.driver.cores", "3")
      .set("spark.driver.memory", "1g")
      .set("spark.executor.memory", "2g")
      .set("spark.rdd.compress", "true")
    conf
  }


}
