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

package ohos.devtools.views.common;

/**
 * Profiler的监控项
 *
 * @since 2021/2/26 9:41
 */
public enum ProfilerMonitorItem {
    CPU("Cpu"), MEMORY("Memory"), NETWORK("Network"), ENERGY("Energy"), DISK_IO("DiskIO"), UNRECOGNIZED("Unrecognized");

    private final String name;

    ProfilerMonitorItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
