
1.需要在hadoop-env.sh中配置JAVA_HOME的具体路径，避免hdfs运行时找不到java

2.在core-site.xml中如果设置了hadoop.tmp.dir，需要在hdfs-site.xml中设置对应的dfs.namenode.name.dir和dfs.datanode.data.dir的配置项，否则会报"URI has an authority component"