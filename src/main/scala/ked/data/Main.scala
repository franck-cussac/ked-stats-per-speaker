package ked.data

import org.apache.spark.sql._
import org.apache.spark.sql.functions._

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().getOrCreate()

    println(s"spark conf = ${spark.conf.getAll}")

    val kedFile = sys.env("KED_FILE")
    val feedbackFile = sys.env("FEEDBACK_FILE")
    val outputFile = sys.env("OUTPUT")

    val keds = spark.read.json(kedFile).cache()
    val feedbacks = spark.read.json(feedbackFile).cache()

    val feedbackAverage = feedbacks
      .groupBy("slotId")
      .agg(avg("mark").as("average_mark"))

    val slotPerSpeakerAfterRefacto = keds
      .filter(col("year") === 2021)
      .filter(col("month") > 2)
      .withColumn("speaker_detailed", explode(col("speakers")))
      .withColumn("speaker", col("speaker_detailed.id"))
      .filter(col("kind").isNull || col("title").contains("mbianceur"))
      .select("speaker", "year", "month", "title", "id")

    val slotPerSpeakerBeforeRefacto = keds
      .withColumn("speaker", explode(col("attendees")))
      .filter(!col("summary").contains("Plénière"))
      .select("speaker", "year", "month", "summary", "id")
      .withColumnRenamed("summary", "title")

    val slotPerSpeaker = slotPerSpeakerAfterRefacto
      .union(slotPerSpeakerBeforeRefacto)
      .withColumn("speaker2", split(col("speaker"), "@"))
      .withColumn("speaker3", explode(col("speaker2")))
      .select(col("speaker3").as("speaker"), col("year"), col("month"), col("title"), col("id"))
      .filter(!col("speaker").contains("."))

    val tmp = slotPerSpeaker.filter(!(col("id").startsWith("cc-") || col("id").startsWith("xke-")))
      .withColumn("slotId", concat(lit("cc-"), col("id")))

    val res = tmp.union(slotPerSpeaker
      .filter(col("id").startsWith("cc-") || col("id").startsWith("xke-")).withColumn("slotId", col("id")))
      .drop("id")

    val statsPerSpeaker = res.join(feedbackAverage, "slotId")

    statsPerSpeaker.coalesce(1)
      .write
      .partitionBy("speaker", "year", "month")
      .option("header", true)
      .mode("OverWrite")
      .json(outputFile)

    println("Successfully run !")
  }
}
