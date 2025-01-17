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

package ohos.devtools.views.layout.swing;

import ohos.devtools.views.common.LayoutConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JLabel;
import java.util.Locale;

/**
 * 数字时钟计时器
 *
 * @version 1.0
 * @date 2021/03/25 17:00
 **/
public class CountingThread extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(CountingThread.class);

    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private JLabel jTextArea;
    private boolean stopFlag = false;

    public CountingThread(JLabel jTextArea) {
        this.jTextArea = jTextArea;
    }

    @Override
    public void run() {
        // 从0开始计时
        while (!stopFlag) {
            try {
                // 1毫秒更新一次显示
                sleep(LayoutConstants.NUMBER_THREAD);
            } catch (InterruptedException exception) {
                LOGGER.error(exception.getMessage());
            }

            if (seconds > LayoutConstants.NUMBER_SECONDS) {
                continue;
            }
            jTextArea.setText("|    " + String.format(Locale.ENGLISH, "%02d", hours) + ":" + String
                .format(Locale.ENGLISH, "%02d", minutes) + ":" + String.format(Locale.ENGLISH, "%02d", seconds));
            seconds++;
            if (seconds > LayoutConstants.NUMBER_SECONDS) {
                seconds = 0;
                minutes++;
                if (minutes > LayoutConstants.NUMBER_SECONDS) {
                    minutes = 0;
                    hours++;
                }
            }
        }
        jTextArea.setText("|   0:0:0");
    }

    /**
     * setStopFlag
     *
     * @param StopFlag StopFlag
     */
    public void setStopFlag(boolean StopFlag) {
        this.stopFlag = StopFlag;
    }

}
