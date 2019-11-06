
## maven-release-plugin

### append scm info and plugin configuration in pom.xml(e.g.)

    <scm>
      <connection>scm:git:http://xxx.git</connection>
      <developerConnection>scm:git:http://xxx.git</developerConnection>
      <tag>HEAD</tag>
    </scm>

    # plugin

    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-release-plugin</artifactId>
      <version>2.5.3</version>
      <configuration>
        <preparationGoals>package deploy</preparationGoals>
        <autoVersionSubmodules>true</autoVersionSubmodules>
        <tagBase>http://xxx.git</tagBase>
        <useReleaseProfile>false</useReleaseProfile>
      </configuration>
    </plugin>

### usage

    # increase to the next version, press enter key if use default value
    mvn release:prepare

    # clean plugin temporary file
    mvn release:clean


## versions-maven-plugin

### config in pom.xml file

    # append plugin(2.2 will throw nullpoint exception, so force use 2.5)
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>versions-maven-plugin</artifactId>
      <version>2.5</version>
    </plugin>

### usage

#### set version

    mvn version:set -DnewVersion=<your version>

#### commit & clean temporary file

    mvn version:commit
