package com.hiscat.ecommerce.common

import com.hiscat.ecommerce.util.ProjectUtil
import org.apache.spark.rdd.RDD

trait TDao {

    def readFile( path:String ): RDD[String] = ProjectUtil.sparkContext().textFile(path)
}
