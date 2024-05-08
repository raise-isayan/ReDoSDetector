Burp suite extension ReDoSDetector
=============

Language/[Japanese](Readme-ja.md)

This tool is an extension of PortSwigger product, Burp Suite.
Supports Burp suite Professional/Community.

## Overview

This extension is a tool for ReDoS determination and detection.

It uses "https://makenowjust-labs.github.io/recheck/" as the library for ReDoS detection.

## About the latest version

The main repository (main) may contain code under development.
Please download the stable release version from the following.

* https://github.com/raise-isayan/ReDoSDetector/releases

Please use the following versions

* Burp suite v2023.1.2 or above

## How to Use

The Burp Suite Extender can be loaded by following the steps below.

1. Click [add] on the [Extender] tab
2. Click [Select file ...] and select ImageMetaExtract.jar.
3. Click [Next], confirm that no error is occurring, and close the dialog with [Close].

### ReDoSDetector Tab

ReDoSDetector tab will be added to Burp Suite.

#### Scan Tab

Manual ReDoS check can be performed.

![ReDoSDetector Tab Scan](/image/ReDoSDetectorTab-Scan.png)

#### Advance

Specifies ReDoS scan options.

![ReDoSDetector Tab Option](/image/ReDoSDetectorTab-Option.png)

For more information on scanning options, see below.

* https://makenowjust-labs.github.io/recheck/docs/usage/parameters/

## GUI Option

There is a GUI mode that runs standalone without Burp Suite.

````
java -jar ReDoSDetector.jar
````

## build

```
gradlew release
```

## Runtime environment

.Java
* JRE (JDK) 17 (Open JDK is recommended) (https://openjdk.java.net/)

.Burp suite
* v2023.1.2 or higher (http://www.portswigger.net/burp/)

## Development environment
* NetBean 21 (https://netbeans.apache.org/)
* Gradle 7.6 (https://gradle.org/)

## Use Library
Building requires a [BurpExtensionCommons](https://github.com/raise-isayan/BurpExtensionCommons) library.
* BurpExtensionCommons v3.1.x
  * https://github.com/raise-isayan/BurpExtensionCommons

## Use Library

* google gson (https://github.com/google/gson)
  * Apache License 2.0
  * https://github.com/google/gson/blob/master/LICENSE

* Universal Chardet for java (https://code.google.com/archive/p/juniversalchardet/)
  * MPL 1.1
  * https://code.google.com/archive/p/juniversalchardet/

* recheck core (https://mvnrepository.com/artifact/codes.quine.labs/recheck-core)
  * MIT license
  * https://makenowjust-labs.github.io/recheck/

Operation is confirmed with the following versions.
* Burp suite v2024.2.1

## important
This tool developed by my own personal use, PortSwigger company is not related at all. Please do not ask PortSwigger about problems, etc. caused by using this tool.
