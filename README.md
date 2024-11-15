[Google Play Store](https://play.google.com/store/apps/details?id=me.ljpb.yosetsukenai) (11/16 審査中)

アプリの機能：害虫忌避剤の使用を記録して，効果が切れる頃に通知を送ることで安心できるアプリ。

現時点におけるポイント：

1. 前回の開発[AlarmByNotification](https://github.com/LJPB/AlarmByNotification)は設計をせずに作ったため，コードの修正が困難だった。今回はその反省を踏まえてアプリの大まかな構造を事前に考えておいた。


現時点における問題点：

1. 技術に対する理解不足により，構造の変更が多々ある
2. 設計に関してUMLなどの一般的な記法/手法を使っていない
3. 開発中の変更点を設計図に反映していない
4. gitのcommitのルール(粒度，メッセージ等)を決めていないため，バージョン管理ができていない


現時点におけるよかった点：

1. 一つのクラス/ファイルに機能を詰め込みすぎないこと，依存関係がわかりやすいことを意識したおかげで，変更時の他コードへの影響範囲の把握が容易だった


### 概観
![overview.png](overview.png)

### UI
![ui_ver1.png](ui_images/ui_ver1.png)
