import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;

public class CnEnWordAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        if (selectedText == null) return;
        Runnable writeAction = () -> {
            // 根据空格分割成单词数组，遍历首字母大写拼接
            String translationResult = TranslateUtil.fetchTranslateResult(selectedText);
            if (translationResult != null && !translationResult.trim().isEmpty()) {
                String[] words = translationResult.split(" ");
                if (words.length == 0) return;
                StringBuilder sb = new StringBuilder();
                for (String word : words) sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
                editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(),
                        sb.toString().replace(",", "").replace("\\", "").replace("/", ""));
            }
        };
        WriteCommandAction.runWriteCommandAction(editor.getProject(), writeAction);
    }
}
