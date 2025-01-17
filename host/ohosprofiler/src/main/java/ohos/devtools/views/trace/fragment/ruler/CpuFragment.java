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

package ohos.devtools.views.trace.fragment.ruler;

import ohos.devtools.views.trace.bean.CpuRateBean;
import ohos.devtools.views.trace.component.AnalystPanel;
import ohos.devtools.views.trace.listener.IRangeChangeListener;
import ohos.devtools.views.trace.util.ColorUtils;
import ohos.devtools.views.trace.util.Db;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * cpu graphics
 *
 * @version 1.0
 * @date 2021/04/22 12:25
 **/
public class CpuFragment extends AbstractFragment {
    private final Color shadowColor = new Color(0x99, 0x99, 0x99, 0xCE);
    private int leftX;
    private int rightX;
    private int selectX;
    private int selectY;

    /**
     * scale center x ，centerX is leftX or rightX
     */
    private int centerX;
    private IRangeChangeListener rangeChangeListener;

    /**
     * Gets the value of selectX .
     *
     * @return the value of int
     */
    public int getSelectX() {
        return selectX;
    }

    /**
     * Sets the selectX .
     * <p>You can use getSelectX() to get the value of selectX</p>
     *
     * @param selectX selectX
     */
    public void setSelectX(final int selectX) {
        this.selectX = selectX;
    }

    /**
     * Gets the value of selectY .
     *
     * @return the value of int
     */
    public int getSelectY() {
        return selectY;
    }

    /**
     * Sets the selectY .
     * <p>You can use getSelectY() to get the value of selectY</p>
     *
     * @param selectY selectY
     */
    public void setSelectY(final int selectY) {
        this.selectY = selectY;
    }

    private Map<Integer, List<CpuRateBean>> listMap;

    /**
     * CpuFragment Constructor
     *
     * @param root     parent
     * @param listener listener
     */
    public CpuFragment(final JComponent root, final IRangeChangeListener listener) {
        this.setRoot(root);
        getRect().setBounds(200, 22, root.getWidth() - 200, 72);
        this.rangeChangeListener = listener;
        ForkJoinPool.commonPool().submit(() -> {
            ArrayList<CpuRateBean> cpus = Db.getInstance().getCpuUtilizationRate();
            listMap = cpus.stream().collect(Collectors.groupingBy(cpuRateBean -> cpuRateBean.getCpu()));
            SwingUtilities.invokeLater(() -> {
                repaint();
            });
        });
    }

    /**
     * listener
     *
     * @param listener listener
     */
    public void setRangeChangeListener(final IRangeChangeListener listener) {
        this.rangeChangeListener = listener;
    }

    /**
     * draw method
     *
     * @param graphics graphics
     */
    @Override
    public void draw(final Graphics2D graphics) {
        getRect().width = getRoot().getWidth() - getRect().x;
        graphics.setColor(Color.white);
        graphics.fillRect(getRect().x, getRect().y, getRect().width, getRect().height);
        if (listMap != null && !listMap.isEmpty()) {
            int height = getRect().height / listMap.size();
            double rw = (getRect().width) / 100.00;
            listMap.forEach((map, beanList) -> {
                for (int index = 0, len = beanList.size(); index < len; index++) {
                    CpuRateBean cpuRateBean = beanList.get(index);
                    graphics.setStroke(new BasicStroke(0));
                    graphics.setComposite(
                        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) cpuRateBean.getRate()));
                    graphics.setColor(ColorUtils.MD_PALETTE[map]);
                    int side = (int) (getRect().x + rw * index);
                    graphics.fillRect(side, getRect().y + map * height, (int) rw + 1, height);
                    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            });
        }
        if (leftX == rightX && leftX != 0) {
            rightX = leftX + 1;
        }
        if (leftX > 0 && leftX <= getRect().x + getRect().width) {
            graphics.setColor(getRoot().getForeground());
            graphics.drawLine(leftX, getRect().y, leftX, getRect().y + getRect().height);
            graphics.setColor(shadowColor);
            graphics.fillRect(getRect().x, getRect().y, leftX - getRect().x, getRect().height);
            graphics.drawRect(getRect().x, getRect().y, leftX - getRect().x, getRect().height);
        }
        if (rightX < getRoot().getWidth() && rightX >= getRect().x) {
            graphics.setColor(getRoot().getForeground());
            graphics.drawLine(rightX, getRect().y, rightX, getRect().y + getRect().height);
            graphics.setColor(shadowColor);
            graphics.fillRect(rightX, getRect().y, getRect().x + getRect().width - rightX, getRect().height);
            graphics.drawRect(rightX, getRect().y, getRect().x + getRect().width - rightX, getRect().height);
        }
        graphics.setColor(getRoot().getForeground());
        graphics.drawRect(leftX, getRect().y, rightX - leftX, getRect().height);
    }

    /**
     * mouse dragged listener
     *
     * @param event event
     */
    public void mouseDragged(final MouseEvent event) {
        if (selectY > getRect().y && selectY < getRect().y + getRect().height) {
            if (event.getX() < selectX) {
                rightX = selectX;
                leftX = event.getX() <= getRect().x ? getRect().x : event.getX();
            } else {
                rightX = event.getX() >= getRoot().getWidth() ? getRoot().getWidth() : event.getX();
                leftX = selectX;
            }
            if (leftX == rightX) {
                if (leftX == getRect().x) {
                    rightX = leftX + 2;
                }
                if (rightX == getRoot().getWidth()) {
                    leftX = rightX - 2;
                }
            }
            if (rangeChangeListener != null) {
                if (selectX == leftX) {
                    centerX = leftX;
                }
                if (selectX == rightX) {
                    centerX = rightX;
                }
                rangeChangeListener.change(leftX, rightX, x2ns(leftX), x2ns(rightX), x2ns(centerX));
            }
            getRoot().repaint();
        }
    }

    /**
     * change time range
     *
     * @param sn startNs
     * @param en endNs
     */
    public void setRange(final long sn, final long en) {
        leftX = (int) (sn * getRect().width / AnalystPanel.DURATION) + getRect().x;
        rightX = (int) (en * getRect().width / AnalystPanel.DURATION) + getRect().x;
        repaint();
    }
}
