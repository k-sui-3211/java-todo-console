# Java ToDo Console App

Javaで作成した、シンプルなコンソールベースのToDoリストアプリです。  
現在はタスクの「追加・編集・完了・削除」に加え、**SQLiteデータベースを使った保存・復元機能**を実装済みです。  
各タスクには一意のIDが自動付与され、操作時にはこのIDをもとにデータベースと連携されます。

## 機能

- タスクの追加／編集／完了／削除
- タスクごとに一意のIDを自動採番（データベース連携用）
- 入力バリデーション（1〜6の範囲チェック、文字入力の排除など）
- 例外処理対応（`InputMismatchException` / `SQLException`）
- SQLiteデータベースへの保存・復元
  - 起動時に前回のタスクを自動復元
  - データ更新時に自動保存
- 表示モード切り替え（全件／未完了／完了）
- UTF-8対応で日本語タスクも安全に保存可能

## 実行方法

以下をまとめてコマンドラインに貼り付ければ、コンパイルと実行が一度で完了します。

javac -encoding UTF-8 -cp ".;sqlite-jdbc-3.36.0.3.jar" Main.java TaskManager.java Task.java && java -cp ".;sqlite-jdbc-3.36.0.3.jar" Main

※ tasks.db というSQLiteファイルが実行時に自動生成されます。
※ sqlite-jdbc-3.36.0.3.jar は同じディレクトリに配置してください。

## 開発環境

- Java 17
- Visual Studio Code
- Git / GitHub
- SQLite（JDBCドライバ v3.36.0.3）

## 構造

- Main：ユーザー入力の受付と操作メニューの表示
- TaskManager：タスク管理（タスクの追加・編集・完了・削除など）
- TaskRepository：DBアクセス専用クラス（CRUD処理）
- DatabaseHelper：SQL実行の汎用クラス
- Task:タスクの状態を管理するクラス

## 今後の展望

- タスクに「締切日」や「優先度」などの属性を追加
- タイトルや日付によるソート機能の実装
- GUI化（JavaFX）
- 完了済タスクの一括削除や検索機能の導入

## ライセンス

MIT License

## 📝 備考
このアプリは、ChatGPTのサポートを活用しながら開発しています。
自ら実行・検証・設計判断を行い、理解を深めながら学習用ポートフォリオとして作成しています。



