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

package ohos.devtools.views.charts.model;

/**
 * Timeline和Chart数据的时间范围
 *
 * @since 2021/1/29 9:26
 */
public class ChartDataRange {
    /**
     * 起始时间
     */
    private int startTime = Integer.MIN_VALUE;

    /**
     * 结束时间
     */
    private int endTime = Integer.MAX_VALUE;

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ChartDataRange{" + "startTime=" + startTime + ", endTime=" + endTime + '}';
    }
}
