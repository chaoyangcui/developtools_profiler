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

import ohos.devtools.views.trace.bean.CpuData;
import ohos.devtools.views.trace.bean.CpuFreqData;
import ohos.devtools.views.trace.component.AnalystPanel;
import ohos.devtools.views.trace.fragment.graph.CheckGraph;
import ohos.devtools.views.trace.fragment.graph.FavoriteGraph;

import javax.swing.JComponent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;

/**
 * cpu frequency data
 *
 * @version 1.0
 * @date 2021/04/22 12:25
 **/
public class CpuFreqDataFragment extends AbstractDataFragment<CpuFreqData> implements CpuData.IEventListener {
    /**
     * The currently selected cpu frequency data
     */
    public static CpuData currentSelectedCpuData;

    /**
     * cpu frequency data collection
     */
    public List<CpuFreqData> data;

    /**
     * Favorite button
     */
    public FavoriteGraph favoriteGraph;

    /**
     * Select button
     */
    public CheckGraph checkGraph;

    private double x1;

    private double x2;

    private Rectangle2D bounds;

    private String name;

    private Map<String, Object> cpuMaxFreq;

    /**
     * Construction method
     *
     * @param root       root
     * @param name       name
     * @param cpuMaxFreq cpuMaxFreq
     * @param data       data
     */
    public CpuFreqDataFragment(JComponent root, String name, Map<String, Object> cpuMaxFreq, List<CpuFreqData> data) {
        this.name = name;
        this.setRoot(root);
        this.data = data;
        this.cpuMaxFreq = cpuMaxFreq;
        favoriteGraph = new FavoriteGraph(this, root);
        checkGraph = new CheckGraph(this, root);
    }

    /**
     * Drawing method
     *
     * @param graphics graphics
     */
    @Override
    public void draw(Graphics2D graphics) {
        super.draw(graphics);

        // Supplement the information on the left
        graphics.setColor(getRoot().getForeground());
        bounds = graphics.getFontMetrics().getStringBounds(name, graphics);
        graphics.drawString(name, getDescRect().x + 10,
            (int) (getDescRect().y + (getDescRect().height) / 2 + bounds.getHeight() / 3));
        favoriteGraph.setRightGraph(isSelected != null ? checkGraph : null);
        checkGraph.setChecked(isSelected);
        checkGraph.draw(graphics);
        favoriteGraph.draw(graphics);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f)); // transparency
        String cupFreqName = cpuMaxFreq.get("name").toString();
        Object value = cpuMaxFreq.get("value");
        data.stream().filter(cpuFreqData -> cpuFreqData.getStartTime() + cpuFreqData.getDuration() > startNS
            && cpuFreqData.getStartTime() < endNS).forEach(cpuGraph -> {
            if (cpuGraph.getStartTime() <= startNS) {
                x1 = 0;
            } else {
                x1 = getXDouble(cpuGraph.getStartTime());
            }
            if (cpuGraph.getStartTime() + cpuGraph.getDuration() >= endNS) {
                x2 = getDataRect().width;
            } else {
                x2 = getXDouble(cpuGraph.getStartTime() + cpuGraph.getDuration());
            }
            if (value instanceof Double) {
                cpuGraph.setMax((Double) value);
            }
            cpuGraph.setRoot(getRoot());
            cpuGraph.setRect(x1 + getDataRect().x, getDataRect().y, x2 - x1 <= 0 ? 1 : x2 - x1, getDataRect().height);
            cpuGraph.draw(graphics);
        });
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // transparency
        bounds = graphics.getFontMetrics().getStringBounds(cupFreqName, graphics);
        graphics.setColor(Color.lightGray);
        graphics.drawString(cupFreqName, getDataRect().x + 2, (int) (getDataRect().y + 2 + bounds.getHeight()));
    }

    /**
     * Mouse click event
     *
     * @param event event
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        if (favoriteGraph.edgeInspect(event)) {
            favoriteGraph.onClick(event);
        }
        if (checkGraph.edgeInspect(event)) {
            checkGraph.onClick(event);
        }
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

    /**
     * Mouse entered event
     *
     * @param event event
     */
    @Override
    public void mouseEntered(MouseEvent event) {
    }

    /**
     * Mouse move event
     *
     * @param event event
     */
    @Override
    public void mouseMoved(MouseEvent event) {
        clearFocus(event);
        favoriteGraph.display(edgeInspectRect(getDescRect(), event));
        if (favoriteGraph.edgeInspect(event)) {
            if (!favoriteGraph.flagFocus) {
                favoriteGraph.flagFocus = true;
                favoriteGraph.onFocus(event);
            }
        } else {
            if (favoriteGraph.flagFocus) {
                favoriteGraph.flagFocus = false;
                favoriteGraph.onBlur(event);
            }
        }
        if (edgeInspect(event)) {
            data.forEach(cpuFreqData -> {
                if (cpuFreqData.edgeInspect(event)) {
                    if (!cpuFreqData.isFlagFocus()) {
                        cpuFreqData.setFlagFocus(true);
                        cpuFreqData.onFocus(event);
                    }
                } else {
                    if (cpuFreqData.isFlagFocus()) {
                        cpuFreqData.setFlagFocus(false);
                        cpuFreqData.onBlur(event);
                    }
                }
            });
        }
    }

    /**
     * Mouse released event
     *
     * @param event event
     */
    @Override
    public void mouseReleased(MouseEvent event) {
    }

    /**
     * Click event
     *
     * @param event   event
     * @param cpuData cpuData
     */
    @Override
    public void click(MouseEvent event, CpuData cpuData) {
        if (currentSelectedCpuData != null) {
            currentSelectedCpuData.select(false);
            currentSelectedCpuData.repaint();
        }
        cpuData.select(true);
        cpuData.repaint();
        currentSelectedCpuData = cpuData;
        if (AnalystPanel.iCpuDataClick != null) {
            AnalystPanel.iCpuDataClick.click(cpuData);
        }
    }

    /**
     * Loss of focus event
     *
     * @param event event
     * @param data  data
     */
    @Override
    public void blur(MouseEvent event, CpuData data) {
    }

    /**
     * Get focus event
     *
     * @param event event
     * @param data  data
     */
    @Override
    public void focus(MouseEvent event, CpuData data) {
    }

    /**
     * Mouse move event
     *
     * @param event event
     * @param data  data
     */
    @Override
    public void mouseMove(MouseEvent event, CpuData data) {
    }
}
