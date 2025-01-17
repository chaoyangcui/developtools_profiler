/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ohos.devtools;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import ohos.devtools.datasources.databases.databaseapi.DataBaseApi;
import ohos.devtools.datasources.utils.device.service.MultiDeviceManager;
import ohos.devtools.datasources.utils.profilerlog.ProfilerLogManager;
import ohos.devtools.datasources.utils.quartzmanager.QuartzManager;
import ohos.devtools.views.layout.swing.HomeWindow;
import ohos.devtools.views.layout.swing.LayoutView;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Profiler Tool Window Factory
 *
 * @version 1.0
 * @Date 2021/3/31 14:38
 **/
public class ProfilerToolWindowFactory implements ToolWindowFactory {
    private static final Logger LOGGER = LogManager.getLogger(ProfilerToolWindowFactory.class);

    /**
     * createToolWindowContent
     *
     * @param project    project
     * @param toolWindow toolWindow
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        LOGGER.error("ohos Profiler Start OS is {}", System.getProperty("os.name"));
        ProfilerLogManager.getSingleton().updateLogLevel(Level.ERROR);
        DataBaseApi apo = DataBaseApi.getInstance();
        apo.initDataSourceManager();
        QuartzManager instance = QuartzManager.getInstance();
        String name = MultiDeviceManager.class.getName();
        MultiDeviceManager manager = MultiDeviceManager.getInstance();
        instance.addExecutor(name, manager);
        instance.startExecutor(name, QuartzManager.DELAY, QuartzManager.PERIOD);
        HomeWindow homeWindow = LayoutView.init();
        toolWindow.getContentManager()
            .addContent(ContentFactory.SERVICE.getInstance().createContent(homeWindow, "", false));
    }
}
