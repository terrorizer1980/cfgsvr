# Configuration Server

## Overview 🔍

Unlike a monolithic app in which everything runs within a single instance, a cloud-native application consists of independent services distributed across virtual machines, containers, and geographic regions. Managing configuration settings for dozens of interdependent services can be challenging. Duplicate copies of configuration settings across different locations is error prone and difficult to manage. Centralized configuration is a critical requirement for distributed cloud-native applications.

Configuration must be stored externally from the application and read-in as needed. Storing configuration values as constants or literal values in code is a violation. The same configuration values are often be used by many services in the same application. Additionally, we must support the same values across multiple environments, such as dev, testing, and production. The best practice is store them in a centralized configuration store.

## Author 🖋️
* **Eliezer Efrain Chavez** -  [eliezerchavez](https://www.linkedin.com/in/eliezerchavez)

### Pre-Requisites 📋

* [Maven](https://maven.apache.org/users/index.html) <br />
  Apache Maven is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information.
* [JUnit](https://junit.org/junit5/) <br />
  The JUnit Platform serves as a foundation for launching testing frameworks on the JVM. It also defines the TestEngine API for developing a testing framework that runs on the platform. Furthermore, the platform provides a Console Launcher to launch the platform from the command line and a JUnit 4 based Runner for running any TestEngine on the platform in a JUnit 4 based environment.
* [Docker](https://www.docker.com/get-started) <br />
  A set of Platform as a Service (PaaS) products that use OS-level virtualization to deliver software in packages called containers. Containers are isolated from one another and bundle their own software, libraries and configuration files; they can communicate with each other through well-defined channels.
* [Docker Compose](https://docs.docker.com/compose/) <br />
  Compose is a tool for defining and running multi-container Docker applications. With Compose, you use a YAML file to configure your application’s services. Then, with a single command, you create and start all the services from your configuration.

### Build

```bash
mvn clean install
```

### Run

If already don't have a ssh key:
```bash
ssh-keygen -b 4096 -C 'you@example.com' -f ~/.ssh/id_rsa -m PEM -N '' -q -t rsa
```
Add the key to GitHub https://github.com/settings/keys.

```bash
docker-compose up --build -d
```
