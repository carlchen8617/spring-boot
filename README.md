
<img width="1356" height="754" alt="Screenshot 2025-10-03 at 6 15 37 pm" src="https://github.com/user-attachments/assets/38ce41c5-5a7a-41f6-a9d7-a2009c56dd96" />

== Learn What You Can Do with Spring Boot

- a java 17 container that runs spring boot applications
- a oxasia openldap container that provide login authentication
- a postgres container that provides data storage via host filesystem

But made changes to build.gradle 
----
the customised code is in initial
----

== Run the Application

If you use gradle

copy the gs-spring-boot folder and cd into it, just run:
----

docker-compose up
----


If you use Maven

copy the gs-spring-boot folder and cd into it, just run:

----

docker-compose up
----

# Note

You can use the Dockerfile to build a new container to use

With the new container, you might see following error


----
Exception in thread "main" javax.net.ssl.SSLHandshakeException: \
PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: \
unable to find valid certification path to requested target
----

This is because Java doesnt trust the artefactory certificate by default. 

One way to resolve this is to export the cert from the brower(using MS Edge when I was using it). Once export the cert in .cer extension, copy it to the container(run the container without docker compose).

Do
----
root@79fc63e86adb:/# cd $JAVA_HOME/jre/lib/security/
root@79fc63e86adb:/usr/local/openjdk-8/jre/lib/security# keytool -import -trustcacerts -alias artifactory  -file /tmp/_.artifactory.lz019.aws.national.com.cer -keystore cacerts
Enter keystore password: 
----

The default keystore password is "changeit"



Once imported sucessfully, 

----
docker commit <container>

----

using the new container should work


---
  
try: (username: p780309d, password: 123456)
---  
http://localhost/

---
http://localhost/hello-here

---  
http://localhost/hello-here?name=yourname  
  
---
http://localhost/management  
  
---
http://localhost/images?jpg=3  

--
http://localhost/db/dbget
  
ldap related
---
  
ldapmodify -x -H ldap://localhost -D cn=admin,cn=config -w config  -f ac.ldif  
  
---  
for ldap, used: https://github.com/osixia/docker-openldap 
adding users: https://www.ibm.com/docs/en/netezza?topic=server-adding-users-ldap  
  
