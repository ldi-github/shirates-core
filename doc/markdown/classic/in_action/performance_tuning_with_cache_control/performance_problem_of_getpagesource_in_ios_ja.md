# iOSにおけるgetPageSource実行時のパフォーマンス問題 (Classic)

要素を選択する前に**Shirates/Classic** は`getPageSource()`を実行してソースXMLを取得し、**TestElementCache** に格納します。
TestElementCache内の要素に対して柔軟な問い合わせ（例：相対セレクター）を行うことができます。

残念ながらiOSのWebDriverAgentにはgetPageSource()
に関するパフォーマンス問題があります。画面上に多くの要素が存在する場合、getPageSource()
を実行すると数分必要となる場合があります。この問題は要素のツリー全体を取得するAPIが提供されていないことに起因していると言われています。この問題を解決するにはAppleがそのようなAPIを提供する必要があります。

この深刻なパフォーマンス問題を回避するために、テストコードを **ダイレクトアクセスモード** を使用してチューニングすることができます。
このモードは機能的に制限がありますが画面上の全要素の取得は必要ありません。

### Link

- [ダイレクトアクセスモード](direct_access_mode_ja.md)


- [index](../../index_ja.md)
