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

#ifndef TRACE_DATA_CACHE_READER_H
#define TRACE_DATA_CACHE_READER_H

#include "log.h"
#include "trace_data_cache_base.h"
#include "trace_stdtype.h"

namespace SysTuning {
namespace TraceStreamer {
using namespace TraceStdtype;
class TraceDataCacheReader : public TraceDataCacheBase {
public:
    TraceDataCacheReader() = default;
    TraceDataCacheReader(const TraceDataCacheReader&) = delete;
    TraceDataCacheReader& operator=(const TraceDataCacheReader&) = delete;
    ~TraceDataCacheReader() override;
public:
    const std::string& GetDataFromDict(DataIndex id) const;
    const Process& GetConstProcessData(InternalPid internalPid) const;
    const Thread& GetConstThreadData(InternalTid internalTid) const;
    const InternalSlices& GetConstInternalSlicesData() const;
    const Filter& GetConstFilterData() const;
    const Raw& GetConstRawTableData() const;
    const Counter& GetConstCounterData() const;
    const ThreadCounterFilter* ThreadCounterFilterData() const;
    const ThreadState& GetConstThreadStateData() const;
    const SchedSlice& GetConstSchedSliceData() const;
    const CpuCounter& GetConstCpuCounterData() const;
    const ThreadCounterFilter& GetConstThreadFilterData() const;
    const Instants& GetConstInstantsData() const;
    const ProcessCounterFilter& GetConstProcessFilterData() const;
    const ProcessCounterFilter& ProcessCounterFilterData() const;
    const std::string& GetConstSchedStateData(uint64_t rowId) const;
    uint64_t BoundStartTime() const;
    uint64_t BoundEndTime() const;
};
}
}
#endif
