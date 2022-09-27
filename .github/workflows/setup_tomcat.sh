#!/bin/bash

apt-get -q -y install tomcat9
systemctl stop tomcat9
echo 'CTSMS_PROPERTIES=/ctsms/properties' >>/etc/default/tomcat9
echo 'CTSMS_JAVA=/ctsms/java' >>/etc/default/tomcat9
sed -r -i "s|# Lifecycle|EnvironmentFile=/etc/default/tomcat9\\n\\n# Lifecycle|" /etc/systemd/system/multi-user.target.wants/tomcat9.service
sed -r -i "s|# Security|# Security\\nReadWritePaths=/ctsms/external_files/|" /etc/systemd/system/multi-user.target.wants/tomcat9.service
cat /etc/default/tomcat9
cat /etc/systemd/system/multi-user.target.wants/tomcat9.service
VERSION=$(grep -oP '<application.version>\K[^<]+' /home/runner/work/ctsms/ctsms/pom.xml)
#sed -r -i "s/^JAVA_OPTS.+/JAVA_OPTS=\"-server -Djava.awt.headless=true -Xms$XMS -Xmx$XMX -Xss$XSS -XX:+UseParallelGC -XX:MaxGCPauseMillis=1500 -XX:GCTimeRatio=9 -XX:+CMSClassUnloadingEnabled -XX:ReservedCodeCacheSize=$PERM\"/" /etc/default/tomcat9
echo 'CTSMS_PROPERTIES=/ctsms/properties' >>/etc/default/tomcat9
echo 'CTSMS_JAVA=/ctsms/java' >>/etc/default/tomcat9
sed -r -i "s|# Lifecycle|EnvironmentFile=/etc/default/tomcat9\\n\\n# Lifecycle|" /etc/systemd/system/multi-user.target.wants/tomcat9.service
sed -r -i "s|# Security|# Security\\nReadWritePaths=/ctsms/external_files/|" /etc/systemd/system/multi-user.target.wants/tomcat9.service
chmod 755 /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION.war
rm /var/lib/tomcat9/webapps/ROOT/ -rf
cp /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION.war /var/lib/tomcat9/webapps/ROOT.war
systemctl daemon-reload
systemctl start tomcat9
