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
 * Scroll distance change listener
 *
 * @version 1.0
 * @date 2021/04/22 12:25
 **/
public interface IScrollSliceLinkListener {
    /**
     * The currently clicked data object
     *
     * @param bean bean
     */
    void linkClick(Object bean);
}