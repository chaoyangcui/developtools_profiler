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

package ohos.devtools.views.trace.fragment;

import ohos.devtools.views.trace.bean.ProcessMem;
import ohos.devtools.views.trace.bean.ProcessMemData;
import ohos.devtools.views.trace.bean.ThreadData;
import ohos.devtools.views.trace.component.AnalystPanel;
import ohos.devtools.views.trace.component.ContentPanel;
import ohos.devtools.views.trace.util.Db;
import ohos.devtools.views.trace.util.Final;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Memory data line
 *
 * @version 1.0
 * @date 2021/04/22 12:25
 **/
public class MemDataFragment extends AbstractDataFragment<ProcessMemData> implements ThreadData.IEventListener {
    /**
     * graph event callback
     */
    public static ThreadData currentSelectedThreadData;

    /**
     * Process memory data
     */
    public List<ProcessMemData> data;

    /**
     * Process memory
     */
    public ProcessMem mem;
    private boolean isLoading;
    private Rectangle2D bounds;
    private int max;

    /**
     * structure
     *
     * @param root root
     * @param mem  mem
     */
    public MemDataFragment(JComponent root, ProcessMem mem) {
        this.mem = mem;
        this.setRoot(root);
    }

    /**
     * Drawing method
     *
     * @param graphics graphics
     */
    @Override
    public void draw(Graphics2D graphics) {
        super.draw(graphics);
        graphics.setFont(Final.NORMAL_FONT);
        graphics.setColor(getRoot().getForeground());
        String name = mem.getTrackName();
        bounds = graphics.getFontMetrics().getStringBounds(name, graphics);
        double wordWidth = bounds.getWidth() / name.length(); // Width per character
        double wordNum = (getDescRect().width - 40) / wordWidth; // How many characters can be displayed on each line
        if (bounds.getWidth() < getDescRect().width - 40) { // Direct line display
            graphics.drawString(name, getDescRect().x + 10, (int) (getDescRect().y + bounds.getHeight() + 10));
        } else {
            String substring = name.substring((int) wordNum);
            if (substring.length() < wordNum) {
                graphics.drawString(name.substring(0, (int) wordNum), getDescRect().x + 10,
                    (int) (getDescRect().y + bounds.getHeight() + 8));
                graphics
                    .drawString(substring, getDescRect().x + 10, (int) (getDescRect().y + bounds.getHeight() * 2 + 8));
            } else {
                graphics.drawString(name.substring(0, (int) wordNum), getDescRect().x + 10,
                    (int) (getDescRect().y + bounds.getHeight() + 2));
                graphics.drawString(substring.substring(0, (int) wordNum), getDescRect().x + 10,
                    (int) (getDescRect().y + bounds.getHeight() * 2 + 2));
            }
        }
        drawData(graphics);
    }

    private void drawData(Graphics2D graphics) {
        if (data != null) {
            List<ProcessMemData> collect = data.stream().filter(
                memData -> memData.getStartTime() + memData.getDuration() > startNS && memData.getStartTime() < endNS)
                .collect(Collectors.toList());
            int x1;
            int x2;
            for (int index = 0, len = collect.size(); index < len; index++) {
                ProcessMemData memData = collect.get(index);
                if (index == len - 1) {
                    memData.setDuration(AnalystPanel.DURATION);
                } else {
                    memData.setDuration(collect.get(index + 1).getStartTime() - memData.getStartTime());
                }
                if (memData.getStartTime() < startNS) {
                    x1 = getX(startNS);
                } else {
                    x1 = getX(memData.getStartTime());
                }
                if (memData.getStartTime() + memData.getDuration() > endNS) {
                    x2 = getX(endNS);
                } else {
                    x2 = getX(memData.getStartTime() + memData.getDuration());
                }
                memData.root = getRoot();
                memData
                    .setRect(x1 + getDataRect().x, getDataRect().y, x2 - x1 <= 0 ? 1 : x2 - x1, getDataRect().height);
                memData.setMaxValue(max);
                memData.draw(graphics);
            }
        } else {
            graphics.setColor(getRoot().getForeground());
            graphics.drawString("Loading...", getDataRect().x, getDataRect().y + 12);
            loadData();
        }
    }

    /**
     * Mouse clicked event
     *
     * @param event event
     */
    @Override
    public void mouseClicked(MouseEvent event) {
    }

    /**
     * Mouse pressed event
     *
     * @param event event
     */
    @Override
    public void mousePressed(MouseEvent event) {
    }

    /**
     * Mouse exited event
     *
     * @param event event
     */
    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        clearFocus(event);
        JComponent component = getRoot();
        if (component instanceof ContentPanel) {
            ContentPanel root = ((ContentPanel) component);
            root.refreshTab();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    private void loadData() {
        if (!isLoading) {
            isLoading = true;
            ForkJoinPool.commonPool().submit(() -> {
                data = Db.getInstance().getProcessMemData(mem.getTrackId());
                SwingUtilities.invokeLater(() -> {
                    data.stream().mapToInt(memData -> memData.getValue()).max().ifPresent(maxData -> {
                        max = maxData;
                    });
                    isLoading = false;
                    repaint();
                });
            });
        }
    }

    /**
     * Mouse click event
     *
     * @param event event
     * @param data  data
     */
    @Override
    public void click(MouseEvent event, ThreadData data) {
        clearSelected();
        data.select(true);
        data.repaint();
        currentSelectedThreadData = data;
        if (AnalystPanel.iThreadDataClick != null) {
            AnalystPanel.iThreadDataClick.click(data);
        }
    }

    /**
     * Mouse blur event
     *
     * @param event event
     * @param data  data
     */
    @Override
    public void blur(MouseEvent event, ThreadData data) {
    }

    /**
     * Mouse focus event
     *
     * @param event event
     * @param data  data
     */
    @Override
    public void focus(MouseEvent event, ThreadData data) {
    }

    /**
     * Mouse move event
     *
     * @param event event
     * @param data  data
     */
    @Override
    public void mouseMove(MouseEvent event, ThreadData data) {
        getRoot().repaint();
    }
}
