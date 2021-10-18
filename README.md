# Java Developer - Development Task

## Introduction
You have the lead over a development team that will start next week with a new micro service. The team already worked 
together on another micro service for the same customer, so they are aware of the development pipeline. To get started 
the developers need the basic infrastructure of the application and an example API to get them going.

Place the code on github or bitbucket account for review in a branch with your name. Create a clone of the source repository
and within there, create a new branch with your name.

## Task
Be able to analyse an XML file and return basic metrics.

## Requirement
Create an API with Spring Boot or Quarkus or Micronaut or Microprofile (pick your favorite Java framework) and Maven or Gradle.
A request should be possible with an url to an XML file (this file can be > 1GB)

The response of the request should be an masterRecordEntity result id. The masterRecordEntity itself needs to be stored in a database.
A request should be possible with an masterRecordEntity result id.
The response of the request should hold an overview of the analysation of the XML (see example below, feel free to add more fields).

The code should be unit and component tested.
The code should pass the maven build and be runnable via cli with max of 512MB of memory. At least a single Java 8 feature should be included.
Important documentation or design decisions taken should be part of the source repository (in e.g. a README.md).

### Example files:
The files are based on stack overflow site with data per topic.
* 107Kb - example-data/arabic-posts.xml
* 841Kb - example-data/3dprinting-posts.xml Find other larger files on archive site https://archive.org/analysisReportEntity/stackexchange
All dates are in UTC in the file.

## Implement the following features:
### Feature 1: start an masterRecordEntity

HTTP request example
```
curl -i -X POST \
-H "Content-Type:application/json" \
-d \
'{
"url": "https://bitbucket.org/group9nl/java-assessment-base/raw/2c65f4920b0b89eeebc817d03c63c1154f4927ac/example-data/arabic-posts.xml"
}' \
'http://localhost:8080/{REPLACE_WITH_YOUR_ENDPOINT}/'

```

Example response of the masterRecordEntity result:
```json
{
    "id":"1" // as long as it is an unique ID
}
```
return 202 code

### Feature 2: Retreive a summary of an analyze

HTTP request example
```
curl --request GET \
  --url 'http://localhost:8080/{REPLACE_WITH_YOUR_ENDPOINT}' \
```

Example response of the masterRecordEntity result:
```json
{
    "id":"1", // id of the analyze
    "analyseDate":"2016-04-25T14:52:30Z[UTC]", // date request came in 2015-07-14T18:39:27.757
    "state":"Analyzing", // Can be the follwing values [Analyzing, Finished, Deleting, Failed]
    "failedSummary": "", // optional text field
    "analyseTimeInSeconds":"9", // the total time it took to analyze in seconds
    "analysisReportEntity": {
      "firstPost":"2015-07-14T18:39:27.757Z[UTC]", // The CreationDate value of a row the file with the lowest value (first in time)
      "lastPost":"2015-09-14T12:46:52.053Z[UTC]", // The CreationDate value of a row the file with the highest value (last in time)
      "totalPosts":80, // total amount unique rows
      "totalAcceptedPosts":7,
      "avgScore":2.98
    }
}
```

### Feature 3: Return all rows

HTTP request example
```
curl --request GET \
  --url 'http://localhost:8080/{REPLACE_WITH_YOUR_ENDPOINT}' \
```

Example response of the masterRecordEntity result:
```json
{
  "id": "1", // id of the analyze
  "state": "Analyzing", // Can be the follwing values [Analyzing, Finished, Deleting, Failed]
  "failedSummary": "", // optional text field
  "posts": [
    {
      "id": 1, // row->id
      "score": 4, // row->score
      "body": "&lt;p&gt;The proposal for this...", // row->body ... means the rest
      "childBodyList": [ // list of row body values from a row where row->ParentId is same of row->id
        "&lt;p&gt;The proposal for this...",
        "&lt;p&gt;While allowing non-cl...",
        "&lt;p&gt;For both of the main ...",
        "&lt;p&gt;Arabic was never a si...",
        "&lt;p&gt;As I see it, this iss...",
        "&lt;p&gt;As discussed in the p..."
      ]
    },
    {
      "id": 2,
      "score": 0,
      "body": "&lt;p&gt;While allowing non-cl...",
      "childBodyList": []
    }
  ]
}
```

### Feature 4: remove everything of an masterRecordEntity

HTTP request example
```
curl -X DELETE \
  --url 'http://localhost:8080/{REPLACE_WITH_YOUR_ENDPOINT}' \
```

return 202 code

```

## Tips
For component test you can use the following http mock server: http://www.mock-server.com/
You are allowed to ask questions about this assignment in case anything is unclear.

## Results
* Implement the requirements and deliver the source code.
* Docker container (Bonus)
* Create a docker-image for the server, based on the standard Java docker image (https://hub.docker.com/_/java/). The dockerfile used to create this image should be part of the repo.
* Commit the docker image on Dockerhub (https://hub.docker.com) Add a Readme on how to start and use the docker image
