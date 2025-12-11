# shardingsphere-switch-mode-test

- For https://github.com/apache/shardingsphere/issues/36672
  and https://github.com/apache/shardingsphere-elasticjob/issues/2221 .
- Execute the following `PowerShell 7` command on the `Windows 11 Home 25H2` instance with `PowerShell/PowerShell`,
  `version-fox/vfox`, `git-for-windows/git` and `rancher-sandbox/rancher-desktop` installed.

```shell
vfox add java
vfox install java@25.0.1-graalce
vfox use --global java@25.0.1-graalce
# Open a new PowerShell 7 terminal
git clone git@github.com:apache/shardingsphere.git
cd ./shardingsphere/
git reset --hard 2833e306106891588ddbb76b65c3b313c24a90f8
./mvnw clean install -Prelease -T1C -DskipTests "-Djacoco.skip=true" "-Dcheckstyle.skip=true" "-Drat.skip=true" "-Dmaven.javadoc.skip=true"
cd ../

git clone git@github.com:linghengqian/shardingsphere-switch-mode-test.git
cd ./shardingsphere-switch-mode-test/
./mvnw -T 1C clean test
```

- Log as follows.

```shell
PS C:\Users\lingh\IdeaProjects\shardingsphere-switch-mode-test> ./mvnw -T 1C clean test
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::staticFieldBase has been called by com.google.inject.internal.aop.HiddenClassDefiner (file:/C:/Users/lingh/.m2/wrapper/dists/apache-maven-3.9.11/03d7e36a140982eea48e22c1dcac01d8862b2550b2939e09a0809bbc5182a5bc/lib/guice-5.1.0-classes.jar)
WARNING: Please consider reporting this to the maintainers of class com.google.inject.internal.aop.HiddenClassDefiner
WARNING: sun.misc.Unsafe::staticFieldBase will be removed in a future release
[INFO] Scanning for projects...
[INFO] 
[INFO] Using the MultiThreadedBuilder implementation with a thread count of 16
[INFO] 
[INFO] -------< io.github.linghengqian:shardingsphere-switch-mode-test >-------
[INFO] Building shardingsphere-switch-mode-test 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.2.0:clean (default-clean) @ shardingsphere-switch-mode-test ---
[INFO] Deleting C:\Users\lingh\IdeaProjects\shardingsphere-switch-mode-test\target
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ shardingsphere-switch-mode-test ---
[INFO] skip non existing resourceDirectory C:\Users\lingh\IdeaProjects\shardingsphere-switch-mode-test\src\main\resources
[INFO] 
[INFO] --- compiler:3.13.0:compile (default-compile) @ shardingsphere-switch-mode-test ---
[INFO] No sources to compile
[INFO] 
[INFO] --- resources:3.3.1:testResources (default-testResources) @ shardingsphere-switch-mode-test ---
[INFO] Copying 3 resources from src\test\resources to target\test-classes
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ shardingsphere-switch-mode-test ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 2 source files with javac [debug release 25] to target\test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ shardingsphere-switch-mode-test ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running io.github.linghengqian.SimpleTest
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by io.netty.util.internal.PlatformDependent0$4 (file:/C:/Users/lingh/.m2/repository/io/netty/netty-common/4.1.103.Final/netty-common-4.1.103.Final.jar)
WARNING: Please consider reporting this to the maintainers of class io.netty.util.internal.PlatformDependent0$4
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 16.58 s -- in io.github.linghengqian.SimpleTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  21.576 s (Wall Clock)
[INFO] Finished at: 2025-12-11T16:45:59+08:00
[INFO] ------------------------------------------------------------------------
```