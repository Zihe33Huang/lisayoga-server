# Basic Concept

* What is CI/CD?

  CI pipeline runs when code changes and should make sure all of your changes work with the rest of the code when it is integrated. It should also compile your code, run tests and check it is functional. 

  CD pipeline deploys built code into production.





# Workflow

* CI pipeline
  * Checkout, use pre-defined actions
  * Setup JDK environment
  * Build with Maven 
  * Build Docker image and publish to DockerHub

* CD pipeline

  * Pull image from Dockerhub
  * Run container

  





# Script tips

* **Prevent flow from stopping**

If the container is not running (maybe it doesn't exist), the `docker stop` command would normally return an error. To prevent the entire script from failing when this happens, the `|| true` part is added. If `docker stop yourapp` fails, the shell will execute the `true` command, which always returns success.

```
 docker stop $MY_APPNAME || true
```



* **Docker image management** 

Previously, when I pulled the image from Github, I did not delete the original image, resulting in the server storage space being full as a result. Now we can configure it as follows:

So basically, pull image from DockerHub, and change the tag as current, so every time new image pulled, just delete current.

```
          docker pull yourdockerusername/yourapp:${{ github.sha }}
          docker stop yourapp || true
          docker rm yourapp || true
          docker rmi yourdockerusername/yourapp:current || true
          docker tag yourdockerusername/yourapp:${{ github.sha }} yourdockerusername/yourapp:current
          docker run -d --name yourapp -p 8080:8080 yourdockerusername/yourapp:current
```



* **Variable**  

In official doc, an example uses $VARIABLE to represent variables,  it is actually used in `echo` command, because this is bash shell syntax.

In Github Action, we should use ${{env.variable}} to placeholder variable.



* Publish images to DockerHub

Here is an issue should be concerned, the name of your image should be like  repo/image:tag,   for example, my repo on DockerHub is same as my username,  `zihehuang33` ,  so as following shown:

```
REPOSITORY                        TAG           IMAGE ID       CREATED         SIZE
lisayoga_database                 latest        09ba9602d568   6 days ago      448MB
zihehuang33/lisayoga_database     latest        09ba9602d568   6 days ago      448MB
```

The first one can not be pushed to the Hub, and report: `denied: requested access to the resource is denied`

# Deploy several containers in single server 

As I have only one AWS server, I need to deploy database, frontend and backend in single server, so I need these containers connecting with each other.

## Virtual Network 

### Bridge Network (Default)

1. **Check that the bridge network is running** by `docker network ls` 

2. `docker run` default add container to `bridge` network

3. **Inspect** 

   ```
   $ docker inspect <container_id> | grep IPAddress
   ```

   or 

   ```
   $ docker network inspect bridge
   ```



Cons： IP address can change,  not convenient, do not support `hostname`



## User-defined Bridge Network

1. **Create network**

   ```
   docker network create xx
   ```

2. **Start a container and connect it to the bridge**

   Use `--net networkname`

   ```
   docker run --rm --net tulip-net --name tulipnginx -d nginx 
   ```

   Or  for a existing container

   ```
   docker network connect tulip-net mongodb
   ```

   

3. **Address another container, using its name as the hostname**

