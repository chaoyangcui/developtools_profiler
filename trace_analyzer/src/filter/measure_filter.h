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

#ifndef THREAD_MEASURE_FILTER_H
#define THREAD_MEASURE_FILTER_H

#include <map>
#include <string_view>
#include <tuple>

#include "double_map.h"
#include "filter_base.h"
#include "trace_data_cache.h"
#include "trace_streamer_filters.h"

namespace SysTuning {
namespace TraceStreamer {
enum FilterType {
    E_THREADMEASURE_FILTER,
    E_THREAD_FILTER,
    E_PROCESS_MEASURE_FILTER,
    E_PROCESS_FILTER_FILTER,
    E_CPU_MEASURE_FILTER
};

class MeasureFilter : private FilterBase {
public:
    explicit MeasureFilter(TraceDataCache*, const TraceStreamerFilters*, FilterType);
    void Init(FilterType);
    MeasureFilter(const MeasureFilter&) = delete;
    MeasureFilter& operator=(const MeasureFilter&) = delete;
    ~MeasureFilter() override;
    uint32_t GetOrCreateCertainFilterId(uint64_t internalTid, DataIndex nameIndex);
    uint32_t GetOrCreateCertainFilterIdByCookie(uint64_t internalTid, DataIndex nameIndex, int64_t cookie);

private:
    void AddCertainFilterId(uint64_t internalTid, DataIndex nameIndex, uint64_t filterId);
    DoubleMap<uint64_t, DataIndex, uint64_t> tidStreamIdFilterIdMap_;
    DoubleMap<uint64_t, DataIndex, uint64_t> cookieFilterIdMap_;
    FilterType filterType_;

    const std::map<FilterType, std::string> filterTypeValue = {
        { E_THREADMEASURE_FILTER, "thread_counter_track" },
        { E_THREAD_FILTER, "thread_track" },
        { E_PROCESS_MEASURE_FILTER, "process_counter_track" },
        { E_PROCESS_FILTER_FILTER, "process_track" },
        { E_CPU_MEASURE_FILTER, "cpu_counter_track" }
    };
};
} // namespace TraceStreamer
} // namespace SysTuning
#endif // THREAD_MEASURE_FILTER_H
