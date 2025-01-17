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

package ohos.devtools.services.memory;

import org.junit.Test;

/**
 * @Description MemoryInstanceInfoTest
 * @Date 2021/4/2 10:39
 **/
public class MemoryInstanceInfoTest {
    /**
     * functional test
     *
     * @tc.name: getInstance
     * @tc.number: OHOS_JAVA_Service_MemoryInstanceInfo_getInstance_0001
     * @tc.desc: getInstance
     * @tc.type: functional testing
     * @tc.require: AR000FK61R
     */
    @Test
    public void getInstance() {
        MemoryInstanceInfo memoryInstanceInfo = new MemoryInstanceInfo();
        memoryInstanceInfo.setInstance("ddd");
        memoryInstanceInfo.setAllocTime(123712L);
        memoryInstanceInfo.setDeallocTime(123412L);
        memoryInstanceInfo.setId(32);
        memoryInstanceInfo.getInstance();
        memoryInstanceInfo.getAllocTime();
        memoryInstanceInfo.getDeallocTime();
        memoryInstanceInfo.getId();
        memoryInstanceInfo.toString();
    }
}