# Tutorials
## JBoss EAP/ Wildfly Servers
#### Assumptions

|`$WILDFLY`| Location where you installed the server to (eg. `~/var/wildfly.eap/)

#### 1 Add new user
JBoss does not create any admin user when installed. You need to create one to access the admin console (default port is 9990)

Run `$WILDFLY/bin/add-user.sh` it will be ran in interactive mode, the user must be `a) Management User`. The username/password can be whatever combination you choose.

#### 2 Add a PostgreSQL (pgjdbc-ng) driver module
1. Follow your platform-specific instructions to install POstgreSQL database.
2. Change to directory `$WILDFLY/modules`
3. Create the following directory structure `mkdir -p org/postgresql/main`
4. Still at the same directory as (2) download the driver with
```bash
wget -O org/postgresql/main/pgjdbc-ng.drv.jar https://repo1.maven.org/maven2/com/impossibl/pgjdbc-ng/pgjdbc-ng-all/0.8.9/pgjdbc-ng-all-0.8.9.jar
```
5. Create a `module.xml` file at `org/postgresql/main` and add the following content to it:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.0" name="org.postgresql">
    <resources>
        <resource-root path="pgjdbc-ng.drv.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```
6. Go to the web admin console `localhost:9990` (if you installed it locally ofc) and add the module you just created by going to the `Configuration` tab and choose on the list below `Subsystems/JDBC Drivers`. At the title of the `JDBC Driver` column, there is a (+) icon, click on it and it will open a modal with several parameters.
7. At the above dialog fill the following parameters:
| Name | Can be filled with any name you'd like|
| Driver Module Name | `org.postgresql`| The same you created in previous steps|
| Driver Class Name | `com.impossibl.postgres.jdbc.PGDriver`| |
| Driver Datasource Class Name | `com.impossibl.postgres.jdbc.PGDataSource` | |


