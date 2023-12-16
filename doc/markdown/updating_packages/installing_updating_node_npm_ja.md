# nodeとnpmのインストール/アップデート

## nodeのインストール

### (Mac)

**node(Node.js)** をbrewでインストールできます。ターミナルウィンドウを開いて以下をタイプします。

```
brew install node
node -v
```

### (Windows)

インストールパッケージをダウンロードします。
https://nodejs.org/en/download/

<br>

**注意:** Appiumのインストールのトラブルを回避するために比較的新しいバージョンを使用してください。

<hr>

## **n**のインストール

`n` node用のパッケージ管理システムです。 (https://github.com/tj/n)

```
npm install -g n
```

<hr>

## nのアップデート

### (Mac)

```
n --version
npm update -g n
n --version
```

## nodeのアップデート

### (Mac)

```
node -v
sudo n latest
node -v
```

## npmのアップデート

### (Mac)

```
npm -v
npm update -g npm
npm -v
```

### Link

- [index](../index_ja.md)
