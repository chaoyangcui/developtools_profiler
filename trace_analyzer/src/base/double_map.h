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

#ifndef SRC_TRACE_BASE_DOUBLEMAP_H
#define SRC_TRACE_BASE_DOUBLEMAP_H

#include <map>

template<class T1, class T2, class T3>
class DoubleMap {
public:
    DoubleMap(T3 invalidValue)
    {
        invalidValue_ = invalidValue;
    }
    void SetInvalidRet(T3 invalidValue)
    {
        invalidValue_ = invalidValue;
    }
    void Insert(T1 t1, T2 t2, T3 t3)
    {
        auto streamIdHookidMap = internamMap_.find(t1);
        if (streamIdHookidMap != internamMap_.end()) {
            auto hookId = (*streamIdHookidMap).second.find(t2);
            if (hookId == (*streamIdHookidMap).second.end()) {
                (*streamIdHookidMap).second.insert(std::make_pair(t2, t3));
            } else {
                (*streamIdHookidMap).second.at(t2) = t3;
            }
        } else {
            std::map<T2, T3> mm = {
                {t2, t3}
            };
            internamMap_.insert(std::make_pair(t1, mm));
        }
    }
    T3 Find(T1 t1, T2 t2)
    {
        auto streamIdHookidMap = internamMap_.find(t1);
        if (streamIdHookidMap != internamMap_.end()) {
            auto hookId = (*streamIdHookidMap).second.find(t2);
            if (hookId == (*streamIdHookidMap).second.end()) {
                return invalidValue_;
            } else {
                return hookId->second;
            }
        } else {
            return invalidValue_;
        }
    }

private:
    std::map<T1, std::map<T2, T3>> internamMap_;
    T3 invalidValue_;
};

#endif // DOUBLEMAP_H
