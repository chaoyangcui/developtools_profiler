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

#ifndef TRACE_STREAMERTOKEN_H
#define TRACE_STREAMERTOKEN_H

#include <memory>

namespace SysTuning {
namespace TraceStreamer {
class SliceFilter;
class ProcessFilter;
class EventFilter;
class CpuFilter;
class MeasureFilter;
class FilterFilter;
class ClockFilter;

class TraceStreamerFilters {
public:
    TraceStreamerFilters();
    ~TraceStreamerFilters();

    std::unique_ptr<ClockFilter> clockFilter_;
    std::unique_ptr<FilterFilter> filterFilter_;
    std::unique_ptr<SliceFilter> sliceFilter_;
    std::unique_ptr<ProcessFilter> processFilter_;
    std::unique_ptr<CpuFilter> cpuFilter_;
    std::unique_ptr<EventFilter> eventFilter_;
    std::unique_ptr<MeasureFilter> cpuCounterFilter_;
    std::unique_ptr<MeasureFilter> threadCounterFilter_;
    std::unique_ptr<MeasureFilter> threadFilter_;
    std::unique_ptr<MeasureFilter> processCounterFilter_;
    std::unique_ptr<MeasureFilter> processFilterFilter_;
};
} // namespace TraceStreamer
} // namespace SysTuning

#endif // TRACE_STREAMERTOKEN_H
