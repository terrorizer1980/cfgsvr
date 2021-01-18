# Configuration Server

## Overview 🔍

Unlike a monolithic app in which everything runs within a single instance, a cloud-native application consists of independent services distributed across virtual machines, containers, and geographic regions. Managing configuration settings for dozens of interdependent services can be challenging. Duplicate copies of configuration settings across different locations is error prone and difficult to manage. Centralized configuration is a critical requirement for distributed cloud-native applications.

Configuration must be stored externally from the application and read-in as needed. Storing configuration values as constants or literal values in code is a violation. The same configuration values are often be used by many services in the same application. Additionally, we must support the same values across multiple environments, such as dev, testing, and production. The best practice is store them in a centralized configuration store.

## Author 🖋️
* **Eliezer Efrain Chavez** -  [eliezerchavez](https://www.linkedin.com/in/eliezerchavez)

### Pre-Requisites 📋

* [Maven](https://maven.apache.org/users/index.html) <br />
  Apache Maven is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information.
* [Spring Boot](https://spring.io/projects/spring-boot) <br />
  Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
* [Spring Cloud](https://spring.io/projects/spring-cloud) <br />
  Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, one-time tokens, global locks, leadership election, distributed sessions, cluster state).
* [JUnit 5](https://junit.org/junit5/) <br />
  The JUnit Platform serves as a foundation for launching testing frameworks on the JVM. It also defines the TestEngine API for developing a testing framework that runs on the platform. JUnit 5 is the next generation of JUnit. The goal is to create an up-to-date foundation for developer-side testing on the JVM. This includes focusing on Java 8 and above, as well as enabling many different styles of testing.
* [Docker](https://www.docker.com/get-started) <br />
  A set of Platform as a Service (PaaS) products that use OS-level virtualization to deliver software in packages called containers. Containers are isolated from one another and bundle their own software, libraries and configuration files; they can communicate with each other through well-defined channels.
* [Docker Compose](https://docs.docker.com/compose/) <br />
  Compose is a tool for defining and running multi-container Docker applications. With Compose, you use a YAML file to configure your application’s services. Then, with a single command, you create and start all the services from your configuration.

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```
or
```bash
docker-compose up --build -d
```

### Related Projects
* The following are the configuration repositories used in this project:
  * [Production](https://github.com/eliezerchavez/cfgrepo-prd) Environment
  * [Pre-Production](https://github.com/eliezerchavez/cfgrepo-pre) Environments

  **IMPORTANT** <br />
  If you're gonna use ssh URIs as source for the config repositories, please execute the following:
  ```bash
  ssh-keygen -b 4096 -C 'you@example.com' -f ~/.ssh/id_rsa -m PEM -N '' -q -t rsa
  ```
  and add the generated public key to [GitHub](https://github.com/settings/keys).

* You can automate the build, test and publishing of this project using the [CI/CD Toolchain](https://github.com/eliezerchavez/cicd-toolchain)
