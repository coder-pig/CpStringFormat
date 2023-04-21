import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CpStringFormatConfig implements Configurable {
    private PluginSettingUI settingUI;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "CpStringFormat";
    }

    @Override
    public @Nullable JComponent createComponent() {
        settingUI = new PluginSettingUI();
        settingUI.updateAppId(PropertiesComponent.getInstance().getValue("baidu_app_id", ""));
        settingUI.updateKey(PropertiesComponent.getInstance().getValue("baidu_key", ""));
        return settingUI.getContentPanel();
    }

    @Override
    public boolean isModified() {
        // 在用户修改设置后，该方法将被调用，可以在该方法中检查设置是否已修改
        return true;
    }

    @Override
    public void apply() {
        PropertiesComponent.getInstance().setValue("baidu_app_id", settingUI.getAppId());
        PropertiesComponent.getInstance().setValue("baidu_key", settingUI.getKey());
    }

    @Override
    public void disposeUIResources() {
        settingUI = null;
    }
}
