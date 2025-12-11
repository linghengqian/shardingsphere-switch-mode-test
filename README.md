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

```