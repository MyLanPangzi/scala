spark2-submit \
--class com.atguigu.qz.controller.DwsController \
--master yarn \
--deploy-mode cluster \
--queue spark \
--num-executors 2 \
--executor-memory 2G \
--executor-cores 2 \
./com_atguigu_warehouse-1.0-SNAPSHOT-jar-with-dependencies.jar

#./com_atguigu_warehouse-1.0-SNAPSHOT.jar
