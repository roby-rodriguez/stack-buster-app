A Java tool for busting the Stack

## Installing the workspace:
1. Open terminal & execute `git clone https://github.com/roby-rodriguez/stack-buster-app`
2. Update to Java9 by executing the following commands: (Ubuntu Trusty 14.04 - check by `lsb_release -a`)
  * `sudo sh -c "echo 'deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main' >> /etc/apt/sources.list.d/c9.list"`
  * `sudo sh -c "echo 'deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main' >> /etc/apt/sources.list.d/c9.list"`
  * `sudo apt-get update`
  * `sudo apt-get install oracle-java9-installer`
  * Follow install steps and then check status by `java -version`
3. [Upgrade to Maven 3.x](https://askubuntu.com/a/707725)
4. Go to 'stack-buster-app' dir & execute `mvn clean install`
5. Then `mvn java:exec`

### References
1) [Firebase Jackson config](https://stackoverflow.com/questions/18125697/how-to-ignore-new-fields-for-an-object-model-with-firebase-1-0-2)
2) [Firebase Async operations](https://medium.com/google-cloud/firebase-asynchronous-operations-with-admin-java-sdk-82ca9b4f6022)
3) [Spring custom annotations processing](http://www.baeldung.com/spring-annotation-bean-pre-processor)
4) [Spring beans manual autowiring](https://stackoverflow.com/questions/11965600/how-do-i-manually-autowire-a-bean-with-spring)
5) [Registering manual autowired beans](https://technology.amis.nl/2018/02/22/java-how-to-fix-spring-autowired-annotation-not-working-issues/)
6) [Using lambdas for shorter enums](https://stackoverflow.com/questions/23361418/lambdas-in-the-classical-operation-enum-example)
7) [Avoiding `instanceof` by using visitor DP](https://stackoverflow.com/questions/3930808/how-to-avoid-large-if-statements-and-instanceof)
8) [Maven exec run scripts on both windows & linux](https://stackoverflow.com/questions/14809931/maven-calls-external-script-on-both-linux-and-windows-platforms)
9) [Firebase CLI deployment (for maven)](https://stackoverflow.com/questions/33939143/firebase-tools-login-from-command-line)
