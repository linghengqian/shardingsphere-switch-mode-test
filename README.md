# shardingsphere-switch-mode-test

- For https://github.com/apache/shardingsphere/issues/36672
  and https://github.com/apache/shardingsphere-elasticjob/issues/2221 .
- Execute the following `PowerShell 7` command on the `Windows 11 Home 25H2` instance with `PowerShell/PowerShell`,
  `version-fox/vfox`, `git-for-windows/git` and `rancher-sandbox/rancher-desktop` installed.

```shell
vfox add java
vfox install java@24.0.2-graalce
vfox use --global java@24.0.2-graalce
# Open a new PowerShell 7 terminal
git clone git@github.com:apache/shardingsphere.git
cd ./shardingsphere/
git reset --hard bb359f23f81f8eb279cd7e804f2cd6e3a924b37d
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
[INFO] --- compiler:3.14.1:compile (default-compile) @ shardingsphere-switch-mode-test ---
[INFO] No sources to compile
[INFO] 
[INFO] --- resources:3.3.1:testResources (default-testResources) @ shardingsphere-switch-mode-test ---
[INFO] Copying 3 resources from src\test\resources to target\test-classes
[INFO] 
[INFO] --- compiler:3.14.1:testCompile (default-testCompile) @ shardingsphere-switch-mode-test ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 1 source file with javac [debug release 24] to target\test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ shardingsphere-switch-mode-test ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running io.github.linghengqian.SimpleTest
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by com.github.benmanes.caffeine.cache.UnsafeAccess (file:/C:/Users/lingh/.m2/repository/com/github/ben-manes/caffeine/caffeine/2.9.3/caffeine-2.9.3.jar)
WARNING: Please consider reporting this to the maintainers of class com.github.benmanes.caffeine.cache.UnsafeAccess
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 20.45 s -- in io.github.linghengqian.SimpleTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  27.132 s (Wall Clock)
[INFO] Finished at: 2025-12-10T17:10:04+08:00
[INFO] ------------------------------------------------------------------------
```