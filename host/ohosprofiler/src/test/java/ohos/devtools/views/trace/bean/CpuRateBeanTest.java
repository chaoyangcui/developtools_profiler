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

package ohos.devtools.views.trace.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test CpuRateBean class
 *
 * @version 1.0
 * @date 2021/4/24 18:04
 **/
class CpuRateBeanTest {
    /**
     * test get the number of cpu .
     */
    @Test
    void getCpu() {
        assertEquals(3, new CpuRateBean() {{
            setCpu(3);
        }}.getCpu());
    }

    /**
     * test set the number of cpu .
     */
    @Test
    void setCpu() {
        assertEquals(3, new CpuRateBean() {{
            setCpu(3);
        }}.getCpu());
    }

    /**
     * test get the index .
     */
    @Test
    void getIndex() {
        assertEquals(3, new CpuRateBean() {{
            setIndex(3);
        }}.getIndex());
    }

    /**
     * test set the index .
     */
    @Test
    void setIndex() {
        assertEquals(3, new CpuRateBean() {{
            setIndex(3);
        }}.getIndex());
    }

    /**
     * test get the rate .
     */
    @Test
    void getRate() {
        assertEquals(3.0D, new CpuRateBean() {{
            setRate(3.0D);
        }}.getRate());
    }

    /**
     * test set the rate .
     */
    @Test
    void setRate() {
        assertEquals(3.0D, new CpuRateBean() {{
            setRate(3.0D);
        }}.getRate());
    }
}