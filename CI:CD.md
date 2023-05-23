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



* **$APP_NAME seems not working** 

  

