# shardingsphere-switch-mode-test

- For https://github.com/apache/shardingsphere/issues/36672
  and https://github.com/oracle/graal/issues/11280 .
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
[INFO] Found GraalVM installation from GRAALVM_HOME variable.
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
[INFO] Compiling 2 source files with javac [debug release 8] to target\test-classes
[WARNING] 源值 8 已过时，将在未来发行版中删除
[WARNING] 目标值 8 已过时，将在未来发行版中删除
[WARNING] 要隐藏有关已过时选项的警告, 请使用 -Xlint:-options。
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ shardingsphere-switch-mode-test ---
[WARNING]  Parameter 'systemProperties' is deprecated: Use systemPropertyVariables instead.
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
com.oracle.svm.configure.trace.AccessAdvisor: Warning: Observed unexpected JNI call to GetStaticMethodID (map(size=7, {(tracer,jni),(function,GetStaticMethodID),(class,java.lang.Boolean),(declaring_class,java.lang.Boolean),(caller_class,jdk.internal.loader.NativeLibraries$NativeLibraryImpl),(result,true),(args,[getBoolean, (Ljava/lang/String;)Z])})). Tracing all subsequent JNI accesses.
[INFO] Running io.github.linghengqian.SimpleTest
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by io.netty.util.internal.PlatformDependent0$4 (file:/C:/Users/lingh/.m2/repository/io/netty/netty-common/4.1.103.Final/netty-common-4.1.103.Final.jar)
WARNING: Please consider reporting this to the maintainers of class io.netty.util.internal.PlatformDependent0$4
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 144.6 s <<< FAILURE! -- in io.github.linghengqian.SimpleTest
[ERROR] io.github.linghengqian.SimpleTest.assertModes -- Time elapsed: 122.3 s <<< ERROR!
org.awaitility.core.ConditionTimeoutException: Condition with Lambda expression in io.github.linghengqian.SimpleTest was not fulfilled within 1 minutes.
        at org.awaitility.core.ConditionAwaiter.await(ConditionAwaiter.java:167)
        at org.awaitility.core.CallableCondition.await(CallableCondition.java:78)
        at org.awaitility.core.CallableCondition.await(CallableCondition.java:26)
        at org.awaitility.core.ConditionFactory.until(ConditionFactory.java:1160)
        at org.awaitility.core.ConditionFactory.until(ConditionFactory.java:1129)
        at io.github.linghengqian.SimpleTest.initEnvironment(SimpleTest.java:70)
        at io.github.linghengqian.SimpleTest.assertZookeeper(SimpleTest.java:60)
        at io.github.linghengqian.SimpleTest.assertModes(SimpleTest.java:35)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
Caused by: org.apache.shardingsphere.infra.exception.kernel.metadata.TableNotFoundException: Table or view 't_order' does not exist.
        at org.apache.shardingsphere.infra.binder.engine.segment.dml.from.type.SimpleTableSegmentBinder.lambda$checkTableExists$7(SimpleTableSegmentBinder.java:179)
        at org.apache.shardingsphere.infra.exception.ShardingSpherePreconditions.checkState(ShardingSpherePreconditions.java:44)
        at org.apache.shardingsphere.infra.binder.engine.segment.dml.from.type.SimpleTableSegmentBinder.checkTableExists(SimpleTableSegmentBinder.java:179)
        at org.apache.shardingsphere.infra.binder.engine.segment.dml.from.type.SimpleTableSegmentBinder.bind(SimpleTableSegmentBinder.java:94)
        at org.apache.shardingsphere.infra.binder.engine.segment.dml.from.TableSegmentBinder.bind(TableSegmentBinder.java:57)
        at org.apache.shardingsphere.infra.binder.engine.statement.dml.SelectStatementBinder.lambda$bind$1(SelectStatementBinder.java:72)
        at java.base/java.util.Optional.map(Optional.java:260)
        at org.apache.shardingsphere.infra.binder.engine.statement.dml.SelectStatementBinder.bind(SelectStatementBinder.java:72)
        at org.apache.shardingsphere.infra.binder.engine.type.DMLStatementBindEngine.bind(DMLStatementBindEngine.java:45)
        at org.apache.shardingsphere.infra.binder.engine.SQLBindEngine.bindSQLStatement(SQLBindEngine.java:73)
        at org.apache.shardingsphere.infra.binder.engine.SQLBindEngine.bind(SQLBindEngine.java:59)
        at org.apache.shardingsphere.driver.jdbc.core.statement.ShardingSphereStatement.createQueryContext(ShardingSphereStatement.java:260)
        at org.apache.shardingsphere.driver.jdbc.core.statement.ShardingSphereStatement.execute(ShardingSphereStatement.java:247)
        at org.apache.shardingsphere.driver.jdbc.core.statement.ShardingSphereStatement.execute(ShardingSphereStatement.java:195)
        at com.zaxxer.hikari.pool.ProxyStatement.execute(ProxyStatement.java:94)
        at com.zaxxer.hikari.pool.HikariProxyStatement.execute(HikariProxyStatement.java)
        at io.github.linghengqian.SimpleTest.doSql(SimpleTest.java:92)
        at io.github.linghengqian.SimpleTest.lambda$initEnvironment$0(SimpleTest.java:71)
        at org.awaitility.core.CallableCondition$ConditionEvaluationWrapper.eval(CallableCondition.java:99)
        at org.awaitility.core.ConditionAwaiter$ConditionPoller.call(ConditionAwaiter.java:248)
        at org.awaitility.core.ConditionAwaiter$ConditionPoller.call(ConditionAwaiter.java:235)
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
        at java.base/java.lang.Thread.run(Thread.java:1474)

[INFO] 
[INFO] Results:
[INFO] 
[ERROR] Errors: 
[ERROR]   SimpleTest.assertModes:35->assertZookeeper:60->initEnvironment:70 ? ConditionTimeout Condition with Lambda expression in io.github.linghengqian.SimpleTest was not fulfilled within 1 minutes.                                                                                                                                        
[INFO] 
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  02:35 min (Wall Clock)
[INFO] Finished at: 2025-12-11T22:43:38+08:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.2.5:test (default-test) on project shardingsphere-switch-mode-test: 
[ERROR] 
[ERROR] Please refer to C:\Users\lingh\IdeaProjects\shardingsphere-switch-mode-test\target\surefire-reports for the individual test results.
[ERROR] Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.
[ERROR] -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
```