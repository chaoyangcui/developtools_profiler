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

package ohos.devtools.views.trace.listener;

/**
 * Timeline range change callback listener
 *
 * @version 1.0
 * @date 2021/04/22 12:25
 **/
public interface IRangeChangeListener {
    /**
     * Range change callback
     *
     * @param leftX x-axis left coordinate
     * @param rightX x-axis right coordinate
     * @param leftNS Nanoseconds on the left
     * @param rightNS Nanoseconds on the right
     * @param centerNS Nanoseconds on the center
     */
    void change(int leftX, int rightX, long leftNS, long rightNS, long centerNS);
}
