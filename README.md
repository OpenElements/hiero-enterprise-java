# Hiero Enterprise Java
This project provides Java modules to interact with a [Hiero network](https://hiero.org) in a Java Enterprise application.
The project provides integrations to Spring Boot or Eclipse Microprofile (like Quarkus) for interacting with Hiero.
This module is based on the [Hedera Java SDK](https://github.com/hashgraph/hedera-sdk-java)(will be migrated to Hiero Java SDK in near future) and provides a set of managed services to interact with a Hiero network.

> [!NOTE]  
> The repo is currently transformed to be compatible with the vendor neutral [Hiero](hiero.org) project.
> Hedera is 100% compatible with Hiero and the transformation is just a renaming of the packages, classes, documentation, and so on.
> The transformation is still in progress and not all is renamed yet.

## Spring Boot support

To use this module, you need to add the following dependency to your project:

```xml 
<dependency>
    <groupId>com.open-elements.hiero</groupId>
    <artifactId>hiero-enterprise-spring</artifactId>
    <version>VERSION</version> 
</dependency>
```

### Configuration

To configure the module, you need to add the following properties to your application.properties file:

```properties
spring.hiero.accountId=0.0.53854625
spring.hiero.privateKey=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
spring.hiero.network=hedera-testnet
```

The `spring.hiero.network` property defines the network that is used to interact with the Hiero network.
In the given example, the [Hedera](https://hedera.com) testnet network is used.
Hedera is based on Hiero and therefore the testnet can be used to interact with a Hiero network.
The account information (`accountId`, `privateKey`) can all be found at the [Hedera portal](https://portal.hedera.com/) for a testnet or previewnet account.
Today only the "DER Encoded Private Key" of the "ECDSA" key type is supported for the `spring.hiero.privateKey` property.

The 2 properties `spring.hiero.accountId` and `spring.hiero.privateKey` define the "operator account".
The operator account is used as the account that sends all transactions against the Hiero network.

### Usage

To use the module, you need to add the `@EnableHiero` annotation to your Spring Boot application class.

```java
import com.open.elements.spring.hiero.EnableHiero;

@SpringBootApplication
@EnableHiero
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
 
Once that is done you can for example autowire the `FileClient` class and call the methods to interact with a Hiero network.

```java

@Service
public class HieroAccountService {

    @Autowired
    private FileClient fileClient;

    public String readFile(String id) {
        FileId fileId = FileId.of(id);
        byte[] content = fileClient.readFile(fileId);
        return new String(content);
    }
}
```

All APIs of the client are synchronous and return the result of the operation.
For asynchronous operations, you can easily wrap calls by use the [`@Async` annotation of spring](https://spring.io/guides/gs/async-method).

### Hiero Spring Sample

A sample application that uses the Hiero Spring module can be found in the `hiero-enterprise-spring-sample` module.
The sample application is a simple Spring Boot application that reads has a REST endpoint at `localhost:8080/` and  shows the hbar balance of the account `0.0.100` on the Hedera testnet.
To use the application, you need to have created a Hedera testnet account at the [Hedera portal](https://portal.hedera.com/).
The account information can be added to the `application.properties` file in the `hedera-spring-sample` module:
```properties
spring.hiero.accountId=0.0.3447271
spring.hiero.privateKey=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
```

The module supports a `.env` file in the root of the module to define the account information.
The file is added to the `.gitignore` file and is not committed to the repository.
By doing that you can define the account information in the `.env` file without the risk of committing it to the repository.
The `.env` file should look like this:
```properties
spring.hiero.accountId=0.0.3447271
spring.hiero.privateKey=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
```

Alternatively, you can provide the account information as environment variables:
```shell
export HEDERA_ACCOUNT_ID=0.0.3447271
export HEDERA_PRIVATE_KEY=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
```

An external sample project can be found at https://github.com/hendrikebbers/spring-hiero-sample

## Microservice support

To use this module, you need to add the following dependency to your project:

```xml 
<dependency>
    <groupId>com.open-elements.hiero</groupId>
    <artifactId>hiero-enterprise-microprofile</artifactId>
    <version>VERSION</version> 
</dependency>
```

The `hiero-enterprise-microprofile-sample` module contains a sample application that uses the Hiero Microprofile module.
The sample application is a simple Quarkus application that reads has a REST endpoint at `localhost:8080/` and  shows
the hbar balance of the account `0.0.100` on the Hedera testnet.
For most of the part, the sample application is the same as the Spring Boot sample application.

A sample project can be found at https://github.com/hendrikebbers/quarkus-hiero-sample

## Managed services

The module provides a set of managed services that can be used to interact with a Hiero network.
The following services are available in spring and microprofile:

- `com.openelements.hiero.base.HieroContext`: component that provides the information about the Hiero network and the operator account
- `com.openelements.hiero.base.AccountClient`: to interact with accounts
- `com.openelements.hiero.base.FileClient`: to interact with files
- `com.openelements.hiero.base.FungibleTokenClient`: to interact with fungible tokens
- `com.openelements.hiero.base.NftClient`: to interact with NFTs
- `com.openelements.hiero.base.SmartContractClient`: to interact with smart contracts
- `com.openelements.hiero.base.verifcation.ContractVerificationClient`: to verify smart contracts
- `com.openelements.hiero.base.mirrornode.AccountRepository`: to query accounts
- `com.openelements.hiero.base.mirrornode.NetworkRepository`: to query network information
- `com.openelements.hiero.base.mirrornode.NftRepository`: to query NFTs
- `com.openelements.hiero.base.mirrornode.TransactionRepository`: to query transaction information

Next to that the following low-level services are available:

- `com.openelements.hiero.base.protocol.ProtocolLayerClient`: to interact with the Hiero protocol layer
- `com.openelements.hiero.base.mirrornode.MirrorNodeClient`: to query the Hiero mirror node 

## Built the project

The project is based on [Maven](https://maven.apache.org/).
To build the project, you can use the following command:

```shell
./mvnw verify
```

The tests in the project are working against any Hiero network.
You need to provide the account id and the private key of an account that is used to run the tests.
**If no account is provided, the tests will fail.**
The most easy way to run the tests is to use the Hedera testnet network.
To run the tests, you need to provide the account id and the "DER Encoded Private Key" of the "ECDSA" testnet account.
That information can be provided as environemt variables:
 
```shell
export HEDERA_ACCOUNT_ID=0.0.3447271
export HEDERA_PRIVATE_KEY=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
export HEDERA_NETWORK=testnet
```

As an alternative you can define the information in a `.env` file in each sub-module that contains tests.
The files are added to the `.gitignore` file and will not be committed to the repository.
The file should look like this:

```
spring.hiero.accountId=0.0.3447271
spring.hiero.privateKey=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
```

### Create a release

The project is using the [JReleaser](https://jreleaser.org) Maven plugin to create and publish releases.
To use the plugin, you need to provide the following environment variables:

```
JRELEASER_GITHUB_TOKEN=your_secret_github_token
JRELEASER_GPG_SECRET_KEY=your_secret_key
JRELEASER_GPG_PASSPHRASE=your_secret_passphrase
JRELEASER_GPG_PUBLIC_KEY=yout_public_key
JRELEASER_NEXUS2_MAVEN_CENTRAL_USERNAME=your_maven_central_username
JRELEASER_NEXUS2_MAVEN_CENTRAL_TOKEN=your_secret_maven_central_token
```

How that environment variables are defined can be found in the
[JReleaser documentation](https://jreleaser.org/guide/latest/examples/maven/maven-central.html) and 
[this blog post](https://foojay.io/today/how-to-release-a-java-module-with-jreleaser-to-maven-central-with-github-actions/).

On a unix based system the environment variables can be defined in the `.env` file in the root of the project.
The file is added to the `.gitignore` file and is not committed to the repository.

Once that is done you can use the `release.sh` script in the root folder of the repos to create a release:

```shell
./release 0.1.0 0.2.0-SNAPSHOT
```

This will create a release on GitHub and publish the artifacts to Maven Central.
As you can see 2 params are passed to the `release.sh` script, The first param defines the version for the release and the second param defines the version after the release.
In the given example the project has defined version 0.1.0-SNAPSHOT before the script is executed.
The execution will release the current code under version 0.1.0 and later switch the version to 0.2.0-SNAPSHOT and commit it.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
