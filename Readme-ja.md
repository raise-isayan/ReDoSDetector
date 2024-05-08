Burp suite 拡張 ReDoSDetector
=============

Language/[English](Readme.md)

このツールは、PortSwigger社の製品であるBurp Suiteの拡張になります。
Burp Pro/Communityに対応しています。

## 概要

この拡張はReDoSの判定および検出を目的としたツールです。

ReDoS判定のライブラリに"https://makenowjust-labs.github.io/recheck/"を利用しています。

## 最新版について

メインのリポジトリ(main)には開発中のコードが含まれている場合があります。
安定したリリース版は､以下よりダウンロードしてください。

* https://github.com/raise-isayan/ReDoSDetector/releases

利用するバージョンは以下のものをご利用ください

* Burp suite v2023.1.2 より後のバージョン

## 利用方法

Burp suite の Extender は以下の手順で読み込めます。

1. [Extender]タブの[add]をクリック
2. [Select file ...]をクリックし、ReDoSDetector.jar を選択する。
3. ｢Next｣をクリックし、エラーがでてないことを確認後、「Close」にてダイヤログを閉じる。

### ReDoSDetector タブ

Burp SuiteにReDoSDetectorタブが追加されます。

#### Scan タブ

手動でのReDoSのチェックを行えます。

![ReDoSDetector Tab Scan](/image/ReDoSDetectorTab-Scan.png)

#### Advance

ReDoSのスキャンオプションを指定します。

![ReDoSDetector Tab Option](/image/ReDoSDetectorTab-Option.png)

スキャンオプションの詳細については以下を参照ください。

* https://makenowjust-labs.github.io/recheck/docs/usage/parameters/

## GUIオプション

Burp Suite不要のスタンドアローンで起動するGUIモードが存在します。

````
java -jar ReDoSDetector.jar
````

## ビルド

```
gradlew release
```

## 実行環境

.Java
* JRE (JDK) 17 (Open JDK is recommended) (https://openjdk.java.net/)

.Burp suite
* v2023.1.2 or higher (http://www.portswigger.net/burp/)

## 開発環境
* NetBean 21 (https://netbeans.apache.org/)
* Gradle 7.6 (https://gradle.org/)

## 必須ライブラリ
ビルドには別途 [BurpExtensionCommons](https://github.com/raise-isayan/BurpExtensionCommons) のライブラリを必要とします。
* BurpExtensionCommons v3.1.x
  * https://github.com/raise-isayan/BurpExtensionCommons

## 利用ライブラリ

* google gson (https://github.com/google/gson)
  * Apache License 2.0
  * https://github.com/google/gson/blob/master/LICENSE

* Universal Chardet for java (https://code.google.com/archive/p/juniversalchardet/)
  * MPL 1.1
  * https://code.google.com/archive/p/juniversalchardet/

* recheck core (https://mvnrepository.com/artifact/codes.quine.labs/recheck-core)
  * MIT license
  * https://makenowjust-labs.github.io/recheck/

以下のバージョンで動作確認しています。
* Burp suite v2024.2.1

## 注意事項
このツールは、私個人が勝手に開発したもので、PortSwigger社は一切関係ありません。本ツールを使用したことによる不具合等についてPortSwiggerに問い合わせないようお願いします。
