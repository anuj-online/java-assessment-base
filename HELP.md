#How to run the application

1. Run gradlew build (./gradlew build if you are using mac) from project root directory


#Dockerize and the application

1. Install Docker from https://docs.docker.com/get-docker/

2. Run 'docker build . --tag java-assessment-base:latest --build-arg VERSION=0.0.1-SNAPSHOT', this will build the docker image

3. verify image is there running docker images from terminal (cmd for windows), you shall be seeing docker image java-assessment-base in the list, copy the imageID, if you managed to update version of the application and created multiple images

4. Run command
    -> docker run -p8080:8080  <image-id>(copied in step 3) 

5. Open the browser and go to **http://localhost:8080/api/swagger-ui.html**


#NOTES FOR REVIEWERS

1. Please visit the README.md to following question, **_list of row body values from a row where row->ParentId is same of row->id_** --> I am not sure if I got this one correctly, just do ctrl+shift+F for .filer and see the condition I used

2. I understand naming wise the controller and services are not perfect, but my idea was to have a running application with whole functionality

3. I was a bit lazy while writing the test cases hence you would see in 'GET' methods I am using 'CREATE' method of controller as well, which I highly avoid in production code. As the code should be independent of any call which is not in actual code execution.

4. Some entity names or props maybe cofusing, maybe because I started on a wrong foot (approach) and later worked my way through this.
