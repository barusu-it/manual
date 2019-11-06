## Scala Installation

    wget https://downloads.lightbend.com/scala/2.11.12/scala-2.11.12.deb
    sudo dpkg -i scala-2.11.12.deb

    scala -verson
    
## SBT Installation

    echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
    sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
    sudo apt install apt-transport-https
    sudo apt update
    sudo apt install -y sbt

    sbt sbtVersion