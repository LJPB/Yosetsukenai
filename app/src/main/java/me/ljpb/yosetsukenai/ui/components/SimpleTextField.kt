package me.ljpb.yosetsukenai.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    textColor: Color,
    textAlign: TextAlign,
    placeholderText: String,
    placeholderTextColor: Color,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    // 参考 : https://mikan-tech.hatenablog.jp/entry/2023/05/29/113743
    // (該当箇所へのリンク) https://mikan-tech.hatenablog.jp/entry/2023/05/29/113743#:~:text=VisualTransformation%E3%82%92%E4%BD%BF%E3%81%A3%E3%81%A6%E5%85%A5%E5%8A%9B%E6%96%87%E5%AD%97%E5%88%97%E3%81%8C%E7%A9%BA%E3%81%AE%E3%81%A8%E3%81%8D%E3%81%AE%E8%A6%8B%E3%81%9F%E7%9B%AE%E3%81%A0%E3%81%91%E5%A4%89%E3%81%88%E3%81%A6%E3%80%81%E3%82%AB%E3%83%BC%E3%82%BD%E3%83%AB%E3%82%92%E5%B8%B8%E3%81%AB%E5%B7%A6%E7%AB%AF%E3%81%AB%E5%AF%84%E3%81%9B%E3%81%A4%E3%81%A4placeholder%E3%81%A3%E3%81%BD%E3%81%84%E3%82%82%E3%81%AE%E3%82%92%E4%BD%9C%E3%82%8B
    
    // placeholderが表示されている時にTextFieldにフォーカスが当たった時の最初のカーソルの位置
    val placeholderOffset = when (textAlign) {
        // textAlignがEndの時は，カーソルも右端におきたい
        TextAlign.End -> placeholderText.length
        // その他の場合は，先頭におく
        else -> 0
    }
    TextOnlyField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle.copy(textAlign = textAlign),
        textColor = if (value == "") placeholderTextColor else textColor,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = VisualTransformation { text ->
            // 実際の値(value)が空かどうか
            val showPlaceHolder = text.text.isEmpty()

            val offsetMapping = object : OffsetMapping { // 表示する文字列に対するカーソル位置と，実際の文字列に対するカーソル位置の相互変換

                // placeholderが表示されていないときは実際の文字列が表示されるため，表示する文字列と実際の文字列との間でカーソルの位置(offset)は変わらない
                // よって，offsetをそのまま返せばいい (つまりelse offset)
                // placeholderが表示されているとき，実際の値は空だから表示するカーソル位置に関わらず，実際の文字列に対するカーソル位置は0にしかできない
                // (実際の文字列が空なため，2文字目3文字目などの位置にカーソルを置けない)
                // よって，transformedToOriginal(表示するカーソル位置を実際の文字列に対するカーソル位置に変換)は常に0を返す
                // 一方，表示するカーソル位置はどこでもいい。文字を入力した場合は，上述の通りカーソル位置が揃うため，入力前のカーソル位置は問題にならないからである
                
                // 実際の文字列に対するカーソル位置を表示する文字列に対するカーソル位置に変換
                // 例：実際の文字列 1234|567 → 表示する文字列 123-4|567 を期待する
                // この場合，実際の文字列のカーソル位置は4，表示する文字列のカーソル位置は5となるため，この変換が必要
                override fun originalToTransformed(offset: Int): Int =
                    if (showPlaceHolder) placeholderOffset else offset

                // 表示するカーソル位置を実際の文字列に対するカーソル位置に変換
                // 例：表示する文字列 123-4|567 → 実際の文字列 1234|567 を期待する
                // この場合，表示する文字列のカーソル位置は5，実際の文字列のカーソル位置は4となるため，この変換が必要
                override fun transformedToOriginal(offset: Int): Int =
                    if (showPlaceHolder) 0 else offset
            }
            TransformedText(
                // 実際の値(value)が空の場合は，placeholderが表示される
                // 実際の値(value)が空ではない場合は，実際の文字列がそのまま表示される
                AnnotatedString(text = text.text.ifEmpty { placeholderText }),
                offsetMapping
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SimpleTextFieldPreview() {
    SimpleTextField(
        value = "",
        onValueChange = {},
        textStyle = MaterialTheme.typography.bodyLarge,
        textColor = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        placeholderText = "placeholder",
        placeholderTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}