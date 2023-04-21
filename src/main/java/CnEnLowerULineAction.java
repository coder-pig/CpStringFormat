import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;

public class CnEnLowerULineAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        if (selectedText == null) return;
        Runnable writeAction = () -> {
            String translationResult = TranslateUtil.fetchTranslateResult(selectedText);
            if (translationResult != null && !translationResult.trim().isEmpty()) {
                editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(),
                        translationResult.toLowerCase().replaceAll(" ", "_").replace(",", "").replace("\\", "").replace("/", ""));
            }
        };
        WriteCommandAction.runWriteCommandAction(editor.getProject(), writeAction);
    }
}
