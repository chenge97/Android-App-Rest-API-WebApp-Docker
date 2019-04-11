# Android-App-Rest-API-WebApp-Docker
An Android app that classifies Dog Cat images with a Rest API. Built with docker (Python and Mysql images)

Specifications
1. Using Python (Flask) to build a webpage and a Rest API to connect Android App
   1.1 The webpage frontend is responsive and was build with Materialize CSS
   1.2 Use of Jquery
2. The classifications inside the app were made by a IMG classifier model build in Keras
3. For the Android App JAVA
   3.1. All traffic between api and app were encrypt and decrypt with RSA (Python and JAVA connection)
4. Every save inside a Mysql Image in docker 
5. Everything build on top od Docker (Dockerfile in the flask and Dockercompose.yaml to connect python and mysql)
